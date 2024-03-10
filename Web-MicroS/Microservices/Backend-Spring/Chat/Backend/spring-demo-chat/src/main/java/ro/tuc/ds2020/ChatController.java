package ro.tuc.ds2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(Message message) {
        System.out.println(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    @MessageMapping("/typing")
    public void handleTyping(Message typingMessage) {
        System.out.println(typingMessage);
        // Trimiteți notificarea de "tastează" către clienții relevanți
        messagingTemplate.convertAndSend("/topic/typing", typingMessage);
    }
    @MessageMapping("/seen")
    @SendTo("/topic/seens")
    public Message handleSeenMessage(Message message) {
        // Nu este necesară nicio actualizare în baza de date
        return new Message("SEEN", message.getReceiver(), message.getSender(), "seen");
    }
}
