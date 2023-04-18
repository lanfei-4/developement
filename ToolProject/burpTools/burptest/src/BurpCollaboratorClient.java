import burp.IBurpCollaboratorClientContext;
import burp.IBurpCollaboratorInteraction;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import dnslogPlatForm.IBackend;

import java.io.PrintWriter;
import java.util.List;

public class BurpCollaboratorClient implements IBackend {
    private String domain;
    private IBurpCollaboratorClientContext dnslogContext;
    private IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderror;
    private String dnsName;


    private IBurpExtenderCallbacks callbacks;


    public BurpCollaboratorClient(IBurpExtenderCallbacks burpExtenderCallbacks){
        this.callbacks = burpExtenderCallbacks;
        this.dnsName = "BurpDnslogPlatForm";
        this.stdout = new PrintWriter(this.callbacks.getStdout());
        this.stderror = new PrintWriter(this.callbacks.getStdout());
        this.stdout.println("wo shi shei");
    }


    @Override
    public String getPlatform() {
        return this.dnsName;
    }

    @Override
    public String generatePayload() {
        stderror.println("in genpayload");

        helpers=this.callbacks.getHelpers();
        dnslogContext = this.callbacks.createBurpCollaboratorClientContext();
        this.domain=this.dnslogContext.generatePayload(true);
        stdout.println("dwadawdaw:"+this.domain);

        return this.domain;
    }

    @Override
    public boolean checkResult() {
      //写一个burp插件检测SSRF漏洞

        helpers=this.callbacks.getHelpers();
        stdout.println("qaz:"+this.domain);
        String prefix = this.domain.split("\\.")[0];
        //捕捉回连结果
        List<IBurpCollaboratorInteraction> res =  this.dnslogContext.fetchCollaboratorInteractionsFor(prefix);
        for(IBurpCollaboratorInteraction val:res){
            if(this.helpers.bytesToString(this.helpers.base64Decode(val.getProperty("raw_query"))).contains(prefix)){
                return true;
            }


        }
        return false;
    }
}
