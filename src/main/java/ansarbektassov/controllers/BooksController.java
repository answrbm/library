package ansarbektassov.controllers;

import ansarbektassov.models.Book;
import ansarbektassov.models.Person;
import ansarbektassov.service.BooksService;
import ansarbektassov.service.PeopleService;
import ansarbektassov.util.BookNotFoundException;
import ansarbektassov.util.PersonNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService bookService,PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getBooks(Model model, HttpServletRequest request) {
        String page = request.getParameter("page");
        String booksPerPage = request.getParameter("books_per_page");
        String sortByYear = request.getParameter("sort_by_year");

        if(booksPerPage != null && page != null)
            if(sortByYear == null)
                model.addAttribute("books",bookService.findAll(page,booksPerPage));
            else
                model.addAttribute("books",bookService.findAll(page,booksPerPage,sortByYear));
        else if(sortByYear != null)
            model.addAttribute("books", bookService.findAll(sortByYear));
        else
            model.addAttribute("books",bookService.findAll());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBook(Model model,@PathVariable("id") int id, @ModelAttribute("person") Person person) throws BookNotFoundException {
        Optional<Book> foundBook = bookService.findById(id);

        if(foundBook.isPresent()) {
            Book book = foundBook.get();
            model.addAttribute("book",book);
            Optional<Person> owner = bookService.getOwner(id);
            if(owner.isPresent()) model.addAttribute("owner",owner);
            else model.addAttribute("people",peopleService.findAll());
        }
        else throw new BookNotFoundException("Book wasn't found");

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

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/update")
    public String updateBook(Model model,@PathVariable("id")int id) throws BookNotFoundException {
        Optional<Book> foundBook = bookService.findById(id);
        if(foundBook.isPresent()) model.addAttribute("book", foundBook.get());
        else throw new BookNotFoundException("Book wasn't found");
        return "books/update";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Validated Book book, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "books/update";

        bookService.update(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id,selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "title",required = false) String title) {
        model.addAttribute("title",title);
        return "books/search";
    }

    @GetMapping("/search/result")
    public String searchResult(Model model, @ModelAttribute("title") String title) {
        model.addAttribute("books",bookService.findByTitle(title));
        return "books/search";
    }
}
