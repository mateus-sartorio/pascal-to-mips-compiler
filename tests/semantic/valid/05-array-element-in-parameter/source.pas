program testeFunctionArray;
var
  meuArray: array[1..5] of integer;
  result: integer;

function teste(a : integer): integer;
begin
  a := a + 1;
  teste := a;
end;
begin
  meuArray[1] := 10;
  result := teste(meuArray[1]);
end.