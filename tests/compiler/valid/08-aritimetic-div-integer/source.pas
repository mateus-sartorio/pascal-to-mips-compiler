program p8;
var
  ia, ib, ic: integer;
  result_str: string;
begin
  
  ia := 8;
  ib := 2;
  ic := ia div ib;
  result_str := 'Result ' + itos(ic);
  writeln(result_str);
end.
