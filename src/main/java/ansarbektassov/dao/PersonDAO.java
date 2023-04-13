package ansarbektassov.dao;

import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Person getPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?",new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public List<Person> getPeople() {
        return jdbcTemplate.query("SELECT * FROM Person",new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name,birthdate) VALUES(?,?)",
                person.getName(),person.getBirthDate());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET name = ?, birthdate = ? WHERE id = ?",
                person.getName(),person.getBirthDate(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?",id);
    }

}
