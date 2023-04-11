import burp.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//!!!要使用这个文件中的代码，需要先将文件名改为BurpExtender.java
public class BurpExtender implements IBurpExtender,IScannerCheck{

    private BurpExtender burpExtender;
    public static IBurpExtenderCallbacks callbacks;

    private IExtensionHelpers helpers;
    configGuI gui;
    private PrintWriter stdout;
    private PrintWriter stderr;
    private Tags tags;

    //
    // implement IBurpExtender
    // 实现IBurpExtender接口
    //
    public static void main(String[] args) {

    }
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // keep a reference to our callbacks object
        this.callbacks = callbacks;

        // obtain an extension helpers object  获取helpers对象
        this.helpers = callbacks.getHelpers();


        // set our extension name 设置插件名称
        callbacks.setExtensionName("xxeScanner");

        this.stdout = new PrintWriter(this.callbacks.getStdout(), true);
        this.stderr = new PrintWriter(this.callbacks.getStderr(), true);

        callbacks.registerScannerCheck(this);

        // 添加tab
        this.tags = new Tags(callbacks,"xxeScanner");

    }

    public static Boolean isJson(String str){
        try {
            JSONObject jsonStr = JSONObject.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<IScanIssue> checkVul(IHttpRequestResponse httpRequestResponse,List<IScanIssue> issues){
//        stdout.println("xzczxczx");
        BurpCollaboratorClient burpCollaboratorClient = new BurpCollaboratorClient();
        String dnsName = burpCollaboratorClient.getDomain(this.callbacks);
        String payload = "http://" + dnsName;
        IHttpService httpService = httpRequestResponse.getHttpService();
        IBurpCollaboratorClientContext dnslog = callbacks.createBurpCollaboratorClientContext();
        //获取请求信息
        IRequestInfo requestInfo = this.helpers.analyzeRequest(httpRequestResponse);
        List<String> request_header = requestInfo.getHeaders(); // 获取请求头
        String type = String.valueOf(requestInfo.getContentType());
        String firstrequest_header = request_header.get(0); //第一行请求包含请求方法、请求uri、http版本
        String method = requestInfo.getMethod();
        stdout.println("checkvul");

        //丢弃静态资源漏洞扫描
        if(firstrequest_header.contains(".png") || firstrequest_header.contains(".js") || firstrequest_header.contains(".jpg") || firstrequest_header.contains(".jpeg") || firstrequest_header.contains(".svg")  || firstrequest_header.contains(".mp4") || firstrequest_header.contains(".css") || firstrequest_header.contains(".mp3")|| firstrequest_header.contains(".ico") || firstrequest_header.contains(".gif")){
            return null;
        }

        String newParameters="";
        String[] firstheaders = firstrequest_header.split(" ");
        String totalurl = firstheaders[1];
        URL url = requestInfo.getUrl();


        //处理URL中带参数的请求
        if(totalurl.contains("?")){
            String[] parameters = totalurl.split("\\?")[1].split("&");
            for(String para:parameters){
                String key = para.split("=")[0];
                newParameters = newParameters+key+"="+payload+"&";
            }
            firstheaders[1] = totalurl.split("\\?")[0]+"?"+newParameters;
            firstheaders[1]=firstheaders[1].substring(0, firstheaders[1].length()-1);
        }

        String newBody="";
        //处理post数据中的参数
        stdout.println(firstheaders[0]+firstheaders[1]+"!!!!!!!!");
        stdout.println(firstheaders[0].contains("POST"));
        if(firstheaders[0].contains("POST")){

            //利用offset获取请求体
            int offset = requestInfo.getBodyOffset();
            byte[] request = httpRequestResponse.getRequest();
            String request2 = new String(request);
            String body = request2.substring(offset);


//            stdout.println("!!!!"+body);
            stdout.println("post");
            stdout.println(type);
            stdout.println(body);
            stdout.println(body.contains("="));
            stdout.println(body.contains("{"));
            stdout.println(isJson(body));
            //设置请求体都是=号类型的
            if(body.contains("=") && !body.contains("{")){
                stdout.println("正常请求");
                String[] paras = body.split("&");
                for(String para:paras){
                    newBody = newBody + para.split("=")[0]+"="+payload+"&";
                }

                //设置请求题是正常json请求的{"a":"b"}
            } else if (isJson(body)) {
                stdout.println("json请求");
                JSONObject jsonBody =  JSON.parseObject(body);
                //遍历json的key和value
                for(String key:jsonBody.keySet()){
                    jsonBody.put(key,payload);
                }
                newBody=jsonBody.toString();

            }
            stdout.println(newBody+"！！！！！！！！1");
        }
        //构造新header
        request_header.set(0,firstheaders[0]+" "+firstheaders[1]+" "+firstheaders[2]);
        //创建新的请求数据包
        byte[] newRequest = this.helpers.buildHttpMessage(request_header,newBody.getBytes());
        //发送请求
        IHttpRequestResponse iHttpRequestResponseNew = callbacks.makeHttpRequest(httpService,newRequest);
        int status =  this.helpers.analyzeResponse(iHttpRequestResponseNew.getResponse()).getStatusCode();
        stdout.println("函数主题");
        if(burpCollaboratorClient.checkResult(dnsName)){
            stdout.println("进入漏洞检测成功");
            String issue = "SSRF Vul";
            //设置Dashboard扫描Issue显示
            issues.add(new CustomScanIssue(httpService, url, new IHttpRequestResponse[]{iHttpRequestResponseNew}, issue, issue, "High"));
            //设置ui界面流量显示
            this.tags.QueueTagFuntion().add(
                    method,
                    url.toString(),
                    status,
                    issue,
                    iHttpRequestResponseNew


            );
        }else {
            String issue = "SSRF Vul Not Found";
            stdout.println("进入漏洞检测失败");
            this.tags.QueueTagFuntion().add(
                    method,
                    url.toString(),
                    status,
                    issue,
                    iHttpRequestResponseNew


            );
        }




//        byte[] response = httpRequestResponse.getResponse();
//        IHttpService iHttpService = httpRequestResponse.getHttpService();
//        URL url = requestInfo.getUrl();
//        String method = requestInfo.getMethod();
//        String host = requestInfo.getUrl().getHost();
//        List<IParameter> parameterList = requestInfo.getParameters();
//
//        for (IParameter iParameter : parameterList) {
//            //不取cookie中的参数
//            if(iParameter.getType()!=2) {
//                BurpCollaboratorClient burpCollaboratorClient = new BurpCollaboratorClient();
//                String dnsName = burpCollaboratorClient.getDomain(this.callbacks);
//                String key = iParameter.getName();
//                String payload = "http://" + dnsName;
//                //iParameter.getType()用来修改参数修改到起原始位置，否则会设置到body中
//                IParameter iParameterNew = this.helpers.buildParameter(key, payload, iParameter.getType());
//                byte[] newRequest = this.helpers.updateParameter(httpRequestResponse.getRequest(), iParameterNew);
//                IHttpRequestResponse iHttpRequestResponseNew = callbacks.makeHttpRequest(iHttpService, newRequest);
//                byte[] responsenew = iHttpRequestResponseNew.getResponse();
//                IResponseInfo iResponseInfo = this.helpers.analyzeResponse(response);
//                int status = iResponseInfo.getStatusCode();
//                if (burpCollaboratorClient.checkResult(dnsName)) {
//                    String issue = "SSRF Vul";
//                    //设置扫描Issue显示
//                    issues.add(new CustomScanIssue(iHttpService, url, new IHttpRequestResponse[]{iHttpRequestResponseNew}, issue, issue, "High"));
//                    //设置ui界面流量显示
//                    this.tags.QueueTagFuntion().add(
//                            method,
//                            url.toString(),
//                            status,
//                            issue,
//                            iHttpRequestResponseNew
//
//
//                    );
//
//                } else {
//                    String issue = "SSRF Vul Not Found";
//
//                    this.tags.QueueTagFuntion().add(
//                            method,
//                            url.toString(),
//                            status,
//                            issue,
//                            iHttpRequestResponseNew
//
//
//                    );
//
//                }
//            }
//        }




        return issues;
    }

    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse iHttpRequestResponse) {
//        if(dnsConfig.getDnsLogSetting("url")==null && dnsConfig.getDnsLogSetting("token")==null){
//            stdout.println("ininin");
//            return null;
//        }

        if(!this.tags.getConfigGuI().isEnable()){return null;}

        List<IScanIssue> issues = new ArrayList<IScanIssue>();
        this.checkVul(iHttpRequestResponse,issues);
        if(issues.size()>0){
            return issues;
        }else{
            stdout.println("dwa");
            return null;
        }

    }

    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse iHttpRequestResponse, IScannerInsertionPoint iScannerInsertionPoint) {
        return null;
    }

    @Override
    public int consolidateDuplicateIssues(IScanIssue iScanIssue, IScanIssue iScanIssue1) {
        return 0;
    }

    //基于IscanIssue实现一个类用于存储漏洞的详细信息
    class CustomScanIssue implements IScanIssue{
        //用来获取头部信息
        private IHttpService httpService;

        private URL url;

        //用户获取修改请求响应信息的
        private IHttpRequestResponse[] httpRequestResponses;
        private String name;
        private String detail;
        private String serverity;

        public CustomScanIssue(IHttpService httpService,URL url,IHttpRequestResponse[] httpRequestResponses,
                               String name,String detail,String serverity)
        {
            this.httpRequestResponses=httpRequestResponses;
            this.httpService = httpService;
            this.url = url;
            this.detail = detail;
            this.name = name;
            this.serverity = serverity;





        }


        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public String getIssueName() {
            return name;
        }

        @Override
        public int getIssueType() {
            return 0;
        }

        //返回问题严重性级别
        @Override
        public String getSeverity() {
            return serverity;
        }

        //返回威胁等级
        @Override
        public String getConfidence() {
            return "Certain";
        }

        @Override
        public String getIssueBackground() {
            return null;
        }

        @Override
        public String getRemediationBackground() {
            return null;
        }

        @Override
        public String getIssueDetail() {
            return detail;
        }

        @Override
        public String getRemediationDetail() {
            return null;
        }

        @Override
        public IHttpRequestResponse[] getHttpMessages() {
            return httpRequestResponses;
        }

        @Override
        public IHttpService getHttpService() {
            return httpService;
        }
    }
}