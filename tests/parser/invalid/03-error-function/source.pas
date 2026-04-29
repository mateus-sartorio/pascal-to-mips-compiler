program ErrorFunction;

{ Erro: Esqueceu os dois pontos ':' antes do tipo de retorno }
function soma(a, b: integer) integer; 
begin
    soma := a + b;
end;

begin
    writeln(soma(1, 2));
end.