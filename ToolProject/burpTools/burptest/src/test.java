

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;


public class test {
    private static boolean isjson(String str) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void main(String[] args) {

        System.out.println(isjson("{\"actionType\":64,\"objectType\":26,\"objectId\":\"a3090b211ad74e37a7c950b17cd04a81\",\"channelType\":5}"));
    }
}

