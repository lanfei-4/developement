import burp.IBurpExtenderCallbacks;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class configGuI {


    private JLabel pluginLabel;

    public enum  Backends{
        Digpm,BurpCollaborate,Dnslog,Ceye
    }

    private JTabbedPane tabs;

    private JPanel backendUI;
    //定义一个输入框变量
    private JTextField apiField;
    private JTextField tokenField;
    private IBurpExtenderCallbacks callbacks;

    private JCheckBox enableCheckBox;


    private JComboBox<String> backendSelector;
    private JCheckBox reverseCheckBox;
    private JLabel reverseLabel;
    private JTabbedPane reverseTabs;


    public configGuI(IBurpExtenderCallbacks callbacks, JTabbedPane tabs){

        /*initui2设置
        JPanel setPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        this.initUI(setPanel,gridBagConstraints);
        tabs.addTab("基本设置",setPanel);*/

        this.tabs=tabs;

        this.initUI();
        this.tabs.addTab("DNSLOG平台设置",this.backendUI);

    }

    private void initUI(){


        this.backendUI = new JPanel();
        this.backendUI.setAlignmentX(0.0f);
        this.backendUI.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.backendUI.setLayout(new BoxLayout(this.backendUI, BoxLayout.Y_AXIS));  // 垂直对齐

        this.pluginLabel = new JLabel("插件:     ");

        this.reverseLabel = new JLabel("回连方法:     ");
        this.enableCheckBox = new JCheckBox("启动", true);



        this.pluginLabel.setForeground(new Color(255, 89, 18));
        this.pluginLabel.setFont(new Font("Serif", Font.PLAIN, this.pluginLabel.getFont().getSize() + 2));



        this.reverseLabel.setForeground(new Color(255, 89, 18));
        this.reverseLabel.setFont(new Font("Serif", Font.PLAIN, this.reverseLabel.getFont().getSize() + 2));

        this.backendSelector = new JComboBox<String>(this.getSelectors());
        this.backendSelector.setSelectedIndex(0);
        this.backendSelector.setMaximumSize(this.backendSelector.getPreferredSize());

        this.reverseTabs = new JTabbedPane();
        this.reverseTabs.addTab("Ceye", this.getPlatFormPanel());






        JPanel runPanel = UIUtil.GetXPanel();
        runPanel.add(this.pluginLabel);
        runPanel.add(this.enableCheckBox);



        JPanel reversePanel = UIUtil.GetXPanel();
        reversePanel.add(this.reverseLabel);
        reversePanel.add(this.backendSelector);

        JPanel settingPanel = UIUtil.GetYPanel();
        settingPanel.add(runPanel);
        settingPanel.add(reversePanel);

        JPanel reverseInfoPanel = UIUtil.GetXPanel();
        reverseInfoPanel.add(this.reverseTabs);



        this.backendUI.add(settingPanel);
        this.backendUI.add(reverseInfoPanel);





    }
//    private void  initUI2(JPanel setPanel,GridBagConstraints gridBagConstraints){
//        JLabel apiLabel = new JLabel("dnsurl:    ");
//        gridBagConstraints.insets = new Insets(5,5,5,5);
//        gridBagConstraints.gridx=0;
//        gridBagConstraints.gridy=2;
//        setPanel.add(apiLabel,gridBagConstraints);
//
//        JLabel tokenLabel = new JLabel("apitoken:   ");
//
//        gridBagConstraints.gridx=0;
//        gridBagConstraints.gridy=3;
//        setPanel.add(tokenLabel,gridBagConstraints);
//
//
//
//        this.apiField = new JTextField();
//        this.apiField.setColumns(20);
//        gridBagConstraints.gridwidth = 3;
//        gridBagConstraints.gridx=1;
//        gridBagConstraints.gridy=2;
//        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//        setPanel.add(apiField,gridBagConstraints);
//
//
//
//        this.tokenField = new JTextField();
//        gridBagConstraints.gridwidth = 3;
//        this.tokenField.setColumns(20);
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 3;
//        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//        setPanel.add(tokenField,gridBagConstraints);
//
//
//        JButton jButton = new JButton("apply");
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 4;
//        gridBagConstraints.anchor = gridBagConstraints.WEST;
//        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//        setPanel.add(jButton,gridBagConstraints);
//        jButton.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String dnslogurl = apiField.getText().trim();
//                String dnstoken = tokenField.getText().trim();
//                if(dnslogurl.length()>0 && dnstoken.length()>0){
//                    JOptionPane.showMessageDialog(null,"输入成功");
//                    dnsConfig.setDnslogSetting("url",dnslogurl);
//                    dnsConfig.setDnslogSetting("token",dnstoken);
//
//                }else {
//                    JOptionPane.showMessageDialog(null,"请检查输入是否正确");
//                }
//
//
//            }
//        });
//    if(dnsConfig.getDnsLogSetting("url")!=null && dnsConfig.getDnsLogSetting("token")!=null){
//        loadDnsConfig();
//    }




    //选择dnslog平台
    private String[] getSelectors() {
        ArrayList<String> selectors = new ArrayList<String>();
        for (Backends backend: Backends.values()) {
            selectors.add(backend.name().trim());
        }
        return selectors.toArray(new String[selectors.size()]);
    }

    //设置输入dnslog信息、以及点击button后设置到系统中，下次自动加载之前配置
    private JPanel getPlatFormPanel() {
        JPanel jPanel = UIUtil.GetYPanel();
        JPanel apiPanel = UIUtil.GetXPanel();
        JPanel tokenPanel = UIUtil.GetXPanel();

        apiField = new JTextField("xxxxxx.ceye.io", 50);
        tokenField = new JTextField("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", 50);

        apiField.setMaximumSize(apiField.getPreferredSize());
        tokenField.setMaximumSize(tokenField.getPreferredSize());

        apiPanel.add(new JLabel("Identifier:     "));
        apiPanel.add(apiField);

        tokenPanel.add(new JLabel("API Token:   "));
        tokenPanel.add(tokenField);

        jPanel.add(apiPanel);
        jPanel.add(tokenPanel);


        JButton applyButton = new JButton("apply");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5,0,5,5);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = gridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        jPanel.add(applyButton,gridBagConstraints);
        //添加事件监听器、点击后提示输入信息是否合法
        applyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dnslogurl = apiField.getText().trim();
                String dnstoken = tokenField.getText().trim();
                if(dnslogurl.length()>0 && dnstoken.length()>0){
                    JOptionPane.showMessageDialog(null,"输入成功");
                    dnsConfig.setDnslogSetting("url",dnslogurl);
                    dnsConfig.setDnslogSetting("token",dnstoken);

                }else {
                    JOptionPane.showMessageDialog(null,"请检查输入是否正确");
                }


            }
        });
        //加载配置
        if(dnsConfig.getDnsLogSetting("url")!=null && dnsConfig.getDnsLogSetting("token")!=null) {loadDnsConfig();}



        return jPanel;
    }

    //加载dnslog的相关配置
    public void loadDnsConfig(){
        this.apiField.setText(dnsConfig.getDnsLogSetting("url"));
        this.tokenField.setText(dnsConfig.getDnsLogSetting("token"));
    }

    //获取api值
    public String getApivalue(){
        return this.apiField.getText().trim();
    }

    //获取apitoken值
    public String getTokenvalue(){
        return this.tokenField.getText().trim();
    }

    //插件是否开启
    public boolean isEnable() {
        return this.enableCheckBox.isSelected();
    }
}