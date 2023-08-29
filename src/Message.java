import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private String type;

    private String message;

    private List<String> connectedClients;

    public Message (String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message (String type, List<String> connectedClients) {
        this.type = type;
        this.connectedClients = connectedClients;
    }
    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getConnectedClients() {
        return connectedClients;
    }
}
