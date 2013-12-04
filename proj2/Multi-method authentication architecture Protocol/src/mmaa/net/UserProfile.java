package mmaa.net;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class UserProfile extends ProtocolContainer {

    private final String username;
    private final String password;
    
    public UserProfile(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name() + "\n"
                + "Username: " + getUsername() +"\n"
                + "Password: " + getPassword();
    }
    
    @Override
    public ContainerType getType() {
        return ContainerType.USER_PROFILE;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
}
