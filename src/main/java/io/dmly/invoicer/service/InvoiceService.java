package io.dmly.invoicer.service;

import io.dmly.invoicer.model.Invoice;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Invoice createInvoice(Invoice invoice);

    Invoice findById(Long id);

    Invoice update(Invoice invoice);

    Page<Invoice> getInvoices(int page, int size);
}
