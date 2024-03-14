package io.dmly.invoicer.controller;

import io.dmly.invoicer.model.Customer;
import io.dmly.invoicer.model.Invoice;
import io.dmly.invoicer.model.InvoicerUserDetails;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.service.CustomerService;
import io.dmly.invoicer.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    @PostMapping(path = "/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                       @RequestBody Invoice invoice) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "invoice", invoiceService.createInvoice(invoice)
                        ))
                        .message("Invoice created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping(path = "/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                     @RequestParam Optional<Integer> page,
                                                     @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customers", invoiceService.getInvoices(page.orElse(0), size.orElse(10))
                        ))
                        .message("Invoices listed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping(path = "/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal InvoicerUserDetails userDetails) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customers", customerService.getCustomers()
                        ))
                        .message("Customers got")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "invoice", invoiceService.findById(id)
                        ))
                        .message("Invoice fetched")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
