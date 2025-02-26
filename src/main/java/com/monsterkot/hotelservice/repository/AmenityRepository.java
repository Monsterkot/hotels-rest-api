package com.monsterkot.hotelservice.repository;
import com.monsterkot.hotelservice.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    @Query("SELECT a FROM Amenity a WHERE LOWER(a.name) IN :names")
    List<Amenity> findByNameInIgnoreCase(@Param("names") List<String> names);

}
