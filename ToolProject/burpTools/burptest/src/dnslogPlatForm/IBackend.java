package dnslogPlatForm;


//定义接口实现dnslog规范
public interface IBackend {



    String getPlatform();

    String generatePayload();

    boolean checkResult();




}
