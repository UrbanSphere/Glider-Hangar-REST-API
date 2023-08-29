package edu.pja.sri.repo;

import edu.pja.sri.model.Glider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GliderRepository extends CrudRepository <Glider, Long> {
    List<Glider> findAll();

    @Query("select h.gliders from Hangar as h where h.id = :hangarId")
    List<Glider> findGlidersByHangarId(@PathVariable Long hangarId);
}
