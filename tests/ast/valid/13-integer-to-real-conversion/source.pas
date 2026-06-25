program p13;

var
  i : integer;
  r : real;

function f1(a : real) : integer;
begin
  f1 := 1;
end;

begin
  i := 1;
  r := i;
  r := 2;

  r := f1(i);
end.