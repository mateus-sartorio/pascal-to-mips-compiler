program p7;
var a: array[1..5] of integer;
var i: integer;
begin
  i := 1;
  a[i] := 10;
  a[i+1] := a[i] * 2;
end.
