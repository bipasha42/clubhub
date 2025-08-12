package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CountryUniadminRepository extends JpaRepository<Country, Integer> {
     @Query("SELECT DISTINCT c FROM Country c LEFT JOIN FETCH c.universities ORDER BY c.name")
    List<Country> findAllWithUniversities();

    @Query("SELECT DISTINCT c FROM Country c LEFT JOIN FETCH c.universities WHERE c.id = :id")
    Country findByIdWithUniversities(@Param("id") Integer id);
}

