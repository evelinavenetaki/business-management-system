package com.example.business_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Get all companies
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    // Get company by ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return companyService.findCompanyById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new company
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company savedCompany = companyService.saveOrUpdateCompany(company);
        return ResponseEntity.ok(savedCompany);
    }

    // Update company
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        return companyService.findCompanyById(id)
            .map(existingCompany -> {
                existingCompany.setName(companyDetails.getName());
                existingCompany.setMainLocation(companyDetails.getMainLocation());
                existingCompany.setPurpose(companyDetails.getPurpose());
                existingCompany.setOperatingStatute(companyDetails.getOperatingStatute());
                existingCompany.setMembers(companyDetails.getMembers());
                Company updatedCompany = companyService.saveOrUpdateCompany(existingCompany);
                return ResponseEntity.ok(updatedCompany);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete company
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}