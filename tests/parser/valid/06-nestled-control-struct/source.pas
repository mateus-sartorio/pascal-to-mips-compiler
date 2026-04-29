program p6;

var
    i, j, total: integer;
begin
    total := 0;
    for i := 1 to 10 do
    begin
        for j := 10 downto 1 do
        begin
            if i > j then
                total := total + 1
            else
                if i = j then
                    total := total + 2; 
        end;
    end;
    writeln('Controle de fluxo OK');
end.