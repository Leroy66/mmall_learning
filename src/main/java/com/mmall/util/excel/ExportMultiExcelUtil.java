package com.mmall.util.excel;

import com.smartwork.msip.cores.helper.EncodeHelper;
import com.smartwork.msip.cores.helper.ReflectionHelper;
import com.smartwork.msip.cores.utils.excel.annotation.ExcelField;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportMultiExcelUtil {

    private static Logger log = LoggerFactory.getLogger(ExportMultiExcelUtil.class);

    /**
     * 工作薄对象
     */
    private SXSSFWorkbook wb;

    /**
     * 注解列表（Object[]{ ExcelField, Field/Method }）
     */
    List<Object[]> annotationList = new ArrayList<>();
    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    private OutputStream out;

    /**
     * //@param sheetNum
     * sheet的位置，0表示第一个表格中的第一个sheet
     *
     * @param sheetTitle sheet的名称
     * @param cls        类名称
     * @param obj        要导出的数据集
     * @return
     */
    public <E> ExportMultiExcelUtil exportMultiExcel(String sheetTitle, Class<E> cls, List<E> obj) {
        return exportMultiExcel(0, "", sheetTitle, cls, obj, 1);
    }

    /***
     *
     * @param sheetNum
     *            sheet的位置，0表示第一个表格中的第一个sheet
     * @param sheetTitle
     *            sheet的名称
     * @param cls
     *            类名称
     * @param obj
     *            要导出的数据集
     * @return
     */
    public <E> ExportMultiExcelUtil exportMultiExcel(int sheetNum, String sheetTitle, Class<E> cls, List<E> obj) {
        return exportMultiExcel(sheetNum, "", sheetTitle, cls, obj, 1);
    }

    /**
     * @param sheetNum   sheet的位置，0表示第一个表格中的第一个sheet
     * @param title      sheet header的名称
     * @param sheetTitle sheet的名称
     * @param cls        类名称
     * @param obj        要导出的数据集
     * @param type       type 导出类型（1:导出数据；2：导出模板）
     * @return ExportMultiExcelUtil
     */
    public <E> ExportMultiExcelUtil exportMultiExcel(int sheetNum, String title, String sheetTitle, Class<E> cls, List<E> obj, int type) {
        annotationList = new ArrayList<>();
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                annotationList.add(new Object[]{ef, f});
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                annotationList.add(new Object[]{ef, m});
            }
        }
        // Field sorting
//		Collections.sort(annotationList, new Comparator<Object[]>() {
//			public int compare(Object[] o1, Object[] o2) {
//				return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
//			};
//		});
        // Initialize
        List<String> headerList = new ArrayList<>();
        for (Object[] os : annotationList) {
            String t = ((ExcelField) os[0]).title();
            // 如果是导出，则去掉注释
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        int rownum = initialize(sheetNum, title, sheetTitle, headerList);
        initdata(sheetNum, rownum, obj);
        return this;
    }

    private <E> void initdata(int sheetNum, int rownum, List<E> list) {
        Sheet sheet = wb.getSheetAt(sheetNum);
        Row row = null;
        for (E e : list) {
            int colunm = 0;
            row = sheet.createRow(rownum++);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                Object val = null;
                // Get entity value
                try {
                    if (StringUtils.isNotBlank(ef.value())) {
                        val = ReflectionHelper.invokeGetter(e, ef.value());
                    } else {
                        if (os[1] instanceof Field) {
                            val = ReflectionHelper.invokeGetter(e, ((Field) os[1]).getName());
                        } else if (os[1] instanceof Method) {
                            val = ReflectionHelper.invokeMethod(e, ((Method) os[1]).getName(), new Class[0], new Object[0]);
                        }
                    }
                    // If is dict, get dict label
                    if (StringUtils.isNotBlank(ef.dictType())) {
                        val = val == null ? "" : val.toString();
                    }
                } catch (Exception ex) {
                    log.info(ex.toString());
                    val = "";
                }
                addCell(row, colunm++, val, ef.align(), ef.fieldType());
                sb.append(val + ", ");
            }
            log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
        }
    }

    /**
     * 初始化函数
     *
     * @param title      表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    private int initialize(int sheetNum, String title, String sheetTitle, List<String> headerList) {
        if (this.wb == null) {
            this.wb = new SXSSFWorkbook(500);
        }
        Sheet sheet = wb.createSheet();
        wb.setSheetName(sheetNum, sheetTitle);
        this.styles = createStyles(wb);
        sheet.setDefaultColumnStyle(100, styles.get("data"));

        int rownum = 0;
        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerList.size() - 1));
        }
        // Create header
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        return rownum++;
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);

        XSSFDataFormat format = (XSSFDataFormat) wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setDataFormat(format.getFormat("@"));
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setDataFormat(format.getFormat("@"));
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(format.getFormat("@"));
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一个单元格
     *
     * @param row    添加的行
     * @param column 添加列号
     * @param val    添加值
     * @param align  对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    private Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType) {
        Cell cell = row.createCell(column);
        CellStyle style = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
        try {
            // 判断值的类型后进行强制类型转换
            String textValue = null;
            if (val == null) {
                textValue = "";
            } else if (val instanceof Boolean) {
                boolean bValue = (Boolean) val;
                textValue = "否";
                if (bValue) {
                    textValue = "是";
                }
            } else if (val instanceof Date) {
                String dateFormat = "yyyy-MM-dd HH:mm:ss"; // 默认为日期
                SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                textValue = format.format(val);
            } else {
                textValue = val.toString();
            }

            if (textValue != null) {
                Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                Matcher matcher = p.matcher(textValue);
                if (matcher.matches()) {
                    // 是数字当作double处理
                    cell.setCellValue(Double.parseDouble(textValue));
                } else {
                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    cell.setCellValue(richString);
                }
            }

        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 输出数据流
     *
     * @param os 输出数据流
     */
    public ExportMultiExcelUtil write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName 输出文件名
     */
    public ExportMultiExcelUtil write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + EncodeHelper.urlEncode(fileName));
        out = response.getOutputStream();
        write(out);
        return this;
    }

    /**
     * 输出到文件
     *
     * @param name 输出文件名
     */
    public ExportMultiExcelUtil writeFile(String name) throws FileNotFoundException, IOException {
        out = new FileOutputStream(name);
        write(out);
        return this;
    }

    /**
     * 清理临时文件
     *
     * @throws IOException
     */
    public ExportMultiExcelUtil dispose() throws IOException {
        wb.dispose();
        out.flush();
        out.close();
        return this;
    }

}
