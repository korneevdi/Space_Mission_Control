package spacemissioncontrol.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

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
                && Objects.equals(category, equipment.category)
                && Objects.equals(weightKg, equipment.weightKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, weightKg);
    }
}
