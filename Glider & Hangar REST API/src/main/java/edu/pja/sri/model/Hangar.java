package edu.pja.sri.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hangar {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotBlank(message="Name is required")
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters long")
    private String Name;
    @OneToMany(mappedBy = "hangar")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Glider> gliders;

}
