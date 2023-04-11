package com.example.srctreasuredetect.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    public String username;
    public String password;
    @ResponseBody
    @RequestMapping(value="/login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public void  Login(String username ,String password){

    }
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public  void TestPage(){
        System.out.println("SRC资产探测项目已启动");
    }
}
