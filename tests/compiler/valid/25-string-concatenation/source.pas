program p25;

var s: string;

procedure p1();
  var s2: string;
begin
  s2 := 'chato';
  writeln(s2);
  
  s2 := s2 + 'a';
  writeln(s2);

  s2 := 'a' + s2;
  writeln(s2);

  s2 := 'a' + 'a';
  writeln(s2);

  s2 := 'abc' + 'a';
  writeln(s2);

  s2 := 'a' + 'abc';
  writeln(s2);

  s2 := 'abc' + 'abc';
  writeln(s2);
end;

procedure p2(s3: string);
begin
  writeln(s3);
  
  s3 := s3 + 'a';
  writeln(s3);

  s3 := 'a' + s3;
  writeln(s3);

  s3 := 'a' + 'a';
  writeln(s3);

  s3 := 'abc' + 'a';
  writeln(s3);

  s3 := 'a' + 'abc';
  writeln(s3);

  s3 := 'abc' + 'abc';
  writeln(s3);
end;

begin
  s := 'legal';
  writeln(s);
  
  s := s + 'a';
  writeln(s);

  s := 'a' + s;
  writeln(s);

  s := 'a' + 'a';
  writeln(s);

  s := 'abc' + 'a';
  writeln(s);

  s := 'a' + 'abc';
  writeln(s);

  s := 'abc' + 'abc';
  writeln(s);

  p1();
  p2('mais ou menos');
end.