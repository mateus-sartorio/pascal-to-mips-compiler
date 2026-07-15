program p21;

var
  arr: array[1..3] of integer;
  i: integer;

procedure p(a: array[1..3] of integer);
begin
  a[1] := 99;
  write('Array: [');
  for i := 1 to 2 do
    write(itos(a[i]) + ', ');
  writeln(itos(a[3]) + ']');
end;

begin
  arr[1] := 10;
  arr[2] := 20;
  arr[3] := 30;

  p(arr);
end.