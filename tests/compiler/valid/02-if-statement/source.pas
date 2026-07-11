program p2;

var
  a: integer;

begin
  a := 1;

  if a > 0 then
    begin
      a := a + 1;
      writeln(itos(a));
    end
  else
    begin
      a := a - 1;
      writeln(itos(a));
    end

end.