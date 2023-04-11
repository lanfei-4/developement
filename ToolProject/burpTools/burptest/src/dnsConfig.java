//dbslog配置，再次加载插件时就不需要重新设置相关属性
public class dnsConfig {
    private static String url = "url";
    private static String token = "token";

    public static String getDnsLogSetting(String name){
        return BurpExtender.callbacks.loadExtensionSetting(name);
    }

    public static  boolean getSetting(String name){
        String result = BurpExtender.callbacks.loadExtensionSetting(name);
        if(result == null){
            return  false;
        }else{
            return  true;
        }
    }
    public static  void setDnslogSetting(String name ,String value){
        BurpExtender.callbacks.saveExtensionSetting(name,value);
    }

}
