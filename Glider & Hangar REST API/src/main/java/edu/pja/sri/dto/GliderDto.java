package edu.pja.sri.dto;

import edu.pja.sri.model.Hangar;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GliderDto extends RepresentationModel {
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
    @JsonIgnoreProperties("gliders")
    private Hangar hangar;

}
