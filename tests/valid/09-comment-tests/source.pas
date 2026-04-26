program p9;
{ 
  Multi-line comment
  using braces.
}
var
  a: integer; (* Old-style comment (parentheses and asterisk) *)
begin
  a := 10;
  
  { "Mixed" comment test (* with another symbol inside *) }
  
  (* "Commented out" code test
     a := 20; 
  *)

  writeln('The value of a is: ', a); { Comment at the end of the line }
end.