program p11;

var
  i, r, acc: integer;

function f(): integer;
begin
  if i > 0 then
  begin
    acc := acc + i;
    writeln(itos(acc));
    i := i - 1;
    f();
  end
  else
    f := acc;
end;

begin
  i := 10;
  acc := 0;
  r := f();
  writeln(itos(r));
end.