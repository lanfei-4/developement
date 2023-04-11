package com.example.srctreasuredetect.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//利用hunter搜索引擎直接搜索、但是由于hunter限制，导致搜索频繁就不能在进行搜索了，故可能需要huntervip账号
public class hunterSearchService {
    private static String username="13030482648";
    private static String api_key="b204afdb2c0edcfdd5c37311c66c31ee365ca247336fca98d34e873bdd19484f";
    private String url = "https://hunter.qianxin.com/openApi/search?";

    private static  String json= "domain.suffix=\"knownsec.com\"";
    private int page_size = 10;


    public void dataAnalyse(String result) throws FileNotFoundException {

        dataAnalasyService dataAnalasyService = new dataAnalasyService();
        JSONObject resultjson = JSONObject.parseObject(result);
        System.out.println("请求数据"+resultjson.toString());
        JSONArray resultArray = resultjson.getJSONObject("data").getJSONArray("arr");
        dataAnalasyService.hunterexcelStrategy(resultArray);


    }

    //获取文件名
    public   static String getFileName(){
        String filename="";

        Pattern pattern = Pattern.compile("[\"]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(hunterSearchService.json);
        matcher.find();
        filename=filename+matcher.group(0)+".xlsx";
        return filename;

    }

    public void hunterSearch() throws IOException {

        String result="";
        //第一次请求获取此次搜集资产的总数，便于后续分页请求和写入
        json= Base64.getEncoder().encodeToString(json.getBytes("utf-8"));
        System.out.println(json);
        url=url+"username="+username+"&api-key="+api_key+"&search="+json+"&page=3"+"&page_size="+page_size;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Response response = okHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            System.out.println("请求出错");
        }
        result=response.body().string();
        JSONObject resultjson = JSONObject.parseObject(result);

        //获取请求数量
        int total = (Integer) resultjson.getJSONObject("data").get("total");
        int page= (int) Math.ceil((double) total/page_size);

        //第二次按页数进行请求交给dataanalyssis进行数据处理
        for(int i=1;i<=page;i++){
            url=url+"username="+username+"&api-key="+api_key+"&search="+json+"&page="+i+"&page_size="+page_size;

            OkHttpClient okHttpClient2 = new OkHttpClient();
            Request request2 = new Request.Builder().url(url).get().build();
            Response response2 = okHttpClient.newCall(request).execute();
            if(!response2.isSuccessful()){
                System.out.println("请求出错");
            }
            result=response2.body().string();
            JSONObject resultjson2 = JSONObject.parseObject(result);
            dataAnalyse(result);
            break;
        }
//        System.out.println(result);

//        System.out.println(result);




    }
    public static void main(String[] args) throws IOException {
            hunterSearchService hunterSearchService = new hunterSearchService();
            hunterSearchService.hunterSearch();
    }
}
