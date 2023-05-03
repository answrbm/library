package ansarbektassov.service;

import ansarbektassov.models.Book;
import ansarbektassov.models.Person;
import ansarbektassov.repositories.BooksRepository;
import ansarbektassov.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(String sortByYear) {
        boolean parsedSortByYear = Boolean.parseBoolean(sortByYear);
        if(parsedSortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return findAll();
    }

    public Page<Book> findAll(String page, String booksPerPage) {
        int parsedPage = Integer.parseInt(page);
        int parsedBooksPerPage = Integer.parseInt(booksPerPage);
        return booksRepository.findAll(PageRequest.of(parsedPage,parsedBooksPerPage));
    }

    public Page<Book> findAll(String page, String booksPerPage, String sortByYear) {
        int parsedPage = Integer.parseInt(page);
        int parsedBooksPerPage = Integer.parseInt(booksPerPage);
        boolean parsedSortByYear = Boolean.parseBoolean(sortByYear);
        if(parsedSortByYear)
            return booksRepository.findAll(PageRequest.of(parsedPage,parsedBooksPerPage, Sort.by("year")));
        else
            return booksRepository.findAll(PageRequest.of(parsedPage,parsedBooksPerPage));
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    public List<Book> findByTitle(String title) {
        return booksRepository.findByTitleContaining(title);
    }

    public Optional<Person> getOwner(int id) {
        Optional<Book> foundBook = findById(id);
        if(foundBook.isPresent()) {
            Book book = foundBook.get();
            if(book.getOwner() != null) return Optional.of(book.getOwner());
        }
        return Optional.empty();
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        if(foundBook.isPresent()) {
            Book book = foundBook.get();
            book.setOwner(null);
            booksRepository.save(book);
        }
    }

    @Transactional
    public void assign(int id, Person selectedOwner) {
        Optional<Book> foundBook = booksRepository.findById(id);
        Optional<Person> foundPerson = peopleRepository.findById(selectedOwner.getId());
        if(foundBook.isPresent()) {
            Book book = foundBook.get();
            if(foundPerson.isPresent()) {
                book.setOwner(foundPerson.get());
                book.setTookAt(LocalDate.now());
            }
            booksRepository.save(book);
        }
    }
}
