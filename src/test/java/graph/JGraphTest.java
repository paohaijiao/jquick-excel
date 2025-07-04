package graph;

import com.github.paohaijiao.enums.JExcelChartType;
import com.github.paohaijiao.factory.JExcelChartFactory;
import com.github.paohaijiao.model.JExcelChartData;
import com.github.paohaijiao.model.JSeriesData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class JGraphTest {
    @Test
    public void column() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("销售数据统计");
        chartData.setCategoryAxisTitle("产品");
        chartData.setValueAxisTitle("销量");
        chartData.setCategories(Arrays.asList("产品A", "产品B", "产品C", "产品D"));
        JSeriesData series1 = new JSeriesData();
        series1.setName("第一季度");
        series1.setData(Arrays.asList(120, 200, 150, 180));
        JSeriesData series2 = new JSeriesData();
        series2.setName("第二季度");
        series2.setData(Arrays.asList(180, 210, 190, 220));
        chartData.setSeries(Arrays.asList(series1, series2));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.COLUMN, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//SalesReport.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bar() {
        JExcelChartData salesData = new JExcelChartData();
        salesData.setTitle("2023年季度销售数据");
        salesData.setCategoryAxisTitle("季度");
        salesData.setValueAxisTitle("销售额(万元)");
        salesData.setCategories(Arrays.asList("第一季度", "第二季度", "第三季度", "第四季度"));
        JSeriesData productA = new JSeriesData();
        productA.setName("产品A");
        productA.setData(Arrays.asList(450, 520, 480, 600));
        JSeriesData productB = new JSeriesData();
        productB.setName("产品B");
        productB.setData(Arrays.asList(320, 380, 420, 500));
        JSeriesData productC = new JSeriesData();
        productC.setName("产品C");
        productC.setData(Arrays.asList(280, 310, 350, 400));
        salesData.setSeries(Arrays.asList(productA, productB, productC));
        salesData.setSeries(Arrays.asList(productA, productB, productC));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                salesData, JExcelChartType.BAR, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//bar.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bar3d() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("产品季度销售(3D)");
        chartData.setCategoryAxisTitle("产品");
        chartData.setValueAxisTitle("销售额(万元)");
        chartData.setCategories(Arrays.asList("笔记本", "手机", "平板", "显示器", "配件"));
        JSeriesData q1 = new JSeriesData();
        q1.setName("第一季度");
        q1.setData(Arrays.asList(450, 680, 320, 280, 190));
        JSeriesData q2 = new JSeriesData();
        q2.setName("第二季度");
        q2.setData(Arrays.asList(520, 720, 380, 310, 210));
        JSeriesData q3 = new JSeriesData();
        q3.setName("第三季度");
        q3.setData(Arrays.asList(480, 750, 350, 330, 230));
        chartData.setSeries(Arrays.asList(q1, q2, q3));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.BAR3D, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//bar3D.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void line() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("2023年北京月平均温度变化");
        chartData.setCategoryAxisTitle("月份");
        chartData.setValueAxisTitle("温度(℃)");
        chartData.setCategories(Arrays.asList(
                "1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"
        ));
        JSeriesData avgTemp = new JSeriesData();
        avgTemp.setName("平均温度");
        avgTemp.setData(Arrays.asList(
                -3.2, 0.5, 7.8, 15.2, 21.3, 25.7,
                27.9, 26.8, 21.5, 14.6, 6.3, -1.0
        ));
        JSeriesData maxTemp = new JSeriesData();
        maxTemp.setName("最高温度");
        maxTemp.setData(Arrays.asList(
                2.1, 5.3, 12.7, 20.5, 26.8, 30.4,
                32.6, 31.5, 27.2, 20.8, 12.5, 4.2
        ));
        JSeriesData minTemp = new JSeriesData();
        minTemp.setName("最低温度");
        minTemp.setData(Arrays.asList(
                -8.5, -4.2, 2.9, 9.9, 15.8, 21.0,
                23.2, 22.1, 15.8, 8.4, 0.1, -6.2
        ));
        chartData.setSeries(Arrays.asList(avgTemp, maxTemp, minTemp));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.LINE, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//line.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pie() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("2023年智能手机市场份额");
        JSeriesData marketShare = new JSeriesData();
        marketShare.setName("市场份额");
        marketShare.setData(Arrays.asList(38.5, 22.3, 15.7, 10.2, 8.5, 4.8));
        chartData.setCategories(Arrays.asList(
                "Apple", "Samsung", "Xiaomi", "OPPO", "vivo", "其他"
        ));
        chartData.setSeries(Collections.singletonList(marketShare));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.PIE, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//pie.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void area() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("季度销售趋势分析");
        chartData.setCategoryAxisTitle("季度");
        chartData.setValueAxisTitle("销售额(万元)");
        chartData.setCategories(Arrays.asList("Q1", "Q2", "Q3", "Q4"));
        JSeriesData productA = new JSeriesData();
        productA.setName("产品线A");
        productA.setData(Arrays.asList(120, 150, 180, 210));
        JSeriesData productB = new JSeriesData();
        productB.setName("产品线B");
        productB.setData(Arrays.asList(80, 110, 130, 170));
        JSeriesData productC = new JSeriesData();
        productC.setName("产品线C");
        productC.setData(Arrays.asList(60, 75, 90, 120));

        chartData.setSeries(Arrays.asList(productA, productB, productC));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.AREA, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//area.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void area3d() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("季度销售趋势(3D)");
        chartData.setCategoryAxisTitle("季度");
        chartData.setValueAxisTitle("销售额(万元)");
        chartData.setCategories(Arrays.asList("Q1", "Q2", "Q3", "Q4"));
        JSeriesData productA = new JSeriesData();
        productA.setName("产品线A");
        productA.setData(Arrays.asList(120, 150, 180, 210));
        JSeriesData productB = new JSeriesData();
        productB.setName("产品线B");
        productB.setData(Arrays.asList(80, 110, 130, 170));
        JSeriesData productC = new JSeriesData();
        productC.setName("产品线C");
        productC.setData(Arrays.asList(60, 75, 90, 120));
        chartData.setSeries(Arrays.asList(productA, productB, productC));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.AREA3D, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//area3D.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scatter() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("身高体重分布");
        chartData.setCategoryAxisTitle("身高(cm)");
        chartData.setValueAxisTitle("体重(kg)");
        chartData.setCategories(Arrays.asList(
                "160", "165", "170", "175", "180", "185", "190"
        ));
        JSeriesData male = new JSeriesData();
        male.setName("男性");
        male.setData(Arrays.asList(55, 60, 65, 70, 75, 80, 85));
        JSeriesData female = new JSeriesData();
        female.setName("女性");
        female.setData(Arrays.asList(50, 55, 58, 62, 65, 68, 70));
        chartData.setSeries(Arrays.asList(male, female));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.SCATTER, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//scatter.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void radar() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("员工能力评估");
        chartData.setCategories(Arrays.asList(
                "编程能力", "沟通能力", "设计能力",
                "解决问题", "团队合作", "学习能力"
        ));
        JSeriesData employeeA = new JSeriesData();
        employeeA.setName("员工A");
        employeeA.setData(Arrays.asList(90, 70, 85, 80, 75, 88));
        JSeriesData employeeB = new JSeriesData();
        employeeB.setName("员工B");
        employeeB.setData(Arrays.asList(75, 85, 70, 90, 80, 75));
        chartData.setSeries(Arrays.asList(employeeA, employeeB));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.RADAR, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//radar.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void surface() {
        JExcelChartData chartData = new JExcelChartData();
        chartData.setTitle("地形高度示例");
        chartData.setCategoryAxisTitle("X坐标");
        chartData.setValueAxisTitle("Y坐标");
        chartData.setCategories(Arrays.asList("1", "2", "3", "4", "5"));
        JSeriesData row1 = new JSeriesData();
        row1.setName("Y=1");
        row1.setData(Arrays.asList(10, 15, 25, 30, 25));
        JSeriesData row2 = new JSeriesData();
        row2.setName("Y=2");
        row2.setData(Arrays.asList(15, 20, 35, 40, 35));
        JSeriesData row3 = new JSeriesData();
        row3.setName("Y=3");
        row3.setData(Arrays.asList(25, 35, 50, 55, 50));
        JSeriesData row4 = new JSeriesData();
        row4.setName("Y=4");
        row4.setData(Arrays.asList(20, 30, 45, 50, 45));
        JSeriesData row5 = new JSeriesData();
        row5.setName("Y=5");
        row5.setData(Arrays.asList(15, 25, 35, 40, 35));
        chartData.setSeries(Arrays.asList(row1, row2, row3, row4, row5));
        XSSFWorkbook workbook = JExcelChartFactory.createWorkbookWithChart(
                chartData, JExcelChartType.SURFACE, "销售报表");
        try (FileOutputStream out = new FileOutputStream("D://test//surface.xlsx")) {
            JExcelChartFactory.writeWorkbookToStream(workbook, out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
