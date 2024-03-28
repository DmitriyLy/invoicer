package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.Customer;
import io.dmly.invoicer.model.Invoice;
import io.dmly.invoicer.repository.CustomerRepository;
import io.dmly.invoicer.service.CustomerService;
import io.dmly.invoicer.service.InvoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> result = new ArrayList<>();
        customerRepository.findAll().iterator().forEachRemaining(result::add);
        return result;
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ApiException("Customer not found"));
    }

    @Override
    public void addInvoiceToCustomer(Long id, Long invoiceId) {
        Customer customer = getCustomer(id);
        Invoice invoice = invoiceService.findById(invoiceId);

        invoice.setCustomer(customer);
        invoiceService.update(invoice);
    }

    @Override
    public void addInvoiceToCustomer(Long id, Invoice invoice) {
        Customer customer = getCustomer(id);
        invoice.setCustomer(customer);
        invoiceService.createInvoice(invoice);
    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int size) {
        return customerRepository.findByNameContaining(name, PageRequest.of(page, size));
    }
}
