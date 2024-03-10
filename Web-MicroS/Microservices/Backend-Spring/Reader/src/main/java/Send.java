import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Send {
    private final static String QUEUE_NAME = "finalq2";

    public static void main(String[] argv) throws Exception {
        String desktopPath = System.getProperty("user.home") + "/Desktop/";
        String sensorFileName = "sensor.csv";
        String sensorFilePath = desktopPath + sensorFileName;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://jwqwbali:GxF8aMxdKKEa6t-l6r0Z0Rn4QJBZ3QzL@shrimp.rmq.cloudamqp.com/jwqwbali");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Trimite linii din "sensor.csv"
            try (BufferedReader br = new BufferedReader(new FileReader(sensorFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String json = createJson(line);
                    System.out.println("Linie citită din sensor: " + line);

                    channel.basicPublish("", QUEUE_NAME, null, json.getBytes(StandardCharsets.UTF_8));
                    Thread.sleep(5000); // Așteaptă 5 secunde între trimiteri
                }
            }
        }
    }

    private static String createJson(String line) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        int iddevice = 55;
        return "{\"iddevice\": " + iddevice + ", \"timestamp\": \"" + formattedTimestamp + "\", \"data\": \"" + line + "\"}";
    }

    private static String readFirstLine(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine();
        }
    }
}
