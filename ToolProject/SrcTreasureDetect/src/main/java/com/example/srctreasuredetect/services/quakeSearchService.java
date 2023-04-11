package com.example.srctreasuredetect.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class quakeSearchService {


    private static String url="https://quake.360.net/api/v3/search/quake_service";
    private static String apikey="2a9dee2e-34f9-4f4b-a401-22e77c88b706";
    private  static  String include ="[\"service.http.host\",\"ip\" ,\"port\", \"transport\",\"service.name\",\"service.http.title\",\"location.isp\",\"components.product_vendor\"]";

    public static String json="{\"query\": \"jd.com\",\"start\":1,\"size\":10,\"include\":%s}";
   //处理json数据
   public void dataAnalysis(String result) throws FileNotFoundException {


       JSONObject jsonObject = JSONObject.parseObject(result);
       JSONArray jsonArray = jsonObject.getJSONArray("data");
       dataAnalasyService dataAnalasyService = new dataAnalasyService();
       dataAnalasyService.quakeexcelStrateg(jsonArray);


   }

   //获取文件名
    public   static String getFileName(){
        String filename="";

        Pattern pattern = Pattern.compile("[^=\"]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(quakeSearchService.json);
        matcher.find();
        filename=filename+matcher.group(0)+".xlsx";
        return filename;

    }


    public void quakeSearch() throws IOException {

//        List<String> include=new ArrayList<String>("ip","post");
        //构建httpclient对象
        String result="";
        int total;
        int page;
        OkHttpClient httpClient = new OkHttpClient();
        String json= String.format(quakeSearchService.json, include);
        System.out.println(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);
        Request request = new Request.Builder().url(url)
                .header("Content-Type","application/json")
                .header("X-QuakeToken",apikey)
                .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Referer","http://quake.360.net")
                .post(body).build();
        Response response = httpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            System.out.println("请求失败");
        }
        result = response.body().string();
        dataAnalysis(result);
////        System.out.println(result);
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        qua
////        total = (int) jsonObject.getJSONObject("meta").getJSONObject("pagination").get("total");
//
//        System.out.println(jsonObject.toJSONString());
////        page=(int)Math.ceil((double) total/10);
////        for
////        System.out.println(total);
//



    }


    public static void main(String[] args) throws IOException {
        quakeSearchService quakeSearchService = new quakeSearchService();
        quakeSearchService.quakeSearch();
    }
}
