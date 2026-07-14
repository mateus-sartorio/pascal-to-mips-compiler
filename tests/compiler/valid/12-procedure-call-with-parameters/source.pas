program p12;

var i: integer;

procedure p(x: integer);
  var a: integer;
begin
  a := x * 4;
  writeln(itos(a));
end;

begin
  p(10);
  
  i := 10;

  writeln(itos(i * 2));
end.