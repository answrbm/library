package ansarbektassov.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Person {

    private int id;

    @NotEmpty
    @Size(min = 2, max = 100, message = "Author name should be between 2 and 100 characters")
    private String name;
    @Pattern(regexp = "\\d{4}.\\d{2}.\\d{2}", message = "Use pattern: year.mm.dd")
    private String birthDate;

    public Person() {}

    public Person(int id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
