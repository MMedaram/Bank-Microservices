package com.branch.controller;

import com.branch.entity.Branch;
import com.branch.entity.BranchDTO;
import com.branch.service.AsyncUseCase;
import com.branch.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BranchController.class,
        properties = {
                "spring.cache.type=NONE"
        }
)
class BranchController_WebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BranchService branchService;

    @MockitoBean
    private AsyncUseCase asyncUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    // ===============================
    // CREATE BRANCH
    // ===============================

    @Test
    @DisplayName("POST /api/branches - should create branch")
    void createBranch_shouldReturnCreated() throws Exception {

        BranchDTO dto = new BranchDTO();
        dto.setBranchCode("BR001");
        dto.setName("Main Branch");

        Branch saved = new Branch();
        saved.setId(1L);
        saved.setBranchCode("BR001");
        saved.setName("Main Branch");

        when(branchService.createBranch(any(BranchDTO.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.branchCode").value("BR001"))
                .andExpect(jsonPath("$.name").value("Main Branch"));

        verify(branchService).createBranch(any(BranchDTO.class));
    }

    // ===============================
    // GET BY BRANCH CODE
    // ===============================

    @Test
    void getBranchByBranchCode_shouldReturnBranch() throws Exception {

        Branch branch = new Branch();
        branch.setId(1L);
        branch.setBranchCode("BR001");
        branch.setName("Main Branch");

        when(branchService.getBranchByBranchCode("BR001"))
                .thenReturn(branch);

        mockMvc.perform(get("/api/branches/BR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchCode").value("BR001"))
                .andExpect(jsonPath("$.name").value("Main Branch"));

        verify(branchService).getBranchByBranchCode("BR001");
    }

    // ===============================
    // GET ALL
    // ===============================

    @Test
    void getAllBranches_shouldReturnList() throws Exception {

        Branch branch1 = new Branch(1L,"BR001", "BR001", "Main");
        Branch branch2 = new Branch(2L, "BR002","BR002", "City");

        when(branchService.getAllBranches())
                .thenReturn(List.of(branch1, branch2));

        mockMvc.perform(get("/api/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].branchCode").value("BR001"))
                .andExpect(jsonPath("$[1].branchCode").value("BR002"));

        verify(branchService).getAllBranches();
    }

    // ===============================
    // UPDATE
    // ===============================

    @Test
    void updateBranch_shouldReturnUpdated() throws Exception {

        BranchDTO dto = new BranchDTO();
        dto.setBranchCode("BR001");
        dto.setName("Updated Branch");

        Branch updated = new Branch(1L, "Updated Branch", "BR001","CTR");

        when(branchService.updateBranch(eq(1L), any(BranchDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/branches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Branch"));

        verify(branchService).updateBranch(eq(1L), any(BranchDTO.class));
    }

    // ===============================
    // DELETE
    // ===============================

    @Test
    void deleteBranch_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/branches/1"))
                .andExpect(status().isNoContent());

        verify(branchService).deleteBranch(1L);
    }

    // ===============================
    // EXISTS
    // ===============================

    @Test
    void isBranchExists_shouldReturnTrue() throws Exception {

        when(branchService.isBranchExists("BR001"))
                .thenReturn(true);

        mockMvc.perform(get("/api/branches/exists/BR001"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(branchService).isBranchExists("BR001");
    }

    // ===============================
    // ASYNC ENDPOINT
    // ===============================

    @Test
    void asyncEndpoint_shouldReturnOk() throws Exception {

        mockMvc.perform(get("/api/branches/async"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verify(asyncUseCase).sendEmail(anyString());
    }

    // ===============================
    // VALIDATION FAILURE
    // ===============================

    @Test
    void createBranch_invalidInput_shouldReturnBadRequest() throws Exception {

        BranchDTO dto = new BranchDTO(); // empty → should fail validation

        mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

}