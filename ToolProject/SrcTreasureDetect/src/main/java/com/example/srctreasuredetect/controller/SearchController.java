package com.example.srctreasuredetect.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class SearchController {
    @ResponseBody
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public void search(){

    }
    public void quakeSearch(){
        String apikey="2a9dee2e-34f9-4f4b-a401-22e77c88b706";



    }
    public void fofaSearch(){

    }
    public void hunterSearch(){

    }
}
