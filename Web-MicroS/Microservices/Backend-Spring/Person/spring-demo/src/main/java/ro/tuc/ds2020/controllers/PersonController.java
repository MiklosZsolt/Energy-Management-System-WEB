package ro.tuc.ds2020.controllers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.services.PersonService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*")
@Controller
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public List<Person> getAll() {
        return personService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable int id) {
        Optional<Person> person = personService.getById(id);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{username}")
    public ResponseEntity<Person> getByUsername(@PathVariable String username) {
        Person person = personService.getByUsername(username);
        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody Person person) {
        Person addPerson = personService.addPerson(person);
        return ResponseEntity.ok(addPerson);
    }

    @PostMapping("/addUser2")
    @ResponseBody
    public Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    public Person savePerson(@RequestBody Person person) throws IOException {
        String password = person.getPassword();
        String newPassword = passwordEncoder.encode(password); // Criptează parola
        person.setPassword(newPassword);
        return personService.savePerson(person);
    }

//    @RequestMapping(method = RequestMethod.POST, value = "/getByUserandPass")
//    @ResponseBody
//    public String getByUsernamePassword(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//        return personService.getByUsernameAndPassword(username, password);
//
//    }
    @RequestMapping(method = RequestMethod.POST, value = "/getByUserandPass")
    @ResponseBody
    public String getByUsernamePassword2(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Person person = personService.getByUsernameAndPassword(username, password);
        String role = person.getRole();

//        if (person != null) {
//            // Verificarea numelui de utilizator pentru a determina tipul de utilizator
//            if (person.equals("client")) {
//                return 1; // Utilizator client
//            } else if (person.equals("admin")) {
//                return 0; // Utilizator admin
//            }
//        }

        return role; // Cod pentru situația în care persoana nu este găsită sau nu este nici client, nici admin
    }




    @RequestMapping(method = RequestMethod.POST, value = "/getById")
    @ResponseBody
    public  Person getById(@RequestParam(name = "id") Integer id){
        return personService.getById(id);
    }

    @GetMapping("/getById2")
    @ResponseBody
    public Person getById2(@RequestParam(name = "id") Integer id) {
        return personService.getById(id);
    }


//    @RequestMapping(method = RequestMethod.POST, value = "/updatePerson")
//    public Person updatePerson(@RequestBody Person person) throws IOException {
//        return personService.savePerson(person);
//    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") Integer id, @RequestBody Person person) {
        person.setId(id);
        Person updatedPerson = personService.updatePerson(person);
        return ResponseEntity.ok(updatedPerson);
    }

    






}





