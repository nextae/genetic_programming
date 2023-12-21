package interpreter.expression.interpreter;

import interpreter.expression.arithmetic.*;
import interpreter.expression.blocks.Block;
import interpreter.expression.blocks.IfBlock;
import interpreter.expression.blocks.WhileBlock;
import interpreter.expression.library.*;
import interpreter.expression.logic.*;
import interpreter.expression.toplevel.Line;
import interpreter.expression.variables.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class ExpressionProcessor {

    private ExpressionProcessor parent = null;
    private final List<Line> list;
    public static final List<String> semanticErrors = new ArrayList<>();

    private final Map<String, Value> values;
    private final Map<String, String> types;

    private boolean firstRunError = false;
    private static Scanner scanner;
    private static File file;
    private static final int maxInstructions = 100;
    private static int computedInstructions = 0;

    private Value getBoolean(Value value){
//        if (value.type.equals("notInit")) semanticErrors.add("Error: variable not declared");
        if (value.type.equals("notInit")) {
//            semanticErrors.add("Error: variable not declared");
            return new Value("");
        }
        else if (value.type.equals("bool")) return value;
        else {
            float f = Float.parseFloat(value.value);
            return f>=0?new Value("true"):new Value("false");
        }
//        return new Value("false");
    }

    private Value getFloat(Value value){
        switch (value.type) {
//            case "notInit" -> semanticErrors.add("Error: variable not declared");
            case "notInit" -> {
//                semanticErrors.add("Error: variable not declared");
                return new Value("");
            }
            case "float" -> {
                return value;
            }
            case "bool" -> {
                return value.value.equals("true") ? new Value("1.0") : new Value("-1.0");
            }
            default -> {
                return new Value(String.valueOf(Float.parseFloat(value.value)));
            }
        }
//        return new Value("0.0");
    }

    private Value getInt(Value value){
        switch (value.type) {
//            case "notInit" -> semanticErrors.add("Error: variable not declared");
            case "notInit" -> {
//                semanticErrors.add("Error: variable not declared");
                return new Value("");
            }
            case "int" -> {
                return value;
            }
            case "bool" -> {
                return value.value.equals("true") ? new Value("1") : new Value("-1");
            }
            default -> {
                return new Value(String.valueOf(Math.round(Float.parseFloat(value.value))));
            }
        }
//        return new Value("0");
    }

    private Value castToLeftType(Value left, Value right){
        Value rightCasted;
        switch (left.type){
            case "int" -> rightCasted = getInt(right);
            case "float" -> rightCasted = getFloat(right);
            case "bool" -> rightCasted = getBoolean(right);
            default -> rightCasted = new Value(""); // Should never go to default
        }
        return rightCasted;
    }

    private Value castToLeftType(String left_type, Value right){
        Value rightCasted;
        if (left_type == null) return new Value("");
        switch (left_type){
            case "int" -> rightCasted = getInt(right);
            case "float" -> rightCasted = getFloat(right);
            case "bool" -> rightCasted = getBoolean(right);
            default -> rightCasted = new Value(""); // Should never go to default
        }
        return rightCasted;
    }

    public ExpressionProcessor(List<Line> lines, String inputFilePath, String inputDelimeter){
        ExpressionProcessor.file = new File(inputFilePath);
        try {
            ExpressionProcessor.scanner = new Scanner(file);
            scanner.useDelimiter(inputDelimeter);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File "+inputFilePath+" not found");
        }
        list = lines;
        values = new HashMap<>();
        types = new HashMap<>();
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
                        Value result = eval(new VarName(p.id, p.token));
                        if (result == null)
                            semanticErrors.add("Error: variable " + p.id + " not declared (" + p.token.getLine() + ")");
                        else if (result.type.equals("notInit"))
                            semanticErrors.add("Error: variable `" + p.id + "` not initialized (" + p.token.getLine() + ")");
                        else evaluations.add(result.toString());
                    } else {
                        evaluations.add(eval(p.expr).toString());
                    }
                } else if (l instanceof WhileBlock w) {
                    Value condition = getBoolean(eval(w.condition));
                    if (condition.type.equals("notInit"))
                        semanticErrors.add("Error: not initialized value (" + w.token.getLine() + ")");
                    else if (condition.value.equals("true")) {
                        List<Line> whileList = new ArrayList<>();
                        whileList.add(w.block);
                        while (condition.value.equals("true") && computedInstructions <= maxInstructions) {
                            computedInstructions++;
                            evaluations.addAll(getEvalResults(whileList));
                            condition = eval(w.condition);
                        }
                    }
                } else if (l instanceof IfBlock i) {
                    Value condition = getBoolean(eval(i.condition));
                    if (condition.type.equals("notInit"))
                        semanticErrors.add("Error: not initialized value (" + i.token.getLine() + ")");
                    else if (condition.value.equals("true")) {
                        ExpressionProcessor ep = new ExpressionProcessor(i.elseBlock.ifBlock.lines, this);
                        evaluations.addAll(ep.getEvalResults(null));
                    } else if (condition.value.equals("false") && i.elseBlock.elseBlock != null) {
                        ExpressionProcessor ep = new ExpressionProcessor(i.elseBlock.elseBlock.lines, this);
                        evaluations.addAll(ep.getEvalResults(null));
                    } else if (condition.value.equals("false") && i.elseBlock.child != null) {
                        List<Line> childList = new ArrayList<>();
                        childList.add(i.elseBlock.child);
                        evaluations.addAll(getEvalResults(childList));
                    }
                } else if (l instanceof Block b) {
                    ExpressionProcessor ep = new ExpressionProcessor(b.lines, this);
                    evaluations.addAll(ep.getEvalResults(null));
                }
            }
        }
        return evaluations;
    }

    private Value eval(Line l){
        Value result = new Value("");

        if(l instanceof Value v){
            result = new Value(v.value);
        } else if (l instanceof VarName v){
            if (values.containsKey(v.id)) result = values.get(v.id);
            else if(parent != null && parent.values.containsKey(v.id)) result = parent.values.get(v.id);
            else semanticErrors.add("Error: variable `"+v.id+"` not declared ("+v.token.getLine()+")");
        } else if (l instanceof Addition a){
            Value left = eval(a.left);
            Value right = eval(a.right);
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+a.token.getLine()+")");
            else if(left.type.equals(right.type)) {
                if (a.operator.equals("+")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = Integer.parseInt(left.value);
                            int rightInt = Integer.parseInt(right.value);
                            result = new Value(Integer.toString(leftInt + rightInt));
                        }
                        case "float" -> {
                            float leftFloat = Float.parseFloat(left.value);
                            float rightFloat = Float.parseFloat(right.value);
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
                            int leftInt = Integer.parseInt(left.value);
                            int rightInt = Integer.parseInt(right.value);
                            result = new Value(Integer.toString(leftInt - rightInt));
                        }
                        case "float" -> {
                            float leftFloat = Float.parseFloat(left.value);
                            float rightFloat = Float.parseFloat(right.value);
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
            Value left = eval(m.left);
            Value right = eval(m.right);
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+m.token.getLine()+")");
            else if(left.type.equals(right.type)) {
                if (m.operator.equals("*")) {
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = Integer.parseInt(left.value);
                            int rightInt = Integer.parseInt(right.value);
                            result = new Value(Integer.toString(leftInt * rightInt));
                        }
                        case "float" -> {
                            float leftFloat = Float.parseFloat(left.value);
                            float rightFloat = Float.parseFloat(right.value);
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
                            int leftInt = Integer.parseInt(left.value);
                            int rightInt = Integer.parseInt(right.value);
                            try {
                                result = new Value(Integer.toString(leftInt / rightInt));
                            } catch (ArithmeticException e) {
                                semanticErrors.add("Error: dividing by zero! (" + m.token.getLine() + ")");
                            }
                        }
                        case "float" -> {
                            float leftFloat = Float.parseFloat(left.value);
                            float rightFloat = Float.parseFloat(right.value);
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
        } else if (l instanceof Power p){
            Value left = eval(p.left);
            Value right = eval(p.right);
            String leftType = left.type;
            String rightType = right.type;
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+p.token.getLine()+")");
            else if(leftType.equals(rightType)){
                switch(leftType){
                    case "int" -> {
                        int leftInt = Integer.parseInt(left.value);
                        int rightInt = Integer.parseInt(right.value);
                        int power = (int) Math.pow(leftInt, rightInt);
                        result = new Value(Integer.toString(power));
                    }
                    case "float" -> {
                        float leftFloat = Float.parseFloat(left.value);
                        float rightFloat = Float.parseFloat(right.value);
                        double power = Math.pow(leftFloat, rightFloat);
                        result = new Value(Double.toString(power));
                    }
                    case "bool" -> {
                        Value leftToInt = getInt(left);
                        Value rightToInt = getInt(right);
                        result = getBoolean(eval(new Power(leftToInt, rightToInt, p.token)));
                    }
                }
            } else if((left.type.equals("float") || left.type.equals("int"))
                    && (right.type.equals("float") || right.type.equals("int"))) {
                double power = Math.pow(Float.parseFloat(left.value), Float.parseFloat(right.value));
                result = new Value(Double.toString(power));
            } else {
                result = eval(new Power(left, castToLeftType(left, right), p.token));
            }
        } else if (l instanceof Modulo m) {
            Value left = eval(m.dividend);
            Value right = eval(m.divisor);

            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+m.token.getLine()+")");

            else if(left.type.equals(right.type)) {
                switch (left.type) {
                    case "int" -> {
                        int leftInt = Integer.parseInt(left.value);
                        int rightInt = Integer.parseInt(right.value);
                        if(rightInt == 0) {
                            semanticErrors.add("Error: can't divide by 0! (" + m.token.getLine() + ")");
                            return new Value(Integer.toString(leftInt));
                        }
                        else {
                            int modulo = leftInt % rightInt;
                            return new Value(Integer.toString(modulo));
                        }
                    }
                    case "float" -> {
                        float leftFloat = Float.parseFloat(left.value);
                        float rightFloat = Float.parseFloat(right.value);
                        if(rightFloat == 0) {
                            semanticErrors.add("Error: can't divide by 0! (" + m.token.getLine() + ")");
                            return new Value(Float.toString(leftFloat));
                        }
                        else {
                            float modulo = leftFloat % rightFloat;
                            return new Value(Float.toString(modulo));
                        }
                    }
                    case "bool" -> {
                        Value leftToInt = getInt(left);
                        Value rightToInt = getInt(right);
                        result = getBoolean(eval(new Modulo(leftToInt, rightToInt, m.token)));
                    }
                }
            } else {
                result = eval(new Modulo(left, castToLeftType(left, right), m.token));
            }
        } else if (l instanceof Combination cb) {
            Value left = getBoolean(eval(cb.left));
            Value right = getBoolean(eval(cb.right));
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
            Value val = getBoolean(eval(n.expr));
            if(val.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+n.token.getLine()+")");
            else if(!val.type.equals("bool")) semanticErrors.add("Error: Can't negate non boolean values! ("+n.token.getLine()+")");
            else switch (val.value) {
                case "true" -> result = new Value("false");
                case "false" -> result = new Value("true");
                case "null" -> result = new Value("null");
            }
        } else if (l instanceof Comparison co){
            Value left = eval(co.left);
            Value right = eval(co.right);
            String operator = co.operator;
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+co.token.getLine()+")");
            else if(left.type.equals(right.type)){
                switch(left.type){
                    case "bool", "int" -> {
                        int leftInt = Integer.parseInt(String.valueOf(getInt(left).value));
                        int rightInt = Integer.parseInt(String.valueOf(getInt(right).value));
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
                        float leftFloat = Float.parseFloat(String.valueOf(getFloat(left).value));
                        float rightFloat = Float.parseFloat(String.valueOf(getFloat(right).value));
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
                float leftFloat = Float.parseFloat(String.valueOf(getFloat(left).value));
                float rightFloat = Float.parseFloat(String.valueOf(getFloat(right).value));
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
        } else if (l instanceof Input i){
            String readValue = "";
            if (scanner.hasNext()) {
                readValue = scanner.next();
            } else {
                semanticErrors.add("Error: no data left to use as input ("+i.token.getLine()+")");
            }
            result = new Value(readValue);
        }
        return result;
    }
}