package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Person;

import java.util.List;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Integer> {

    void deleteById(Integer id);

    List<Person> findByUsernameAndPassword(String username, String password);

    List<Person> findByUsername(String username);





}
