package org.alexjdev.utils.mail;

import org.alexjdev.utils.domain.EntityAttachment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.defaultString;

/**
 * Бин, который хранит содержимое рассылки и адреса получателей
 */
public class EmailContent {

    private List<String> recipients;
    private String subject;
    private String text;
    private List<EntityAttachment> attachments;

    public List<String> getRecipients() {
        if (recipients == null) {
            recipients = new LinkedList<>();
        }
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<EntityAttachment> getAttachments() {
        if (attachments == null) {
            attachments = new LinkedList<>();
        }
        return attachments;
    }

    public void setAttachments(List<EntityAttachment> attachments) {
        this.attachments = attachments;
    }

    public byte[] buildEML() throws MessagingException, IOException {
        Message message = new MimeMessage(Session.getInstance(System.getProperties()));
        Multipart multipart = new MimeMultipart();
        setRecipients(message, getRecipients());
        setEmailText(multipart, defaultString(text));
        setAttachmentContent(multipart, getAttachments());
        message.setSubject(MimeUtility.encodeText(defaultString(subject), "UTF-8", null));
        message.setContent(multipart);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);

        return outputStream.toByteArray();
    }

    private void setEmailText(Multipart multipart, String text) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text, "UTF-8");
        multipart.addBodyPart(messageBodyPart);
    }

    private void setAttachmentContent(Multipart multipart, List<EntityAttachment> attachments) throws MessagingException,
            UnsupportedEncodingException {
        for (EntityAttachment attachment : attachments) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(attachment.getContent(), "application/octet-stream");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeText(attachment.getFileName(), "UTF-8", null));
            multipart.addBodyPart(messageBodyPart);
        }
    }

    private void setRecipients(Message message, List<String> recipients) throws MessagingException {
        for (String recipient : recipients) {
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        }
    }

}
