package com.example.business_management;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application saveOrUpdateApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public Application approveApplication(Long id, User employee) {
        return applicationRepository.findById(id)
            .map(existingApplication -> {
                existingApplication.setStatus(ApplicationStatus.APPROVED);
                existingApplication.setApprovedBy(employee);
                existingApplication.setApprovedAt(LocalDateTime.now());
                existingApplication.getCompany().setVatNumber(generateVatNumber()); // Generate VAT
                return applicationRepository.save(existingApplication);
            }).orElse(null);
    }

    public Application rejectApplication(Long id, User employee) {
        return applicationRepository.findById(id)
            .map(existingApplication -> {
                existingApplication.setStatus(ApplicationStatus.REJECTED);
                existingApplication.setApprovedBy(employee);
                existingApplication.setApprovedAt(LocalDateTime.now());
                return applicationRepository.save(existingApplication);
            }).orElse(null);
    }
    
    public List<Application> findApplicationsByUser(User user) {
        return applicationRepository.findBySubmittedBy(user);
    }
    
    private String generateVatNumber() {
        return "VAT-" + (int)(Math.random() * 100000);  // Random VAT number for demonstration
    }

}

