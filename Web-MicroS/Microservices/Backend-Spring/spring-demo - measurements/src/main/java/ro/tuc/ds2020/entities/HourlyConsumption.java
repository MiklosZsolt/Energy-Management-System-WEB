package ro.tuc.ds2020.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class HourlyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int idDevice;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private int hourlyConsumption;

    public HourlyConsumption() {
        // Constructor implicit necesar pentru JPA
    }

    public HourlyConsumption(int idDevice, Date time, int hourlyConsumption) {
        this.idDevice = idDevice;
        this.time = time;
        this.hourlyConsumption = hourlyConsumption;
    }

    // Getters și setters
    // ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getHourlyConsumption() {
        return hourlyConsumption;
    }

    public void setHourlyConsumption(int hourlyConsumption) {
        this.hourlyConsumption = hourlyConsumption;
    }
}
