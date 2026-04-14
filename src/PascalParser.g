parser grammar PascalParser;

options {
    tokenVocab = PascalLexer;
}

// Fix isso porque não definimos DIRECTIVE no lexer
directive : IDENTIFIER ;

// -------------------- 6.2 Blocks, scopes, and activations --------------------

block : label_declaration_part constant_definition_part type_definition_part variable_declaration_part procedure_and_function_declaration_part statement_part ;

label_declaration_part : ( LABEL IDENTIFIER ( COMMA LABEL )* SEMICOLON )? ;
constant_definition_part : ( CONST constant_definition SEMICOLON ( constant_definition SEMICOLON )* )? ;
type_definition_part : ( TYPE type_definition COMMA ( type_definition SEMICOLON )* )? ;
variable_declaration_part : ( VAR variable_declaration COMMA ( variable_declaration SEMICOLON )* )? ;
procedure_and_function_declaration_part : ( ( procedure_declaration | function_declaration ) SEMICOLON )* ;

statement_part : compound_statement ;


// -------------------- 6.3 Constant-definitions --------------------

constant_definition : IDENTIFIER EQUAL_TO constant ;
constant : ( ( SIGN )? ( UNSIGNED_INTEGER | UNSIGNED_REAL | constant_identifier ) ) | CHARACTER_STRING ;
constant_identifier : IDENTIFIER ;


// -------------------- 6.4 Type-definitions --------------------

// TODO: Avaliar quais desses tipos vamos de fato implementar

type_definition : IDENTIFIER EQUAL_TO type_denoter ;
type_denoter : type_identifier | new_type ;

// Não vamos implementar new_pointer_type
// new_type : new_ordinal_type | new_structured_type | new_pointer_type ;
new_type : new_ordinal_type | new_structured_type ;

simple_type_identifier : type_identifier ;
structured_type_identifier : type_identifier ;
pointer_type_identifier : type_identifier ;
type_identifier : IDENTIFIER ;

// 6.4.2 Simple-types
simple_type : ordinal_type | real_type_identifier ;
ordinal_type : new_ordinal_type | ordinal_type_identifier ;
new_ordinal_type : enumerated_type | subrange_type ;
ordinal_type_identifier : type_identifier ;
real_type_identifier : type_identifier ;

// 6.4.2.3 Enumerated-types
enumerated_type : OPEN_PARENTHESIS identifier_list CLOSE_PARENTHESIS ;
identifier_list : IDENTIFIER ( COMMA IDENTIFIER )* ;

// 6.4.2.4 Subrange-types
subrange_type : constant RANGE constant ;

// 6.4.3 Structured-types
structured_type : new_structured_type | structured_type_identifier ;
new_structured_type : ( PACKED )? unpacked_structured_type ;
unpacked_structured_type : array_type | record_type | set_type | file_type ;

// 6.4.3.2 Array-types
array_type : ARRAY OPEN_BRACKET index_type ( COMMA index_type )* CLOSE_BRACKET OF component_type ;
index_type : ordinal_type ;
component_type : type_denoter ;

// 6.4.3.3 Record-types
record_type : RECORD field_list END ;
field_list : ( ( ( fixed_part  ( SEMICOLON variant_part )* ) | variant_part ) ( SEMICOLON )? )? ;
fixed_part : record_section ( COMMA record_section )* ;
record_section : identifier_list COLON type_denoter ;
field_identifier : IDENTIFIER ;
variant_part : CASE variant_selector OF variant ( COMMA variant )* ;
variant_selector : ( tag_field COLON )? tag_type ;
tag_field : IDENTIFIER ;
variant : case_constant_list COLON OPEN_PARENTHESIS field_list CLOSE_PARENTHESIS ;
tag_type : ordinal_type_identifier ;
case_constant_list : case_constant ( COMMA case_constant )* ;
case_constant : constant ;

// 6.4.3.4 Set-types
set_type : SET OF base_type ;
base_type : ordinal_type ;

// 6.4.3.5 File-types
file_type : FILE OF component_type ;

// 6.4.4 Pointer-types
// Não vamos implementar


// -------------------- 6.5 Declarations and denotations of variables --------------------

