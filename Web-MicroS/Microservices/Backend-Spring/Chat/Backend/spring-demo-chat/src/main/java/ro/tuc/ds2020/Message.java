package ro.tuc.ds2020;

public class Message {
    private String type; // Adăugați acest câmp pentru a diferenția tipurile de mesaj
    private String sender;
    private String receiver;
    private String content;

    // Constructor fără 'type', pentru mesaje normale
    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = "CHAT"; // Setează implicit tipul ca "CHAT"
    }

    // Constructor cu toate câmpurile, inclusiv 'type'
    public Message(String type, String sender, String receiver, String content) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }


    // Getteri și setteri pentru toate câmpurile, inclusiv 'type'
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message() {
    }


    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
