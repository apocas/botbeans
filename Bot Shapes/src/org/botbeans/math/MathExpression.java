/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.math;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import org.botbeans.shapes.ShapeTopComponent;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 * Avaliador de expressoes matematicas
 * @author Apocas
 */
public class MathExpression {

    private String expr = "";

    public MathExpression(String str) {
        this.add(str);
    }

    public void reset() {
        expr = "";
    }

    public void add(String e) {
        e = clear(e);
        this.expr += e;
        normalizeSpaces();
        System.out.println(expr);
    }

    /**
     * verifica a expressao
     * @return
     * @throws IOException
     */
    public boolean check() throws IOException {
        Stack<String> s = new Stack<String>();
        String aux = expr;

        while (aux.length() > 0) {
            String str = "";
            if (aux.indexOf(' ') == -1) {
                str = aux.substring(0, aux.length());
                aux = "";
            } else {
                str = aux.substring(0, aux.indexOf(' '));
                aux = aux.substring(aux.indexOf(' ') + 1, aux.length());
            }

            if (str.charAt(0) == '(' || str.charAt(0) == '[' || str.charAt(0) == '{') {
                s.push("" + str.charAt(0));
            } else if (str.charAt(0) == ')' || str.charAt(0) == ']' || str.charAt(0) == '}') {
                if (s.empty()) {
                    return false;
                }
                char par = (s.pop()).charAt(0);
                if (!match(par, str.charAt(0))) {
                    return false;
                }
            }
        }
        if (s.empty()) {
            return true;
        }
        return false;
    }

    /**
     * Calcula valor
     * @return
     * @throws IOException
     */
    public double value() throws IOException {
        toPosFix();

        Stack<Double> oper = new Stack<Double>();
        String aux = expr;

        while (aux.length() > 0) {
            String str = "";
            if (aux.indexOf(' ') == -1) {
                str = aux.substring(0, aux.length());
                aux = "";
            } else {
                str = aux.substring(0, aux.indexOf(' '));
                aux = aux.substring(aux.indexOf(' ') + 1, aux.length());
            }

            if (str.length() == 0) {
                continue;
            }
            if (!isOperator(str)) {
                oper.push(Double.parseDouble(str));
            } else if (need2Value(str)) {
                double a = (oper.pop()).doubleValue();
                double b = (oper.pop()).doubleValue();
                oper.push(new Double(result(b, str, a)));
            }
        }
        return (oper.pop()).doubleValue();
    }

    /**
     * Converte para posfixo
     * @throws IOException
     */
    public void toPosFix() throws IOException {
        Stack<String> oper = new Stack<String>();
        String newExpr = "";
        String aux = expr;

        while (aux.length() > 0) {
            String str = "";
            if (aux.indexOf(' ') == -1) {
                str = aux.substring(0, aux.length());
                aux = "";
            } else {
                str = aux.substring(0, aux.indexOf(' '));
                aux = aux.substring(aux.indexOf(' ') + 1, aux.length());
            }

            if (isOperator(str)) {
                if (oper.empty()) {
                    oper.push(str);
                } else if (operatorPriority(str) > operatorPriority(oper.peek())) {
                    oper.push(str);
                } else {
                    while (!oper.empty() && operatorPriority(str) <= operatorPriority(oper.peek())) {
                        newExpr += ' ' + oper.pop() + ' ';
                    }
                    oper.push(str);
                }
            } else if (str.charAt(0) == '(') {
                oper.push(str);
            } else if (str.charAt(0) == ')') {
                while (!oper.empty() && (oper.peek()).charAt(0) != '(') {
                    newExpr += ' ' + oper.pop() + ' ';
                    //descartar o (
                }
                oper.pop();
            } else {
                newExpr += ' ' + str + ' ';
            }
        }

        while (!oper.empty()) {
            newExpr += ' ' + oper.pop() + ' ';
        }
        expr = newExpr;
    }

    @Override
    public String toString() {
        return expr;
    }

    private static boolean match(char c, char d) {
        return c == d;
    }

    public static boolean isOperator(String op) {
        return (op.equals("+")
                || op.equals("-")
                || op.equals("*")
                || op.equals("/")
                || op.equals("^")
                || op.equals("%"));
    }

    private static boolean need2Value(String op) {
        return (op.equals("+")
                || op.equals("-")
                || op.equals("*")
                || op.equals("/")
                || op.equals("^")
                || op.equals("%"));
    }

    private static int operatorPriority(String op) {
        if (op.equals("+")) {
            return 1;
        } else if (op.equals("-")) {
            return 1;
        } else if (op.equals("*")) {
            return 2;
        } else if (op.equals("/")) {
            return 2;
        } else if (op.equals("^")) {
            return 3;
        } else if (op.equals("%")) {
            return 4;
        } else {
            return 0;
        }
    }

    private static double result(double a, String op, double b) {
        if (op.equals("+")) {
            return a + b;
        } else if (op.equals("-")) {
            return a - b;
        } else if (op.equals("*")) {
            return a * b;
        } else if (op.equals("/")) {
            return a / b;
        } else if (op.equals("^")) {
            return Math.pow(a, b);
        } else if (op.equals("%")) {
            return (int) a % (int) b;
        } else {
            return 0;
        }
    }

    public static long factorial(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static String clear(String e) {
        double dTemp = 0.0;
        int iTemp = 0;
        try {

            dTemp = Double.parseDouble(e);
            iTemp = (int) dTemp;

            if ((dTemp - (double) iTemp) == 0) {
                return "" + iTemp;
            } else {
                return "" + dTemp;
            }
        } catch (NumberFormatException er) {
            return e;
        }
    }

    public static String formatDisplayNumber(String value) {
        return value.replace('.', ',');
    }

    public static double getNumberFromString(String value) {
        return Double.parseDouble(value.replace(".", "").replace(',', '.'));
    }

    /**
     * Remove todos os espacos
     */
    public void removeSpaces() {
        StringTokenizer st = new StringTokenizer(expr, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        expr = t;
    }

    /**
     * Normaliza os espacos
     */
    public void normalizeSpaces() {
        removeSpaces();
        String t = "";
        for (int i = 0; i < expr.length(); i++) {
            if (isOperator("" + expr.charAt(i)) || expr.charAt(i) == '(' || expr.charAt(i) == ')' || expr.charAt(i) == '=') {
                if (i != 0 && t.charAt(t.length() - 1) != ' ') {
                    t += " ";
                }
                t += expr.charAt(i);
                t += " ";
            } else {
                t += expr.charAt(i);
            }
        }
        expr = t;
    }

    /**
     * Substitui variaveis pelos seus valores correspondentes
     * @param memoria onde estao as variaveis
     */
    public void swapVars(HashMap memory) {
        String aux = "";
        String var = "";
        removeSpaces();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                var += c;
            } else {
                if (!var.isEmpty() && (isOperator("" + c) || c == '(' || c == ')')) {
                    aux += memory.get(var);
                    var = "";
                    aux += c;
                } else if (!var.isEmpty()) {
                    var += c;
                } else {
                    aux += c;
                }
            }
        }
        if (!var.isEmpty()) {
            ShapeTopComponent tc = ShapeTopComponent.getLastActivatedComponent();
            if (var.equals("sonar")) {
                int dist = 0;
                try {
                    tc.send(new int[]{1, 16});
                    dist = tc.getIn().readInt();
                    aux += dist;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (var.equals("compass")) {
                int dist = 0;
                try {
                    tc.send(new int[]{1, 17});
                    dist = tc.getIn().readInt();
                    aux += dist;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                aux += memory.get(var);
            }
        }
        expr = aux;
        normalizeSpaces();
    }
}
