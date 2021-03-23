package com.yss.datamiddle.tools;

import com.yss.datamiddle.annotations.CellMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @description: excel工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public final class ExcelUtil {

    /**
     * 设置返回结果数量，限制10条
     */
    private static final int RESULT_COUNT = 10;

    /**
     * 设置上传文件大小，限制100兆
     */
    private static final int MAX_MEGA = 100;
    private static final int KILO = 1024;

    /**
     * 描述：创建workbook
     *
     * @param title
     * @param colName
     * @param dataList
     * @return org.apache.poi.hssf.usermodel.HSSFWorkbook
     * @author fangzhao at 2020/11/11 16:11
     */
    public static HSSFWorkbook createWorkbookForPage(String title, List<String> colName, List<String[]> dataList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);

        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);

        // 获取列头样式对象
        HSSFCellStyle columnTopStyle = getColumnTopStyle(wb);
        // 单元格样式对象
        HSSFCellStyle style = getStyle(wb);

        sheet.addMergedRegion(new CellRangeAddress(0, 0 + 1, 0, (colName.size() - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);

        // 定义所需列数
        int columnNum = colName.size();
        // 在索引2的位置创建行(最顶端的行开始的第二行)
        HSSFRow rowRowName = sheet.createRow(0 + 2);

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(n);
            HSSFRichTextString text = new HSSFRichTextString(colName.get(n));
            cellRowName.setCellValue(text);
            cellRowName.setCellStyle(columnTopStyle);
        }

        //将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(i + 3);

            String[] obj = dataList.get(i);
            int length = obj.length;

            for (int j = 0; j < length; j++) {
                HSSFCell cell = row.createCell(j);
                if (null != obj[j] && !"".equals(obj[j])) {
                    cell.setCellValue(obj[j]);
                }
                cell.setCellStyle(style);
            }
        }

        // 宽度自适应
        excelAutoWidth(sheet, 0, 0, columnNum);
        return wb;
    }

    /**
     * 描述：下载excel
     *
     * @param response
     * @param wb
     * @param fileName
     * @return void
     * @author fangzhao at 2020/11/11 16:12
     */
    public static void download(HttpServletResponse response, HSSFWorkbook wb, String fileName) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        wb.write(out);
        wb.close();
    }

    /**
     * 描述：导出图片 excel
     *
     * @param wb
     * @param sheet
     * @param title
     * @param headerList
     * @param dataList
     * @param startRowNum
     * @param startColNum
     * @return void
     * @author fangzhao at 2020/11/11 16:12
     */
    public static void exportExecl(HSSFWorkbook wb, HSSFSheet sheet, String title, List<String> headerList, List<List<String>> dataList, int startRowNum, short startColNum) {
        // 产生表格标题行
        HSSFRow rowm = sheet.getRow(startRowNum);
        if (null == rowm) {
            rowm = sheet.createRow(startRowNum);
        }

        HSSFCell cellTiltle = rowm.createCell(startColNum);

        HSSFCellStyle columnTopStyle = getColumnTopStyle(wb);
        HSSFCellStyle style = getStyle(wb);

        int columnNum = headerList.size();
        int mergeWidth = columnNum;
        if (0 == mergeWidth) {
            mergeWidth = dataList.get(0).size();
        }

        sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum + 1, startColNum, (startColNum + mergeWidth - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);

        // 定义所需列数
        HSSFRow rowRowName = sheet.getRow(startRowNum + 2);
        if (null == rowRowName) {
            rowRowName = sheet.createRow(startRowNum + 2);
        }

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(startColNum + n);
            HSSFRichTextString text = new HSSFRichTextString(headerList.get(n));
            cellRowName.setCellValue(text);
            cellRowName.setCellStyle(columnTopStyle);
        }

        //将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {

            List<String> obj = dataList.get(i);
            HSSFRow row = sheet.getRow(startRowNum + i + 3);
            if (null == row) {
                row = sheet.createRow(startRowNum + i + 3);
            }

            for (int j = 0; j < obj.size(); j++) {
                HSSFCell cell = row.createCell(j + startColNum);
                cell.setCellValue(obj.get(j));
                cell.setCellStyle(style);
            }
        }

        //宽度自适应
        excelAutoWidth(sheet, startRowNum, startColNum, startColNum + columnNum);

    }

    /**
     * 内容宽度自适应
     *
     * @param sheet
     * @param startColNum
     * @param endColNum
     */
    public static void excelAutoWidth(HSSFSheet sheet, int startRowNum, int startColNum, int endColNum) {
        //让列宽随着导出的列长自动适应
        for (int colNum = startColNum; colNum < endColNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = startRowNum; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (null == sheet.getRow(rowNum)) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (null != currentRow.getCell(colNum)) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (0 == colNum) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
    }

    /**
     * 描述：列头单元格样式
     *
     * @param workbook
     * @return org.apache.poi.hssf.usermodel.HSSFCellStyle
     * @author fangzhao at 2020/11/11 13:52
     */
    public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 14);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("宋体");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    /**
     * 描述：列数据信息单元格样式
     *
     * @param workbook
     * @return org.apache.poi.hssf.usermodel.HSSFCellStyle
     * @author fangzhao at 2020/11/11 13:53
     */
    public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 12);
        //字体加粗
        font.setBold(false);
        //设置字体名字
        font.setFontName("宋体");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        /*style.setAlignment(HorizontalAlignment.LEFT);*/
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    /**
     * 创建图片
     *
     * @param wb
     * @param patriarch
     * @param startRow
     * @param startCol
     * @param columnWidthInPixels
     * @param baseString
     * @return
     * @throws IOException
     */
    public static short createPicture(HSSFWorkbook wb, HSSFPatriarch patriarch, int startRow, short startCol, float columnWidthInPixels, String baseString) throws IOException {
        int endCol;
        byte[] bytes = Base64.decodeBase64(baseString.replaceAll("data:image/png;base64,", ""));
        InputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bufferImg = ImageIO.read(bais);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(bufferImg, "png", byteArrayOut);

        //获取图片的占用excel cell的宽度 高度
        int width = bufferImg.getWidth();
        int widthCol = (int) Math.ceil(width / columnWidthInPixels);
        endCol = widthCol + startCol - 1;

        int height = bufferImg.getHeight();
        int heightRow = (int) Math.ceil(height / columnWidthInPixels);

        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 150, 0, 210, startCol, startRow, (short) endCol, startRow + heightRow);
        // 插入图片
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize();

        startCol = (short) (startCol + widthCol + 1);
        return startCol;
    }

    /**
     * 描述：excel导入
     *
     * @param inputStream
     * @param fileType
     * @param clazz
     * @return java.util.List<T>
     * @author fangzhao at 2020/11/11 16:12
     */
    public static <T> List<T> analysisFile(InputStream inputStream, String fileType, Class<T> clazz) {
        try {
            return analysisFile(inputStream, fileType, 0, 0, clazz);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException：" + e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error("InstantiationException：" + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException：" + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException：" + e.getMessage(), e);
        }
        return null;
    }

    private static <T> List<T> analysisFile(InputStream inputStream, String fileName, int bgnIgnore, int endIgnore, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 表格数据对象
        List<T> modelList = new ArrayList<>();

        // 用来存放表格中数据
        List<Map<String, Object>> list = new ArrayList<>();

        // 读取excel文件信息
        Workbook wb = readExcel(inputStream, fileName);

        if (null == wb) {
            return modelList;
        }

        // 获取sheet总数
        int sheetNum = wb.getNumberOfSheets();

        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
            // 获取sheet
            Sheet sheet = wb.getSheetAt(sheetIndex);

            // 获取最大行数 int rowNum = sheet.getPhysicalNumberOfRows(); int rowNum = sheet.getLastRowNum()
            int rowNum = sheet.getPhysicalNumberOfRows();

            // 除去结尾忽略的行数
            rowNum -= endIgnore;

            // 获取开始行(0 + bgnIgnore)
            Row titleRow = sheet.getRow(bgnIgnore);
            if (null == titleRow) {
                continue;
            }
            // 获取明细列数 titleRow.getPhysicalNumberOfCells()
            int colNum = titleRow.getLastCellNum();

            // excel表头校验
            Set<String> titleSet = new HashSet<>();
            for (int i = 0; i < colNum; i++) {
                Cell titleCell = titleRow.getCell(i);
                if (null == titleCell) {
                    throw new RuntimeException("上传文件内容错误！");
                }
                String titleName = (String) getCellFormatValue(titleCell);
                titleSet.add(titleName.trim());
            }
            T model4Title = clazz.getDeclaredConstructor().newInstance();
            Field[] fields4Title = model4Title.getClass().getDeclaredFields();
            for (Field field : fields4Title) {
                Annotation[] annotations = field.getAnnotations();
                if (0 == annotations.length) {
                    continue;
                }
                for (Annotation an : annotations) {
                    field.setAccessible(true);
                    if (an.annotationType().isAssignableFrom(CellMapping.class)) {
                        CellMapping column = field.getAnnotation(CellMapping.class);
                        String mappedName = column.name();
                        if (!titleSet.contains(mappedName)) {
                            throw new RuntimeException(MessageFormat.format("上传文件标题【{0}】缺失！", mappedName));
                        }
                    }
                }
            }

            // 有效的数据开始行，即标题行+1
            int rowBgn = bgnIgnore + 1;
            // 遍历明细
            for (int rowIndex = rowBgn; rowIndex < rowNum; rowIndex++) {
                // 行map
                Map<String, Object> map = new HashMap<>();
                // 行信息
                Row row = sheet.getRow(rowIndex);

                if (null != row) {
                    // 遍历行并获取到返回值
                    for (int cellIndex = 0; cellIndex < colNum; cellIndex++) {
                        Cell titleCell = titleRow.getCell(cellIndex);
                        Cell cell = row.getCell(cellIndex);
                        if (null != cell) {
                            // 标题
                            String titleCellData = (String) getCellFormatValue(titleCell);
                            // 数据
                            Object cellData = getCellFormatValue(cell);
                            map.put(titleCellData, cellData);
                        }
                    }
                    if (map.isEmpty()) {
                        log.info("ExcelUtil analysisExcelFile: row {} is empty!", rowIndex);
                    }
                    list.add(map);
                } else {
                    break;
                }
            }
        }
        log.info("解析sheet完毕,数据共有 ：[{}]条合规记录", list.size());

        generateModelList(modelList, list, clazz);
        return modelList;
    }

    /**
     * 描述：将对账文件信息存储于类中
     *
     * 正则校验
     * 考虑到批量导入存在正则不匹配情况，去掉正则校验，只在页面操作时保留 2020-07-08
     * Pattern annotation = field.getAnnotation(Pattern.class);
     * if (null != annotation) {
     *     String regexp = annotation.regexp();
     *     if (!value.toString().matches(regexp)) {
     *         throw new BizException(annotation.message() + "【" + value + "】");
     *     }
     * }
     *
     * @param list
     * @param clazz
     * @return void
     * @author fangzhao at 2020/11/11 17:10
     */
    private static <T> void generateModelList(List<T> modelList, List<Map<String, Object>> list, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (null == list || CollectionUtils.isEmpty(list)) {
            return;
        }
        for (Map<String, Object> map : list) {
            // T model = cls.newInstance(); Deprecated
            T model = clazz.getDeclaredConstructor().newInstance();
            // 获取类字段
            Field[] fields = model.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 获取字段上的注解
                Annotation[] annotations = field.getAnnotations();
                if (0 == annotations.length) {
                    continue;
                }

                for (Annotation an : annotations) {
                    field.setAccessible(true);
                    // 若扫描到CellMapping注解
                    if (an.annotationType().isAssignableFrom(CellMapping.class)) {
                        // 获取指定类型注解
                        CellMapping column = field.getAnnotation(CellMapping.class);
                        String mappedName = column.name();

                        // 获取model属性的类型
                        Class<?> modelType = field.getType();
                        // 获取map中的数据
                        Object value = map.get(mappedName);

                        // 匹配字段类型
                        if (null != value && !"".equals(value)) {
                            // 获取map中存主的字段的类型
                            Class<?> cellType = value.getClass();

                            // 处理int类型不匹配问题
                            boolean cellMatch = (double.class == cellType || Double.class == cellType) && (int.class == modelType || Integer.class == modelType || String.class == modelType);
                            if (cellMatch) {
                                String temp = value.toString();
                                if (String.class == modelType) {
                                    value = temp.split("[.]")[0];
                                } else {
                                    value = Integer.valueOf(temp.split("[.]")[0]);
                                }
                            }
                            // 处理double/bigDecimal类型不匹配问题
                            boolean decimalMatch = (double.class == cellType || String.class == cellType) && BigDecimal.class == modelType;
                            if (decimalMatch) {
                                // 不使用bigDecimal(double),否则bigDecimal(0.1)有惊喜
                                value = new BigDecimal(value.toString());
                            }
                            if (String.class == cellType && double.class == modelType) {
                                value = Double.valueOf(value.toString());
                            }
                            // 处理String类型不匹配问题
                            if (String.class == cellType && String.class == modelType) {
                                value = value.toString().trim();
                                // 长度校验
//                                Length length = field.getAnnotation(Length.class);
//                                if (null != length) {
//                                    if (value.toString().length() > length.max()) {
//                                        throw new RuntimeException(MessageFormat.format("字段【{0}】长度不能超过{1}", mappedName, length.max()));
//                                    }
//                                }
                                // 正则校验，暂时去掉
                            }
                            field.set(model, value);
                        }
                    }
                }
                field.setAccessible(false);
            }
            modelList.add(model);
        }
    }

    /**
     * 描述：读取excel
     *
     * String extName = fileName.substring(fileName.lastIndexOf("."));
     * try {
     *     if (".xls".equals(extName)) {
     *         return new HSSFWorkbook(inputStream);
     *     } else if (".xlsx".equals(extName)) {
     *         return new XSSFWorkbook(inputStream);
     *     }
     * } catch (IOException e) {
     *     log.error(e.getMessage(), e);
     * }
     *
     * @param inputStream
     * @param fileName
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author fangzhao at 2020/11/11 16:57
     */
    private static Workbook readExcel(InputStream inputStream, String fileName) {

        try {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 描述：读取Cell
     *
     * @param cell
     * @return java.lang.Object
     * @author fangzhao at 2020/6/19 16:58
     */
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        // 判断cell类型
        switch (cell.getCellTypeEnum()) {
            case NUMERIC: {
                cellValue = cell.getNumericCellValue();
                break;
            }
            case FORMULA: {
                // 判断cell是否为日期格式
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    // 转换为日期格式YYYY-mm-dd
                    cellValue = cell.getDateCellValue();
                } else {
                    // 数字
                    cellValue = cell.getNumericCellValue();
                }
                break;
            }
            case STRING: {
                cellValue = cell.getRichStringCellValue().getString();
                break;
            }
            default:
                cellValue = "";
        }
        return cellValue;
    }

    /**
     * 描述：返回导入结果
     *
     * @param builder
     * @param total
     * @param importCount
     * @param successCount
     * @param startTime
     * @param repeatSum
     * @param blankSum
     * @return void
     * @author fangzhao at 2020/11/11 16:14
     */
    public static void getImportResult(StringBuilder builder, int total, int importCount, int successCount, long startTime, int repeatSum, int blankSum) {

        if (0 < repeatSum + blankSum) {
            builder.insert(0, "失败信息：<br/>");
            if (RESULT_COUNT < repeatSum + blankSum) {
                builder.append("...<br/>").append(MessageFormat.format("重复数据共【{0}】条，空数据共【{1}】条", repeatSum, blankSum));
            }
        }
        String str = MessageFormat.format("总数据：【{0}】条，导入数据：【{1}】条，导入成功：【{2}】条，导入失败【{3}】条，耗时：{4}毫秒。<br/>",
                total, importCount, successCount, importCount - successCount, System.currentTimeMillis() - startTime);
        builder.insert(0, str);
    }

    /**
     * 描述：excel导出，jxls
     *
     * @param tempFile
     * @param destFile
     * @param model
     * @return void
     * @author fangzhao at 2020/11/11 16:15
     */
    public static void exportExcel(File tempFile, File destFile, Map<String, Object> model) {
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            exportExcel(inputStream, new FileOutputStream(destFile), model);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void exportExcel(String templateName, OutputStream os, Map<String, Object> model) {
        try (InputStream inputStream = new FileInputStream(templateName)) {
            exportExcel(inputStream, os, model);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) {

        Assert.notEmpty(model, "model is empty");
        Context context = new Context();
        for (String key : model.keySet()) {
            context.putVar(key, model.get(key));
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        try {
            jxlsHelper.processTemplate(context, transformer);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 描述：文件校验
     *
     * @param fileName
     * @param fileSize
     * @return java.lang.String
     * @author fangzhao at 2020/11/11 17:53
     */
    public static String validateFile(String fileName, Long fileSize) {

        if (StringUtils.isBlank(fileName)) {
            return "请选择导入文件";
        }
        String[] split = fileName.split("[.]");
        String extName = split[split.length - 1];
        if (!extName.matches("xls|xlsx")) {
            return "文件格式不正确";
        }

        if (null != fileSize && 0 < fileSize) {
            if (MAX_MEGA * KILO * KILO < fileSize) {
                return "上传文件大小超过限制（100M）！";
            }
        }
        return null;
    }
}
