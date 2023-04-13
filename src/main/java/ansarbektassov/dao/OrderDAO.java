package ansarbektassov.dao;

import ansarbektassov.models.Book;
import ansarbektassov.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order getOrder(int id) {
        return jdbcTemplate.query("SELECT * FROM Orders WHERE id = ?",new Object[]{id},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny().orElse(null);
    }

    public Optional<Order> getOrderByBookId(int bookId) {
        return jdbcTemplate.query("SELECT * FROM Orders WHERE book_id = ?",new Object[]{bookId},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public List<Order> getOrdersByPersonId(int personId) {
        return new ArrayList<>(jdbcTemplate.query(
                "SELECT * FROM Orders AS o " +
                        "JOIN Book AS b ON o.book_id = b.id WHERE person_id = ?", new Object[]{personId},
                new BeanPropertyRowMapper<>(Order.class)));
    }

    public List<Order> getOrders() {
        return jdbcTemplate.query("SELECT * FROM Orders",new BeanPropertyRowMapper<>(Order.class));
    }

    public void save(Order order) {
        jdbcTemplate.update("INSERT INTO Orders(book_id,person_id) VALUES(?,?)",
                order.getBookId(),order.getPersonId());
    }

    public void update(int id, Order order) {
        jdbcTemplate.update("UPDATE Orders SET book_id = ?, person_id = ? WHERE id = ?",
                order.getBookId(),order.getPersonId(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Orders WHERE id = ?",id);
    }
}
