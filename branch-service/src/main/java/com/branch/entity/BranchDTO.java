package com.branch.entity;

import jakarta.validation.constraints.NotNull;


public class BranchDTO {

    @NotNull(message = "Branch name is required")
    private String name;

    @NotNull(message = "Branch code is required")
    private String branchCode;

    @NotNull(message = "Address is required")
    private String address;

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
        return "BranchDTO{" +
                "name='" + name + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
