program p3;

var 
  globalInteger: integer;
  globalStr: string;

// olhar com calma essa questao de passar o resultado por referência, e se é necessário ou não (usando o VAR)
procedure calcularSoma(a : integer; VAR resultado : integer);
var 
  copiaLocalProcedure: integer;
  textoLocalProcedure: string;
begin
  textoLocalProcedure := 'Calculando a soma de ';
  copiaLocalProcedure := a;
  resultado := copiaLocalProcedure + 10;
end;

function FormatarNome(id: integer; sufixo: string) : String;
var
  nomeFormatado: string; // Variável local para armazenar o nome formatado
begin
  nomeFormatado := 'ID_' + sufixo; // Formata o nome usando a variável local
  FormatarNome := nomeFormatado; // Retorna o nome formatado
end;

begin
  globalInteger := 5;
  calcularSoma(globalInteger, globalInteger); // Passa a variável global por referência
  writeln('Resultado da soma: ', globalInteger);
  
  globalStr := FormatarNome(123, 'Teste');
  writeln('Nome formatado: ', globalStr);
end.