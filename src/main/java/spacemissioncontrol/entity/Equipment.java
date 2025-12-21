package spacemissioncontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "equipment",
uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer id;

    @Size(max = 100, message = "Equipment name must not exceed {max} characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 100, message = "Category must not exceed {max} characters")
    @Column(name = "category", nullable = false)
    private String category;

    @Positive(message = "Weight must be positive")
    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    // missions: many-to-one
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public String toString() {
        return """
                Equipment
                    ID: %s,
                    Name: %s,
                    Category: %s,
                    Weight: %s kg,
                    Mission: %s
                """.formatted(id, name, category, weightKg, mission.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(name, equipment.name)
                && Objects.equals(category, equipment.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category);
    }
}
