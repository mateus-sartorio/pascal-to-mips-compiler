program TesteTabelas;

var
    idade, ano : integer;
    salario : real;
    nome : string;

procedure Inicializar(var texto : string; valor : real);
begin
    texto := 'Iniciando o programa';
    salario := valor
end;

begin
    idade := 21;
    ano := 2026;
    Inicializar(nome, 1500.50);
    
    for ano := 1 to 10 do
    begin
        nome := 'Iterando no loop'
    end;
end.