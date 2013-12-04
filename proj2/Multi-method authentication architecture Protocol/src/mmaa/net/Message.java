package mmaa.net;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class Message extends ProtocolContainer {
    
    private final MessageType type;
    
    public Message(MessageType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name() + "\n"
                + "Type: " + getMessage().name();
    }
    
    @Override
    public ContainerType getType() {
        return ContainerType.MESSAGE;
    }
    
    public MessageType getMessage() {
        return type;
    }
}
