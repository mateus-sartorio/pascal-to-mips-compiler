program p24;

var
  data: array[1..10] of integer;
  i, targetValue, foundIndex: integer;

function binarySearchArray(arr: array[1..10] of integer; low: integer; high: integer; target: integer): integer;
var
  mid: integer;
begin
  if low > high then
  begin
    binarySearchArray := -1;
    exit;
  end;

  mid := low + (high - low) div 2;
  if arr[mid] = target then
    binarySearchArray := mid
  else if arr[mid] < target then
    binarySearchArray := binarySearchArray(arr, mid + 1, high, target)
  else
    binarySearchArray := binarySearchArray(arr, low, mid - 1, target);
end;

begin
  data[1] := 2;
  data[2] := 5;
  data[3] := 8;
  data[4] := 12;
  data[5] := 16;
  data[6] := 23;
  data[7] := 38;
  data[8] := 56;
  data[9] := 72;
  data[10] := 91;

  write('Array: [');
  for i := 1 to 9 do
    write(itos(data[i]) + ', ');
  writeln(itos(data[10]) + ']');

  targetValue := 12;

  foundIndex := binarySearchArray(data, 1, 10, targetValue);

  if foundIndex <> -1 then
    writeln('Found ' + itos(targetValue) + ' at index ' + itos(foundIndex))
  else
    writeln(itos(targetValue) + ' is not in the array');
end.