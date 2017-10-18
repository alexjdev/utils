package org.alexjdev.utils.mail;

import java.util.Properties;

/**
 * Интерфейс для отправки email
 */
public interface EmailSender {

    /**
     * Отправка письма получателям
     *
     * @param settingsBean бин с настройками
     * @param contentBean бин с содержимым рассылки
     * @throws RuntimeException при проблемах с отправкой писем
     */
    void sendEmail(SMTPSettings settingsBean, EmailContent contentBean);

    /**
     * Получение настроек для подключения к smtp серверу
     *
     * @param settingsBean бин с настройками
     * @return настройки подключения
     */
    Properties getConnectionProperties(SMTPSettings settingsBean);
}
