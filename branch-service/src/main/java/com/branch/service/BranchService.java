package com.branch.service;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;


    public Branch createBranch(BranchDTO branchDTO) {
        if (branchRepository.findByBranchCode(branchDTO.getBranchCode()).isPresent()) {
            throw new RuntimeException("Branch code must be unique");
        }

        Branch branch = new Branch();
        branch.setName(branchDTO.getName());
        branch.setBranchCode(branchDTO.getBranchCode());
        branch.setAddress(branchDTO.getAddress());

        return branchRepository.save(branch);
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Branch updateBranch(Long id, BranchDTO branchDTO) {
        Branch branch = getBranchById(id);
        branch.setName(branchDTO.getName());
        branch.setBranchCode(branchDTO.getBranchCode());
        branch.setAddress(branchDTO.getAddress());

        return branchRepository.save(branch);
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }

    public boolean isBranchExists(String branchCode) {
        return branchRepository.existsByBranchCode(branchCode);
    }
}
