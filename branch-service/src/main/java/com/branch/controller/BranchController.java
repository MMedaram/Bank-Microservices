package com.branch.controller;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Tag(name = "Branch Management", description = "Operations pertaining to branch management")
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;


    @Operation(summary = "Create a new branch", description = "Create a new branch with given details")
    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody @Valid BranchDTO branchDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(branchDTO));
    }

    @GetMapping("/{branchCode}")
    @Operation(summary = "Get branch details by branchCode", description = "Fetch details of the branch by its branchCode")
    public ResponseEntity<Branch> getBranchByBranchCode(@PathVariable String branchCode) {
        return ResponseEntity.ok(branchService.getBranchByBranchCode(branchCode));
    }

    @GetMapping
    @Operation(summary = "Get all branches", description = "Fetch all branch details")
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update branch details", description = "Update the details of a branch by its ID")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody @Valid BranchDTO branchDTO) {
        return ResponseEntity.ok(branchService.updateBranch(id, branchDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete branch", description = "Delete a branch by its ID")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{branchCode}")
    @Operation(summary = "Search branch", description = "Checking a branch is available or not by branchCode")
    public boolean isBranchExists(@PathVariable String branchCode) {
        return branchService.isBranchExists(branchCode);
    }
}
