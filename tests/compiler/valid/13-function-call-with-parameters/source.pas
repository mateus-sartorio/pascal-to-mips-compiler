program p13;
var
  c: integer;

function soma(a, b: integer): integer;
begin
  soma := a + b;
end;

begin
  c := soma(1, 3);
  writeln(itos(c));
end.