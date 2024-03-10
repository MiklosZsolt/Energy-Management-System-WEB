package ro.tuc.ds2020.entities;

import lombok.*;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hibernate.type.descriptor.java.JdbcDateTypeDescriptor.DATE_FORMAT;

@Getter
@Setter
//@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idmeasurements;

    @Column(nullable = false)
    private int iddevice;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timest;

    @Column(nullable = false)
    private Double val;

    @Column(nullable = false)
    private Double sumofmeasurments;



    @Column(nullable = false)
    private Double mhec;


    public Double getMhec() {
        return mhec;
    }

    public void setMhec(Double mhec) {
        this.mhec = mhec;
    }

    public Double getSumofmeasurments() {
        return sumofmeasurments;
    }

    public void setSumofmeasurments(Double sumofmeasurments) {
        this.sumofmeasurments = sumofmeasurments;
    }




    public int getIdmeasurements() {
        return idmeasurements;
    }

    public void setIdmeasurements(int idmeasurements) {
        this.idmeasurements = idmeasurements;
    }

    public int getIddevice() {
        return iddevice;
    }

    public void setIddevice(int iddevice) {
        this.iddevice = iddevice;
    }

    public Date getTimest() {
        return timest;
    }

    public void setTimest(Date timest) {
        this.timest = timest;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public Measurement(int idmeasurements, int iddevice, Date timest, Double val, Double sumofmeasurments) {
        this.idmeasurements = idmeasurements;
        this.iddevice = iddevice;
        this.timest = timest;
        this.val = val;
        this.sumofmeasurments = sumofmeasurments;
    }

    public Measurement(int idmeasurements, int iddevice, Date timest, Double val, Double sumofmeasurments, Double mhec) {
        this.idmeasurements = idmeasurements;
        this.iddevice = iddevice;
        this.timest = timest;
        this.val = val;
        this.sumofmeasurments = sumofmeasurments;
        this.mhec = mhec;
    }

    public Measurement(){

    }


    public void setTimest(String date1) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        try {
            // Parsează String-ul într-un obiect de tip Date
            Date parsedDate = dateFormat.parse(date1);

            // Setează valoarea obiectului de tip Date în câmpul timest
            this.timest = parsedDate;
        } catch (ParseException e) {
            // Tratează cazul în care formatul datei nu este corect
            e.printStackTrace();
        }
    }
}
