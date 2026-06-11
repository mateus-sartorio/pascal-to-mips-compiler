program DeepExpression;
var
res,a,b: integer;
x,y,z: string;

function testeFunction(aParamFunc: integer; bParamFunc: real):integer;
var
xLocalVarFunc: integer;
yLocalVarFunc: real;
begin
xLocalVarFunc := aParamFunc + 10;
yLocalVarFunc := bParamFunc * 2.5;
res := xLocalVarFunc + round(yLocalVarFunc);
testeFunction := res;
end;

procedure testeProcedure(aParamProc: integer; bParamProc: real);
var
xLocalVarProc: integer;
yLocalVarProc: real;
begin
xLocalVarProc := aParamProc - 5;
yLocalVarProc := bParamProc / 2.0;
res := xLocalVarProc + round(yLocalVarProc);
end;

begin
a := 5;
b := 10;
x := 'Hello';
y := 'World';
z := x + ' ' + y;   
end.