import burp.IBurpExtenderCallbacks;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

public class Tags implements ITab {
    private IBurpExtenderCallbacks callbacks;


    public configGuI getConfigGuI() {
        return configGuI;
    }

    public scanGUI getScanGUI() {
        return scanGUI;
    }

    private configGuI configGuI;

    private scanGUI scanGUI;

    private final JTabbedPane tabs;

    private String tagName;
    public Tags(IBurpExtenderCallbacks callbacks,String name){
        this.callbacks=callbacks;
        this.tagName=name;
        this.tabs= new JTabbedPane();

        this.configGuI = new configGuI(this.callbacks,this.tabs);
        this.scanGUI = new scanGUI(this.callbacks,this.tabs);

        this.callbacks.customizeUiComponent(this.tabs);
        this.callbacks.addSuiteTab(this);


    }


    @Override
    public String getTabCaption() {
        return this.tagName;
    }

    @Override
    public Component getUiComponent() {
        return this.tabs;
    }

    public scanGUI QueueTagFuntion() {
        return this.scanGUI;
    }
    public configGuI configGuI(){
        return this.configGuI;
    }
}
