/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.math;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Avaliador de expressoes logicas
 * @author Apocas
 */
public class LogicExpression {

    /**
     * Avalia uma expressao
     * @param expr
     * @param memory
     * @return
     * @throws Exception
     */
    public static boolean process(String expr, HashMap memory) throws Exception {
        String[] teste = null;
        int type = -1;
        if (expr.contains("<")) {
            teste = expr.split("<");
            type = 0;
        } else if (expr.contains(">")) {
            teste = expr.split(">");
            type = 1;
        } else if (expr.contains("=")) {
            teste = expr.split("=");
            type = 2;
        }
        if (teste == null || teste.length != 2) {
            throw new Exception("Expression error");
        } else {
            teste[0].trim();
            teste[1].trim();

            MathExpression mexpr = new MathExpression("");
            mexpr.add(teste[0]);
            mexpr.swapVars(memory);
            double v1 = mexpr.value();
            mexpr.reset();
            mexpr.add(teste[1]);
            mexpr.swapVars(memory);
            double v2 = mexpr.value();

            switch (type) {
                case 0:
                    if (v1 < v2) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    if (v1 > v2) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (v1 == v2) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        }
    }
}
