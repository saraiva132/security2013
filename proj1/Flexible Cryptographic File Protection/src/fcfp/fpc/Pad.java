/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fcfp.fpc;

import java.util.Random;

/**
 *
 * @author rafael
 */
public class Pad {
    
    public static void fill(byte[] contentToPad, int offset, int length) {
        if (length > contentToPad.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Random r = new Random();
        //Pad tem que ter um valor pseudo-aleatorio entre 5% e 15% do content(talvez meter menos??)
        //double randomValue = 0.05 + (0.15 - 0.05) * r.nextDouble();
        //int pad = (int) ((int) contentToPad.length * randomValue);
        byte[] padData = new byte[length - offset];
        new Random().nextBytes(padData);
        System.arraycopy(padData, 0, contentToPad, offset, length-offset);
    }
}
