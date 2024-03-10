package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.services.HourlyConsumptionService;

@Component
public class HourlyConsumptionScheduler {

    private int deviceId; // Păstrați ID-ul dispozitivului aici

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Autowired
    private HourlyConsumptionService hourlyConsumptionService;

    @Scheduled(cron = "0 0 * * * *") // Rulează la fiecare oră
    public void calculateAndSaveHourlyConsumption() {
        // Calcul și salvare pentru dispozitivul curent
        hourlyConsumptionService.calculateAndSaveHourlyConsumption(deviceId);
    }
}



