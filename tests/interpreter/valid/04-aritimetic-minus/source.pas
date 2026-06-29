program p4;
var
  ia, ib, ic: integer;
  ra, rb, rc: real;
  result_str: string;
begin
  ia := 2;
  ib := 1;
  ic := ia - ib;
  result_str := 'Result' + itos(ic);
  Writeln(result_str);

  ra := 2.0;
  rb := 1.0;
  rc := ra - rb;
  result_str := 'Result' + rtos(rc);
  writeln(result_str);
end.
