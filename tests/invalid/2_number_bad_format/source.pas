program NumberBadFormat;
var
  r: real;
begin
  r := 10.;   { Erro: Espera-se dígitos após o ponto }
  r := .5;    { Erro: Pascal exige dígito antes do ponto: 0.5 }
  r := 1.2e;  { Erro: Expoente vazio }
end.