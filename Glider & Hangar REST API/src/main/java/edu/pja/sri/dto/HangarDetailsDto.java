package edu.pja.sri.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class HangarDetailsDto extends RepresentationModel<HangarDetailsDto> {
    private Long id;
    @NotBlank(message="Name is required")
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters long")
    private String Name;
    private Set<GliderDto> gliders;
}
