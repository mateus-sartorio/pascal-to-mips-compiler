program p2;

var
  s: string;
  shortStr: string;
  inteiro: integer;
  numero_real: real;

procedure Exibir(msg : string);
begin
    writeln(msg)
end;

begin
  s := 'Hello, World!';
  s := 'Linha 1;\tLinha 2!\n';
  shortStr := 'S';
  writeln('The string is: ', s);
  writeln('The short string is: ', shortStr);
  Exibir('Testando a literal passada para a procedure Exibir');
  inteiro := 42;
  numero_real := 3.14;
  inteiro := +5 + -3 * 2;
  numero_real := 1.5 + 2.0;
  writeln('The integer is: ', inteiro);
  writeln('The real number is: ', numero_real);
end;