// 6.5.1 Variable-declarations
variable_declaration : identifier_list COLON type_denoter ;

// Não vamos implementar identified_variable e buffer_variable 
// variable_access : entire_variable | component_variable | identified_variable | buffer_variable ;
variable_access : entire_variable | component_variable ;

// 6.5.2 Entire-variables
entire_variable : variable_identifier ;
variable_identifier : IDENTIFIER ;

// 6.5.3 Component-variables
component_variable : indexed_variable | field_designator ;

// 6.5.3.2 Indexed-variables
indexed_variable : array_variable OPEN_BRACKET ( index_expression  ( COMMA index_expression )* ) CLOSE_BRACKET ;
array_variable : variable_access ;
index_expression : expression ;

// 6.5.3.3 Field-designators
field_designator : ( record_variable PERIOD field_specifier ) | field_designator_identifier ;
record_variable : variable_access ;
field_specifier : field_identifier ;

// 6.5.5 Buffer-variables
// Will not be implemented
file_variable : variable_access ;

// -------------------- 6.6 Procedure and function declarations --------------------

// 6.6.1 Procedure-declarations

procedure_declaration : procedure_heading SEMICOLON directive
                        | procedure_identification SEMICOLON procedure_block
                        | procedure_heading SEMICOLON procedure_block ;

procedure_heading : PROCEDURE IDENTIFIER ( formal_parameter_list )? ;
procedure_identification : PROCEDURE procedure_identifier ;
procedure_identifier : IDENTIFIER ;
procedure_block : block ;

// 6.6.2 Function-declarations

function_declaration : function_heading SEMICOLON directive
                        | function_identification SEMICOLON function_block
                        | function_heading SEMICOLON function_block ;

function_heading : FUNCTION IDENTIFIER ( formal_parameter_list )? COLON result_type ;
function_identification : FUNCTION function_identifier ;
function_identifier : IDENTIFIER ;

// TODO: pointer_type_identifier não será implementado
// result_type : simple_type_identifier | pointer_type_identifier;
result_type : simple_type_identifier;
function_block : block ;

// 6.6.3 Parameters

// 6.6.3.1 General

formal_parameter_list : OPEN_BRACKET formal_parameter_section ( SEMICOLON formal_parameter_section )* CLOSE_BRACKET ;
formal_parameter_section : value_parameter_speficiation
                        | variable_parameter_specification
                        | procedural_parameter_specification
                        | functional_parameter_specification ;

value_parameter_speficiation : identifier_list COLON type_identifier ;
variable_parameter_specification : VAR identifier_list COLON type_identifier ;
procedural_parameter_specification : procedure_heading ;
functional_parameter_specification : function_heading ;

// 6.6.3.7 Conformant array parameters
// TODO : Pulei essa seção

// -------------------- 6.7 Expressions --------------------

// 6.7.1 General

expression : simple_expression ( relational_operator simple_expression )? ;
simple_expression : ( SIGN )? term ( adding_operator term )* ;
term : factor ( multiplying_operator factor )* ;
factor : variable_access
        | unsigned_constant
        | function_designator
        | set_constructor
        | OPEN_PARENTHESIS expression CLOSE_PARENTHESIS
        | NOT factor ;
unsigned_constant : UNSIGNED_INTEGER | UNSIGNED_REAL | CHARACTER_STRING | constant_identifier | NIL;
set_constructor : OPEN_BRACKET ( member_designator ( COMMA member_designator )* )? CLOSE_BRACKET ;
member_designator : expression | ( RANGE expression )? ;

// 6.7.2 Operators

// 6.7.2.1 General

multiplying_operator : MULTIPLICATION | DIVISION | DIV | MOD | AND ;
adding_operator : ADDITION | SUBTRACTION | OR ;
relational_operator : EQUAL_TO | NOT_EQUAL_TO | LESS_THAN | LESS_THAN_OR_EQUAL_TO | GREATER_THAN | GREATER_THAN_OR_EQUAL_TO | IN ;

// 6.7.2.3 Boolean operators
boolean_expression : expression ;

