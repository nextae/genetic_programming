grammar Hello_copy;

@header {
    package interpreter.antlr;
}

SEMICOLON: ';';

STRING
    : '"'(.)*?'"';

BOOL
    :'true'
    | 'false'
    | 'null';

INT
    : '-'?[1-9]([0-9])*
    | '0';

FLOAT
    : '-'?[0-9]*'.'[0-9]+// Not sure about the scientific notation
    | '-'?[0-9]'.';

LOGIC_OP
    : '==' | '!='
    | '>' | '<' | '>=' | '<='
    | 'and'
    | 'or'
    | 'xor'
    | '!' | 'not'
    | 'nand'
    | 'nor';

WS
    : [ \t\n\r]+ -> skip;

TYPE
    : 'int'
    | 'float'
    | 'bool'
    | 'var'
    | 'string'
    | MULTIPLE;


MULTIPLE
    : 'multiple';

VARNAME
    : [a-z][a-zA-Z0-9_]*;

FUNCNAME
    : [A-Z][a-zA-Z0-9_]*;

ARITHMETIC
    : '^' //
    | '*' | '/' //
    | '+' | '-' //
    | '%' //
    | '^='
    | '*=' | '/='
    | '+=' | '-='
    | '%=' ;

LBRACKET : '(';
RBRACKET : ')';

start
    : line* EOF;

line
    : statement
    | block //
    | ifBlock //
    | forBlock
    | whileBlock //
    | commentBlock; //

commentBlock
    : '$' ~('$')* '$';

statement
    : (varDeclaration
    | multipleDeclaration
    | varIncrement //
    | assignment //
    | funcCall //
    | print)? ';' //
    | funcDeclaration; //

varDeclaration
    : TYPE VARNAME                  # NoValueDeclare //
    | TYPE VARNAME '=' expr         # AssignDeclare //
    | TYPE multipleAssignment       # MultipleDeclare
    ;

multipleDeclaration //Może powinno być ...TYPE'['INT']', żeby był określony rozmiar?
    : MULTIPLE TYPE VARNAME
    | MULTIPLE TYPE VARNAME '=' '['expr?']'
    | MULTIPLE TYPE VARNAME '=' '['(expr ',')+ expr']';
// multiple int[] tab;
// multiple int[] tab2 = []
// multiple int[] tab3 = [5, 7, 1, 3, 9, 2]
varIncrement //
    : VARNAME ('++' | '--'); //

funcDeclaration //
    : FUNCNAME arglistDeclare '->' arglistDeclare block;
/* Example:
int a,b,c;
a=1; b=5;

int add(int o1, int o2) -> (int result){
    result = a+b;
}

add(a,b) -> (c); $ albo również wersja poniżej, jeżeli tylko jedna zmienna zwracana $
c = add(a,b);
*/

print //
    : 'print'LBRACKET (expr) RBRACKET; //

funcCall //
    : FUNCNAME arglistInput (('->')arglistOutput)?; //TODO: Not so sure

whileBlock //
    : 'while' expr block; //

forBlock
    : 'foreach' TYPE VARNAME 'in' VARNAME block ;

ifBlock //
    : 'if' expr elseIfBlock; //

elseIfBlock //
    : block                         # BlockIf //
    | block 'else' block            # Else //
    | block 'else' ifBlock          # ElseIf //
    ;

arglistDeclare
    : LBRACKET (varDeclaration? | ((varDeclaration',')+ varDeclaration)) RBRACKET; //

arglistInput
    : LBRACKET ( expr? | ((expr',')+ expr) ) RBRACKET; //

arglistOutput
    : LBRACKET (
          TYPE? VARNAME
        | ((TYPE? VARNAME',')+ TYPE? VARNAME)
    ) RBRACKET; //
//TODO: Maybe add type() function like in Python?
expr //Things with (in theory) value behind them
    : VARNAME                       # Variable //
    | funcCall                      # FunctionCall
    | LBRACKET expr RBRACKET        # Brackets //
    | expr '^' expr                 # Power //
    | expr ('*' | '/') expr         # Multiplicative //
    | expr ('+' | '-') expr         # Additive //
    | expr '%' expr                 # Modulo //
    | expr ('==' | '!=' | '>'
          | '<' | '>=' | '<=') expr # Compare //
    | expr ('and' | 'or' | 'xor'
        | 'nand' | 'nor') expr      # Combination //
    | ('!' | 'not') expr            # Negation //
    | VARNAME'['INT']'              # Table
    | funcCall '['INT']'            # FuncCallTable
    | val                           # Value //Not sure if done
    | cast expr                     # Casting //
    ;

val
    : STRING                        # STRING
    | INT                           # INT
    | FLOAT                         # FLOAT
    | BOOL                          # BOOL
    ;

assignment
    : VARNAME '=' expr              # Assign //
    | VARNAME ('^='| '*=' | '/='
      | '+=' | '-=' | '%=' ) expr   # ArithmeticAssign //
    ;

multipleAssignment
    : (VARNAME',')+ VARNAME '=' (expr',')+ expr; // Nie wiem, czy zgodność liczby zmiennych po lewej i wartości po prawej powinno być załatwione na poziomie gramatyki

block // Not sure if done, but so far everything works well
    : '{' line* '}';

cast
    : LBRACKET'@'TYPE RBRACKET; //Try to cast to given type (maybe remove brackets?)