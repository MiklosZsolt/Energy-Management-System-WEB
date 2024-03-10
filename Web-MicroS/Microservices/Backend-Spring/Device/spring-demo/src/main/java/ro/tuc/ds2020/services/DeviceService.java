package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.IDeviceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    IDeviceRepository iDeviceRepository;

    public List<Device> getAll() {
        return iDeviceRepository.findAll();
    }

    public Optional<Device> getById(int id) {
        return iDeviceRepository.findById(id);
    }

    public Device saveDevice(Device device) {
        return iDeviceRepository.save(device);
    }


    public ResponseEntity<String> deleteDevice(Long id) {
        if (iDeviceRepository.existsById(Math.toIntExact(id))) {
            iDeviceRepository.deleteById(Math.toIntExact(id));
            return ResponseEntity.status(HttpStatus.OK).body("Device deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found");
        }
    }

    public List<Device> getDevicesByUsernameOwner(String usernameOwner) {
        List<Device> devices = iDeviceRepository.findByUsernameOwner(usernameOwner);
        if (devices.isEmpty()) {
            return null;
        }
        return devices;
    }


    public ResponseEntity<String> deleteDevicesByUsernameOwner(String username) {
        // Găsiți toate dispozitivele cu usernameOwner-ul dat
        List<Device> devicesToDelete = iDeviceRepository.findByUsernameOwner(username);

        if (devicesToDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No devices found for the specified username");
        }

        // Ștergeți toate dispozitivele găsite
        iDeviceRepository.deleteAll(devicesToDelete);

        return ResponseEntity.status(HttpStatus.OK).body("All devices for the specified username deleted successfully");
    }



    public Device updateDevice(Device device) {
        Optional<Device> existingDevice = iDeviceRepository.findById(Math.toIntExact(Long.valueOf(device.getId())));

        if (existingDevice.isPresent()) {
            Device currentDevice = existingDevice.get();

            // Verificăm dacă adresa din cerere este goală sau nu
            if (!device.getAddress().isEmpty()) {
                currentDevice.setAddress(device.getAddress());
            }
            if (!device.getDescription().isEmpty()) {
                currentDevice.setDescription(device.getDescription());
            }
            if (!device.getMhec().isEmpty()) {
                currentDevice.setMhec(device.getMhec());
            }
            if (!device.getUsernameOwner().isEmpty()) {
                currentDevice.setUsernameOwner(device.getUsernameOwner());
            }

            return iDeviceRepository.save(currentDevice);
        } else {
            // Dispozitivul nu a fost găsit în baza de date
            throw new RuntimeException("Dispozitivul nu a fost găsit.");
        }
    }

    public void updateOwner(String oldUsername, String newUsername) {
        List<Device> devices = iDeviceRepository.findByUsernameOwner(oldUsername);

        for (Device device : devices) {
            device.setUsernameOwner(newUsername);
        }

        iDeviceRepository.saveAll(devices);
    }

}

