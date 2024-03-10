package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.IMeasurementRepository;
import java.time.temporal.ChronoUnit;
import ro.tuc.ds2020.entities.HourlyConsumption;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MeasurementService {

    @Autowired
    IMeasurementRepository iMeasurementRepository;




public Double getCurrentSumOfMeasurementsForDevice(int deviceId) {
    return iMeasurementRepository.getLastSumOfMeasurementsByDeviceId(deviceId);
}
    public void saveMeasurement(Measurement measurement) {
        iMeasurementRepository.save(measurement);
    }



    public void updateMhecForDeviceSeven(Double mhec) {
        int deviceId = 7; // ID-ul dispozitivului
       iMeasurementRepository.updateMhecByDeviceId(mhec,deviceId);
    }






    public List<Measurement> getAll(){
        return iMeasurementRepository.findAll();
    }

}
