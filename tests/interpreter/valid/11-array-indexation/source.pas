program p11;
var
  c: char;
  arr: array[10..15] of char;
  i: integer;
begin
  arr[10] := 'l';
  arr[11] := 'e';
  arr[12] := 'g';
  arr[13] := 'a';
  arr[14] := 'l';

  for i := 10 to 15 do
  begin
    c := arr[i];
    writeln(c); 
  end;
end.
