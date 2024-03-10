package ro.tuc.ds2020.entities;

import javax.persistence.*;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true, unique = false)
    private String description;

    @Column(nullable = true, unique = false)
    private String address;

    @Column(nullable = true, unique = false)
    private String mhec;

    @Column(nullable = true, unique = false)
    private String usernameOwner;

    public String getUsernameOwner() {
        return usernameOwner;
    }

    public void setUsernameOwner(String usernameOwner) {
        this.usernameOwner = usernameOwner;
    }

    public Device(int id, String description, String address, String mhec, String usernameOwner) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.mhec = mhec;
        this.usernameOwner = usernameOwner;
    }

    public Device(int id, String description, String address, String mhec) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.mhec = mhec;
    }

    public Device() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMhec() {
        return mhec;
    }

    public void setMhec(String mhec) {
        this.mhec = mhec;
    }


}