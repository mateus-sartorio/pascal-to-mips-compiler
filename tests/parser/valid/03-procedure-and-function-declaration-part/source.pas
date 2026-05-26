program p3;

var
  v1: integer;
  v2: array[1..3] of integer;

procedure ProcedWithoutParameter;
begin
  writeln('This is a procedure with no parameters.');
end;

function soma(a, b: integer): integer;
var
  result: integer;
begin
  result := a + b;
  soma := result;
end;

procedure ProcedWithParam(x: integer; y: array[1..3] of integer);
begin
  x := y[1];
end;

begin
  ProcedWithoutParameter;
  v1 := soma(5, 10);
  ProcedWithParam(v1, v2);
  writeln('Procedure and Function declaration test passed!');
end.