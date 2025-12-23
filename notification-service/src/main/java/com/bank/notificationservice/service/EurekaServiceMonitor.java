package com.bank.notificationservice.service;

import com.bank.notificationservice.dto.DownService;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class EurekaServiceMonitor {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    EmailService emailService;

    private Map<String, String> serviceMap;
    private Set<String> expected;

    @PostConstruct
    public void loadServices() throws Exception {
        Properties props = new Properties();
        props.load(new ClassPathResource("services.properties").getInputStream());

        serviceMap = new HashMap<>();

        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key).trim().toUpperCase();
            serviceMap.put(key, value);
        }

        expected = new HashSet<>(serviceMap.values());
        System.out.println("Expecting services: " + expected);
    }


    @Scheduled(cron = "0 */59 * * * ?")
    public void check() {

        // 1. Get actual Eureka services
        List<Application> apps = eurekaClient.getApplications().getRegisteredApplications();

        // Set of service names actually present
        Set<String> actual = apps.stream()
                .map(Application::getName)
                .collect(Collectors.toSet());

        // 2. Find services that are missing
        Set<String> missing = new HashSet<>(expected);
        missing.removeAll(actual);

        // 3. Build detailed list with service name + IP address
        List<DownService> downList = new ArrayList<>();

        for (String svc : missing) {

            // Find if Eureka has any instance for this service (just in case)
            Application app = eurekaClient.getApplication(svc);

            String ip = "N/A";   // default if not found

            if (app != null && !app.getInstances().isEmpty()) {
                // get first instance IP (if multiple, you can decide logic)
                ip = app.getInstances().get(0).getIPAddr();
            }

            downList.add(new DownService(svc, ip));
        }

        // 4. Send email only if something missing
        if (!downList.isEmpty()) {
            emailService.sendEmailWhenListOfServicesDown(downList);
        }
    }


}
