package ansarbektassov.service;

import ansarbektassov.models.Book;
import ansarbektassov.models.Person;
import ansarbektassov.repositories.BooksRepository;
import ansarbektassov.repositories.PeopleRepository;
import ansarbektassov.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository,BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    public List<Book> findByOwner(Person owner) {
        List<Book> books = booksRepository.findByOwner(owner);
        return checkOverdue(books);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> checkOverdue(List<Book> books) {
        for(Book book : books) {
            LocalDate currentTime = LocalDate.now();
            LocalDate tookAtTime = book.getTookAt();
            long daysBetween = ChronoUnit.DAYS.between(tookAtTime,currentTime);
            System.out.println(daysBetween);
            if(daysBetween > 10) book.setOverdue(true);
        }
        return books;
    }
}
