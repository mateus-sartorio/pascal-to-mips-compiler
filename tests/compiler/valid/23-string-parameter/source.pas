program p21;

var
  str1: string;
  i: integer;

procedure p(s: string);
begin
  write('String: [');
  for i := 0 to 1 do
    write(s[i] + ',');
  writeln(s[2] + ']');
end;

begin
  str1 := 'leg';
  p(str1);
end.