program p3;
var
  ia, ib, ic: integer;
  ra, rb, rc: real;
begin
  ia := 1;
  ib := 2;
  ic := ia + ib;
  writeln(itos(ic));

  ra := 1.0;
  rb := 2.0;
  rc := ra + rb;
  writeln(rtos(rc));
end.
