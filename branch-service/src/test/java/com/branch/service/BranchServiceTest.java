package com.branch.service;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.repository.BranchRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private BranchDTO dto;
    private Branch branch;

    @BeforeAll
    static void beforeAll() {
        System.out.println("=== BranchService Test Started ===");
    }

    @BeforeEach
    void setUp() {
        dto = new BranchDTO();
        dto.setName("Main Branch");
        dto.setBranchCode("B001");
        dto.setAddress("Hyderabad");

        branch = new Branch();
        branch.setId(1L);
        branch.setName("Main Branch");
        branch.setBranchCode("B001");
        branch.setAddress("Hyderabad");

        reset(branchRepository);
    }

    @AfterEach
    void tearDown() {
        reset(branchRepository);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("=== BranchService Test Completed ===");
    }

    // ----------------------------------------------------------
    // createBranch()
    // ----------------------------------------------------------

    @Test
    void createBranch_success() {
        when(branchRepository.findByBranchCode("B001")).thenReturn(Optional.empty());
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);

        Branch result = branchService.createBranch(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBranchCode()).isEqualTo("B001");

        verify(branchRepository).findByBranchCode("B001");
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void createBranch_throwsException_whenBranchCodeAlreadyExists() {
        when(branchRepository.findByBranchCode("B001")).thenReturn(Optional.of(branch));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> branchService.createBranch(dto));

        assertThat(ex.getMessage()).isEqualTo("Branch code must be unique");

        verify(branchRepository).findByBranchCode("B001");
        verify(branchRepository, never()).save(any());
    }

    // ----------------------------------------------------------
    // getBranchById()
    // ----------------------------------------------------------

    @Test
    void getBranchById_success() {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        Branch result = branchService.getBranchById(1L);

        assertThat(result).isSameAs(branch);
        verify(branchRepository).findById(1L);
    }

    @Test
    void getBranchById_throwsException_whenNotFound() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> branchService.getBranchById(1L));

        assertThat(ex.getMessage()).isEqualTo("Branch not found");
        verify(branchRepository).findById(1L);
    }

    // ----------------------------------------------------------
    // getAllBranches()
    // ----------------------------------------------------------

    @Test
    void getAllBranches_success() {
        when(branchRepository.findAll()).thenReturn(List.of(branch));

        List<Branch> list = branchService.getAllBranches();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getBranchCode()).isEqualTo("B001");

        verify(branchRepository).findAll();
    }

    // ----------------------------------------------------------
    // updateBranch()
    // ----------------------------------------------------------

    @Test
    void updateBranch_success() {
        // existing branch returned
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        // updated entity after save
        Branch updated = new Branch();
        updated.setId(1L);
        updated.setName("Updated");
        updated.setBranchCode("B010");
        updated.setAddress("New Address");

        BranchDTO updateDTO = new BranchDTO();
        updateDTO.setName("Updated");
        updateDTO.setBranchCode("B010");
        updateDTO.setAddress("New Address");

        when(branchRepository.save(any(Branch.class))).thenReturn(updated);

        Branch result = branchService.updateBranch(1L, updateDTO);

        assertThat(result.getName()).isEqualTo("Updated");
        assertThat(result.getBranchCode()).isEqualTo("B010");

        verify(branchRepository).findById(1L);
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void updateBranch_throwsException_whenIdNotFound() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        BranchDTO updateDTO = new BranchDTO();
        updateDTO.setName("New");

        assertThrows(RuntimeException.class,
                () -> branchService.updateBranch(1L, updateDTO));

        verify(branchRepository).findById(1L);
        verify(branchRepository, never()).save(any());
    }

    // ----------------------------------------------------------
    // deleteBranch()
    // ----------------------------------------------------------

    @Test
    void deleteBranch_success() {

        Branch branch = new Branch();
        branch.setId(1L);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        branchService.deleteBranch(1L);

        verify(branchRepository).findById(1L);
        verify(branchRepository).delete(branch);
    }

    // ----------------------------------------------------------
    // isBranchExists()
    // ----------------------------------------------------------

    @Test
    void isBranchExists_returnsTrue() {
        when(branchRepository.existsByBranchCode("B001")).thenReturn(true);

        boolean exists = branchService.isBranchExists("B001");

        assertThat(exists).isTrue();
        verify(branchRepository).existsByBranchCode("B001");
    }

    @Test
    void isBranchExists_returnsFalse() {
        when(branchRepository.existsByBranchCode("B001")).thenReturn(false);

        boolean exists = branchService.isBranchExists("B001");

        assertThat(exists).isFalse();
        verify(branchRepository).existsByBranchCode("B001");
    }
}
