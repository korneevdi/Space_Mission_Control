package spacemissioncontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "mission_details")
public class MissionDetails {

    @Id
    private Integer id;

    @Positive(message = "Budget must be positive")
    @Column(name = "budget_million_usd")
    private BigDecimal budgetMillionUSD;

    @Positive(message = "Mission duration must be positive")
    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBudgetMillionUSD() {
        return budgetMillionUSD;
    }

    public void setBudgetMillionUSD(BigDecimal budgetMillionUSD) {
        this.budgetMillionUSD = budgetMillionUSD;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }
}
