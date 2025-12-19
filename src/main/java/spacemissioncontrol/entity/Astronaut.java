package spacemissioncontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "astronauts")
public class Astronaut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "astronaut_id")
    private Integer id;

    @Size(max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "rank")
    private String rank;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Size(max = 50)
    @Column(name = "country", nullable = false)
    private String country;

    // missions: many-to-many
    @ManyToMany(mappedBy = "astronautList")
    private List<Mission> missionList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Mission> getMissionList() {
        return missionList;
    }

    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }

    @Override
    public String toString() {
        return """
                Astronaut
                    ID: %s,
                    First name: %s,
                    Last name: %s,
                    Rank: %s,
                    Birth date: %s,
                    Country: %s
                """.formatted(id, firstName, lastName, rank, birthDate, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Astronaut astronaut = (Astronaut) o;
        return Objects.equals(firstName, astronaut.firstName)
                && Objects.equals(lastName, astronaut.lastName)
                && Objects.equals(birthDate, astronaut.birthDate)
                && Objects.equals(country, astronaut.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate, country);
    }
}
