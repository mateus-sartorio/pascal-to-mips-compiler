program p7;
var
  ra, rb, rc: real;
  result_str: string;
begin
  ra := 8.0;
  rb := 2.0;
  rc := ra / rb;
  result_str := 'Result ' + rtos(rc);
  writeln(result_str);
end.
