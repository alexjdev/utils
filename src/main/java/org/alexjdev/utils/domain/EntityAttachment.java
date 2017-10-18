package org.alexjdev.utils.domain;

import java.util.Date;

public interface EntityAttachment {
    Long getId();

    void setId(Long id);

    String getFileName();

    void setFileName(String fileName);

    byte[] getContent();

    void setContent(byte[] content);

    String getUserLogin();

    void setUserLogin(String userLogin);

    Date getAddDate();

    void setAddDate(Date addDate);

    Date getRemovalDate();

    void setRemovalDate(Date removalDate);
}
