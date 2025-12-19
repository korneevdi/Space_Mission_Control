package spacemissioncontrol.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "spaceships")
public class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_id")
    private Integer id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    // missions: many-to-many
    @ManyToMany(mappedBy = "spaceshipList")
    private List<Mission> missionList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
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
                Spaceship
                    ID: %s,
                    Model: %s,
                    Manufacturer: %s,
                    Weight: %s kg
                """.formatted(id, model, manufacturer, weightKg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spaceship spaceship = (Spaceship) o;
        return Objects.equals(model, spaceship.model)
                && Objects.equals(manufacturer, spaceship.manufacturer)
                && Objects.equals(capacity, spaceship.capacity)
                && Objects.equals(weightKg, spaceship.weightKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, manufacturer, capacity, weightKg);
    }
}
