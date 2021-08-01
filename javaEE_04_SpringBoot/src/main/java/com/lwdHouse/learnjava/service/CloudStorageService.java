package com.lwdHouse.learnjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
/* 可以用Profiles或者ConditionalOnProperty进行条件装配，Profile控制比较粗浅，不如直接用application.yml里的属性加ConditionalOnProperty来决定是否装配这个Bean */
// @Profile("!default")   // 当Profile不为default时，创建这个Bean
// 如果运行的时候加上vm参数-Dspring.profiles.active=test,master
// 则会显示2021-06-19 02:37:02.442  INFO......: initializing cloud storage...
@ConditionalOnProperty(value = "storage.type", havingValue = "aws")
public class CloudStorageService implements StorageService{

    @Value("${storage.aws.bucket:}")
    String bucket;

    @Value("${storage.aws.access-key:}")
    String accessKey;

    @Value("${storage.aws.access-secret:}")
    String accessSecret;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        logger.info("initializing cloud storage...");
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        // TODO:
        throw new IOException("File not found: " + uri);
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        // TODO:
        throw new IOException("Unable to access cloud storage.");
    }
}
