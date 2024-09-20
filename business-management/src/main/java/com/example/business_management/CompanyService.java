package com.example.business_management;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    // Get all companies
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional // Ensure that LOB operations are done within a transaction
    public Optional<Company> findCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // Save or update company
    @Transactional  // Make sure the method operates within a transaction
    public Company saveOrUpdateCompany(Company company) {
        return companyRepository.save(company);
    }

    // Delete company by ID
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
    
}