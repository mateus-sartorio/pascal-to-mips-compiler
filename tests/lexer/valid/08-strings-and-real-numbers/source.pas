program p8;
var s: string;
var r: real;
begin
  s := 'don''t panic';
  r := 12.5e+2;
  if r <= 2000
  then
    s := 'ok'
  else
    s := 'too big';
end.
