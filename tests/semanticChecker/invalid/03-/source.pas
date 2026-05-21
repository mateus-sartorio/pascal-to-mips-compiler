program p3;

var
  notas : array[1..5] of real;
  i : integer;
  media : real;

procedure InicializarNotas();
begin
  // Testa o visitFor_statement usando variável global
  for i := 1 to 5 do
  begin
    // Testa o visitVariable_access caindo no ramo 'indexed_variable'
    notas[i] := 0.0; 
    writeln('Zerando nota...') // Testa o Unsigned_constant e Procedure_statement
  end
end;

begin
  InicializarNotas(); // Testa a chamada de uma procedure do usuário
  media := 7.5;
  writeln('Fim do programa!')
end.