// 6.7.3 Function-designators
function_designator : function_identifier ( actual_parameter_list )? ;
actual_parameter_list : OPEN_PARENTHESIS actual_parameter ( COMMA actual_parameter )* CLOSE_PARENTHESIS ;
actual_parameter : expression | variable_access | procedure_identifier | function_identifier ;


// -------------------- 6.8 Statements --------------------

// 6.8.1 General

statement : ( IDENTIFIER COLON )? ( simple_statement | structured_statement ) ;

// 6.8.2 Simple-statements

// 6.8.2.1 General

// Não vamos implementar goto_statement
// simple_statement : empty_statement 
//                   | assignment_statement
//                   | procedure_statement
//                   | goto_statement
//                  ;

simple_statement : empty_statement 
                  | assignment_statement
                  | procedure_statement
                 ;

empty_statement : /* empty */ ;

// 6.8.2.2 Assignment-statements
assignment_statement : ( variable_access | function_identifier ) ASSIGNMENT expression ;

// 6.8.2.3 Procedure-statements
procedure_statement : procedure_identifier ( actual_parameter_list )?
                    | read_parameter_list
                    | readln_parameter_list
                    | write_parameter_list
                    | writeln_parameter_list
                    ;

// 6.8.2.4 Goto-statements
// Não vamos implementar


// 6.8.3 Structured-statements

// 6.8.3.1 General

structured_statement : compound_statement
                     | conditional_statement
                     | repetitive_statement
                     | with_statement
                     ;

statement_sequence : statement ( SEMICOLON statement )* ;

// 6.8.3.2 Compound-statements
compound_statement : BEGIN statement_sequence END ;

// 6.8.3.3 Conditional-statements
conditional_statement : if_statement | case_statement ;

// 6 .8 .3 .4 If-statements
if_statement : IF boolean_expression THEN statement ( else_part )? ;
else_part : ELSE statement ;

// 6.8.3.5 Case-statements
case_statement : CASE case_index OF case_list_element ( SEMICOLON case_list_element )* (SEMICOLON)? END ;
case_list_element : case_constant_list COLON statement ;
case_index : expression ;

// 6.8.3.6 Repetitive-statements
repetitive_statement : repeat_statement | while_statement | for_statement ;

// 6.8.3.7 Repeat-statements
repeat_statement : REPEAT statement_sequence UNTIL boolean_expression ;

// 6.8.3.8 While-statements
while_statement : WHILE boolean_expression DO statement ;

// 6.8.3.9 For-statements
for_statement : FOR control_variable ASSIGNMENT initial_value ( TO | DOWNTO ) final_value DO statement ;
control_variable : entire_variable ;
initial_value : expression ;
final_value : expression ;

// 6.8.3.10 With-statements
with_statement : WITH record_variable_list DO statement ;
record_variable_list : record_variable ( COMMA record_variable )* ;
field_designator_identifier : IDENTIFIER ;


// -------------------- 6.9 Input and output --------------------

// 6.9.1 The procedure read
read_parameter_list : OPEN_PARENTHESIS ( file_variable COMMA )? variable_access ( COMMA variable_access )* CLOSE_PARENTHESIS ;

// 6.9.2 The procedure readln
readln_parameter_list : ( OPEN_PARENTHESIS ( file_variable | variable_access ) ( COMMA variable_access )* CLOSE_PARENTHESIS )? ;

// 6.9.3 The procedure write
write_parameter_list : OPEN_PARENTHESIS ( file_variable COMMA )? write_parameter ( COMMA write_parameter )* CLOSE_PARENTHESIS ;
write_parameter : expression ( COLON expression ( COLON expression )? )? ;

// 6.9.4 The procedure writeln
writeln_parameter_list : ( OPEN_PARENTHESIS ( file_variable | write_parameter ) ( COMMA write_parameter )* CLOSE_PARENTHESIS )? ;


// -------------------- 6.10 Programs --------------------

program : program_heading SEMICOLON program_block PERIOD ;
program_heading : PROGRAM IDENTIFIER ( OPEN_BRACKET program_parameter_list CLOSE_BRACKET )? ;
program_parameter_list : identifier_list ;
program_block : block ;