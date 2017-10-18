package org.alexjdev.utils.file;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Вспомогательный класс для перемещения файлов
 */
public class FileReplacer {

    private static final Log LOG = LogFactory.getLog(FileReplacer.class);

    /**
     * Перемещение файла в указанную директорию
     *
     * @param file                 исходный файл
     * @param destinationDirectory директория
     */
    public static void moveFile(File file, String destinationDirectory) {
        try {
            LOG.info("Move file " + file.getCanonicalPath() + " to " + destinationDirectory);
            FileUtils.moveFileToDirectory(file, new File(destinationDirectory), false);
        } catch (FileExistsException err) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            String strDate = dateFormat.format(date);
            try {
                String fileName = file.getCanonicalPath();
                File renamedFile = new File(FilenameUtils.removeExtension(fileName) + "_" + strDate +
                                                    "." + FilenameUtils.getExtension(fileName));
                if (file.renameTo(renamedFile)) {
                    FileUtils.moveFileToDirectory(renamedFile, new File(destinationDirectory), false);
                } else {
                    LOG.error("Couldn't rename file " + file.getPath());
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            LOG.warn("File " + file.getPath() + " is already exists.");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
