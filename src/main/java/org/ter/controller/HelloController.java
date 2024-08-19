package org.ter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {
    @GetMapping("/word")
    public String word(){
        return "你好!";
    }
    @GetMapping("/getTest")
    public void getTest() throws Exception{
        System.out.println("接受到请求..........");
        TimeUnit.HOURS.sleep(1);
    }
}
