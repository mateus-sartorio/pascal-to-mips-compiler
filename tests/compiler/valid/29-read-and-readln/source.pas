program p29;
var
  a: integer;
  r: real;
  b: boolean;
  c: char;
  s, frase: string;
begin
  
  read(frase);
  readln(a);
  write(frase + itos(a) + '\n');

  read(frase);
  readln(r);
  write(frase + rtos(r) + '\n');

  read(frase);
  readln(b);
  write(frase + btos(b) + '\n');

  read(frase);
  readln(c);
  write(frase + c + '\n');

  read(frase);
  readln(s);
  write(frase + s + '\n');

end.