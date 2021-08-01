package com.lwdHouse.ioc;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
// 当OnSmtpEnvCondition.class这个condition满足条件时(matches方法返回true)，才会创建这个bean
@Conditional(OnSmtpEnvCondition.class)
public class SmtpMailService extends MailService{

}
