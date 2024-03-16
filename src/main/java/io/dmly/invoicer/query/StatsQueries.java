package io.dmly.invoicer.query;

public class StatsQueries {
    public static final String STATS_QUERY = """
            WITH
            total_customers_t AS (
            	SELECT COALESCE(COUNT(*), 0) total_customers FROM customer
            ),
                        
            total_invoices_t AS (
            	SELECT COALESCE(COUNT(*), 0) total_invoices FROM invoice
            ),
                        
            total_billed_t AS (
            	SELECT COALESCE(SUM(total), 0.00) total_billed FROM invoice
            )
            	
            SELECT * FROM total_customers_t, total_invoices_t, total_billed_t;
            """;
}
