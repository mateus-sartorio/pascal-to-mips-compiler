program TesteParametroInvalido;
  procedure Inverter(n: integer);
  begin
      { Inverte o número }
  end;

begin
    Inverter('texto'); { Esperava INTEGER, recebeu STRING }
end.