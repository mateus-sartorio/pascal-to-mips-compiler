program p2;

var
  s: string;
  shortStr: string;
  inteiro: integer;
  numeroReal: real;

procedure Exibir(msg : string);
begin
  inteiro := 2;
end;

begin
  s := 'Hello, World!';
  s := 'Linha 1;\tLinha 2!\n';
  shortStr := 'S';
  Exibir('Testando a literal passada para a procedure Exibir');
  inteiro := 42;
  numeroReal := 3.14;
  inteiro := +5 + (-3) * 2;
  numeroReal := 1.5 + 2.0;
end.