package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Device;
import java.util.List; // Adăugați importul pentru List

@Repository
public interface IDeviceRepository extends JpaRepository<Device, Integer> {

    void deleteById(Integer id);

    List<Device> findByUsernameOwner(String usernameOwner); // Metoda pentru a găsi dispozitivele după usernameOwner
}
