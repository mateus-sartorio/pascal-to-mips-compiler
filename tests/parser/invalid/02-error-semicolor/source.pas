program ErrorSemicolon;
type
    myType = integer { Faltando o ';' aqui }
var
    x: myType;
begin
    x := 5;
end.