program p9;

var
  a, b, c, d : integer;
  e, res : boolean;

begin
  a := 1; b := 2; c := 3; d := 4;
  e := false;

  res := (((a + b) * c) > d) and (not e);
end.