program p14;
var
  n, i: integer;

function fib(k: integer): integer;
begin
  if k < 2 then
    fib := k
  else
    fib := fib(k - 1) + fib(k - 2);
end;

begin
  n := 10;
  for i := 0 to n do
    writeln(itos(fib(i)));
end.