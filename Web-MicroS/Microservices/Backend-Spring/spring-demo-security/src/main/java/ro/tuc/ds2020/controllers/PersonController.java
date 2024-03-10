package ro.tuc.ds2020.controllers;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.security.JwtTokenUtil;
import ro.tuc.ds2020.security.JwtUserDetailsService;
import ro.tuc.ds2020.services.PersonService;

import ro.tuc.ds2020.security.JwtRequest;
import ro.tuc.ds2020.security.JwtResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return null;
        }
    };

//    @RequestMapping(method = RequestMethod.POST, value="/update")
//    @ResponseBody
//    public Person updatePerson(@RequestParam(name="id") Integer id,@RequestParam(name="username") String username, @RequestParam(name="password") String password) throws IOException{
//
//        return personService.updatePerson(id, username, password);
//    }


    // New method for creating JWT token after successful authentication
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String role = userDetails.getAuthorities().toString(); // Presupunând că rolul este stocat în autorități

        return ResponseEntity.ok(new JwtResponse(token, role));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    public Person savePerson(@RequestBody Person person) {
        String externalApiUrl = "http://localhost:8080/person/save";
        try {
            // Presupunând că doriți să trimiteți persoana primită direct către API-ul extern
            ResponseEntity<Person> response = restTemplate.postForEntity(externalApiUrl, person, Person.class);
            return response.getBody(); // Returnează corpul răspunsului primit de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return null; // sau gestionarea adecvată a erorilor
        }
    }


    @GetMapping("/getById")
    public ResponseEntity<Person> getById(@RequestParam(name = "id") Integer id) {
        String externalApiUrl = "http://localhost:8080/person/getById2?id=" + id;

        try {
            // Efectuați cererea GET și primiți răspunsul
            ResponseEntity<Person> response = restTemplate.getForEntity(
                    externalApiUrl,
                    Person.class
            );

            // Returnează corpul răspunsului primit de la API-ul extern
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getByUsernameOwner/{usernameOwner}")
    public ResponseEntity<List<Device>> getByUsernameOwner(@PathVariable String usernameOwner) {
        String externalApiUrl = "http://localhost:8081/device/byOwner/" + usernameOwner;

        try {
            // Efectuați cererea GET și primiți răspunsul
            ResponseEntity<List<Device>> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Device>>() {}
            );

            // Returnează corpul răspunsului primit de la API-ul extern
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        String externalApiUrl = "http://localhost:8080/person/delete/" + id;
        try {
            // Apelarea API-ului extern pentru ștergerea persoanei
            restTemplate.delete(externalApiUrl);
            return ResponseEntity.ok("Person deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting person");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<Person> getAll() {
        String externalApiUrl = "http://localhost:8080/person/all";
        try {
            ResponseEntity<List<Person>> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Person>>() {
                    }
            );
            return response.getBody(); // Returnează lista de persoane primită de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return null; // sau gestionarea adecvată a erorilor
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allM")
    public List<Measurement> getAllMeasurement() {
        String externalApiUrl = "http://localhost:8082/measurement/all";
        try {
            ResponseEntity<List<Measurement>> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Measurement>>() {
                    }
            );
            return response.getBody(); // Returnează lista de persoane primită de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return null; // sau gestionarea adecvată a erorilor
        }
    }

    ///devices
    @RequestMapping(method = RequestMethod.GET, value = "/allDevice")
    @ResponseBody
    public List<Device> getAllDevice() {
        String externalApiUrl = "http://localhost:8081/device/all";
        try {
            ResponseEntity<List<Device>> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Device>>() {
                    }
            );
            return response.getBody(); // Returnează lista de dispozitive primită de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return null; // sau gestionarea adecvată a erorilor
        }
    }

    @DeleteMapping("/deleteDevice/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
        String externalApiUrl = "http://localhost:8081/device/delete/" + id;
        try {
            restTemplate.delete(externalApiUrl);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting device");
        }
    }

    @PostMapping("/CheckifUserClient/{username}")
    @ResponseBody
    public ResponseEntity<Person> getPersonByUsername(@PathVariable String username) {
        String externalApiUrl = "http://localhost:8080/person/" + username;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Person> response = restTemplate.postForEntity(externalApiUrl, entity, Person.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/saveDevice")
    @ResponseBody
    public Device saveDevice(@RequestBody Device device) {
        String externalApiUrl = "http://localhost:8081/device/save";
        try {
            // Presupunând că doriți să trimiteți persoana primită direct către API-ul extern
            ResponseEntity<Device> response = restTemplate.postForEntity(externalApiUrl, device, Device.class);
            return response.getBody(); // Returnează corpul răspunsului primit de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return null; // sau gestionarea adecvată a erorilor
        }
    }

    @PostMapping("/updateDevice/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable(value = "id") Integer id, @RequestBody Device device) {
        System.out.println("lofasz");
        String externalApiUrl = "http://localhost:8081/device/updateD/" + id; // Presupunând că endpoint-ul extern este de acest tip
        try {
            device.setId(id); // Setează ID-ul dispozitivului cu ID-ul primit în path
            ResponseEntity<Device> response = restTemplate.postForEntity(externalApiUrl, device, Device.class);
            return response; // Returnează întreg răspunsul primit de la API-ul extern
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Returnează un răspuns de eroare
        }
    }

        @PostMapping("/updatePerson/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") Integer id, @RequestBody Person person) {
            String externalApiUrl = "http://localhost:8080/person/update/" + id;

            // Setarea ID-ului persoanei pentru a corespunde cu cel din cale
            try {
                person.setId(id); // Setează ID-ul dispozitivului cu ID-ul primit în path
                ResponseEntity<Person> response = restTemplate.postForEntity(externalApiUrl, person, Person.class);
                return response; // Returnează întreg răspunsul primit de la API-ul extern
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Returnează un răspuns de eroare
            }
        }


    @DeleteMapping("/deleteDeviceByUsername/{username}")
    @ResponseBody
    public ResponseEntity<String> deleteDevicesByOwnerUsername(@PathVariable String username) {
        String externalApiUrl = "http://localhost:8081/device/deleteDevices/" + username;
        try {
            restTemplate.delete(externalApiUrl);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting device");
        }
    }






}

