package io.dmly.invoicer.controller;

import io.dmly.invoicer.model.Customer;
import io.dmly.invoicer.model.Invoice;
import io.dmly.invoicer.model.InvoicerUserDetails;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.service.CustomerService;
import io.dmly.invoicer.service.StatsService;
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
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final StatsService statsService;

    @GetMapping(path = "/list")
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                     @RequestParam Optional<Integer> page,
                                                     @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "page", customerService.getCustomers(page.orElse(0), size.orElse(10)),
                                "stats", statsService.getStats()
                        ))
                        .message("Customers listed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping(path = "/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                       @RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customer", customerService.createCustomer(customer)
                        ))
                        .message("Customer created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customer", customerService.getCustomer(id)
                        ))
                        .message("Customer fetched")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(path = "/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                       @RequestParam Optional<String> name,
                                                       @RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "page", customerService.searchCustomers(name.orElse(""), page.orElse(0), size.orElse(10))
                        ))
                        .message("Customer searched")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PutMapping(path = "/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                       @RequestBody Customer customer) {
        customerService.createCustomer(customer);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customer", customerService.updateCustomer(customer)
                        ))
                        .message("Customer update")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping(path = "/add/invoice/to/customer/{id}")
    public ResponseEntity<HttpResponse> addInvoice(@AuthenticationPrincipal InvoicerUserDetails userDetails,
                                                   @PathVariable Long id,
                                                   @RequestBody Invoice invoice) {
        customerService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of(
                                "user", userDetails.getUser(),
                                "customer", customerService.getCustomer(id)
                        ))
                        .message("Invoice created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
}
