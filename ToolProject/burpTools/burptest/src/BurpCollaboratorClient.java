import burp.IBurpCollaboratorClientContext;
import burp.IBurpCollaboratorInteraction;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;

import java.util.List;

public class BurpCollaboratorClient {
    private String dnsName;
    private IBurpCollaboratorClientContext dnslogContext;
    private IExtensionHelpers helpers;


    //获取Collabora域名
    public String getDomain(IBurpExtenderCallbacks callbacks){
        this.helpers=callbacks.getHelpers();
        this.dnslogContext = callbacks.createBurpCollaboratorClientContext();
        dnsName=this.dnslogContext.generatePayload(true);

        return dnsName;
    }

    public boolean checkResult(String dnsName){

        String prefix = dnsName.split("\\.")[0];
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
