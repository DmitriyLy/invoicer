package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.Invoice;
import io.dmly.invoicer.repository.InvoiceRepository;
import io.dmly.invoicer.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setNumber(RandomStringUtils.randomAlphabetic(8).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new ApiException("Invoice not found"));
    }

    @Override
    public Invoice update(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices(int page, int size) {
        return invoiceRepository.findAll(PageRequest.of(page, size));
    }
}
