package com.example.business_management;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // JpaRepository provides basic CRUD operations
}

