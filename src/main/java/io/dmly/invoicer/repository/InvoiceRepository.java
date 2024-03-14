package io.dmly.invoicer.repository;

import io.dmly.invoicer.model.Invoice;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {
}
