package edu.pja.sri.repo;

import edu.pja.sri.model.Hangar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HangarRepository extends CrudRepository<Hangar, Long> {

    List<Hangar> findAll();
    @Query("from Hangar as h left join fetch h.gliders where h.id=:hangarId")
    Optional<Hangar> getHangarDetailById(@Param("hangarId") long hangarId);
}

