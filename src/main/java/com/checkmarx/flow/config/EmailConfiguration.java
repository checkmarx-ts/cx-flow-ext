package com.checkmarx.flow.config;

import com.checkmarx.sdk.utils.ScanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.beans.ConstructorProperties;
import java.util.Properties;

@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties
public class EmailConfiguration {

    private final EmailProperties emailProperties;

    @ConstructorProperties({"emailProperties"})
    public EmailConfiguration(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Primary
    @Bean("cxFlowExtMailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        if(emailProperties == null || !emailProperties.isEnabled()){
            return mailSender;
        }
        Properties props = mailSender.getJavaMailProperties();

        if(!ScanUtils.empty(emailProperties.getUsername()) &&
                emailProperties.getPort() != null && !ScanUtils.empty(emailProperties.getHost())){
            mailSender.setHost(emailProperties.getHost());
            mailSender.setPort(emailProperties.getPort());
            mailSender.setUsername(emailProperties.getUsername());
            mailSender.setPassword(emailProperties.getPassword());
            props.put("mail.smtp.auth", "true");

        }
        props.put("mail.transport.protocol", JavaMailSenderImpl.DEFAULT_PROTOCOL);
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

}
