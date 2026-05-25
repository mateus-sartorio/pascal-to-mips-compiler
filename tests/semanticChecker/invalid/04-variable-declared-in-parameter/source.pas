program p4;

var
  x : integer;

procedure TesteErro(a : integer);
var
  a : real; // ERRO SEMÂNTICO! 'a' já foi declarado como parâmetro na linha anterior
begin
  x := 10
end;

begin
  TesteErro(5)
end.