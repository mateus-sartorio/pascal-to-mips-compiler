program p14;

procedure p(x: integer);
begin
  if x >= 0 then
  begin
    writeln(itos(x));
    p(x - 1);
  end;
end;

begin
  p(10);
end.
