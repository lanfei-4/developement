package dnslogPlatForm;

import burp.*;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Random;


public class dnslog implements IBackend{
    private IBurpExtenderCallbacks burpExtenderCallbacks;
    private IExtensionHelpers helpers;
    private String domain;

    private PrintWriter stdout;

    private PrintWriter stderror;

    private String dnsName;

    public  dnslog(IBurpExtenderCallbacks callbacks){
        this.burpExtenderCallbacks = callbacks;
        this.helpers = this.burpExtenderCallbacks.getHelpers();
        this.dnsName = "DnslogPlatForm";
        this.stdout = new PrintWriter(this.burpExtenderCallbacks.getStdout());
        this.stderror = new PrintWriter(this.burpExtenderCallbacks.getStderr());
    }

    @Override
    public String getPlatform() {
        return this.dnsName;
    }

    @Override
    public String generatePayload() {
        this.stderror.println("in dnslog");
        //获取dnslog domain
        String url = "http://www.dnslog.cn/getdomain.php?t=0"+ new Random().nextLong();
        System.out.println(url);
        //构建请求
        try {
            System.out.println("123");
            byte[] rawrequest = this.helpers.buildHttpRequest(new URL(url));

            IHttpService httpService = this.helpers.buildHttpService("www.dnslog.cn",80,"HTTP");

            IHttpRequestResponse iHttpRequestResponse = this.burpExtenderCallbacks.makeHttpRequest(httpService,rawrequest);

            byte[] rawresponse = iHttpRequestResponse.getResponse();
            IResponseInfo iResponseInfo = this.helpers.analyzeResponse(rawresponse);
            System.out.println(iResponseInfo.getStatusCode());
            //获取响应体偏移值
            int offset = iResponseInfo.getBodyOffset();
            domain = rawresponse.toString().substring(offset);
            System.out.println(domain);


        }catch (Exception e){
            this.stdout.println("dnslog平台地址获取失败");
        }


        return domain;

    }

    @Override
    public boolean checkResult() {
        String url = "http://www.dnslog.cn/getrecords.php?t"+new Random().nextLong();
        try {
            byte[] rawrequest = this.helpers.buildHttpRequest(new URL(url));
            IHttpService httpService = this.helpers.buildHttpService("www.dnslog.cn",80,"HTTP");
            IHttpRequestResponse requestResponse = this.burpExtenderCallbacks.makeHttpRequest(httpService,rawrequest);

            byte[] response = requestResponse.getResponse();
            int offset = this.helpers.analyzeResponse(response).getBodyOffset();
            String body = response.toString().substring(offset);

            if(body.contains(this.domain)){
                return true;
            }

        }catch (Exception e){
            this.stdout.println("请求出错");
        }
        return false;
    }


}
