package com.example.srctreasuredetect.services;

import com.example.srctreasuredetect.services.fofaSearchService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import com.example.srctreasuredetect.entity.dataExcelVo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dataAnalasyService {

    public String root="/Users/lanfei/工作/信息搜集/";




    //一次写入实现
    public  void onePutData(List<List> txlist,String filename) throws FileNotFoundException {


        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //默认设置为水平居中
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 15);
        headWriteCellStyle.setWriteFont(headWriteFont);

        //设置字体大小
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 15);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

//        Pattern pattern = Pattern.compile("[^=\"]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
//        Matcher matcher =pattern.matcher(fofaSearchService.json);
//        matcher.find();
//        filename=filename+matcher.group(0)+".xlsx";
//        System.out.println(filename);
        filename=root+filename;
        //导出excel
            FileOutputStream outputStream = new FileOutputStream(new File(filename));

        //单次写入
            ExcelWriter writer = EasyExcel.write(outputStream)
                    //绑定头和内容策略
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                    .build();
            WriteSheet sheet = EasyExcel.writerSheet(0, "sheet").head(dataExcelVo.class).build();
            writer.write(txlist, sheet);
            writer.finish();
    }



    //多次写入数据
    public  void morePutDta(List<List> txlist,String filename){

        System.out.println(filename);
        System.out.println("======");
        System.out.println(txlist);

//        //以域名方式提取文件名并保存
//        Pattern pattern = Pattern.compile("[^=\"]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
//        Matcher matcher =pattern.matcher(fofaSearchService.json);
//        matcher.find();
//        filename=filename+matcher.group(0)+".xlsx";
//        System.out.println(filename);
//        System.out.println(filename);
        filename=root+filename;
        File templateFile = new File(filename);
        File destFile = new File("/Users/lanfei/工作/信息搜集/资产中专.xlsx");
        System.out.println(destFile);

        try {
            if (templateFile.exists()) {
                //追加数据，目标文件与原始文件不能是同一个文件名
                //withTemplate()指定模板文件
                ExcelWriter excelWriter = EasyExcel.write().
                        withTemplate(templateFile)
                        //.file() 指定目标文件，不能与模板文件是同一个文件
                        .file(destFile).autoCloseStream(false).build();
                WriteSheet sheet = EasyExcel.writerSheet(0, "sheet").head(dataExcelVo.class).build();
                excelWriter.write(txlist, sheet);
                excelWriter.finish();
            } else {
                //设置excel格式
                WriteCellStyle headWriteCellStyle = new WriteCellStyle();
                //默认设置为水平居中
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
                contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

                WriteFont headWriteFont = new WriteFont();
                headWriteFont.setFontHeightInPoints((short) 15);
                headWriteCellStyle.setWriteFont(headWriteFont);

                //设置字体大小
                WriteFont contentWriteFont = new WriteFont();
                contentWriteFont.setFontHeightInPoints((short) 15);
                contentWriteCellStyle.setWriteFont(contentWriteFont);
                ExcelWriter writer = EasyExcel.write(templateFile,dataExcelVo.class)
                        //绑定头和内容策略
                        .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                        .build();
                WriteSheet sheet = EasyExcel.writerSheet(0, "sheet").head(dataExcelVo.class).build();
                writer.write(txlist, sheet);
                writer.finish();
            }
        }catch (Exception exception){
            System.out.println(exception.toString());
        }

        if (destFile.exists()) {
            //删除原模板文件，新生成的文件变成新的模板文件
            templateFile.delete();
            destFile.renameTo(templateFile);
        }
    }


    //设置excel显示策略
    public void excelInit(){


        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //默认设置为水平居中
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);

        //设置字体大小
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);


    }

    public String getStatus(String url) throws IOException {
        String code="";
        try {
            //有的url是没有http或者https的
            if (url.startsWith("http") || url.startsWith("https")) {
                url = url;
            } else {
                url = "http://" + url;
            }
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(15, TimeUnit.SECONDS).build();//设置读取超时时间;
            Request request = new Request.Builder().url(url).get().build();
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("请求错误:"+url);
            }
            code=String.valueOf(response.code());
            response.close();
        }catch (Exception e){
            code="";
        }


        return code;
    }
    //fofa搜索结果处理
    public  void fofaexcelStrategy(JSONArray dataArray) throws IOException {
        String filename="";

        //将fastjson的array中数据放入List中，方便后续对List中内容做修改
        System.out.println("excelStrategy方法运行中.......");
        List<List> txlist = new ArrayList<>();


        //
        for (int index = 0; index <dataArray.size() ; index++) {
            String url="";
            String code="";
            List<String> data;
//            System.out.println("====================");
            //由于fofa API不直接返回状态码，这里专门调用getStatus函数来获取statuscode值
//            System.out.println();
            url= (String) dataArray.getJSONArray(index).get(0);
            code=getStatus(url);
            List<String> list=JSONObject.parseArray(dataArray.getJSONArray(index).toJSONString(),String.class);
            list.set(1,code);
//            System.out.println("******"+list);
////            System.out.println(dataArray.get(index).toString());
//            System.out.println("=====================");
//            System.out.println(dataArray.toString());
            txlist.add(list);
//            break;
        }


            //设置头策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //默认设置为水平居中
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);

        //设置字体大小
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);


        dataAnalasyService dataAnalasyService = new dataAnalasyService();
        filename=fofaSearchService.getFileName();
        dataAnalasyService.morePutDta(txlist,filename);

        }



    //hunter搜索结果数据处理
    public void hunterexcelStrategy(JSONArray dataArray) throws FileNotFoundException {
        String filename="";
        List<List> listList = new ArrayList<>();

        //抽取需要的数据
        for(int index=0;index<dataArray.size();index++){
            try {
                System.out.println(dataArray.get(index));
                List<String> list = new ArrayList<>();
                JSONObject jsonObject = dataArray.getJSONObject(index);
                list.add(jsonObject.get("domain").toString());
                list.add(jsonObject.get("status_code").toString());
                list.add(jsonObject.get("protocol").toString());
                list.add(jsonObject.get("component").toString());
                list.add(jsonObject.get("ip").toString());
                list.add(jsonObject.get("port").toString());
                list.add(jsonObject.get("component").toString());
                list.add(jsonObject.get("web_title").toString());
                list.add(jsonObject.get("number").toString());
                listList.add(list);
            }catch (Exception e){
                System.out.println("空值");
            }
//
        }
        dataAnalasyService dataAnalasyService = new dataAnalasyService();
        filename=hunterSearchService.getFileName();
        dataAnalasyService.morePutDta(listList,filename);

    }



    public void quakeexcelStrateg(JSONArray jsonArray) throws FileNotFoundException {
        String filename="";

        List<List> listList = new ArrayList<>();
        System.out.println("!!!!!!!!2");
        for(int index=0;index<jsonArray.size();index++){
            try {
                System.out.println(jsonArray.get(index));
                List<String> list = new ArrayList<>();
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                list.add(jsonObject.getJSONObject("service").getJSONObject("http").get("host").toString());
                list.add("NULL");

                list.add(jsonObject.getJSONObject("service").get("name").toString());
                list.add(jsonObject.get("components").toString());
                list.add(jsonObject.get("ip").toString());
                list.add(jsonObject.get("post").toString());
                list.add(jsonObject.get("components").toString());
                list.add(jsonObject.getJSONObject("service").getJSONObject("http").get("title").toString());
                list.add("NULL");
                System.out.println("!!!!!!!!!!!!");
                System.out.println(list);
                listList.add(list);
            }catch (Exception e){
                System.out.println("异常"+e);
            }
        }
        System.out.println("））））））））））））））））））））））））））））））");
        System.out.println(listList);
        dataAnalasyService dataAnalasyService = new dataAnalasyService();
        filename=quakeSearchService.getFileName();
        dataAnalasyService.morePutDta(listList,filename);


    }

}

