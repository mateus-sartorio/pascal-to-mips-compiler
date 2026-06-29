program p1;

var
  a: integer;
  b: real;
  c: string;
  d: boolean;
  e: char;
begin
  a := 1;
  b := 2.0;
  c := 'test';
  d := true;
  e := 'a';

  writeln(rtos(b));
  writeln(c);
  writeln(btos(d));
  writeln(e);
end.