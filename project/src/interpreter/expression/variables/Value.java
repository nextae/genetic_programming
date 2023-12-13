package interpreter.expression.variables;

import interpreter.expression.toplevel.Expr;

public final class Value extends Expr {
    public String type;
    public String value;

    public Value(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public Value(String value) {
        this.value = value;

//        if(value.matches("\"(.*)\"")) type = "string";
//        else if(value.equals("true") || value.equals("false") || value.equals("null")) type = "bool";
//        else if(value.contains(".")) type = "float";
//        else if(!value.equals(""))type = "int";
//        else type = "notInit";
        if(value.matches("\"(.*)\"")) type = "string";
        else if(value.equals("true") || value.equals("false") || value.equals("null")) type = "bool";
        else if(value.matches("-?[0-9]*\\.[0-9]+") || value.matches("-?[0-9]\\.")) type = "float";
        else if(value.matches("-?[1-9]([0-9])*") || value.matches("0"))type = "int";
        else if(value.isEmpty())type = "notInit";
        else type ="wrongType";
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String evaluate() {
        return value;
    }
}
