package com.bank.notificationservice.dto;

public class DownService {

    private String serviceName;
    private String ipAddress;

    public DownService(String serviceName, String ipAddress) {
        this.serviceName = serviceName;
        this.ipAddress = ipAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "DownService{" +
                "serviceName='" + serviceName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
