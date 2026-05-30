package com.bank.account.service;

import com.bank.account.client.CustomerClient;
import com.bank.account.exception.CustomerServiceUnavailableException;
import feign.FeignException;
import feign.Request;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerLookupServiceTest {

    private static final String CUSTOMER_NUMBER = "C001";

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private CustomerLookupService customerLookupService;

    @Test
    void getCustomerByCustomerNumberMapsCustomerNotFoundToEntityNotFound() {
        when(customerClient.getCustomerByCustomerNumber(CUSTOMER_NUMBER))
                .thenThrow(new FeignException.NotFound(
                        "Customer not found",
                        request(),
                        new byte[0],
                        Collections.emptyMap()
                ));

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> customerLookupService.getCustomerByCustomerNumber(CUSTOMER_NUMBER)
        );

        assertThat(ex.getMessage()).contains(CUSTOMER_NUMBER);
    }

    @Test
    void customerLookupFallbackThrowsCustomerServiceUnavailableException() {
        RuntimeException cause = new RuntimeException("Connection refused");

        CustomerServiceUnavailableException ex = assertThrows(
                CustomerServiceUnavailableException.class,
                () -> customerLookupService.customerLookupFallback(CUSTOMER_NUMBER, cause)
        );

        assertThat(ex.getMessage()).contains("Customer service is currently unavailable");
        assertThat(ex).hasCause(cause);
    }

    private Request request() {
        return Request.create(
                Request.HttpMethod.GET,
                "/api/customers/" + CUSTOMER_NUMBER,
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );
    }
}
