/* REXX */                       
                                 
leftBorder = 1                   
rightBorder = 200                
k = 5                            
do p = 1 to k by 1               
    do i = 1 to k by 1           
        j = i * p                
        num.j = random(1,200)    
                                 
    end                          
end                              
                                 
string. = ""                     
                                 
do i = 1 to k by 1               
    do j = 1 to k by 1           
        p = j * i                
        string.i = string.i num.p
    end                          
end                              