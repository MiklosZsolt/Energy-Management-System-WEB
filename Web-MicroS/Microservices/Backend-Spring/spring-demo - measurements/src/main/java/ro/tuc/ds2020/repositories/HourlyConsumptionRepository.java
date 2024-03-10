package ro.tuc.ds2020.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.HourlyConsumption;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, Long> {

    List<HourlyConsumption> findByIdDeviceAndTimeBetween(int idDevice, Date start, Date end);

    @Query(value = "SELECT SUM(m.val) FROM Measurement m WHERE m.iddevice = :idDevice AND m.timest BETWEEN :start AND :end")
    Double calculateHourlyConsumption(int idDevice, Date start, Date end);

    default void saveHourlyConsumption(int idDevice, Date start, int consumption) {
        HourlyConsumption hourlyConsumption = new HourlyConsumption(idDevice, start, consumption);
        save(hourlyConsumption);
    }
}

