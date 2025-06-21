package lexer.im;

import com.paohaijiao.javelin.model.JExcelImportModel;
import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JImLexerTest {

    private JExcelImportModel parseImportConfig(String input) {
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.importConfig();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        @SuppressWarnings("unchecked")
        JExcelImportModel result = (JExcelImportModel)visitor.visit(tree);
        return result;
    }
    @Test
    public void testSimpleImport() {
        String input = "IMPORT FROM \"d://student.xlsx\" TO myTable";
        JExcelImportModel result = parseImportConfig(input);
        System.out.println(result);
    }
    @Test
    public void testImportWithOptions() {
        String input = "IMPORT FROM \"d://student.xlsx\" WITH SHEET=1, HEADER='YES', RANGE=\"A1:D100\" TO sales_data";
        JExcelImportModel result = parseImportConfig(input);
        System.out.println(result);
    }
    @Test
    public void testImportWithMappings() {
        String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                "MAPPING = {\"Name\": \"full_name\", \"Age\": \"employee_age\", \"Dept\": \"department\"} " +
                "TO employee_records";
        JExcelImportModel result = parseImportConfig(input);
       System.out.println(result);
    }
    @Test
    public void testImportWithTransforms() {
        String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                "TRANSFORM = {\"Price\": \"formatCurrency($.Price)\", \"Date\": \"parseDate($.Date)\"} " +
                "TO product_list";
        JExcelImportModel model = parseImportConfig(input);
        System.out.println(model);

    }
    @Test
    public void testComplexImport() {
        String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                "SHEET=\"Q1 Report\", " +
                "HEADER=YES" +
                "RANGE=\"B2:M50\", " +
                "MAPPING = {\"Account\": \"account_code\", \"Amount\": \"convertToDecimal($.Amount)\"}, " +
                "TRANSFORM = {\"Notes\": \"sanitizeText($.Notes)\"} " +
                "TO q1_financials";
        JExcelImportModel model = parseImportConfig(input);
        System.out.println(model);
    }

}
