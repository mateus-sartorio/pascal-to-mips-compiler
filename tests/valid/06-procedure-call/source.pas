program p6;

var x: integer;

procedure inc2(var a: integer);
begin
  a := a + 2;
end;

function inc3(var a: integer) : integer;
begin
  a := a + 2;
  inc3 := a;
end;

begin
  x := 1;
  inc2(x);
  x := inc3(x);
end.