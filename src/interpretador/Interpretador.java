package interpretador;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo
 */
public class Interpretador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Sistema s = new Sistema();
        try {
            s.realizaInterpretacao();
        } catch (IOException ex) {
            Logger.getLogger(Interpretador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
