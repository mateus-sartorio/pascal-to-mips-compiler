program p11;
var
  c: string;
  arr: array[10..15] of string;
  i: integer;
  s: string;
begin
  c := 'l';
  arr[10] := c;
  c := 'e';
  arr[11] := c;
  c := 'g';
  arr[12] := c;
  c := 'a';
  arr[13] := c;
  c := 'l';
  arr[14] := c;

  for i := 10 to 15 do
  begin
    c := arr[i];
    writeln(c); 
  end;
end.
