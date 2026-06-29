program p12;
var
  c: char;
  arr: array[10..15] of char;
  i: integer;

procedure p();
  var x : integer;
begin
  x := 1;
  c := 'a';
  writeln(itos(x));
end;

begin
  p();
  writeln(c);
end.
