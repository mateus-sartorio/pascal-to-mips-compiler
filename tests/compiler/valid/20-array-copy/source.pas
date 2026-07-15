program p20;
var
  arr1, arr2: array[1..3] of integer;
  i: integer;
begin
  arr1[1] := 5;
  arr1[2] := 2;
  arr1[3] := 3;

  arr2 := arr1;

  arr2[1] := 10;

  write('Array 1: [');
  for i := 1 to 2 do
    write(itos(arr1[i]) + ', ');
  writeln(itos(arr1[3]) + ']');
  
  write('Array 2: [');
  for i := 1 to 2 do
    write(itos(arr2[i]) + ', ');
  writeln(itos(arr2[3]) + ']');
end.