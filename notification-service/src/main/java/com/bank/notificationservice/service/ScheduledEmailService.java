package com.bank.notificationservice.service;

import com.bank.notificationservice.client.AccountClient;
import com.bank.notificationservice.client.CustomerClient;
import com.bank.notificationservice.dto.Customer;
import com.bank.notificationservice.dto.Transaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledEmailService {

    private JavaMailSender mailSender;
    private final PdfReportGenerator pdfReportGenerator;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private AccountClient accountClient;

    public ScheduledEmailService(JavaMailSender mailSender, PdfReportGenerator pdfReportGenerator) {
        this.mailSender = mailSender;
        this.pdfReportGenerator = pdfReportGenerator;
    }

    /*
        0 → At second 0
        *slash5 → Every 5 minutes
        * → Every hour
        * → Every day of the month
        * → Every month
        ? → Any day of the week
     */
        @Scheduled(cron = "0 0 9 1 * ?")  // Runs on the 1st day of every month at 9 AM
        public void sendMonthlyTransactionReports() {

        List<Customer> customers = customerClient.getAllCustomers();

        for (Customer customer : customers) {
            accountClient.getAllAccountsByCustomerNumber(customer.getCustomerNumber()).forEach( account -> sendPdfReport(account.getAccountNumber(),customer) );
        }
    }

    private void sendPdfReport(String accountNumber, Customer customer) {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = startDate.format(formatter); // e.g., "2025-01-01"
        String formattedEndDate = endDate.format(formatter);
        List<Transaction> transactions = accountClient.getTransactionByAccountNumberAndDateRange(accountNumber, formattedStartDate, formattedEndDate);

        byte[] pdfBytes = pdfReportGenerator.generateMonthlyTransactionReport(customer.getName(),accountNumber ,transactions);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(customer.getEmail());
            helper.setSubject("Your Monthly Transaction Report");
            helper.setText("Please find attached your transaction report for " + startDate.getMonth() + ".", false);

            helper.addAttachment("Transaction_Report_" + startDate.getMonth() + ".pdf", () -> new ByteArrayInputStream(pdfBytes));

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send monthly report to " + customer.getEmail() + ": " + e.getMessage());
        }
    }
}