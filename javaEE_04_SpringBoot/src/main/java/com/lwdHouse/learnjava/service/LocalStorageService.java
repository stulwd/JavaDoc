package com.lwdHouse.learnjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Component
// 当Profile为default时，即启动时不要加-Dspring.profiles.active=xxx参数指定其他profile，就会创建这个Bean
// @Profile("!default")
// 当application.yml有storage.type属性，并且值为local时，装配这个bean，要是没有这个属性，也装配这个bean
@ConditionalOnProperty(value = "storage.type", havingValue = "local", matchIfMissing = true)
/**
 * 类似的条件装配注解还有
 * 如果存在xxx这个bean，则装配：                 @ConditionalOnBean(value = xxx.class)
 * 如果不存在xxx这个bean，则装配：               @ConditionalOnMissingBean(value = xxx.class)
 * 如果classpath里找不到xxx这个class, 则装配    @ConditionalOnClass(value = xxx.class)
 * 如果在Web环境中，则装配                     @ConditionalOnWebApplication
 * 根据表达式判断条件是否生效。                 @ConditionalOnExpression
 */
public class LocalStorageService implements StorageService{

    // ${xxx.xxx}会去application.yml文件中读取属性
    @Value("${storage.local.root-dir:/var/static}")
    String localStorageRootDir;

    // 把持有配置的类StorageConfiguration注入
    @Autowired
    StorageConfiguration storageConfiguration;

    private String localRootDir;
    private int localMaxSize;
    private boolean localAllowEmpty;
    private List<String> localAllowTypes;


    final Logger logger = LoggerFactory.getLogger(getClass());

    private File localStorageRoot;

    @PostConstruct
    public void init(){
        logger.info("Initializing local storage with root dir: {}", localStorageRootDir);
        this.localStorageRoot = new File(this.localStorageRootDir);

        // 注入属性
        localRootDir = storageConfiguration.getRootDir();
        localMaxSize = storageConfiguration.getMaxSize();
        localAllowEmpty = storageConfiguration.isAllowEmpty();
        localAllowTypes = storageConfiguration.getAllowTypes();
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        File targetFile = new File(this.localStorageRoot, uri); // 两个路径拼起来是最终路径
        return new BufferedInputStream(new FileInputStream(targetFile));
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + extName;
        File targetFile = new File(this.localStorageRoot, fileName);
        try(OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile))){
            input.transferTo(output);
        }
        return fileName;
    }
}
