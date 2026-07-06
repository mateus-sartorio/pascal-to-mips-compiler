program p17;
var
  ia, ib, ic : integer;
  ra, rb, rc : real;
  ca, cb, cc : char;
  sa, sb, sc : string;
  ba, bb, bc : boolean;
begin
  
  // write test
  sa := ' write sucessful ';
  write(sa);

  // writeln test
  sa := ' writeln sucessful ';
  writeln(sa);

  // itos test
  ia := 123;
  sa := itos(ia);
  writeln('itos(123) = ' + sa);
  
  // rtos test
  ra := 123.456;
  sa := rtos(ra);
  writeln('rtos(123.456) = ' + sa);

  // btos test
  ba := true;
  sa := btos(ba);
  writeln('btos(true) = ' + sa);

  // abs test
  ia := -3;
  ra := abs(ia);
  sa := rtos(ra);
  writeln('abs(-3) = ' + sa);

  // sqr test
  ia := 3;
  rb := sqr(ia);
  sa := rtos(rb);
  writeln('sqr(3) = ' + sa);

  // sqrt test
  ra := 9.0;
  rb := sqrt(ra);
  sa := rtos(rb);
  writeln('sqrt(9) = ' + sa);

  // trunc test
  ra := 9.99;
  ia := trunc(ra);
  sa := itos(ia);
  writeln('trunc(9.99) = ' + sa);

  // round test
  ra := 9.99;
  ia := round(ra);
  sa := itos(ia);
  writeln('round(9.99) = ' + sa);

  // ord test
  ca := 'A';
  ia := ord(ca);
  sa := itos(ia);
  writeln('ord(A) = ' + sa);

  // chr test
  ia := 65;
  ca := chr(ia);
  sa := ca;
  writeln('chr(65) = ' + sa);

  // succ test
  ia := 65;
  ia := succ(ia);
  sa := chr(ia);
  writeln('succ(A) = ' + sa);

  // pred test
  ia := 65;
  ia := pred(ia);
  sa := chr(ia);
  writeln('pred(A) = ' + sa);

  // upcase test
  ca := 'a';
  ca := upcase(ca);
  sa := ca;
  writeln('upcase(a) = ' + sa);

  // read test
  writeln('Please enter a string:');
  read(sa);
  writeln('You entered: ' + sa);

  // readln test
  writeln('Please enter a line:');
  readln(sa);
  writeln('You entered: ' + sa);


end.