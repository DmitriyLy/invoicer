package io.dmly.invoicer.service;

import io.dmly.invoicer.model.Customer;
import io.dmly.invoicer.model.Invoice;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    Page<Customer> getCustomers(int page, int size);

    List<Customer> getCustomers();

    Customer getCustomer(Long id);

    void addInvoiceToCustomer(Long id, Long invoiceId);

    void addInvoiceToCustomer(Long id, Invoice invoice);

    Page<Customer> searchCustomers(String name, int page, int size);
}
