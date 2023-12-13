package interpreter.expression.interpreter;

import interpreter.expression.arithmetic.Addition;
import interpreter.expression.arithmetic.Multiplication;
import interpreter.expression.arithmetic.Power;
import interpreter.expression.blocks.Block;
import interpreter.expression.blocks.IfBlock;
import interpreter.expression.library.Print;
import interpreter.expression.logic.Combination;
import interpreter.expression.logic.Comparison;
import interpreter.expression.logic.Negation;
import interpreter.expression.toplevel.Line;
import interpreter.expression.variables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExpressionProcessor_copy {

    private ExpressionProcessor_copy parent = null;
    List<Line> list;
    public final List<String> semanticErrors = new ArrayList<>();

    private final Map<String, Value> values;
    private final Map<String, String> types = new HashMap<>();

    public ExpressionProcessor_copy(List<Line> lines){
        list = lines;
        values = new HashMap<>();
    }

    public ExpressionProcessor_copy(List<Line> lines, ExpressionProcessor_copy parent){
        list = lines;
        values = new HashMap<>();
        this.parent = parent;
    }

    public List<String> getEvalResults(){ //TODO: copy to oder EvalResults
        List<String> evaluations = new ArrayList<>();

        for(Line l: list){
            if(l instanceof Variable v){
                System.out.println("OH-OH! variable value: " + v.value);
                try {
                    values.put(v.id, eval(v.value));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else if (l instanceof VarDeclaration v){
                Value value = eval(v.variable.value);
                if(values.containsKey(v.variable.id))
                    semanticErrors.add("Error: variable `"+v.variable.id+"` already declared ("+v.variable.token.getLine()+")");
                values.put(v.variable.id, value);
                types.put(v.variable.id, v.variable.type);
                if(v.variable.value instanceof VarName vn && types.containsKey(vn.id) && !types.get(vn.id).equals(types.get(v.variable.id)))
                    semanticErrors.add("Error: mismatched types! (" + v.variable.token.getLine() + ")");
                    // Checks if declared types match ^
                else if(!types.get(v.variable.id).equals(value.type) && !value.type.equals("notInit"))
                    semanticErrors.add("Error: mismatched types! (" + v.variable.token.getLine() + ")");
            } else if (l instanceof Assignment a){
                Value value = eval(a.expr);
                if(!values.containsKey(a.id)){
                    if(parent != null && parent.values.containsKey(a.id)) {
                        parent.values.put(a.id, value);
                        if(!parent.types.get(a.id).equals(value.type))
                            semanticErrors.add("Error: mismatched types! ("+a.token.getLine()+")");
                    }
                    else semanticErrors.add("Error: variable `"+a.id+"` not declared ("+a.token.getLine()+")");
                } else {
                    values.put(a.id, eval(a.expr));
                    if (!types.get(a.id).equals(value.type))
                        semanticErrors.add("Error: mismatched types! ("+a.token.getLine()+")");
                    else if (values.get(a.id) == null)
                        semanticErrors.add("Error: variable `"+a.id+"` not initialized ("+a.token.getText()+")");
                }
            } else if (l instanceof Print p){
                if(p.id != null) {
                    Value result = eval(new VarName(p.id, p.token));
                    if(result.type.equals("notInit"))
                        semanticErrors.add("Error: variable `"+p.id+"` not initialized ("+p.token.getLine()+")");
                    evaluations.add(result.toString());
                } else {
                    evaluations.add(eval(p.expr).toString());
                }
            } else if (l instanceof IfBlock i){
                Value condition = eval(i.condition);
                if(condition.type.equals("notInit"))
                    semanticErrors.add("Error: not initialized value ("+i.token.getLine()+")");
                else if(!condition.type.equals("bool"))
                    semanticErrors.add("Error: can't resolve truth-value for given condition ("+i.token.getLine()+")");
                else if (condition.value.equals("true")){
                    ExpressionProcessor_copy ep = new ExpressionProcessor_copy(i.elseBlock.ifBlock.lines, this);
                    evaluations.addAll(ep.getEvalResults());
                    semanticErrors.addAll(ep.semanticErrors);
                } else if (condition.value.equals("false") && i.elseBlock.elseBlock != null){
                    ExpressionProcessor_copy ep = new ExpressionProcessor_copy(i.elseBlock.elseBlock.lines, this);
                    evaluations.addAll(ep.getEvalResults());
                    semanticErrors.addAll(ep.semanticErrors);
                } else if (condition.value.equals("false") && i.elseBlock.child != null){
                    List<Line> childList = new ArrayList<>();
                    childList.add(i.elseBlock.child);
                    evaluations.addAll(getEvalResults(childList));
                }
            } else if (l instanceof Block b){
                ExpressionProcessor_copy ep = new ExpressionProcessor_copy(b.lines, this);
                evaluations.addAll(ep.getEvalResults());
                semanticErrors.addAll(ep.semanticErrors);
            }
        }
        return evaluations;
    }

    private List<String> getEvalResults(List<Line> list){
        List<String> evaluations = new ArrayList<>();

        for(Line l: list){
            if(l instanceof Variable v){
                System.out.println("OH-OH! variable value: " + v.value);
                try {
                    values.put(v.id, eval(v.value));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else if (l instanceof VarDeclaration v){
                Value value = eval(v.variable.value);
                if(values.containsKey(v.variable.id))
                    semanticErrors.add("Error: variable `"+v.variable.id+"` already declared ("+v.variable.token.getLine()+")");
                values.put(v.variable.id, value);
                types.put(v.variable.id, v.variable.type);
                if(v.variable.value instanceof VarName vn && types.containsKey(vn.id) && !types.get(vn.id).equals(types.get(v.variable.id)))
                    semanticErrors.add("Error: mismatched types! (" + v.variable.token.getLine() + ")");
                    // Checks if declared types match ^
                else if(!types.get(v.variable.id).equals(value.type) && !value.type.equals("notInit"))
                    semanticErrors.add("Error: mismatched types! (" + v.variable.token.getLine() + ")");
            } else if (l instanceof Assignment a){
                Value value = eval(a.expr);
                if(!values.containsKey(a.id)){
                    if(parent != null && parent.values.containsKey(a.id)) {
                        parent.values.put(a.id, value);
                        if(!parent.types.get(a.id).equals(value.type))
                            semanticErrors.add("Error: mismatched types! ("+a.token.getLine()+")");
                    }
                    else semanticErrors.add("Error: variable `"+a.id+"` not declared ("+a.token.getLine()+")");
                } else {
                    values.put(a.id, eval(a.expr));
                    if (!types.get(a.id).equals(value.type))
                        semanticErrors.add("Error: mismatched types! ("+a.token.getLine()+")");
                    else if (values.get(a.id) == null)
                        semanticErrors.add("Error: variable `"+a.id+"` not initialized ("+a.token.getText()+")");
                }
            } else if (l instanceof Print p){
                if(p.id != null) {
                    Value result = eval(new VarName(p.id, p.token));
                    if(result.type.equals("notInit"))
                        semanticErrors.add("Error: variable `"+p.id+"` not initialized ("+p.token.getLine()+")");
                    evaluations.add(result.toString());
                } else {
                    evaluations.add(eval(p.expr).toString());
                }
            } else if (l instanceof IfBlock i){
                Value condition = eval(i.condition);
                if(condition.type.equals("notInit"))
                    semanticErrors.add("Error: not initialized value ("+i.token.getLine()+")");
                else if(!condition.type.equals("bool"))
                    semanticErrors.add("Error: can't resolve truth-value for given condition ("+i.token.getLine()+")");
                else if (condition.value.equals("true")){
                    ExpressionProcessor_copy ep = new ExpressionProcessor_copy(i.elseBlock.ifBlock.lines, this);
                    evaluations.addAll(ep.getEvalResults());
                    semanticErrors.addAll(ep.semanticErrors);
                } else if (condition.value.equals("false") && i.elseBlock.elseBlock != null){
                    ExpressionProcessor_copy ep = new ExpressionProcessor_copy(i.elseBlock.elseBlock.lines, this);
                    evaluations.addAll(ep.getEvalResults());
                    semanticErrors.addAll(ep.semanticErrors);
                } else if (condition.value.equals("false") && i.elseBlock.child != null){
                    List<Line> childList = new ArrayList<>();
                    childList.add(i.elseBlock.child);
                    evaluations.addAll(getEvalResults(childList));
                }
            } else if (l instanceof Block b){
                ExpressionProcessor_copy ep = new ExpressionProcessor_copy(b.lines, this);
                evaluations.addAll(ep.getEvalResults());
                semanticErrors.addAll(ep.semanticErrors);
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
            if(left.type.equals(right.type)){
                if(a.operator.equals("+")){
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
                        case "string" -> result = new Value(left.value + right.value);
                        case "bool" -> semanticErrors.add("Error: So far - can't add booleans! ("+a.token.getLine()+")");
                    }
                } else if(a.operator.equals("-")){
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
                        case "string" -> semanticErrors.add("Error: Can't subtract strings! ("+a.token.getLine()+")");
                        case "bool" -> semanticErrors.add("Error: Can't subtract booleans! ("+a.token.getLine()+")");
                    }
                }
            } else semanticErrors.add("Error: Types don't match! ("+a.token.getLine()+")");

        } else if (l instanceof Multiplication m){
            Value left = eval(m.left);
            Value right = eval(m.right);
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+m.token.getLine()+")");
            if(left.type.equals(right.type)){
                if(m.operator.equals("*")){
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
                        case "string" -> semanticErrors.add("Error: Can't multiply strings! ("+m.token.getLine()+")");
                        case "bool" -> semanticErrors.add("Error: Can't multiply booleans! ("+m.token.getLine()+")");
                    }
                } else if(m.operator.equals("/")){
                    switch (left.type) {
                        case "int" -> {
                            int leftInt = Integer.parseInt(left.value);
                            int rightInt = Integer.parseInt(right.value);
                            result = new Value(Integer.toString(leftInt / rightInt));
                        }
                        case "float" -> {
                            float leftFloat = Float.parseFloat(left.value);
                            float rightFloat = Float.parseFloat(right.value);
                            result = new Value(Float.toString(leftFloat / rightFloat));
                        }
                        case "string" -> semanticErrors.add("Error: Can't divide strings! ("+m.token.getLine()+")");
                        case "bool" -> semanticErrors.add("Error: Can't divide booleans! ("+m.token.getLine()+")");
                    }
                }
            } else semanticErrors.add("Error: Types don't match! ("+m.token.getLine()+")");
        } else if (l instanceof Power p){
            Value left = eval(p.left);
            Value right = eval(p.right);
            String leftType = left.type;
            String rightType = right.type;
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+p.token.getLine()+")");
            if(leftType.equals(rightType)){
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
                    default -> semanticErrors.add("Error: only numeric variables can be exponentiatied ("+p.token.getLine()+")");
                }
            } else if((left.type.equals("float") || left.type.equals("int"))
                    && (right.type.equals("float") || right.type.equals("int"))) {
                double power = Math.pow(Float.parseFloat(left.value), Float.parseFloat(right.value));
                result = new Value(Double.toString(power));
            } else semanticErrors.add("Error: only numeric variables can be exponentiatied ("+p.token.getLine()+")");
        } else if (l instanceof Combination cb) {
            Value left = eval(cb.left);
            Value right = eval(cb.right);
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+cb.token.getLine()+")");
            if(!(left.type.equals("bool") && right.type.equals("bool")))
                semanticErrors.add("Error: combining non-bool types! ("+cb.token.getLine()+")");
            else {
                String operator = cb.operator;
                String b1 = left.value;
                String b2 = right.value;
                switch (operator) {
                    case "and" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            boolean B1 = Boolean.parseBoolean(b1);
                            boolean B2 = Boolean.parseBoolean(b2);
                            result = new Value(Boolean.toString(B1 && B2));
                        } else if (b1.equals("false") || b2.equals("false")) result = new Value("false");
                        else result = new Value("null");
                    }
                    case "nand" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            boolean B1 = Boolean.parseBoolean(b1);
                            boolean B2 = Boolean.parseBoolean(b2);
                            result = new Value(Boolean.toString(!(B1 && B2)));
                        } else if (b1.equals("false") || b2.equals("false")) result = new Value("true");
                        else result = new Value("null");
                    }
                    case "or" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            boolean B1 = Boolean.parseBoolean(b1);
                            boolean B2 = Boolean.parseBoolean(b2);
                            result = new Value(Boolean.toString(B1 || B2));
                        } else if (b1.equals("true") || b2.equals("true")) result = new Value("true");
                        else result = new Value("null");
                    }
                    case "nor" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            boolean B1 = Boolean.parseBoolean(b1);
                            boolean B2 = Boolean.parseBoolean(b2);
                            result = new Value(Boolean.toString(!(B1 || B2)));
                        } else if (b1.equals("true") || b2.equals("true")) result = new Value("false");
                        else result = new Value("null");
                    }
                    case "xor" -> {
                        if (!(b1.equals("null") || b2.equals("null"))) {
                            boolean B1 = Boolean.parseBoolean(b1);
                            boolean B2 = Boolean.parseBoolean(b2);
                            result = new Value(Boolean.toString(B1 ^ B2));
                        } else result = new Value("null");
                    }
                }
            }
        } else if (l instanceof Negation n) {
            Value val = eval(n.expr);
            if(val.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized! ("+n.token.getLine()+")");
            if(!val.type.equals("bool")) semanticErrors.add("Error: Can't negate non boolean values! ("+n.token.getLine()+")");
            else switch (val.value) {
                case "true" -> result = new Value("false");
                case "false" -> result = new Value("true");
                case "null" -> result = new Value("null");
            }
        } else if (l instanceof Comparison co){
            Value left = eval(co.left);
            Value right = eval(co.right);
            String operator = co.operator;
            List<String> arithmetic = new ArrayList<>();
            arithmetic.add(">"); arithmetic.add("<"); arithmetic.add(">="); arithmetic.add("<=");
            if(left.type.equals("notInit") || right.type.equals("notInit"))
                semanticErrors.add("Error: value not initialized ("+co.token.getLine()+")");
            if(left.type.equals(right.type)){
                switch(left.type){
                    case "bool" -> {
                        if(arithmetic.contains(operator))
                            semanticErrors.add("Error: boolean values can't be compared arithmetically ("+co.token.getLine()+")");
                        else if(operator.equals("==")) {
                            if(!(left.value.equals("null") || right.value.equals("null"))) {
                                boolean b1 = Boolean.parseBoolean(left.value);
                                boolean b2 = Boolean.parseBoolean(right.value);
                                result = new Value(Boolean.toString(b1 == b2));
                            } else result = new Value("null");
                        }
                        else if(operator.equals("!=")) {
                            if(!(left.value.equals("null") || right.value.equals("null"))) {
                                boolean b1 = Boolean.parseBoolean(left.value);
                                boolean b2 = Boolean.parseBoolean(right.value);
                                result = new Value(Boolean.toString(b1 != b2));
                            } else result = new Value("null");
                        }
                    }
                    case "string" -> {
                        if(arithmetic.contains(operator))
                            semanticErrors.add("Error: so far strings can't be compared arithmetically ("+co.token.getLine()+")");
                        else if(operator.equals("==")) result = new Value(Boolean.toString(left.value.equals(right.value)));
                        else if(operator.equals("!=")) result = new Value(Boolean.toString(!left.value.equals(right.value)));
                    }
                    case "int" -> {
                        switch (operator) {
                            case "==" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            == Integer.parseInt(right.value)));
                            case "!=" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            != Integer.parseInt(right.value)));
                            case ">" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            > Integer.parseInt(right.value)));
                            case "<" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            < Integer.parseInt(right.value)));
                            case ">=" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            >= Integer.parseInt(right.value)));
                            case "<=" ->
                                    result = new Value(Boolean.toString(Integer.parseInt(left.value)
                                            <= Integer.parseInt(right.value)));
                        }
                    }
                    case "float" -> {
                        switch (operator) {
                            case "==" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            == Float.parseFloat(right.value)));
                            case "!=" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            != Float.parseFloat(right.value)));
                            case ">" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            > Float.parseFloat(right.value)));
                            case "<" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            < Float.parseFloat(right.value)));
                            case ">=" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            >= Float.parseFloat(right.value)));
                            case "<=" ->
                                    result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                            <= Float.parseFloat(right.value)));
                        }
                    }
                }
            } else if((left.type.equals("float") || left.type.equals("int"))
                    && (right.type.equals("float") || right.type.equals("int"))) {
                switch (operator) {
                    case "==" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    == Float.parseFloat(right.value)));
                    case "!=" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    != Float.parseFloat(right.value)));
                    case ">" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    > Float.parseFloat(right.value)));
                    case "<" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    < Float.parseFloat(right.value)));
                    case ">=" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    >= Float.parseFloat(right.value)));
                    case "<=" ->
                            result = new Value(Boolean.toString(Float.parseFloat(left.value)
                                    <= Float.parseFloat(right.value)));
                }
            } else semanticErrors.add("Error: comparing non-comparable types! ("+co.token.getLine()+")");
        }
        return result;
    }
}
