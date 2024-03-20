package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.Customer;
import io.dmly.invoicer.repository.CustomerRepository;
import io.dmly.invoicer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@Transactional
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("John Doe", savedCustomer.getName());
        assertNotNull(savedCustomer.getCreatedAt());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer updatedCustomer = customerService.updateCustomer(customer);

        assertNotNull(updatedCustomer);
        assertEquals(1L, updatedCustomer.getId());
        assertEquals("John Doe", updatedCustomer.getName());
    }

    @Test
    void testGetCustomers() {
        List<Customer> customers = new ArrayList<>();

        customers.add(Customer.builder().id(1L).name("John Doe").build());
        customers.add(Customer.builder().id(2L).name("Jane Smith").build());

        Page<Customer> page = new PageImpl<>(customers);

        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Customer> retrievedCustomers = customerService.getCustomers(0, 10);

        assertNotNull(retrievedCustomers);
        assertEquals(2, retrievedCustomers.getTotalElements());
    }

    @Test
    void testGetCustomersById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer retrievedCustomer = customerService.getCustomer(1L);

        assertNotNull(retrievedCustomer);
        assertEquals(1L, retrievedCustomer.getId());
        assertEquals("John Doe", retrievedCustomer.getName());
    }
}
