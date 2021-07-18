TARGETS = Test_Palindrome_Finder.exe Paren_Checker.exe Test_Infix_To_Postfix_std.exe \
Test_Infix_To_Postfix_KW.exe Test_Infix_To_Postfix_KWLIST.exe Test_Infix_To_Postfix_Parens.exe \
Test_Postfix_Evaluator.exe

CPPFLAGS = /Zi /EHsc /GR

all: $(TARGETS)

Test_Palindrome_Finder.exe:	Test_Palindrome_Finder.obj Palindrome_Finder.obj
	cl $(CPPFLAGS) /Fe$@ $**

Paren_Checker.exe:	Paren_Checker.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Infix_To_Postfix_std.exe:	Test_Infix_To_Postfix_std.obj Infix_To_Postfix_std.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Infix_To_Postfix_KW.exe:	Test_Infix_To_Postfix_KW.obj Infix_To_Postfix_KW.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Infix_To_Postfix_KWLIST.exe:	Test_Infix_To_Postfix_KWLIST.obj Infix_To_Postfix_KWLIST.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Infix_To_Postfix_Parens.exe:	Test_Infix_To_Postfix.obj Infix_To_Postfix_Parens.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Postfix_Evaluator.exe:	Test_Postfix_Evaluator.obj Postfix_Evaluator.obj
	cl $(CPPFLAGS) /Fe$@ $**

Test_Palindrome_Finder.obj:	Test_Palindrome_Finder.cpp Palindrome_Finder.h
	cl $(CPPFLAGS) /c Test_Palindrome_Finder.cpp

Palindrome_Finder.obj:	Palindrome_Finder.cpp Palindrome_Finder.h
	cl $(CPPFLAGS) /c Palindrome_Finder.cpp

Paren_Checker.obj:	Paren_Checker.cpp
	cl $(CPPFLAGS) /c Paren_Checker.cpp

Test_Infix_To_Postfix_std.obj:	Test_Infix_To_Postfix.cpp Infix_To_Postfix.h \
Syntax_Error.h
	cl $(CPPFLAGS) /c /Fo$@ Test_Infix_To_Postfix.cpp

Test_Infix_To_Postfix_KW.obj:	Test_Infix_To_Postfix.cpp Infix_To_Postfix.h \
Syntax_Error.h stack.h Stack.tc
	cl $(CPPFLAGS) /DUSEKW /c /Fo$@ Test_Infix_To_Postfix.cpp

Test_Infix_To_Postfix_KWLIST.obj: Test_Infix_To_Postfix.cpp Infix_To_Postfix.h \
Syntax_Error.h stack.h Linked_Stack.tc Node.h
	cl $(CPPFLAGS) /DUSEKW /DUSELIST /c /Fo$@ Test_Infix_To_Postfix.cpp

Infix_To_Postfix_std.obj:	Infix_To_Postfix.cpp Infix_To_Postfix.h Syntax_Error.h
	cl $(CPPFLAGS) /c /Fo$@ Infix_To_Postfix.cpp

Infix_To_Postfix_KW.obj:	Infix_To_Postfix.cpp Infix_To_Postfix.h Syntax_Error.h stack.h \
Stack.tc
	cl $(CPPFLAGS) /DUSEKW /c /Fo$@ Infix_To_Postfix.cpp

Infix_To_Postfix_KWLIST.obj:	Infix_To_Postfix.cpp Infix_To_Postfix.h Syntax_Error.h stack.h \
Linked_Stack.tc Node.h
	cl $(CPPFLAGS) /DUSEKW /DUSELIST /c /Fo$@ Infix_To_Postfix.cpp

Infix_To_Postfix_Parens.obj:	Infix_To_Postfix_Parens.cpp Infix_To_Postfix.h Syntax_Error.h
	cl $(CPPFLAGS) /c Infix_To_Postfix_Parens.cpp

Test_Postfix_Evaluator.obj:	Test_Postfix_Evaluator.cpp \
Postfix_Evaluator.h Syntax_Error.h
	cl $(CPPFLAGS) /c Test_Postfix_Evaluator.cpp

Postfix_Evaluator.obj:	Postfix_Evaluator.cpp Postfix_Evaluator.h \
Syntax_Error.h
	cl $(CPPFLAGS) /c Postfix_Evaluator.cpp

clean:
	del *.obj
	del *.exe
	del *.pdb
	del *.ilk


