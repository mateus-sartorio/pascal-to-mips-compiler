lexer grammar PascalLexer;

options { caseInsensitive=true; }

// -------------------- Special symbols --------------------
ASSIGNMENT: ':=' ;
NOT_EQUAL_TO: '<>' ;
LESS_THAN_OR_EQUAL_TO: '<=' ;
GREATER_THAN_OR_EQUAL_TO: '>=' ;
RANGE: '..' ;
PLUS: '+' ;
MINUS: '-' ;
EQUAL_TO: '=' ;
MULTIPLICATION: '*' ;
DIVISION: '/' ;
DIV: 'div';
LESS_THAN: '<' ;
GREATER_THAN: '>' ;
OPEN_BRACKET: '[' | '(.' ;
CLOSE_BRACKET: ']' | '.)' ;
PERIOD: '.' ;
COMMA: ',' ;
COLON: ':' ;
SEMICOLON: ';' ;
OPEN_PARENTHESIS: '(' ;
CLOSE_PARENTHESIS: ')' ;


// -------------------- Word-symbols --------------------
AND: 'and' ;
ARRAY: 'array' ;
BEGIN: 'begin' ;
DO: 'do' ;
DOWNTO: 'downto' ;
ELSE: 'else' ;
END: 'end' ;
FOR: 'for' ;
FUNCTION: 'function' ;
IF: 'if' ;
NOT: 'not' ;
OF: 'of' ;
OR: 'or' ;
PACKED: 'packed' ;
PROCEDURE: 'procedure' ;
PROGRAM: 'program' ;
THEN: 'then' ;
TO: 'to' ;
TYPE: 'type' ;
VAR: 'var' ;
TRUE: 'true' ;
FALSE: 'false' ;
EXIT : 'exit' ;


// -------------------- Types identifiers --------------------
INTEGER: 'integer' ;
REAL: 'real' ;
BOOLEAN: 'boolean' ;
CHAR: 'char' ;
STRING: 'string' ;


// -------------------- Numbers --------------------
fragment DIGIT: [0-9] ;
fragment DIGIT_SEQUENCE: DIGIT+ ;
fragment SCALE_FACTOR: ( PLUS | MINUS )? DIGIT_SEQUENCE ;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE ;
UNSIGNED_INTEGER: DIGIT_SEQUENCE ;
UNSIGNED_REAL: ( DIGIT_SEQUENCE '.' FRACTIONAL_PART ( 'e' SCALE_FACTOR )? )| ( DIGIT_SEQUENCE 'e' SCALE_FACTOR ) ;


// -------------------- String literals --------------------
fragment APOSTROPHE_IMAGE: '\'\'' ;
fragment STRING_CHARACTER: ~['\r\n] ;
fragment STRING_ELEMENT: APOSTROPHE_IMAGE | STRING_CHARACTER ;
CHARACTER_STRING: '\'' STRING_ELEMENT+ '\'' ;


// -------------------- Comments --------------------
COMMENTARY: ( ('{' .*? '}') | ('(*' .*? '*)') | ('//' ~[\r\n]*) ) -> skip ;


// -------------------- Identifiers --------------------
IDENTIFIER: [a-z_] [a-z0-9_]* ;


// -------------------- Token separators --------------------
WS: [ \t\r\n]+ -> skip ;