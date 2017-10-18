package org.alexjdev.utils.mail;

import java.util.Properties;

/**
 * Реализация отправки писем, используя SSL соединение
 */
public class SSLEmailSender extends GeneralEmailSender implements EmailSender {

    private static final String SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";

    @Override
    public Properties getConnectionProperties(SMTPSettings settingsBean) {
        Properties props = new Properties();
        props.put("mail.smtp.host", settingsBean.smtpHost);
        props.put("mail.smtp.port", settingsBean.smtpPort);
        props.put("mail.stmp.user", settingsBean.userName);
        props.put("mail.smtp.password", settingsBean.password);
        props.put("mail.smtp.socketFactory.port", settingsBean.smtpPort);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY_CLASS);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", settingsBean.smtpHost);
        return props;
    }

}
