package ro.tuc.ds2020;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.HourlyConsumptionScheduler;
import ro.tuc.ds2020.entities.HourlyConsumption;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.HourlyConsumptionRepository;
import ro.tuc.ds2020.repositories.IMeasurementRepository;
import ro.tuc.ds2020.services.HourlyConsumptionService;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.websocket.WebSocketController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Ds2020Application {
    public static void main(String[] args) {

        SpringApplication.run(Ds2020Application.class, args);
    }


    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebSocketController webSocketTextController;
    @Autowired
    MeasurementService measurementService;
    HourlyConsumption hourlyConsumption;
    HourlyConsumptionRepository hourlyConsumptionRepository;
    IMeasurementRepository iMeasurementRepository;
    HourlyConsumptionService hourlyConsumptionService;
    HourlyConsumptionScheduler hourlyConsumptionScheduler;


    @RabbitListener(queues = "finalq2")
    public void handleMessage(String msg1) throws Exception {
        System.out.println(msg1);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(msg1);

        int idDevice = jsonNode.get("iddevice").asInt();
        String timestampStr = jsonNode.get("timestamp").asText();
        Date timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(timestampStr);
        Double data = jsonNode.get("data").asDouble();

        Measurement measurement = new Measurement();
        measurement.setIddevice(idDevice);
        measurement.setTimest(timestamp);
        measurement.setVal(data);
        measurement.setMhec(200.0);

        double currentSum = measurementService.getCurrentSumOfMeasurementsForDevice(idDevice);
        measurement.setSumofmeasurments(currentSum + measurement.getVal());

        if (currentSum + measurement.getVal() > 200.0) {
            webSocketTextController.sendMessage("over");

        }
        measurementService.saveMeasurement(measurement);
    }


//    @Autowired
//    private IMeasurementRepository measurementRepository;
//
//    @RabbitListener(queues = "secondQueue")
//    public void handleMessage2(String message) {
//
//        // Convertim mesajul la tipul dorit (presupunând că este un Double)
//        Double mhec = Double.parseDouble(message);
////        System.out.println(message);
//
//
//        // Apelăm metoda din service pentru a actualiza mhec-ul pentru dispozitivul cu ID-ul 7
//        measurementService.updateMhecForDeviceSeven(mhec);
//    }
//}
}