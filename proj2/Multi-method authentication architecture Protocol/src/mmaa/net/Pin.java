package mmaa.net;

import java.security.SecureRandom;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class Pin extends ProtocolContainer {

    private String pin;
    private static final char[] values = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9'
    };

    public Pin(int length) {
        pin = new String();
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < length; i++) {
            pin += values[r.nextInt(values.length)];
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name() + "\n"
                + "Pin: " + getPin();
    }

    @Override
    public ContainerType getType() {
        return ContainerType.PIN;
    }

    public String getPin() {
        return pin;
    }
}
