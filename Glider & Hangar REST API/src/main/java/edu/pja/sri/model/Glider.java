package edu.pja.sri.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Glider {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotBlank(message="Name is required")
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters long")
    private String Name;
    @NotBlank(message="Origin is required")
    @Size(min=2, max=15, message="Origin must be between 2 and 15 characters long")
    private String Origin;
    @NotBlank(message="Date of first flight is required")
    private String firstFlightDate;
    private Long maxVel;
    @ManyToOne
    @JoinColumn(name="hangar_id")
    private Hangar hangar;

}
