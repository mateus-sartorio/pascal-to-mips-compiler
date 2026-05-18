parser grammar PascalParser;

@header {
package parser;
}

options {
	tokenVocab = PascalLexer;
}


// -------------------- 6.2 Blocks, scopes, and activations --------------------

block: type_definition_part variable_declaration_part procedure_and_function_declaration_part compound_statement ;

type_definition_part: ( TYPE ( type_definition SEMICOLON )+ )?;
variable_declaration_part: ( VAR variable_declaration ( SEMICOLON variable_declaration )* SEMICOLON )*;
procedure_and_function_declaration_part: ( ( procedure_declaration | function_declaration ) SEMICOLON )* ;


// -------------------- 6.3 Constant-definitions --------------------

constant_deifinition: IDENTIFIER EQUAL_TO constant ;
constant: ( ( PLUS | MINUS )? UNSIGNED_INTEGER | UNSIGNED_REAL ) | CHARACTER_STRING ;


// -------------------- 6.4 Type-definitions --------------------

primitive_type: INTEGER | REAL | BOOLEAN | CHAR | STRING ;
type_definition: IDENTIFIER EQUAL_TO type_denoter ;
type_denoter: primitive_type | array_type | IDENTIFIER ;

// 6.4.2 Simple-types Not needed

// 6.4.2.3 Enumerated-types
identifier_list: IDENTIFIER ( COMMA IDENTIFIER )* ;

// 6.4.2.4 Subrange-types
subrange_type: constant RANGE constant ;

// 6.4.3.2 Array-types
array_type: ( PACKED )? ARRAY OPEN_BRACKET subrange_type ( COMMA subrange_type )* CLOSE_BRACKET OF type_denoter ;


// -------------------- 6.5 Declarations and denotations of variables --------------------

// 6.5.1 Variable-declarations
variable_declaration: identifier_list COLON type_denoter;

variable_access: IDENTIFIER | indexed_variable;

// 6.5.3.2 Indexed-variables
indexed_variable: IDENTIFIER OPEN_BRACKET (expression ( COMMA expression)* ) CLOSE_BRACKET ;


// -------------------- 6.6 Procedure and function declarations --------------------

// 6.6.1 Procedure-declarations

procedure_declaration: procedure_heading SEMICOLON compound_statement ;

procedure_heading: PROCEDURE IDENTIFIER ( formal_parameter_list )? ;

// 6.6.2 Function-declarations

function_declaration: function_heading SEMICOLON compound_statement ;

function_heading: FUNCTION IDENTIFIER (formal_parameter_list)? COLON type_denoter ;

// 6.6.3 Parameters

// 6.6.3.1 General

formal_parameter_list: OPEN_PARENTHESIS formal_parameter_section ( SEMICOLON formal_parameter_section )* CLOSE_PARENTHESIS ;

formal_parameter_section:
	value_parameter_speficiation
	| variable_parameter_specification
	| procedure_heading
	| function_heading
  ;

value_parameter_speficiation: identifier_list COLON type_denoter ;

variable_parameter_specification: VAR identifier_list COLON type_denoter ;


// -------------------- 6.7 Expressions --------------------

// 6.7.1 General

expression: simple_expression (relational_operator simple_expression)? ;
simple_expression: ( PLUS | MINUS)? term ( adding_operator term)* ;
term: factor ( multiplying_operator factor)* ;

factor:
	variable_access
	| unsigned_constant
	| function_designator
	| OPEN_PARENTHESIS expression CLOSE_PARENTHESIS
	| NOT factor
  ;

unsigned_constant:
	UNSIGNED_INTEGER
	| UNSIGNED_REAL
	| CHARACTER_STRING
  ;

// 6.7.2 Operators

// 6.7.2.1 General

multiplying_operator: MULTIPLICATION | DIVISION | AND ;
adding_operator: PLUS | MINUS | OR ;
relational_operator:
	EQUAL_TO
	| NOT_EQUAL_TO
	| LESS_THAN
	| LESS_THAN_OR_EQUAL_TO
	| GREATER_THAN
	| GREATER_THAN_OR_EQUAL_TO
  ;

// 6.7.3 Function-designators
function_designator: IDENTIFIER ( actual_parameter_list )? ;
actual_parameter_list: OPEN_PARENTHESIS actual_parameter ( COMMA actual_parameter )* CLOSE_PARENTHESIS ;
actual_parameter: expression | variable_access | IDENTIFIER ;


// -------------------- 6.8 Statements --------------------

// 6.8.1 General

statement: ( IDENTIFIER COLON )? ( simple_statement | structured_statement ) ;

// 6.8.2 Simple-statements

// 6.8.2.1 General

simple_statement:
	empty_statement
	| assignment_statement
	| procedure_statement
  ;

empty_statement: /* empty */;

// 6.8.2.2 Assignment-statements
assignment_statement: variable_access ASSIGNMENT expression ;

// 6.8.2.3 Procedure-statements

procedure_statement: IDENTIFIER ( actual_parameter_list )? ;

// 6.8.3 Structured-statements

// 6.8.3.1 General

structured_statement:
	compound_statement
	| if_statement
	| for_statement
  ;

statement_sequence: statement ( SEMICOLON statement )* ;

// 6.8.3.2 Compound-statements
compound_statement: BEGIN statement_sequence END ;

// 6 .8 .3 .4 If-statements
if_statement: IF expression THEN statement ( else_part )? ;
else_part: ELSE statement ;

// 6.8.3.9 For-statements
for_statement: FOR IDENTIFIER ASSIGNMENT expression ( TO | DOWNTO ) expression DO statement ;


// -------------------- 6.10 Programs --------------------

program: program_heading SEMICOLON block PERIOD ;

program_heading: PROGRAM IDENTIFIER ( OPEN_BRACKET identifier_list CLOSE_BRACKET )? ;