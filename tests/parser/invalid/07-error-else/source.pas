program ErrorElse;
begin
    if (10 > 5) then
        writeln('Maior'); { Erro: este ponto e vírgula mata o else abaixo }
    else
        writeln('Menor');
end.