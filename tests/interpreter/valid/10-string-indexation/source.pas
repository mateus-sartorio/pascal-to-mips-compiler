program p10;
var
  c: char;
  s: string;
  i: integer;
begin
  s := 'legal';

  for i := 0 to 5 do
  begin
    c := s[i];
    writeln(c); 
  end;
end.
