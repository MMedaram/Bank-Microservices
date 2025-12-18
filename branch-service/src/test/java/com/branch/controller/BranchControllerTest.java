package com.branch.controller;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.service.BranchService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    // Reusable sample objects (fresh copies created in @BeforeEach)
    private BranchDTO dto;
    private Branch sampleBranch;

    @BeforeAll
    static void beforeAll() {
        // runs once before all tests — useful for expensive global setup or logging
        System.out.println("=== Starting BranchControllerTest ===");
    }

    @BeforeEach
    void setUp() {
        // Create fresh objects for each test (no reflection)
        dto = new BranchDTO();
        dto.setBranchCode("B001");
        dto.setName("Main Branch");

        sampleBranch = new Branch();
        sampleBranch.setId(1L);
        sampleBranch.setBranchCode("B001");
        sampleBranch.setName("Main Branch");

        // optionally verify mocks are clean
        Mockito.reset(branchService);
    }

    @AfterEach
    void tearDown() {
        // runs after each test — useful to reset or verify interactions
        // reset mock state so tests remain isolated
        Mockito.reset(branchService);
    }

    @AfterAll
    static void afterAll() {
        // runs once after all tests complete
        System.out.println("=== Finished BranchControllerTest ===");
    }

    @Test
    void createBranch_returnsCreatedAndBody() {
        when(branchService.createBranch(any(BranchDTO.class))).thenReturn(sampleBranch);

        ResponseEntity<Branch> response = branchController.createBranch(dto);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isSameAs(sampleBranch);

        verify(branchService).createBranch(dto);
    }

    @Test
    void getBranchById_returnsOkAndBody() {
        Long id = 5L;
        Branch branch = new Branch();
        branch.setId(id);
        branch.setBranchCode("B005");
        branch.setName("Branch 5");

        when(branchService.getBranchByBranchCode("B005")).thenReturn(branch);

        ResponseEntity<Branch> response = branchController.getBranchByBranchCode("B005");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isSameAs(branch);

        verify(branchService).getBranchByBranchCode("B005");
    }

    @Test
    void getAllBranches_returnsOkAndList() {
        Branch b1 = new Branch(); b1.setId(1L); b1.setBranchCode("B1"); b1.setName("One");
        Branch b2 = new Branch(); b2.setId(2L); b2.setBranchCode("B2"); b2.setName("Two");
        List<Branch> list = Arrays.asList(b1, b2);

        when(branchService.getAllBranches()).thenReturn(list);

        ResponseEntity<List<Branch>> response = branchController.getAllBranches();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(list);

        verify(branchService).getAllBranches();
    }

    @Test
    void getAllBranches_returnsEmptyList_whenNoBranches() {
        when(branchService.getAllBranches()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Branch>> response = branchController.getAllBranches();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();

        verify(branchService).getAllBranches();
    }

    @Test
    void updateBranch_returnsOkAndUpdatedBody() {
        Long id = 10L;
        BranchDTO updateDto = new BranchDTO();
        updateDto.setBranchCode("B010");
        updateDto.setName("Updated Branch");

        Branch updated = new Branch();
        updated.setId(id);
        updated.setBranchCode("B010");
        updated.setName("Updated Branch");

        when(branchService.updateBranch(eq(id), any(BranchDTO.class))).thenReturn(updated);

        ResponseEntity<Branch> response = branchController.updateBranch(id, updateDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isSameAs(updated);

        verify(branchService).updateBranch(id, updateDto);
    }

    @Test
    void deleteBranch_callsServiceAndReturnsNoContent() {
        Long id = 7L;

        ResponseEntity<Void> response = branchController.deleteBranch(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(response.getBody()).isNull();

        verify(branchService).deleteBranch(id);
    }

    @Test
    void isBranchExists_returnsTrueOrFalse_fromService() {
        String code = "B77";
        when(branchService.isBranchExists(code)).thenReturn(true);

        boolean exists = branchController.isBranchExists(code);
        assertThat(exists).isTrue();
        verify(branchService).isBranchExists(code);

        when(branchService.isBranchExists(code)).thenReturn(false);
        boolean exists2 = branchController.isBranchExists(code);
        assertThat(exists2).isFalse();
        verify(branchService, times(2)).isBranchExists(code);
    }

    @Test
    void serviceException_inGetBranchById_propagates() {
        String branchCode = "B005";
        when(branchService.getBranchByBranchCode(branchCode)).thenThrow(new RuntimeException("not found"));

        assertThrows(RuntimeException.class, () -> branchController.getBranchByBranchCode(branchCode));
        verify(branchService).getBranchByBranchCode(branchCode);
    }
}
