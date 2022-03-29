package com.penek.shortUrl.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ViewController {

    @GetMapping("")
    public String goHome()
    {
        log.info("call home");
        return "content/home";
    }

    @GetMapping("dashboard/{}")
    public String dashboard(){
        return "content/dashboard";
    }

}
