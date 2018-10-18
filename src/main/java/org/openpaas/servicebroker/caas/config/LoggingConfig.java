package org.openpaas.servicebroker.caas.config;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Logging을 위한 설정 클래스
 *
 * @author Hyerin
 * @since 2018.07.24
 * @version 20180724
 */
@Configuration
public class LoggingConfig {

    public LoggingConfig () { }

    @Bean
    public ConsoleAppender consoleAppender () {
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold( Level.ALL );
        PatternLayout patternLayout = new PatternLayout( "%d %-5p [%c{1}] %m %n" );
        consoleAppender.setLayout( patternLayout );
        return consoleAppender;
    }

}
