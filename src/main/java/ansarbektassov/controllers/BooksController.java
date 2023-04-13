package ansarbektassov.controllers;

import ansarbektassov.dao.BookDAO;
import ansarbektassov.dao.OrderDAO;
import ansarbektassov.dao.PersonDAO;
import ansarbektassov.models.Book;
import ansarbektassov.models.Order;
import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final OrderDAO orderDAO;
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(OrderDAO orderDAO, BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.orderDAO = orderDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getBooks(Model model) {
        model.addAttribute("books",bookDAO.getBooks());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBook(Model model,@PathVariable("id") int id) {
        Optional<Order> order = orderDAO.getOrderByBookId(id);

        if(order.isPresent()) {
            Person person = personDAO.getPerson(order.get().getPersonId());
            order.get().setPersonName(person.getName());
        }

        model.addAttribute("person",new Person());
        model.addAttribute("people",personDAO.getPeople());
        model.addAttribute("order",order);
        model.addAttribute("emptyOrder",new Order());
        model.addAttribute("book",bookDAO.getBook(id));

        return "books/book";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        model.addAttribute("book",new Book());
        return "books/create";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Validated Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/create";

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/update")
    public String updateBook(Model model,@PathVariable("id")int id) {
        model.addAttribute("book",bookDAO.getBook(id));
        return "books/update";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Validated Book book, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "books/update";

        bookDAO.update(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

}
