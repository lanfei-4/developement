import burp.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class scanGUI  extends AbstractTableModel implements ITab, IMessageEditorController {
    private IBurpExtenderCallbacks callbacks;
    private  String tagName;

    private JTabbedPane tabs;

    private JPanel scanui;

    private JSplitPane mainSplitPane;

    private  JTabbedPane reqPane;

    private JTabbedPane respPane;

    private JSplitPane httpSplitPane;

    private  JScrollPane tablePane;

    private List<scanGUI.TableData> tasks = new ArrayList();  // 适配jdk8语法

    private IMessageEditor messagereq;
    private IMessageEditor messageresp;
    private IHttpRequestResponse currentHttp;

    public scanGUI(IBurpExtenderCallbacks callbacks,JTabbedPane tabs){
        this.callbacks=callbacks;
        this.tabs = tabs;
        this.InitiUI();
        this.tabs.addTab("扫描面板",this.scanui);




    }
    public void InitiUI(){
        //创建扫描的主界面
        this.scanui = new JPanel(new BorderLayout());
        //创建显示流量包的面板垂直分割
        this.mainSplitPane =  new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        //创建具体请求响应数据包显示面板
        this.httpSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //设置http显示信息均分面板
        this.httpSplitPane.setDividerLocation(.5d);
        scanGUI.Table table = new scanGUI.Table(scanGUI.this);
        //把分割的主界面放入到一一个可以滚动的panel中
        this.tablePane = new JScrollPane(table);

        //获取请求响应数据
        this.messagereq = this.callbacks.createMessageEditor(this,false);
        this.messageresp = this.callbacks.createMessageEditor(this,false);




        //设置请求信息面板和响应信息面板
        this.reqPane = new JTabbedPane();
        this.respPane = new JTabbedPane();
        //getComponent方法返回ui组件加入到请求pane中
        this.reqPane.addTab("Request",this.messagereq.getComponent());
        this.respPane.addTab("Response",this.messageresp.getComponent());
        //将请求和响应pane中放入http pan中
        this.httpSplitPane.add(this.reqPane);
        this.httpSplitPane.add(this.respPane);

        this.mainSplitPane.add(this.tablePane);
        this.mainSplitPane.add(this.httpSplitPane);
        this.scanui.add(this.mainSplitPane);

    }


    @Override
    public int getRowCount() {
        return this.tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        scanGUI.TableData data = scanGUI.this.tasks.get(rowIndex);
        switch (columnIndex){
            case 0:return data.id;
            case 2:return data.method;
            case 3:return data.url;
            case 4:return data.status;
            case 5:return data.issue;

        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "id";
            case 2: return "method";
            case 3: return "url";
            case 4: return "status";
            case 5: return "issue";

        }
        return null;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    public  int add(String reuestMethod,String url,int status,String issue,IHttpRequestResponse requestResponse){
        synchronized (this.tasks){
            int id = this.tasks.size();

            this.tasks.add(
                    new TableData(
                            id,
                            reuestMethod,
                            url,
                            status,
                            issue,
                            requestResponse


                    )
            );
            fireTableRowsInserted(id,id);
            return id;
        }
    }


    public int save(int id,String method,String url,int status,String issue, IHttpRequestResponse requestResponse){
        scanGUI.TableData dataEntry = scanGUI.this.tasks.get(id);

        synchronized (this.tasks){
            this.tasks.set(
                    id,
                    new TableData(
                            id,
                            method,
                            url,
                            status,
                            issue,
                            requestResponse
                    )
            );
        }
        fireTableRowsUpdated(id,id);
        return  id;
    }
    @Override
    public IHttpService getHttpService() {
        return this.currentHttp.getHttpService();
    }

    @Override
    public byte[] getRequest() {
        return this.currentHttp.getRequest();
    }

    @Override
    public byte[] getResponse() {
        return this.currentHttp.getResponse();
    }



    @Override
    public String getTabCaption() {
        return  this.tagName;
    }

    @Override
    public Component getUiComponent() {
        return this.mainSplitPane;
    }
    private class Table extends  JTable{
        public  Table(TableModel dm){
            super(dm);
        }

        public void changeSelection(int rowIndex,int columnIndex, boolean toggle,boolean extend){

            scanGUI.TableData data = tasks.get(this.convertRowIndexToModel(rowIndex));
            //构建请求响应信息到messageEditor中，然后调用后续方法，将信息显示到面板中
            scanGUI.this.messagereq.setMessage(data.iHttpRequestResponse.getRequest(),true);
            scanGUI.this.messageresp.setMessage(data.iHttpRequestResponse.getResponse(),false);

            scanGUI.this.currentHttp = data.iHttpRequestResponse;
            super.changeSelection(rowIndex,columnIndex,toggle,extend);
        }
    }

    private static class TableData{
        final int id;
        final String url;

        final String method;
        final int status;
        final String issue;
        final IHttpRequestResponse iHttpRequestResponse;

        public TableData(int id,String url,String method,int status,String issue,IHttpRequestResponse iHttpRequestResponse) {
            this.id = id;
            this.url = url;
            this.method = method;
            this.status = status;
            this.issue = issue;
            this.iHttpRequestResponse=iHttpRequestResponse;


        }




    }
}
