package com.greencomm.Publications.Repository;

import com.greencomm.Publications.Models.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {


    @Transactional(readOnly = true)
    @Query("SELECT p FROM Publication p ORDER BY p.fecha DESC ")
    List<Publication> findAllByOrderByFechaDesc();
}
