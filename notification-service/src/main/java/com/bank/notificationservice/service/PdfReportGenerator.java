package com.bank.notificationservice.service;

import com.bank.notificationservice.dto.Transaction;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfReportGenerator {

    public byte[] generateMonthlyTransactionReport(String customerName, String accountNumber, List<Transaction> transactions) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Create a document instance
            Document document = new Document();

            // Create PdfWriter instance
            PdfWriter.getInstance(document, out);

            // Open the document for writing
            document.open();

            // Add Title and Customer Information
            document.add(new Paragraph("Monthly Transaction Report"));
            document.add(new Paragraph("Customer: " + customerName));
            document.add(new Paragraph("Account Number: " + accountNumber));
            document.add(new Paragraph("------------------------------------------------------"));

            // Create a table with 3 columns for transactions
            PdfPTable table = getPdfPTable(transactions);

            // Add table to the document
            document.add(table);

            // Close the document
            document.close();

            // Return the generated PDF as byte array
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF: " + e.getMessage(), e);
        }
    }

    private static PdfPTable getPdfPTable(List<Transaction> transactions) {
        PdfPTable table = new PdfPTable(3);  // 3 columns: Date, Transaction Type, Amount
        table.addCell("Date");
        table.addCell("Transaction Type");
        table.addCell("Amount");

        // Loop through transactions and add data to the table
        for (Transaction transaction : transactions) {
            table.addCell(String.valueOf(transaction.getTransactionDate()));
            table.addCell(transaction.getTransactionType());
            table.addCell(String.valueOf(transaction.getTransactionAmount()));
        }
        return table;
    }
}
