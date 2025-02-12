package com.bank.customer.clinet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "branch-service", path = "/api/branches")
public interface BranchClient {

    @GetMapping("/exists/{branchCode}")
    boolean isBranchExists(@PathVariable String branchCode);
}