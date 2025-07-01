package css;

import com.github.paohaijiao.jstyle.factory.JExcelStyleFactory;
import com.github.paohaijiao.jstyle.model.JExcelStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;

public class JGraphTest {
    @Test
    public void column() {
        Workbook workbook = new XSSFWorkbook();

        JExcelStyle headerStyle = JExcelStyleFactory.create(workbook)
                .withFontName("Arial")
                .withFontSize((short)14)
                .withBold(true)
                .withFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex())
                .withBorder(BorderStyle.THIN)
                .withAlignment(HorizontalAlignment.CENTER)
                .build();
        CellStyle cellStyle = JExcelStyleFactory.create(workbook)
                .withFontName("Calibri")
                .withFontColor(IndexedColors.RED.getIndex())
                .withItalic(true)
                .withWrapText(true)
                .buildAndApply();
        JExcelStyle customStyle = JExcelStyleFactory.create(workbook)
                .withProperty("custom.property1", "value1")
                .withProperty("custom.property2", 12345)
                .build();
    }
}
