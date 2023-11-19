grammar Lang;

@header {
    package antlr;
}

BOOL
    :'true'
    | 'false';

INT
    : '-'?[1-9]([0-9])*
    | '0';

FLOAT
    : '-'?[0-9]*'.'[0-9]+
    | '-'?[0-9]'.';

WS
    : [ \t\n\r]+ -> skip;

TYPE
    : 'int'
    | 'float'
    | 'bool';

VARNAME
    : [a-z][a-zA-Z0-9_]*;

LBRACKET : '(';
RBRACKET : ')';

start //
    : line* EOF;

line //
    : statement //
    | block //
    | ifBlock //
    | whileBlock;//

statement
    : (varDeclaration //
    | assignment //
    | print )? ';';//

varDeclaration
    : TYPE VARNAME '=' expr         # AssignDeclare //
    ;

print //
    : 'print'LBRACKET (expr) RBRACKET; //

stdin
    : 'input'LBRACKET RBRACKET;

whileBlock //
    : 'while' expr block; //

ifBlock //
    : 'if' expr elseIfBlock; //

elseIfBlock //
    : block                         # BlockIf //
    | block 'else' block            # Else //
    | block 'else' ifBlock          # ElseIf //
    ;

expr //Things with (in theory) value behind them
    : VARNAME                       # Variable //
    | LBRACKET expr RBRACKET        # Brackets //
    | expr '^' expr                 # Power //
    | expr ('*' | '/') expr         # Multiplicative //
    | expr ('+' | '-') expr         # Additive //
    | expr '%' expr                 # Modulo //
    | expr ('==' | '!=' | '>'
          | '<' | '>=' | '<=') expr # Compare //
    | expr ('and' | 'or') expr      # Combination //
    | '!' expr                      # Negation //
    | val                           # Value //
    | stdin                         # Input
    ;

val
    : INT                           # INT
    | FLOAT                         # FLOAT
    | BOOL                          # BOOL
    ;

assignment
    : VARNAME '=' expr              # Assign //
    ;

block //
    : '{' line* '}';