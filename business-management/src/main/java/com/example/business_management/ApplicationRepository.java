package com.example.business_management;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
   // Find applications submitted by a specific user
    List<Application> findBySubmittedBy(User user);
}
