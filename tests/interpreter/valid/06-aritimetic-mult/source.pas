program p6;
var
  ia, ib, ic: integer;
  ra, rb, rc: real;
  result_str: string;
begin
  ia := 2;
  ib := 2;
  ic := ia * ib;
  result_str := 'Result ' + itos(ic);
  Writeln(result_str);

  ra := 2.0;
  rb := 2.3;
  rc := ra * rb;
  result_str := 'Result ' + rtos(rc);
  writeln(result_str);
end.
