program p3;
  var s1, s2, s3: string;
begin
  s1 := 'a';
  s2 := 'a';
  s3 := 'b';

  if s1 = s2 then
    writeln('equal!');

  if s1 <> s3 then
    writeln('s1 and s3 are different!');

  if s1 < s2
  then
    writeln('s1 come before s2 in lexical order!')
  else
    writeln('s1 comes after s2 in lexical order')
end.