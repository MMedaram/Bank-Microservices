package com.branch.service;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @CachePut(value = "branchCache", key = "'branch:' + #result.branchCode")
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

    @CacheEvict(value = "branchCache", key = "'branch:' + #branch.branchCode")
    public Branch updateBranch(Long id, BranchDTO branchDTO) {

        Branch branch = getBranchById(id);
        branch.setName(branchDTO.getName());
        branch.setBranchCode(branchDTO.getBranchCode());
        branch.setAddress(branchDTO.getAddress());

        return branchRepository.save(branch);
    }

    @CacheEvict(value = "branchCache", key = "'branch:' + #existing.branchCode")
    public void deleteBranch(Long id) {
        Branch existing = getBranchById(id);
        branchRepository.delete(existing);
    }

    public boolean isBranchExists(String branchCode) {
        return branchRepository.existsByBranchCode(branchCode);
    }

    @Cacheable(value = "branchCache", key = "'branch:' + #branchCode")
    public Branch getBranchByBranchCode(String branchCode) {
        return branchRepository.findByBranchCode(branchCode)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }
}
