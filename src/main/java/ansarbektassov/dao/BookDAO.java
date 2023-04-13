package ansarbektassov.dao;

import ansarbektassov.models.Book;
import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book getBook(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id = ?",new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public List<Book> getBooks() {
        return jdbcTemplate.query("SELECT * FROM Book",new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title,author,year) VALUES(?,?,?)",
                book.getTitle(),book.getAuthor(),book.getYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET title = ?, author = ?, year = ? WHERE id = ?",
                book.getTitle(),book.getAuthor(),book.getYear(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?",id);
    }
}
