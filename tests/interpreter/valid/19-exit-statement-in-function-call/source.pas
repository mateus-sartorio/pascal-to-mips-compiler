program p19;
function f() : integer;
begin
  f := 1;
  exit;
  f := 2;
end;
begin
  writeln(itos(f()));
end.