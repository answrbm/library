package ansarbektassov.repositories;

import ansarbektassov.models.Book;
import ansarbektassov.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {

    List<Book> findByOwner(Person owner);
    List<Book> findByTitleContaining(String title);
}
