package com.example.business_management;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.List;

import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false)
    private String name;

    @Column(name = "main_location", nullable = false)  // Renamed to match the schema
    private String mainLocation;

    @Column(nullable = false)
    private String purpose;

    @Lob
@Basic(fetch = FetchType.LAZY)
@Column(nullable = true)
private byte[] operatingStatute;

    @ElementCollection
    @CollectionTable(name = "company_members", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "member_name")
    private List<String> members;  // A list of company members' names

    @Column(nullable = true)
    private String vatNumber;  // Assigned upon approval, can be null initially

    // Default constructor
    public Company() {
    }

    // Constructor with fields
    public Company(String name, String mainLocation, String purpose, List<String> members, byte[] operatingStatute) {
        this.name = name;
        this.mainLocation = mainLocation;
        this.purpose = purpose;
        this.members = members;
        this.operatingStatute = operatingStatute;
    }

    // Getters and setters

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainLocation() {
        return mainLocation;
    }

    public void setMainLocation(String mainLocation) {
        this.mainLocation = mainLocation;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public byte[] getOperatingStatute() {
        return operatingStatute;
    }

    public void setOperatingStatute(byte[] operatingStatute) {
        this.operatingStatute = operatingStatute;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }
}