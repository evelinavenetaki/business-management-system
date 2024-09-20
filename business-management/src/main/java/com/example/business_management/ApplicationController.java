package com.example.business_management;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // Get all applications
    @GetMapping("/all")
public ResponseEntity<List<Application>> getAllApplicationsForEmployees() {
    List<Application> applications = applicationService.findAllApplications();
    return ResponseEntity.ok(applications);
}

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.findApplicationById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new application
     @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
public ResponseEntity<Application> createApplication(
        @RequestParam("companyName") String companyName,
        @RequestParam("members") String members,
        @RequestParam("purpose") String purpose,
        @RequestParam("location") String location,
        @RequestParam("file") MultipartFile file) {
    
    // Create a new Company object
    Company newCompany = new Company();
    newCompany.setName(companyName);
    newCompany.setMembers(Arrays.asList(members.split(",")));  // Split members by comma
    newCompany.setPurpose(purpose);
    newCompany.setMainLocation(location);

    // Convert and save the file (PDF) as a byte array
    try {
        newCompany.setOperatingStatute(file.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Save the company first
    Company savedCompany = companyService.saveOrUpdateCompany(newCompany);

    // Create a new Application object
    Application newApplication = new Application();
    newApplication.setCompany(savedCompany);  // Set the saved company in the application
    newApplication.setStatus(ApplicationStatus.PENDING);  // Initially set to PENDING
    newApplication.setSubmittedBy(userService.getCurrentUser());  // Get current user (applicant)
    newApplication.setSubmittedAt(LocalDateTime.now());

    // Save the application
    Application savedApplication = applicationService.saveOrUpdateApplication(newApplication);

    return ResponseEntity.ok(savedApplication);
}

    // Update an existing application
    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        return applicationService.findApplicationById(id)
            .map(existingApplication -> {
                existingApplication.setCompany(applicationDetails.getCompany());
                existingApplication.setStatus(applicationDetails.getStatus());
                existingApplication.setSubmittedBy(applicationDetails.getSubmittedBy());
                Application updatedApplication = applicationService.saveOrUpdateApplication(existingApplication);
                return ResponseEntity.ok(updatedApplication);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an application by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok().build();
    }

    // Approve an application
    @PutMapping("/approve/{id}")
    public ResponseEntity<Application> approveApplication(@PathVariable Long id, @RequestBody User employee) {
        Application approvedApplication = applicationService.approveApplication(id, employee);
        return approvedApplication != null ? ResponseEntity.ok(approvedApplication) : ResponseEntity.notFound().build();
    }

    // Reject an application
    @PutMapping("/reject/{id}")
    public ResponseEntity<Application> rejectApplication(@PathVariable Long id, @RequestBody User employee) {
        Application rejectedApplication = applicationService.rejectApplication(id, employee);
        return rejectedApplication != null ? ResponseEntity.ok(rejectedApplication) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/user")
public ResponseEntity<List<Application>> getApplicationsForUser() {
    User currentUser = userService.getCurrentUser();  // Assuming userService can get the logged-in user
    List<Application> applications = applicationService.findApplicationsByUser(currentUser);
    return ResponseEntity.ok(applications);
}
}