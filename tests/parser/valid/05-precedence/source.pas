program p5;
var
    res: real;
    a, b: integer;
begin
    a := 10;
    b := 5;
    
    res := (a + b) * 2 / (10 - 5);
    if not (a < b) or (res = 6.0) and (a <> 0) then
        writeln('Precedencia OK');
end.