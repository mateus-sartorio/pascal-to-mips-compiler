program p3;

var 
  globalInteger: integer;
  globalStr: string;

{ Olha com calma essa questao de passar o resultado por referencia, e se eh necessario ou nao (usando o VAR) }
procedure calcularSoma(a : integer; resultado : integer);
var 
  copiaLocalProcedure: integer;
  textoLocalProcedure, newVariable : string;
begin
  textoLocalProcedure := 'Calculando a soma de ';
  copiaLocalProcedure := a;
  resultado := copiaLocalProcedure + -10;
end;

function FormatarNome(id: integer; sufixo: string) : string;
var
  nomeFormatado: string;
begin
  begin
    nomeFormatado := 'ID_' + sufixo;
    FormatarNome := nomeFormatado;
  end;
end;

begin
  globalInteger := 5;
  calcularSoma(globalInteger, globalInteger);
  writeln(1);
  writeln('''');
  
  globalStr := FormatarNome(123, 'Teste');
end.