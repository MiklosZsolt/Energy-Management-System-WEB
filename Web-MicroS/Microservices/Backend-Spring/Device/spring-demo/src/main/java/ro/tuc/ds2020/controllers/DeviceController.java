package ro.tuc.ds2020.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.UpdateOwnerRequest;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.services.DeviceService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.springframework.amqp.rabbit.core.RabbitAdmin.QUEUE_NAME;

@Controller
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public DeviceController(DeviceService deviceService, AmqpTemplate amqpTemplate) {
        this.deviceService = deviceService;
        this.amqpTemplate = amqpTemplate;
        ((RabbitTemplate) amqpTemplate).setDefaultReceiveQueue("dd"); // Setarea cozii de primire pentru RabbitTemplate
    }

//    @PostConstruct
//    public void setupQueue() {
//        try {
//            // Configurează conexiunea la RabbitMQ
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setUri("amqps://fmghotcn:8QOUk_U3LiMW-bnXf8-TGrk65pl8Br-Y@shrimp.rmq.cloudamqp.com/fmghotcn"); // Configurare conexiune
//
//            // Creați o conexiune și un canal
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//
//            // Definește coada
//            String queueName = "secondQueue";
//            channel.queueDeclare(queueName, true, false, false, null);
//
//            // Înregistrează un consumator pentru coada definită
//            channel.basicConsume(queueName, true, (consumerTag, message) -> {
//                // Logică pentru procesarea mesajelor primite în coadă
//                byte[] body = message.getBody();
//                String receivedMessage = new String(body, StandardCharsets.UTF_8);
//                System.out.println("Received message: " + receivedMessage);
//            }, consumerTag -> {});
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public List<Device> getAll() {
        return deviceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable int id) {
        Optional<Device> person = deviceService.getById(id);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    public Device saveDevice(@RequestBody Device device) throws IOException {
        return deviceService.saveDevice(device);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
        return deviceService.deleteDevice(id);
    }

    @DeleteMapping("/deleteDevices/{username}")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable String username) {
        return deviceService.deleteDevicesByUsernameOwner(username);
    }

    @GetMapping("/byOwner/{usernameOwner}")
    @ResponseBody
    public ResponseEntity<List<Device>> getDevicesByOwner(@PathVariable String usernameOwner) {
        List<Device> devices = deviceService.getDevicesByUsernameOwner(usernameOwner);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/updateByUsername/{UsernameOwner}")
    @ResponseBody
    public ResponseEntity<List<Device>> updateByOwner(@PathVariable String usernameOwner) {
        List<Device> devices = deviceService.getDevicesByUsernameOwner(usernameOwner);
        return ResponseEntity.ok(devices);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable(value = "id") Integer id, @RequestBody Device device) {
        device.setId(id);

        // Actualizează dispozitivul în baza de date
        Device updatedDevice = deviceService.updateDevice(device);

        try {
            // Configurează conexiunea la RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqps://fmghotcn:8QOUk_U3LiMW-bnXf8-TGrk65pl8Br-Y@shrimp.rmq.cloudamqp.com/fmghotcn");

            // Creați o conexiune și un canal
            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                // Numele cozi
                String queueName = "queue"; // înlocuiește cu numele cozi tale

                // Declara coada (dacă nu este deja declarată)
                channel.queueDeclare(queueName, true, false, false, null);

                // Mesajul simplu de trimis
                String message = updatedDevice.getMhec();

                // Trimite mesajul către coada RabbitMQ
                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Mesaj trimis către coada: " + queueName);
            } catch (IOException | TimeoutException e) {
                // Tratează erorile legate de conexiune sau trimitere
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(updatedDevice);
    }




    @PostMapping("/updateD/{id}")
    public ResponseEntity<Device> updatePerson(@PathVariable(value = "id") Integer id, @RequestBody Device device) {
        device.setId(id);
        Device updatedDevice = deviceService.updateDevice(device);
        return ResponseEntity.ok(updatedDevice);
    }




    @PostMapping("/updateOwner")
    public ResponseEntity<String> updateOwner(@RequestBody UpdateOwnerRequest updateRequest) {
        String oldUsername = updateRequest.getOldUsername();
        String newUsername = updateRequest.getNewUsername();

        deviceService.updateOwner(oldUsername, newUsername);

        return ResponseEntity.ok("Actualizare cu succes!");
    }





}






