package com.checkmarx.flow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "email")
@Validated
public class EmailProperties {

        private String host;
        private Integer port = 25;
        private String username;
        private String password;
        private boolean enabled = false;
        private List<String> cc;
        private String template;

        public String getHost() {
            return this.host;
        }

        public Integer getPort() {
            return this.port;
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getCc() { return this.cc; }

        public void setCc(List<String> cc) {
            this.cc = cc;
        }

        public String getTemplate() { return template; }

        public void setTemplate(String template) { this.template = template; }
}