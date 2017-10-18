package org.alexjdev.utils.mail;

import org.alexjdev.utils.domain.EntityAttachment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;
import java.util.Properties;

/**
 * Базовый класс для отправки писем
 */
public class GeneralEmailSender implements EmailSender {

    private static final String ATTACHMENT_MIME_TYPE = "application/octet-stream";

    @Override
    public void sendEmail(final SMTPSettings settingsBean, EmailContent contentBean) {
        try {
            Session session = Session.getInstance(getConnectionProperties(settingsBean));
            Message message = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            setRecipients(message, contentBean.getRecipients());
            setEmailText(multipart, contentBean.getText());
            setAttachmentContent(multipart, contentBean.getAttachments());
            message.setFrom(new InternetAddress(settingsBean.userName));
            message.setSubject(contentBean.getSubject());
            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception err) {
            throw new RuntimeException(err.getMessage(), err);
        }
    }

    @Override
    public Properties getConnectionProperties(SMTPSettings settingsBean) {
        Properties props = new Properties();
        props.put("mail.smtp.host", settingsBean.smtpHost);
        props.put("mail.smtp.port", settingsBean.smtpPort);
        return props;
    }

    private void setAttachmentContent(Multipart multipart, List<EntityAttachment> attachments) throws MessagingException {
        for (EntityAttachment attachment : attachments) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(attachment.getContent(), ATTACHMENT_MIME_TYPE);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachment.getFileName());
            multipart.addBodyPart(messageBodyPart);
        }
    }

    private void setEmailText(Multipart multipart, String text) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text);
        multipart.addBodyPart(messageBodyPart);
    }

    private void setRecipients(Message message, List<String> recipients) throws MessagingException {
        for (String recipient : recipients) {
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        }
    }

}
