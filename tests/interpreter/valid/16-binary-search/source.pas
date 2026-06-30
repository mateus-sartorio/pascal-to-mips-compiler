program p16;

var
  data: array[1..10] of integer;
  i, r, foundIndex, b: integer;

function binarySearchArray(a: array[1..10] of integer): integer;
var
  mid: integer;
begin
  writeln('Array:');
  for i := 1 to 10 do
  begin
    b := a[i];
    writeln(itos(b));
  end;

  binarySearchArray := 1;
end;

begin
  data[1]  := 2;
  data[2]  := 5;
  data[3]  := 8;
  data[4]  := 12;
  data[5]  := 16;
  data[6]  := 23;
  data[7]  := 38;
  data[8]  := 56;
  data[9]  := 72;
  data[10] := 91;
  
  binarySearchArray(data);
end.