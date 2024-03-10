package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.HourlyConsumption;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.HourlyConsumptionRepository;
import ro.tuc.ds2020.repositories.IMeasurementRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class HourlyConsumptionService {

    private final IMeasurementRepository measurementRepository;
    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    @Autowired
    public HourlyConsumptionService(IMeasurementRepository measurementRepository, HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.measurementRepository = measurementRepository;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    public void calculateAndSaveHourlyConsumption(int deviceId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startHour = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime previousHour = startHour.minusHours(1);

        Date start = Date.from(previousHour.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(startHour.atZone(ZoneId.systemDefault()).toInstant());

        List<Measurement> measurements = measurementRepository.findByTimestBetweenAndIddevice(start, end, deviceId);
        double hourlyConsumption = calculateHourlyConsumption(measurements);

        HourlyConsumption hourlyConsumptionEntity = new HourlyConsumption(deviceId, start, (int) hourlyConsumption);
        hourlyConsumptionRepository.save(hourlyConsumptionEntity);
    }

    private double calculateHourlyConsumption(List<Measurement> measurements) {
        return measurements.stream().mapToDouble(Measurement::getVal).sum();
    }
}
