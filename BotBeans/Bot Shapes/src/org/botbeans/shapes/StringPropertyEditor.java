/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Apocas
 */
public class StringPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        return getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }
}
