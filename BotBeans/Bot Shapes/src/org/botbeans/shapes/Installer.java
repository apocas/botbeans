/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.botbeans.blocks.BlocksTopComponent;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        MiniThread m = new MiniThread();
        try {
            SwingUtilities.invokeAndWait(m);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    class MiniThread implements Runnable {

        @Override
        public void run() {
            BlocksTopComponent btc = BlocksTopComponent.findInstance();
        }
    }
}
