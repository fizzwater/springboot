package com.qin.springboot.a005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class StatusService {
    private String status;

    @Autowired
    private TestYmlRead testYmlRead;

    public String getStatus() {
        System.out.println("1 host name is "+testYmlRead.getHostname());
        return status;
    }

    public void setStatus(String status) {
        System.out.println("2 host name is "+testYmlRead.getHostname());
        this.status = status;
    }
}