package com.branch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "branches",schema = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Branch name is required")
    private String name;

    @NotNull(message = "Branch code is required")
    @Column(unique = true)
    private String branchCode;

    @NotNull(message = "Address is required")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
