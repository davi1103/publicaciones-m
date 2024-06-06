package com.greencomm.Publications.Repository;

import com.greencomm.Publications.Models.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
}
