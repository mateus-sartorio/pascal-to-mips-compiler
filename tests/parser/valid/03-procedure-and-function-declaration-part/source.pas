program p3;

type
  arrayOfInteger = array [1..10] of integer;

var
  v1: integer;
  v2: arrayOfInteger;

procedure ProcedWithoutParameter;
begin
  writeln('This is a procedure with no parameters.');
end;

function soma(a, b: integer): integer;
begin
  soma := a + b;
end;

procedure ProcedWithParam(var x: integer; y: arrayOfInteger);
begin
  x := y[1];
end;

begin
  ProcedWithoutParameter;
  v1 := soma(5, 10);
  ProcedWithParam(v1, v2);
  writeln('Procedure and Function declaration test passed!');
end.