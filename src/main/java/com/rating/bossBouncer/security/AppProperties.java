package com.rating.bossbouncer.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private long otpExpirationMesc;

        public long getOtpExpirationMesc() {
            return otpExpirationMesc;
        }

        public void setOtpExpirationMesc(long otpExpirationMesc) {
            this.otpExpirationMesc = otpExpirationMesc;
        }

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }


    public Auth getAuth() {
        return auth;
    }
}
