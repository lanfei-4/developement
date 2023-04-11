package com.example.srctreasuredetect.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

public class hunterDataExcelVo {
    @ExcelProperty(value = "域名",index = 0)//设置表头名
    @ColumnWidth(20)//设置行宽
    private String domain;

    @ExcelProperty(value = "网站状态码", index = 1)
    @ColumnWidth(20)
    private String status;

    @ExcelProperty(value = "服务名称",index = 2)
    @ColumnWidth(20)
    private String protocal;

    @ExcelProperty(value = "应用名称",index = 3)
    @ColumnWidth(30)
    private String applicaiton;

    @ExcelProperty(value = "IP地址",index = 4)
    @ColumnWidth(20)
    private String ip;

    @ExcelProperty(value = "端口号",index = 5)
    @ColumnWidth(10)
    private String  port;

    @ExcelProperty(value = "中间件",index = 6)
    @ColumnWidth(30)
    private String server;

    @ExcelProperty(value = "网页标题",index = 7)
    @ColumnWidth(30)
    private String title;



    @ExcelProperty(value = "备案号",index = 8)
    @ColumnWidth(25)
    private String icp;

    @ExcelProperty(value = "网站指纹",index = 9)
    @ColumnWidth(15)
    private String banner;


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getApplicaiton() {
        return applicaiton;
    }

    public void setApplicaiton(String applicaiton) {
        this.applicaiton = applicaiton;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

}
