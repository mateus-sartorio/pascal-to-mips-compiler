program TesteComentarios;
{ 
  Comentario de multiplas linhas 
  usando chaves.
}
var
  a: integer; (* Comentario estilo antigo (parenteses e asterisco) *)
begin
  a := 10; // Comentario de linha unica (estilo Delphi/Moderno)
  
  { Teste de comentario "misturado" (* com outro simbolo dentro *) }
  
  (* Teste de codigo "comentado"
     a := 20; 
  *)

  writeln('O valor de a e: ', a); { Comentario no fim da linha }
end.