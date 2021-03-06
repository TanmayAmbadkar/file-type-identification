(* A small-scale example illustrating partially-static monoids.

   References for the unstaged printf function:

     Functional Unparsing
     Olivier Danvy
     May 1998 -- JFP 8(6):621-625, 1998, extended version

     On typing delimited continuations: three new solutions to the
     printf problem
     Kenichi Asai
     Higher-Order and Symbolic Computation, 2009

   Reference for a staged "tagged" (GADT-based) printf function:

     Modular macros (extended abstract)
     Jeremy Yallop and Leo White
     OCaml 2015

   The implementation that uses partially-static data is new here.
*)

(** A standard typed printf function based on CPS with an accumulator. *)
module Printf_unstaged :
sig
  type (_,_) t

  val lit : string -> ('a, 'a) t

  val (++) : ('b, 'a) t -> ('c, 'b) t -> ('c, 'a) t

  val int : ('a, int -> 'a) t
  val str : ('a, string -> 'a) t

  val sprintf : (string, 'a) t -> 'a
end =
struct
  type ('a,'r) t = (string -> 'a) -> (string -> 'r)

  let lit x = fun k -> fun s -> k (s ^ x)

  let (++) f1 f2 = fun k -> f1 (f2 k)

  let sprintf p = p (fun s -> s) ""

  let int' x = string_of_int x

  let str' x = x

  let (!%) to_str = fun k -> fun s -> fun x -> k (s ^ to_str x)

  let int x = !% int' x
  let str x = !% str' x
end

(** A staged version of Printf_unstaged -- identical except for
    quoting and splicing annotations and the 'code' type constructor.

    The continuation function is treated as static, and so it doesn't
    appear in the generated code.
*)
module Printf_staged :
sig
  type (_,_) t

  val lit : string -> ('a, 'a) t

  val (++) : ('b, 'a) t -> ('c, 'b) t -> ('c, 'a) t

  val int : ('a, int code -> 'a) t
  val str : ('a, string code -> 'a) t

  val sprintf : (string code, 'a) t -> 'a
end =
struct
  type ('a,'r) t = (string code -> 'a) -> (string code -> 'r)

  let lit x = fun k -> fun s -> k .<.~s ^ x>.

  let (++) f1 f2 = fun k -> f1 (f2 k)

  let sprintf p = p (fun s -> s) .<"">.

  let int' x = .<string_of_int .~x>.
  let str' x = x

  let (!%) to_str = fun k -> fun s -> fun x -> k .<.~s ^ .~(to_str x)>.

  let int x = !% int' x
  let str x = !% str' x
end

(** An improved version of Printf_staged that uses partially-static
    strings rather than dynamic strings internally.  The interface is
    identical.  Using partially-static strings means that adjacent
    literal strings appear already catenated in the generated code,
    even if they are not adjacent in the source:

        (x ++ lit "a") ++ (lit "b" ++ y)
      ~>
        .< ... x ^ "ab" ^ y ... >.

    In this way the partially-static data structure makes use of the
    associativity of the underlying operation to generate more
    efficient code. *)
module Printf_partially_static :
sig
  type (_,_) t

  val lit : string -> ('a, 'a) t

  val (++) : ('b, 'a) t -> ('c, 'b) t -> ('c, 'a) t

  val int : ('a, int code -> 'a) t
  val str : ('a, string code -> 'a) t

  val sprintf : (string code, 'a) t -> 'a
end =
struct
  module String_monoid : Algebra.MONOID with type t = string = struct
    type t = string
    let unit = ""
    let (<*>) = (^)
  end

  module String_code_monoid : Algebra.MONOID with type t = string code = struct
    type t = string code
    let unit = .<"">.
    let (<*>) x y = .< .~x ^ .~y >.
  end

  module Ps_string = Monoids.PS_monoid(String_monoid)(String_code_monoid)
  (* module Ps_monoid = (\* Monoids.MkMONOID *\)(Ps_string) *)

  open Ps_string
  open Ps_string.Op
  (* open Ps_monoid *)

  type ('a,'r) t = (Ps_string.T.t -> 'a) -> (Ps_string.T.t -> 'r)

  let lit x = fun k -> fun s -> k (s <*> sta x)

  let (++) f1 f2 = fun k -> f1 (f2 k)

  let sprintf p = p (fun s -> cd s) unit

  let int' x = var (Aux.var .<string_of_int .~x>.)
  let str' x = var (Aux.var x)

  let (!%) to_str = fun k -> fun s -> fun x -> k (s <*> to_str x)

  let int x = !% int' x
  let str x = !% str' x
end


let unstaged_example : int -> int -> string =
  Printf_unstaged.(sprintf
                     (lit "(" ++ int ++ lit "," ++ int ++ lit ")"))

let staged_example : int code -> int code -> string code =
  Printf_staged.(sprintf
                     (lit "(" ++ int ++ lit "," ++ int ++ lit ")"))

let partially_static_example : int code -> int code -> string code =
  Printf_partially_static.(sprintf
                             (lit "(" ++ int ++ lit "," ++ int ++ lit ")"))

let () = begin
  (** Code generated by staged printf *)
  let staged_code = .< fun x y -> .~(staged_example .<x>. .<y>.) >. in

  (** Code generated by improved staged printf with partially-static data *)
  let ps_code = .< fun x y -> .~(partially_static_example .<x>. .<y>.) >. in

  (** All three implementations should produce identical behaviour *)
  assert (unstaged_example 3 4 = "(3,4)");
  assert (Runcode.run staged_code 3 4 = "(3,4)");
  assert (Runcode.run ps_code 3 4 = "(3,4)");

  (** A tiny example showing how the partially-static version
      generates better code by incorporating associativity. *)

  (** First, the output of the staged version.
      Observe how the concatenation of "a" and "b" is work still
      to be done by the generated code, even though both are known
      statically. *)
  Format.printf "(* generated code using dynamic strings *)@\n%a@\n"
  Print_code.print_code
    .< fun x y ->
      .~(Printf_staged.(sprintf
                          ((int ++ lit "a") ++ (lit "b" ++ int)))
           .<x>. .<y>.) >.;

  (** Second, the output of the improved staged version that uses
      partially-static data.  The format string is identical to the
      output above.

      Observe how "a" and "b" are catenated in the generated code.

      The partially-static monoid also drops the concatenation of units,
      reducing 4 concatenations to 2.
  *)
  Format.printf "(* generated code using partially-static strings *)@\n%a@\n"
  Print_code.print_code
    .< fun x y ->
      .~(Printf_partially_static.(sprintf
                                    ((int ++ lit "a") ++ (lit "b" ++ int)))
           .<x>. .<y>.) >.;

end
