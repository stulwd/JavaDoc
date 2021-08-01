package com.lwdHouse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 条件测试
 * @EnableOnOs
 * @DisabledOnOs
 * @DisabledOnJre(JRE.JAVA_8)
 * @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
 * @EnabledIfEnvironmentVariable(named = "DEBUG", matches = "true")
 *    需要传入环境变量DEBUG=true才能执行的测试，可以用@EnabledIfEnvironmentVariable：
 *
 */
public class Part04_ConditionTest {
    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    public void testWin() {
        assertEquals("usr/local/test.cfg", Config.getConfigFile("test.cfg"));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testUnix() {
        System.out.println(System.getProperty("os.arch"));
        // amd64
        assertEquals("C:\\test.ini", Config.getConfigFile("test.ini"));
    }
}
