package ro.tuc.ds2020.services;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.IPersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    IPersonRepository iPersonRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public List<Person> getAll() {
        return iPersonRepository.findAll();
    }

    public Optional<Person> getById(int id) {
        return iPersonRepository.findById(id);
    }

    public Person addPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person nu poate fi null");
        }
        if (person.getUsername() == null || person.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Numele personului este obligatoriu");
        }
        if (person.getPassword() == null || person.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Parola personului este obligatorie");
        }

        return iPersonRepository.save(person);
    }

    public Person getById(Integer id) {
        if (iPersonRepository.findById(id).isPresent()) {
            return iPersonRepository.findById(id).get();

        }

        return null;

    }

    public Person getByUsername(String username) {
        List<Person> persons = iPersonRepository.findByUsername(username);

        if (!persons.isEmpty()) {
            return persons.get(0); // Returnați prima persoană găsită în listă
        }

        return null; // Sau returnați o valoare implicită
    }

    public Person getByUsernameAndPassword(String username, String password) {
        List<Person> persons = iPersonRepository.findByUsername(username);

        if (!persons.isEmpty()) {
            for (Person person : persons) {
                // Dacă în viitor dorești să implementezi criptarea parolelor, poți decomenta și utiliza linia următoare:
                // if (passwordEncoder.matches(password, person.getPassword())) {
                return person;
                // }
            }
        }
        return null;
    }




    public List<Person> getByUsernameAndPassword2(String username, String password) {
        List<Person> persons = iPersonRepository.findByUsername(username);
        List<Person> matchingPersons = new ArrayList<>();

        if (!persons.isEmpty()) {
            for (Person person : persons) {
                if (passwordEncoder.matches(password, person.getPassword())) {
                    matchingPersons.add(person);
                }
            }
        }
        return matchingPersons;
    }





    public Person savePerson(Person person){
        return iPersonRepository.save(person);
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





}
