package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app1")
public class App1Controller {


    @GetMapping("/test")
    public Object test() {


        return "app1";
    }

    @GetMapping("/xss")
    public Object test2() {

        return "test2";
    }


}
