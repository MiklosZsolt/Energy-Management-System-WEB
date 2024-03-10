package ro.tuc.ds2020.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.services.PersonService;

@Service
public class JwtUserDetailsService implements UserDetailsService {
@Autowired
    PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personService.getByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
            return new User(person.getUsername(), person.getPassword(), authorities);
        }
    }

}