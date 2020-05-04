package com.checkmarx.flow.service;

import com.checkmarx.flow.config.EmailProperties;
import com.checkmarx.sdk.utils.ScanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

@Service
public class EmailNotificationService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(EmailNotificationService.class);
    private final EmailProperties emailProperties;
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    @ConstructorProperties({"emailProperties", "templateEngine", "emailSender"})
    public EmailNotificationService(EmailProperties emailProperties, @Qualifier("cxFlowExtTemplateEngine") TemplateEngine templateEngine, @Qualifier("cxFlowExtMailSender") JavaMailSender emailSender) {
        this.emailProperties = emailProperties;
        this.templateEngine = templateEngine;
        this.emailSender = emailSender;
    }

    /**
     * Send email
     *
     * @param recipients
     * @param subject
     * @param ctx
     */
    public void sendmail(List<String> recipients, @NotNull String subject, @NotNull Map<String, Object> ctx){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            log.info("Sending Custom CxExt email");
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            String contact = ""; //= properties.getContact();
            if(!ScanUtils.empty(contact)){
                messageHelper.setFrom(contact);
            }

            if(!ScanUtils.empty(recipients)) {
                messageHelper.setTo(recipients.toArray(new String[0]));
                messageHelper.setCc((emailProperties.getCc()).toArray(new String[0]));
            }else
            {
                messageHelper.setTo((emailProperties.getCc()).toArray(new String[0]));
            }

            messageHelper.setSubject(subject);
            String content = generateContent(ctx, emailProperties.getTemplate());
            messageHelper.setText(content, true);
        };
        try {
            if(emailProperties != null && emailProperties.isEnabled()) {
                emailSender.send(messagePreparator);
            }
        } catch (MailException e) {
            log.error("Error occurred while attempting to send an email",e);
        }


    }

    /**
     * Generate HTML content for email
     *
     * @param ctx
     * @param template
     * @return
     */
    private String generateContent(Map<String, Object> ctx, String template) {
        Context context = new Context();
        context.setVariables(ctx);
        return templateEngine.process(template, context);
    }
}

