package ansarbektassov.controllers;

import ansarbektassov.dao.BookDAO;
import ansarbektassov.dao.OrderDAO;
import ansarbektassov.dao.PersonDAO;
import ansarbektassov.models.Order;
import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final BookDAO bookDAO;
    private final OrderDAO orderDAO;
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(BookDAO bookDAO, OrderDAO orderDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.orderDAO = orderDAO;
    }

    @GetMapping()
    public String getPeople(Model model) {
        model.addAttribute("people",personDAO.getPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String getPerson(Model model,@PathVariable("id") int id) {
        List<Order> orders = orderDAO.getOrdersByPersonId(id);
        orders.forEach(order -> order.setBookName(bookDAO.getBook(order.getBookId()).getTitle()));

        model.addAttribute("person",personDAO.getPerson(id));
        model.addAttribute("orders",orders);
        return "people/person";
    }

    @GetMapping("/create")
    public String createPerson(Model model) {
        model.addAttribute("person",new Person());
        return "people/create";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Validated Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "people/create";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/update")
    public String updatePerson(Model model,@PathVariable("id")int id) {
        model.addAttribute("person",personDAO.getPerson(id));
        return "people/update";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Validated Person person, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/update";

        personDAO.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
