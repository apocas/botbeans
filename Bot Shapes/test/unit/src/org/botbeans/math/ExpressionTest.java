/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.math;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openide.util.Exceptions;

/**
 *
 * @author Apocas
 */
public class ExpressionTest {

    public ExpressionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAdd() {
        MathExpression expr = new MathExpression("");
        HashMap mem = new HashMap();
        mem.put("a1a", 2);
        mem.put("basd", 3);
        mem.put("a", 0);


        expr.add("(( 2)+1)*a1a*(9 *3)*basd ");

       /// expr.add("(( a )");

        expr.swapVars(mem);
        System.out.println(expr);

        try {
            System.out.println(expr.value());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
