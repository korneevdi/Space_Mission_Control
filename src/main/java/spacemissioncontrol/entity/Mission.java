package spacemissioncontrol.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Column(name = "status")
    private String status;

    // mission_details: one-to-one
    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private MissionDetails missionDetails;

    // equipment: one-to-many
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipment> equipmentList;

    // spaceships: many-to-many
    @ManyToMany
    @JoinTable(
            name = "mission_spaceships",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "spaceship_id")
    )
    private List<Spaceship> spaceshipList;

    // astronauts: many-to-many
    @ManyToMany
    @JoinTable(
            name = "mission_astronauts",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "astronaut_id")
    )
    private List<Astronaut> astronautList;

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

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MissionDetails getMissionDetails() {
        return missionDetails;
    }

    public void setMissionDetails(MissionDetails missionDetails) {
        this.missionDetails = missionDetails;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<Spaceship> getSpaceshipList() {
        return spaceshipList;
    }

    public void setSpaceshipList(List<Spaceship> spaceshipList) {
        this.spaceshipList = spaceshipList;
    }

    public List<Astronaut> getAstronautList() {
        return astronautList;
    }

    public void setAstronautList(List<Astronaut> astronautList) {
        this.astronautList = astronautList;
    }

    @Override
    public String toString() {
        return """
                Mission
                    ID: %s,
                    Name: %s,
                    Launch date: %s,
                    Status: %s,
                    Budget: %s Mio. USD,
                    Duration: %s days,
                    Description: %s
                """.formatted(id, name, launchDate, status,
                missionDetails.getBudgetMillionUSD(), missionDetails.getDurationDays(), missionDetails.getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return Objects.equals(name, mission.name)
                && Objects.equals(launchDate, mission.launchDate)
                && Objects.equals(status, mission.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, launchDate, status);
    }
}
