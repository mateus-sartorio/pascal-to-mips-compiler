program p7;

var
  resultado: integer;

function Dobro(n: integer): integer;
begin
  Dobro := n * 2;
  exit;
end;

function Area(base, altura: integer): integer;
begin
  Area := base * altura;
end;

begin
  { Chamada de função onde os argumentos são resultados de outras funções }
  resultado := Area(Dobro(5), Dobro(2 + 3));
  
  if (resultado > 0) then
    begin
      writeln('Resultado positivo');
    end;
end.