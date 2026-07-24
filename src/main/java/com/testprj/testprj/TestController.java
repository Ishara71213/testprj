package com.testprj.testprj;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TestController {

    @GetMapping("/test")
    public String getMethodName(@RequestParam String param) {
        return "Hello World"+param;
    }
    
    @GetMapping("/test2")
    public String getMethod2Name() {
        return "Hello World";
    }
}
