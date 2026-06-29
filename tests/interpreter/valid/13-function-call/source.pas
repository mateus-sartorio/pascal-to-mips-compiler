program p13;

var
  i: integer;

function d(n : integer) : integer;
  var x : integer;
begin
  x := 5;
  writeln(itos(n));
  d := 2 * n;
end;

begin
  i := d(2);
  writeln(itos(i));
end.
