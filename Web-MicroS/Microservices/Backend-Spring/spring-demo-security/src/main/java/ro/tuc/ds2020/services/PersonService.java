package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.IPersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {
    @Autowired
    IPersonRepository iPersonRepository;
//    @Autowired
//    IDeviceRepository ideviceRepository;

    public List<Person> getAll(){
        return iPersonRepository.findAll();
    }

    public Person savePerson(Person person){

        return iPersonRepository.save(person);
    }


    public void delete(Integer id) {
        iPersonRepository.deleteById(id);
    }

    public Person updatePerson(Integer id, String username, String password){
        Optional<Person> personOptional = iPersonRepository.findById(id);

        if (personOptional.isPresent()) {
            Person oldPerson = personOptional.get();
            oldPerson.setUsername(username);
            oldPerson.setPassword(password);
            return iPersonRepository.save(oldPerson);
        }
        return null;
    }


    public Person getByUsernameAndPassword(String username, String password) {
        if (iPersonRepository.getByUsernameAndPassword(username,password) != null){
            return iPersonRepository.getByUsernameAndPassword(username,password);
        }
        return null;
    }
    public Person getByUsername(String username) {
       Optional<Person> user = iPersonRepository.findAll().stream().filter(o -> o.getUsername().equals(username)).findFirst();
       return user.orElse(null);
    }

    public ResponseEntity<String> deletePerson(Long id) {
        if (iPersonRepository.existsById(Math.toIntExact(id))) {
            iPersonRepository.deleteById(Math.toIntExact(id));
            return ResponseEntity.status(HttpStatus.OK).body("Device deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found");
        }
    }



    public Person updatePerson(Person person) {
        Optional<Person> existingUtilizator = iPersonRepository.findById(Math.toIntExact(Long.valueOf(person.getId())));

        if (existingUtilizator.isPresent()) {
            Person currentPerson = existingUtilizator.get();

            // Actualizarea câmpurilor cu valorile noi, dacă sunt furnizate în cerere
            if (person.getUsername() != null) {
                currentPerson.setUsername(person.getUsername());
            }
            if (person.getPassword() != null) {
                currentPerson.setPassword(person.getPassword());
            }
            if (person.getRole().equals("admin")) {
                // Dacă role este 1, efectuează acțiuni corespunzătoare
                currentPerson.setRole(person.getRole());
            } else if (person.getRole().equals("client")) {
                currentPerson.setRole(person.getRole());
            }

            return iPersonRepository.save(currentPerson);
        } else {
            // Utilizatorul nu există în baza de date
            throw new RuntimeException("Utilizatorul nu a fost găsit.");
        }


    }

    public Person getById(Integer id) {
        if (iPersonRepository.findById(id).isPresent()) {
            return iPersonRepository.findById(id).get();

        }

        return null;

    }


}

