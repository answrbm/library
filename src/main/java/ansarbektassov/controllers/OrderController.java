package ansarbektassov.controllers;

import ansarbektassov.dao.OrderDAO;
import ansarbektassov.models.Book;
import ansarbektassov.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;

    @Autowired
    public OrderController(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

//    @GetMapping()
//    public String getOrders(Model model) {
//        model.addAttribute("orders",orderDAO.getOrders());
//        return "orders/index";
//    }

//    @GetMapping("/{id}")
//    public String getOrder(Model model,@PathVariable("id") int id) {
//        Order order = orderDAO.getOrder(id);
//
//        model.addAttribute("order",order);
//        return "orders/order";
//    }

    @PostMapping("/create/{bookId}")
    public String createOrder(Order order, @PathVariable("bookId") int bookId) {
        order.setBookId(bookId);
        orderDAO.save(order);
        return "redirect:/books/"+bookId;
    }

//    @PatchMapping("/{id}")
//    public String updateOrder(@ModelAttribute("order") @Validated Order order, BindingResult bindingResult,
//                             @PathVariable("id") int id) {
//        if(bindingResult.hasErrors()) return "orders/update";
//
//        orderDAO.update(id,order);
//        return "redirect:/orders";
//    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        int bookId = orderDAO.getOrder(id).getBookId();
        orderDAO.delete(id);
        return "redirect:/books/"+bookId;
    }

}
