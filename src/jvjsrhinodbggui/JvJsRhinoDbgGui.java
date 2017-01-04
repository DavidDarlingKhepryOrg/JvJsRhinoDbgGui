/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvjsrhinodbggui;

import java.io.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.debugger.Main;

/**
 *
 * @author Khepry Quixote
 */
public class JvJsRhinoDbgGui {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        boolean useString = false;

        String jsScript = "for(var x = 0; x < 5; x++) {\n" +
                            "  java.lang.System.out.println(\"Hello, World!\");\n" +
                          "}";

        String file = "~/Documents/NetBeansProjects/JvJsRhinoDbgGui/src/javascript/test.js";
        file = file.replace("~", System.getProperty("user.home"));

        ContextFactory factory = new ContextFactory();

        Main dbg;
        if (useString) {
            dbg = new Main("");
        }
        else {
            dbg = new Main(file);
        }
        dbg.attachTo(factory);

        Context context = factory.enterContext();
        ScriptableObject scope = context.initStandardObjects();

        // redirect std I/O
        System.setIn(dbg.getIn());
        System.setOut(dbg.getOut());
        System.setErr(dbg.getErr());

        dbg.setBreakOnEnter(true);
        dbg.setScope(scope);
        dbg.setSize(1280, 1024);
        dbg.setVisible(true);
        dbg.setExitAction(new ExitOnClose());

        if (useString) {
            context.evaluateString(scope, jsScript, "Hello World", 1, null);
        }
        else {
            try (Reader reader = new FileReader(file)) {
              context.evaluateReader(scope, reader, file, 1, null);
            }
        }
  }

  private static class ExitOnClose implements Runnable {
    @Override
    public void run() {
      System.exit(0);
    }
  }
    
}
