program p9;

var
  arr: array[1..10] of integer;
  i: integer;

begin
  for i := 1 to 10 do
    arr[i] := 2 * i;
  
  write('Array: [');
  for i := 1 to 9 do
    write(itos(arr[i]) + ', ');
  writeln(itos(arr[10]) + ']');
end.