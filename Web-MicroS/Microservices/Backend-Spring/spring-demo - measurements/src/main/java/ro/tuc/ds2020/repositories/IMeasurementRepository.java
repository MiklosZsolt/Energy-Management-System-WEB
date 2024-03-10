package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Measurement;

import java.util.Date;
import java.util.List;

@Repository
public interface IMeasurementRepository extends JpaRepository<Measurement,Integer> {

    @Query("SELECT SUM(m.sumofmeasurments) FROM Measurement m WHERE m.iddevice = :deviceId")
    Double getCurrentSumOfMeasurementsByDeviceId2(int deviceId);

    @Query(value = "SELECT sumofmeasurments FROM Measurement WHERE iddevice = :deviceId ORDER BY timest DESC LIMIT 1", nativeQuery = true)
    Double getLastSumOfMeasurementsByDeviceId(int deviceId);

    @Query("SELECT m FROM Measurement m WHERE m.iddevice = :deviceId")
    List<Measurement> findByDeviceId(int deviceId);
    List<Measurement> findByIddevice(int deviceId);
    Measurement findById(int id); // Adăugăm metoda pentru a găsi o măsurătoare după id

    List<Measurement> findByTimestBetweenAndIddevice(Date start, Date end, int idDevice);
    @Modifying
    @Query("UPDATE Measurement m SET m.mhec = :mhecValue WHERE m.iddevice = :deviceId")
    void updateMhecByDeviceId(Double mhecValue, int deviceId);

}




