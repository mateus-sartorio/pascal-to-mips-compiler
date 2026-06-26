program p4;

var 
  arrayVar: array[0..10] of boolean;

begin
  arrayVar[0] := 1 < 2;
  arrayVar[1 + 1] := 1 < 2;

  arrayVar[0] := (1 < 2) or (1 > 2);
end.