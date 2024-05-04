package io.dmly.invoicer.report;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class CustomerReport {
    private static final String[] HEADERS = {"Id", "Name", "Email", "Type", "Status", "Address", "Phone", "Created at"};

    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final List<Customer> customers;


    public CustomerReport(List<Customer> customers) {
        this.customers = customers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Customers");
        setHeaders();
    }

    private void setHeaders() {
        Row headerRow = sheet.createRow(0);

        CellStyle style = getHeadersCellStyle();

        IntStream.range(0, HEADERS.length).forEach(headerIndex -> {
            Cell cell = headerRow.createCell(headerIndex);
            cell.setCellValue(HEADERS[headerIndex]);
            cell.setCellStyle(style);
        });
    }

    private CellStyle getHeadersCellStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFont(getHeadersFont());
        return style;
    }

    private CellStyle getContentCellStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFont(getContentFont());
        return style;
    }

    private XSSFFont getHeadersFont() {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        return font;
    }

    private XSSFFont getContentFont() {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        return font;
    }

    public InputStreamResource generateReport() {
        try (var out = new ByteArrayOutputStream()) {

            CellStyle style = getContentCellStyle();

            int rowIndex = 1;

            for (Customer customer : customers) {
                getReportRow(customer, rowIndex++, style);
            }

            workbook.write(out);

            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
            throw new ApiException(e.getMessage(), e);
        }
    }

    private void getReportRow(Customer customer, int rowIndex, CellStyle style) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(customer.getId());
        row.createCell(1).setCellValue(customer.getName());
        row.createCell(2).setCellValue(customer.getEmail());
        row.createCell(3).setCellValue(customer.getType());
        row.createCell(4).setCellValue(customer.getStatus());
        row.createCell(5).setCellValue(customer.getAddress());
        row.createCell(6).setCellValue(customer.getPhone());
        row.createCell(7).setCellValue(customer.getCreatedAt().toString());
        row.setRowStyle(style);
    }
}
