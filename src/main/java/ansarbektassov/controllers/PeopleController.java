package ansarbektassov.controllers;

import ansarbektassov.models.Person;
import ansarbektassov.service.PeopleService;
import ansarbektassov.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService service;

    @Autowired
    public PeopleController(PeopleService service) {
        this.service = service;
    }

    @GetMapping()
    public String getPeople(Model model) {
        model.addAttribute("people",service.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String getPerson(Model model,@PathVariable("id") int id) throws PersonNotFoundException {
        Optional<Person> foundPerson = service.findById(id);
        if(foundPerson.isPresent()) {
            Person person = foundPerson.get();
            model.addAttribute("person",person);
            model.addAttribute("books",service.findByOwner(person));
        }
        else throw new PersonNotFoundException("Person wasn't found");

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

        service.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/update")
    public String updatePerson(Model model,@PathVariable("id")int id) throws PersonNotFoundException {
        Optional<Person> foundPerson = service.findById(id);
        if(foundPerson.isPresent()) model.addAttribute("person",foundPerson.get());
        else throw new PersonNotFoundException("Person wasn't found");
        return "people/update";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Validated Person person, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/update";

        service.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        service.delete(id);
        return "redirect:/people";
    }

}
