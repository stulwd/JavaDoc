package com.lwdHouse.ioc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class LogoService {
    @Value("1")
    private String version;
    @Value("classpath:/logo.txt")
    private Resource resource;
    private String logo;

    @PostConstruct
    public void init() throws IOException {
        try(var reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
            this.logo = reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public void printLogo(){
        System.out.println(logo);
        System.out.println("app version: " + version);
    }
}
