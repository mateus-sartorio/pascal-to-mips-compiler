program p1;

var
  i: integer;
  c: string;
begin
  c := 'test';
  writeln(c);

  i := 2;
  i := 1 + i;
  writeln(itos(i));
end.