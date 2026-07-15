program p10;

var
  i: integer;

procedure p();
begin
  if i > 0 then
  begin
    writeln(itos(i));
    i := i - 1;
    p();
  end;
end;

begin
  i := 10;
  p();
end.