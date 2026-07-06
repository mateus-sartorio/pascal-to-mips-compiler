program p1;
  var
    n: integer;
    r: real;
begin
  n := 7;
  
  if n >= 5
  then
    n := n + 1
  else
    n := n - 1;
  
  if n <> 0
  then
    n := n;

  if n < 10
  then
  begin
    n := n * 2;
    n := n div 2;
    r := n div 2;
  end;
end.
