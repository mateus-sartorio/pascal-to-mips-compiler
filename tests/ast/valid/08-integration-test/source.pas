program p8;

var
  i, auxiliar : integer;
  testBool, booleanVar : boolean;
  boolArray : array[0..10] of boolean;
  realArray : array[0..10] of real;

begin
  { --- Bloco 1: Expressões Booleanas, Relacionais e Precedência --- }
  testBool := true;
  booleanVar := 1 < 2;
  booleanVar := true;
  booleanVar := false;
  booleanVar := (false or true) and (true and not testBool);

  { --- Bloco 2: Vetores e Indexação Dinâmica com Expressões --- }
  boolArray[0] := 1 < 2;
  boolArray[1 + 1] := 1 < 2;
  boolArray[0] := (1 < 2) or (1 > 2);

  { --- Bloco 3: Operações com Ponto Flutuante / Real  --- }
  realArray[0] := 1.0 + 5.0;

  { --- Bloco 4: Controle de Fluxo - Laço FOR (downto) com Comando Simples --- }
  for i := 10 downto 1 do
    writeln(itos(i));

  { --- Bloco 5: Controle de Fluxo - Laço FOR (to) com Bloco Composto --- }
  for i := 1 to 10 do
  begin
    auxiliar := i;
    writeln(itos(auxiliar));
  end;
end.