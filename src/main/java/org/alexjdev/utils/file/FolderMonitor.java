package org.alexjdev.utils.file;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Collections.emptyList;

/**
 * Получает список файлов из директории
 */
public class FolderMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(FolderMonitor.class);

    /**
     * Получает список файлов из директории
     *
     * @param folderFullPath путь до директории
     * @param countFiles     количество файлов, которые необходимо получить из директории.
     *                       Если параметр меньше или равен 0, то выбираются все файлы из директории
     * @return список файлов из директории
     */
    public static List<String> getFilePaths(final String folderFullPath, long countFiles) {
        if (StringUtils.isEmpty(folderFullPath) || "EMPTY_DIR".equals(folderFullPath)) {
            return emptyList();
        }
        File fileResource = new File(folderFullPath);
        if (!fileResource.exists()) {
            LOG.warn("{} must be exists", fileResource.getAbsolutePath());
            return emptyList();
        }
        if (!fileResource.isDirectory()) {
            LOG.warn("{} must be folder", folderFullPath);
            return emptyList();
        }
        DirectoryStream.Filter<Path> filter;
        if (countFiles > 0) {
            filter = createFilterWithCounter(countFiles);
        } else {
            filter = createFilterWithoutCounter();
        }
        List<String> fileNames = new LinkedList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderFullPath), filter)) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {
            //пропускаем. нечего писать
        }
        return copyOf(fileNames);
    }

    private static DirectoryStream.Filter<Path> createFilterWithCounter(final long maxFiles) {
        return new DirectoryStream.Filter<Path>() {
            long countFiles = 0;

            public boolean accept(Path file) throws IOException {
                if (!Files.isDirectory(file)) {
                    countFiles++;
                }
                return !Files.isDirectory(file) && countFiles <= maxFiles;
            }
        };
    }

    private static DirectoryStream.Filter<Path> createFilterWithoutCounter() {
        return new DirectoryStream.Filter<Path>() {
            public boolean accept(Path file) throws IOException {
                return !Files.isDirectory(file);
            }
        };
    }
}
