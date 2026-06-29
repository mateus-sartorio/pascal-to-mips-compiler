program p9;
var
  a,b,c,res_str: string;
  res: boolean;
begin
  a := 'abc';
  b := 'abc';
  c := 'dif';
  res := a = b;
  res_str := (a + ' + ' + b + ' = ');
  writeln(res_str);
  writeln(btos(res));
  res := a = c;
  res_str := (a + ' + ' + c + ' = ');
  writeln(res_str);
  writeln(btos(res));
  
end.
