program p13;

var
  i: real;

function d(x: integer; y: real) : real;
  var z : real;
begin
  writeln(itos(x));
  writeln(rtos(y));
  z := x * y;
  writeln(rtos(z));
  d := z;
end;

begin
  i := d(3, 2.5);
  writeln(rtos(i));
end.
