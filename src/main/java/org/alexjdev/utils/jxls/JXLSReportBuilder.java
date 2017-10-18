package org.alexjdev.utils.jxls;

import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import static org.alexjdev.utils.log.Log.error;

/**
 * Утилиты для создания отчетов через JXLS
 */
public class JXLSReportBuilder {

    public static byte[] runJXLSTransformer(Map<String, Object> beans, String excelTemplate, String[] excludedSheets) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Workbook workbook = createWorkBook(beans, excelTemplate, excludedSheets);
            workbook.write(outputStream);
        } catch (Exception e) {
            return error(e, JXLSReportBuilder.class, "");
        }
        return outputStream.toByteArray();
    }

    public static byte[] runJXLSWithSXSSFTransformer(Map<String, Object> beans, String excelTemplate, String[] excludedSheets) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Workbook workbook = createSXSSFWorkBook(beans, excelTemplate, excludedSheets);
            workbook.write(outputStream);
            if (workbook instanceof SXSSFWorkbook) {
                SXSSFWorkbook sxssfWorkbookBook = (SXSSFWorkbook) workbook;
                sxssfWorkbookBook.dispose();
            }
        } catch (Exception e) {
            return error(e, JXLSReportBuilder.class, "");
        }
        return outputStream.toByteArray();
    }

    public static Workbook createWorkBook(Map<String, Object> beans, String excelTemplate, String[] excludedSheets) {
        return createWorkbookByTemplateFromStream(beans,  JXLSReportBuilder.class.getResourceAsStream(excelTemplate), excludedSheets);

    }

    public static Workbook createWorkbookByTemplateFromStream(Map<String, Object> beans, InputStream inputStream, String[] excludedSheets) {
        XLSTransformer transformer = new XLSTransformer();
        Configuration configuration = new Configuration();
        transformer.setConfiguration(configuration);
        if (excludedSheets != null) {
            for (String excludedSheet : excludedSheets) {
                configuration.addExcludeSheet(excludedSheet);
            }
        }
        try {
            Workbook workbook = transformer.transformXLS(inputStream, beans);
            workbook.setForceFormulaRecalculation(true);
            return workbook;
        } catch (Exception e) {
            return error(e, JXLSReportBuilder.class, "");
        }
    }

    public static Workbook createSXSSFWorkBook(Map<String, Object> beans, String excelTemplate, String[] excludedSheets) {
        InputStream inputStream = JXLSReportBuilder.class.getResourceAsStream(excelTemplate);
        XLSTransformer transformer = new XLSTransformer();
        Configuration configuration = new Configuration();
        transformer.setConfiguration(configuration);
        if (excludedSheets != null) {
            for (String excludedSheet : excludedSheets) {
                configuration.addExcludeSheet(excludedSheet);
            }
        }
        try {
            Workbook workbook = new SXSSFWorkbook((XSSFWorkbook) transformer.transformXLS(inputStream, beans), 500);
            workbook.setForceFormulaRecalculation(true);
            return workbook;
        } catch (Exception e) {
            return error(e, JXLSReportBuilder.class, "");
        }

    }

}
