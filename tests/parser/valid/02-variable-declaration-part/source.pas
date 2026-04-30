program p2;

type
  arrayOfInteger = array [1..10] of integer;
  arrayOfChar = array [1..20] of char;
  arrayOfReal = array [1..5] of real;
  arrayOfBoolean = array [1..15] of boolean;
  arrayOfString = array [1..25] of string;

var
  array1, array2, array3: arrayOfBoolean;
  array4: arrayOfInteger;
  array5: char;
  array6: arrayOfReal; array7: arrayOfChar; array8: arrayOfString;

begin
    writeln('Variable declaration part test passed!');
end.