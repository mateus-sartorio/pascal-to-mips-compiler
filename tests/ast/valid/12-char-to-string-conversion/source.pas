program p12;

var
  c : char;
  s : string;

procedure p(b : string);
begin
end;

function f1(a : char) : string;
begin
  f1 := a + a;
end;

function f2(a : char) : string;
begin
  f2 := a;
end;

begin
  c := 'a';
  s := c;
  s := 'a';

  p(c);

  s := f1(s[0]);
  s := f2(s[0]);
end.