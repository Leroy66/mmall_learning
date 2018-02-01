package com.mmall.util.excel;

import com.smartwork.msip.cores.helper.JsonHelper;
import com.smartwork.msip.cores.helper.date.DateUtils;
import com.smartwork.msip.cores.utils.excel.ImportExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2018/2/1 17:49
 */
public class ImportExcelUtil {

    @Test
    public  void importExcel() throws InvalidFormatException, IOException, InstantiationException, IllegalAccessException {
        // 读取excel
        String file = "C:\\Users\\leroy\\Desktop\\组织活动\\1.xlsx";
        ImportExcel ei = new ImportExcel(file, 0, 0);
        List<TransTaskContentDTO> userList = ei.getDataList(TransTaskContentDTO.class);
        //TODO:进行数据处理
        for (TransTaskContentDTO transTaskContentDTO : userList) {
            String str = "\"" + transTaskContentDTO.getEnglish()
                              + "\", \"" + transTaskContentDTO.getChinese()
                              + "\", \"" + transTaskContentDTO.getJapanese()
                              + "\", \"" + transTaskContentDTO.getKorean()
                              + "\", \"" + transTaskContentDTO.getSpanish()
                              + "\", \"" + transTaskContentDTO.getRussian()
                              + "\", \"" + transTaskContentDTO.getVietnamese()
                              + "\", \"" + transTaskContentDTO.getFrench()
                              + "\", \"" + transTaskContentDTO.getGerman() + "\"";
            System.out.println(str);
        }
        // 导出excel
        String filename = "__" + DateUtils.getDate() + ".xlsx";
        new ExportMultiExcelUtil().exportMultiExcel("用户信息", TransTaskContentDTO.class, userList).writeFile(file + filename).dispose();
        System.out.println(JsonHelper.getJSONString(userList));
    }
}
