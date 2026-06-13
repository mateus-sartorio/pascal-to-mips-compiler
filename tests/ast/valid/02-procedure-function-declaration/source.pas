program p2;

var 
  globalInteger: integer;
  globalStr: string;

procedure calcularSoma(a : integer; resultado : integer);
var 
  copiaLocalProcedure: integer;
  textoLocalProcedure, newVariable : string;
begin
  textoLocalProcedure := 'Calculando a soma de ';
  copiaLocalProcedure := a;
  resultado := copiaLocalProcedure + -10;
end;

procedure FormatarNome(id: integer; sufixo: string);
var
  nomeFormatado: string;
begin
  begin
    nomeFormatado := 'ID_' + sufixo;
  end;
end;

begin
  globalInteger := 5;
  calcularSoma(globalInteger, globalInteger);
  writeln(1);
  writeln('''');
  
  FormatarNome(123, 'Teste');
end.