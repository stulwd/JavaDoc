package com.lwdHouse.ioc;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnSmtpEnvCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 条件是：存在环境变量，且值为true
        return "true".equalsIgnoreCase(System.getenv("smtp"));
    }
}
