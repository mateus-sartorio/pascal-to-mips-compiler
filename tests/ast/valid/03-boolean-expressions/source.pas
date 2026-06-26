program p3;

var 
  booleanVar, test: boolean;

begin
  test := true;
  booleanVar := 1 < 2;
  booleanVar := true;
  booleanVar := false;
  booleanVar := (false or true) and (true and not test);
end.