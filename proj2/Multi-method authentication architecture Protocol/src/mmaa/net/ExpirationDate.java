package mmaa.net;

import java.util.Calendar;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class ExpirationDate extends ProtocolContainer {

    private final Calendar date;
    
    public ExpirationDate(Calendar date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name() + "\n"
                + "Expiration Date: " + date.getTime();
    }
    
    @Override
    public ContainerType getType() {
        return ContainerType.EXPIRATION_DATE;
    }
    
    public Calendar getExpirationDate() {
        return date;
    }
}
