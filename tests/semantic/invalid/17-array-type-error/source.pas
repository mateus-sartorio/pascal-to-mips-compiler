program Teste;
var
    meuArray: array[1..5] of integer;
begin
    meuArray['erro'] := 10; { Erro: Índice de array precisa ser um INTEGER }
end.