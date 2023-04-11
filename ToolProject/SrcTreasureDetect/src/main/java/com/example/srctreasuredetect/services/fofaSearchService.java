package com.example.srctreasuredetect.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mchange.util.Base64Encoder;
import okhttp3.*;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class fofaSearchService {
    private static String email="damit5@protonmail.com";
    private static String apikey="aa74e3e72d77ca7850f897e182491fde";
    private String url = "https://fofa.info/api/v1/search/all?fields=";

    public static String json= "domain=\"\"";
    private int size = 20;


    /*
    该字段用于控制fofa API搜索返回结果的字段值有哪些
    fid为指纹、方便后续攻击统一框架网站,由于账号权限问题，fid信息被删除，后续添加可以直接在fields加入fid，在datavo中取消注视
    */
    private String fields= "host,status_code,protocol,server,ip,port,os,title,icp";

    //取result字段、并将result进行分行处理
    public void dataAnalyse(String result) throws IOException {

        dataAnalasyService dataAnalasyService = new dataAnalasyService();
        JSONObject resultJson = JSONObject.parseObject(result);
        System.out.println(resultJson.getClass().getName());
        JSONArray resultArray =  resultJson.getJSONArray("results");
//        System.out.println(resultArray.toString());

//        dataAnalasyService.excelStrategy(resultArray);

        dataAnalasyService.fofaexcelStrategy(resultArray);







    }


    public   static String getFileName(){
        String filename="";

        Pattern pattern = Pattern.compile("[^=\"]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(fofaSearchService.json);
        matcher.find();
        filename=filename+matcher.group(0)+".xlsx";
        return filename;

    }

    //使用fofa进行搜索
    public void fofaSearch() throws IOException {

//        json.getBytes("utf-8")
        String result="";
        //控制单页搜索数据量
        url=url+fields+"&email="+email+"&key="+apikey+"&qbase64="
                + Base64.getEncoder().encodeToString(json.getBytes("utf-8"))
                +"&page=1&size="+size;

        System.out.println("请求URL----->   "+url+'\n');


        //创建httpclinet进行请求获取搜索结果
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS).build();//设置读取超时时间;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),json);
        Request request = new Request.Builder().url(url).get().build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()){
            System.out.println("Fofa API请求错误");
        }
        result=response.body().string();
        System.out.println(result);
        response.close();

        dataAnalyse(result);

        /*
        判断数据类型
         System.out.println(result.getClass().getName().toString());
         */
    }



    public static void main(String[] args) throws IOException {
        fofaSearchService fofaSearchService = new fofaSearchService();
        fofaSearchService.fofaSearch();

    }
}
