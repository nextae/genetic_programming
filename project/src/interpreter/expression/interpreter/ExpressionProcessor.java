package interpreter.expression.interpreter;

import interpreter.expression.arithmetic.*;
import interpreter.expression.blocks.Block;
import interpreter.expression.blocks.IfBlock;
import interpreter.expression.blocks.WhileBlock;
import interpreter.expression.library.*;
import interpreter.expression.logic.*;
import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Line;
import interpreter.expression.variables.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class ExpressionProcessor {

    private ExpressionProcessor parent = null;
    private List<Line> list;
    public static List<String> semanticErrors = new ArrayList<>();
    private Map<String, Value> values;
    private Map<String, String> types;
    private boolean firstRunError = false;
    private int maxInstructions;
    private int computedInstructions;
    private static List<Integer> inputs;
    private int currentInput = 0;

    public void reset(List<Line> lines, List<Integer> inputs){
        currentInput = 0;
        computedInstructions = 0;
        values.clear();
        types.clear();
        semanticErrors.clear();
        list = lines;
        ExpressionProcessor.inputs = inputs;
    }
    private Value getBoolean(Value value){
//        if (value.type.equals("notInit")) semanticErrors.add("Error: variable not declared");
        value = (Value) checkIfNull(value);
        if (value.type.equals("notInit")) {
//            semanticErrors.add("Error: variable not declared");
            return new Value("1");
        }
        else if (value.type.equals("bool")) return value;
        else {
            float f = parseFloat(value.value);
            return f>=0?new Value("true"):new Value("false");
        }
//        return new Value("false");
    }

    private Value getFloat(Value value){
        value = (Value) checkIfNull(value);
        switch (value.type) {
//            case "notInit" -> semanticErrors.add("Error: variable not declared");
            case "notInit" -> {
//                semanticErrors.add("Error: variable not declared");
                return new Value("1");
            }
            case "float" -> {
                return value;
            }
            case "bool" -> {
                return value.value.equals("true") ? new Value("1.0") : new Value("-1.0");
            }
            default -> {
                return new Value(String.valueOf(parseFloat(value.value)));
            }
        }
//        return new Value("0.0");
    }

    private Value getInt(Value value){
        value = (Value) checkIfNull(value);
        switch (value.type) {
//            case "notInit" -> semanticErrors.add("Error: variable not declared");
            case "notInit" -> {
//                semanticErrors.add("Error: variable not declared");
                return new Value("1");
            }
            case "int" -> {
                return value;
            }
            case "bool" -> {
                return value.value.equals("true") ? new Value("1") : new Value("-1");
            }
            default -> {
                return new Value(String.valueOf(Math.round(parseFloat(value.value))));
            }
        }
//        return new Value("0");
    }

    private Value castToLeftType(Value left, Value right){
        Value rightCasted;
        if (left.type == null || right.type == null) return new Value("1");
        switch (left.type){
            case "int" -> rightCasted = getInt(right);
            case "float" -> rightCasted = getFloat(right);
            case "bool" -> rightCasted = getBoolean(right);
            default -> rightCasted = new Value("1"); // Should never go to default
        }
        return rightCasted;
    }

    private Value castToLeftType(String left_type, Value right){
        Value rightCasted;
        if (left_type == null || right.type == null) return new Value("1");
        switch (left_type){
            case "int" -> rightCasted = getInt(right);
            case "float" -> rightCasted = getFloat(right);
            case "bool" -> rightCasted = getBoolean(right);
            default -> rightCasted = new Value("1"); // Should never go to default
        }
        return rightCasted;
    }

    private Line checkIfNull(Line line){
        if(line == null) return new Value("0");
        else return line;
    }

    private int parseInt(String s){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException n) {
            return 1;
        }
    }

    private float parseFloat(String s){
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException n) {
            return 1;
        }
    }

    public ExpressionProcessor(List<Line> lines, List<Integer> inputs){
        ExpressionProcessor.inputs = inputs;
        list = lines;
        values = new HashMap<>();
        types = new HashMap<>();
        computedInstructions = 0;
        maxInstructions = 1000;
    }

    public ExpressionProcessor(List<Line> lines, ExpressionProcessor parent){
        list = lines;
        this.values = parent.values;
        this.types = parent.types;
        this.parent = parent;
    }

    /**
     * Evaluates <i>list</i> of Lines for my language
     * @param list if list==null uses this.list
     * @return List of string evaluations
     */
    public List<String> getEvalResults(List<Line> list){
        List<String> evaluations = new ArrayList<>();

        if(list == null)list = this.list;
        for(Line l: list){
            computedInstructions++;
            if(computedInstructions >= maxInstructions) break;
            if(l instanceof VarDeclaration v){
                if(values.containsKey(v.variable.id)) {
                    semanticErrors.add("Error: variable `" + v.variable.id + "` " +
                            "already declared (" + v.variable.token.getLine() + ")");
                    firstRunError = true;
                }
                values.put(v.variable.id, null);
                types.put(v.variable.id, v.variable.type);
            }
        }
        if(!firstRunError) {
            for (Line l : list) {
                computedInstructions++;
                if(computedInstructions >= maxInstructions) break;
                if (l instanceof Variable v) {
                    System.out.println("OH-OH! variable value: " + v.value);
                    try {
                        values.put(v.id, eval(v.value));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (l instanceof VarDeclaration v) {
                    Value value = castToLeftType(v.variable.type, eval(v.variable.value));
                    values.put(v.variable.id, value);

                } else if (l instanceof Assignment a) {
                    String idType = "";
                    if (!values.containsKey(a.id)) {
                        if (parent != null) idType = parent.types.get(a.id);
                    } else idType = types.get(a.id);
                    Value value = castToLeftType(idType, eval(a.expr));
                    if (!values.containsKey(a.id)) {
                        if (parent != null && parent.values.containsKey(a.id)) {
                            parent.values.put(a.id, value);
                        } else
                            semanticErrors.add("Error: variable `" + a.id + "` not declared (" + a.token.getLine() + ")");
                    } else {
                        values.put(a.id, value);
                    }
                } else if (l instanceof Print p) {
                    if (p.id != null) {
                        Value result = getInt(eval(new VarName(p.id, p.token)));
                        if (result == null)
                            semanticErrors.add("Error: variable " + p.id + " not declared (" + p.token.getLine() + ")");
                        else if (result.type.equals("notInit"))
                            semanticErrors.add("Error: variable `" + p.id + "` not initialized (" + p.token.getLine() + ")");
                        else evaluations.add(result.toString());
                    } else {
                        evaluations.add(getInt(eval(p.expr)).toString());
                    }
                } else if (l instanceof WhileBlock w) {
                    Value condition = getBoolean(eval(w.condition));
                    if (condition.type.equals("notInit"))
                        semanticErrors.add("Error: not initialized value (" + w.token.getLine() + ")");
                    else if (condition.value.equals("true")) {
                        List<Line> whileList = new ArrayList<>();
                        whileList.add(w.block);
                        int counter = 0;
                        while (condition.value.equals("true") && computedInstructions <= maxInstructions && counter < 100) {
                            computedInstructions++;
                            counter++;
                            evaluations.addAll(getEvalResults(whileList));
                            condition = eval(w.condition);
                        }
                    }
                } else if (l instanceof IfBlock i) {
                    Value condition = getBoolean(eval(i.condition));
                    if (condition.type.equals("notInit"))
                        semanticErrors.add("Error: not initialized value (" + i.token.getLine() + ")");
                    else if (condition.value.equals("true")) {
//                        ExpressionProcessor ep = new ExpressionProcessor(i.elseBlock.ifBlock.lines, this);
//                        evaluations.addAll(ep.getEvalResults(null));
                        evaluations.addAll(this.getEvalResults(i.elseBlock.ifBlock.lines));
                    } else if (condition.value.equals("false") && i.elseBlock.elseBlock != null) {
//                        ExpressionProcessor ep = new ExpressionProcessor(i.elseBlock.elseBlock.lines, this);
//                        evaluations.addAll(ep.getEvalResults(null));
                        evaluations.addAll(this.getEvalResults(i.elseBlock.elseBlock.lines));
                    } else if (condition.value.equals("false") && i.elseBlock.child != null) {
                        List<Line> childList = new ArrayList<>();
                        childList.add(i.elseBlock.child);
                        evaluations.addAll(getEvalResults(childList));
                    }
                } else if (l instanceof Block b) {
//                    ExpressionProcessor ep = new ExpressionProcessor(b.lines, this);
//                    evaluations.addAll(ep.getEvalResults(null));
                    evaluations.addAll(this.getEvalResults(b.lines));
                }
            }
        }
        return evaluations;
    }

    private Value eval(Line l){
        Value result = new Value("");
        l = checkIfNull(l);
        if(l instanceof Value v){
            result = new Value(v.value);
        } else if (l instanceof VarName v){
            if (values.containsKey(v.id)) result = values.get(v.id);
            else if(parent != null && parent.values.containsKey(v.id)) result = parent.values.get(v.id);
            else semanticErrors.add("Error: variable `"+v.id+"` not declared ("+v.token.getLine()+")");
        } else if (l instanceof Addition a) {
            Value left = eval(checkIfNull(a.left));
            Value right = eval(checkIfNull(a.right));
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+a.token.getLine()+")");
            else if(left.type.equals(right.type)) {
                if (a.operator.equals("+")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = parseInt(left.value);
                            int rightInt = parseInt(right.value);
                            result = new Value(Integer.toString(leftInt + rightInt));
                        }
                        case "float" -> {
                            float leftFloat = parseFloat(left.value);
                            float rightFloat = parseFloat(right.value);
                            result = new Value(Float.toString(leftFloat + rightFloat));
                        }
                        case "bool" -> {
                            Value leftToInt = getInt(left);
                            Value rightToInt = getInt(right);
                            result = getBoolean(eval(new Addition(leftToInt, a.operator, rightToInt, a.token)));
                        }
                    }
                } else if (a.operator.equals("-")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = parseInt(left.value);
                            int rightInt = parseInt(right.value);
                            result = new Value(Integer.toString(leftInt - rightInt));
                        }
                        case "float" -> {
                            float leftFloat = parseFloat(left.value);
                            float rightFloat = parseFloat(right.value);
                            result = new Value(Float.toString(leftFloat - rightFloat));
                        }
                        case "bool" -> {
                            Value leftToInt = getInt(left);
                            Value rightToInt = getInt(right);
                            result = getBoolean(eval(new Addition(leftToInt, a.operator, rightToInt, a.token)));
                        }
                    }
                }
            } else { // Cast to type of the left expression
                result = eval(new Addition(left, a.operator, castToLeftType(left, right), a.token));
            }

        } else if (l instanceof Multiplication m){
            Value left = eval(checkIfNull(m.left));
            Value right = eval(checkIfNull(m.right));
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+m.token.getLine()+")");
            else if(left.type.equals(right.type)) {
                if (m.operator.equals("*")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = parseInt(left.value);
                            int rightInt = parseInt(right.value);
                            result = new Value(Integer.toString(leftInt * rightInt));
                        }
                        case "float" -> {
                            float leftFloat = parseFloat(left.value);
                            float rightFloat = parseFloat(right.value);
                            result = new Value(Float.toString(leftFloat * rightFloat));
                        }
                        case "bool" -> {
                            Value leftToInt = getInt(left);
                            Value rightToInt = getInt(right);
                            result = getBoolean(eval(new Multiplication(leftToInt, m.operator, rightToInt, m.token)));
                        }
                    }
                } else if (m.operator.equals("/")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = parseInt(left.value);
                            int rightInt = parseInt(right.value);
                            if (rightInt == 0) rightInt = 1;
                            try {
                                result = new Value(Integer.toString(leftInt / rightInt));
                            } catch (ArithmeticException e) {
                                semanticErrors.add("Error: dividing by zero! (" + m.token.getLine() + ")");
                            }
                        }
                        case "float" -> {
                            float leftFloat = parseFloat(left.value);
                            float rightFloat = parseFloat(right.value);
                            if (rightFloat == 0) rightFloat = 1;
                            try {
                                result = new Value(Float.toString(leftFloat / rightFloat));
                            } catch (ArithmeticException e) {
                                semanticErrors.add("Error: dividing by zero! (" + m.token.getLine() + ")");
                            }
                        }
                        case "bool" -> {
                            Value leftToInt = getInt(left);
                            Value rightToInt = getInt(right);
                            result = getBoolean(eval(new Multiplication(leftToInt, m.operator, rightToInt, m.token)));
                        }
                    }
                }
            } else {
                result = eval(new Multiplication(left, m.operator, castToLeftType(left, right), m.token));
            }
        } else if (l instanceof Combination cb) {
            Value left = getBoolean(eval(checkIfNull(cb.left)));
            Value right = getBoolean(eval(checkIfNull(cb.right)));
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+cb.token.getLine()+")");
            else if(!(left.type.equals("bool") && right.type.equals("bool")))
                semanticErrors.add("Error: combining non-bool types! ("+cb.token.getLine()+")"); // Should never get here
            else {
                String operator = cb.operator;
                String b1 = left.value;
                String b2 = right.value;
                boolean B1 = Boolean.parseBoolean(b1);
                boolean B2 = Boolean.parseBoolean(b2);
                switch (operator) {
                    case "and" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            result = new Value(Boolean.toString(B1 && B2));
                        } else if (b1.equals("false") || b2.equals("false")) result = new Value("false");
                        else result = new Value("null");
                    }
                    case "or" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            result = new Value(Boolean.toString(B1 || B2));
                        } else if (b1.equals("true") || b2.equals("true")) result = new Value("true");
                        else result = new Value("null");
                    }
                }
            }
        } else if (l instanceof Negation n) {
            Value val = eval(checkIfNull(n.expr));
            switch(val.type){
                case "int" -> {
                    int i = parseInt(val.value);
                    return new Value(Integer.toString(-i));
                }
                case "float" -> {
                    float f = parseFloat(val.value);
                    return new Value(Float.toString(-f));
                }
                case "bool" -> {
                    if(val.value.equals("true")) return new Value("false");
                    else return new Value("true");
                }
            }

        } else if (l instanceof Comparison co){
            Value left = eval(checkIfNull(co.left));
            Value right = eval(checkIfNull(co.right));
            String operator = co.operator;
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+co.token.getLine()+")");
            else if(left.type.equals(right.type)){
                switch(left.type){
                    case "bool", "int" -> {
                        int leftInt = parseInt(String.valueOf(getInt(left).value));
                        int rightInt = parseInt(String.valueOf(getInt(right).value));
                        switch (operator) {
                            case "==" -> result = new Value(Boolean.toString(leftInt == rightInt));
                            case "!=" -> result = new Value(Boolean.toString(leftInt != rightInt));
                            case ">" -> result = new Value(Boolean.toString(leftInt > rightInt));
                            case "<" -> result = new Value(Boolean.toString(leftInt < rightInt));
                            case ">=" -> result = new Value(Boolean.toString(leftInt >= rightInt));
                            case "<=" -> result = new Value(Boolean.toString(leftInt <= rightInt));
                        }
                    }
                    case "float" -> {
                        float leftFloat = parseFloat(String.valueOf(getFloat(left).value));
                        float rightFloat = parseFloat(String.valueOf(getFloat(right).value));
                        switch (operator) {
                            case "==" -> result = new Value(Boolean.toString(leftFloat == rightFloat));
                            case "!=" -> result = new Value(Boolean.toString(leftFloat != rightFloat));
                            case ">" -> result = new Value(Boolean.toString(leftFloat > rightFloat));
                            case "<" -> result = new Value(Boolean.toString(leftFloat < rightFloat));
                            case ">=" -> result = new Value(Boolean.toString(leftFloat >= rightFloat));
                            case "<=" -> result = new Value(Boolean.toString(leftFloat <= rightFloat));
                        }
                    }
                }
            } else if((left.type.equals("float") || left.type.equals("int"))
                    && (right.type.equals("float") || right.type.equals("int"))) {
                float leftFloat = parseFloat(String.valueOf(getFloat(left).value));
                float rightFloat = parseFloat(String.valueOf(getFloat(right).value));
                switch (operator) {
                    case "==" -> result = new Value(Boolean.toString(leftFloat == rightFloat));
                    case "!=" -> result = new Value(Boolean.toString(leftFloat != rightFloat));
                    case ">" -> result = new Value(Boolean.toString(leftFloat > rightFloat));
                    case "<" -> result = new Value(Boolean.toString(leftFloat < rightFloat));
                    case ">=" -> result = new Value(Boolean.toString(leftFloat >= rightFloat));
                    case "<=" -> result = new Value(Boolean.toString(leftFloat <= rightFloat));
                }
            } else {
                result = eval(new Comparison(left, co.operator, castToLeftType(left, right), co.token));
            }
        } else if (l instanceof Input){
            int value;
            if(inputs.isEmpty())value = 0;
            else if (currentInput >= inputs.size()) {
                value = inputs.get(0);
                currentInput = 1;
            } else {
                value = inputs.get(currentInput);
                currentInput++;
            }
            result = new Value(Integer.toString(value));
        }
        return (Value) checkIfNull(result);
    }
}