package com.turistearg.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FotoController {
     @GetMapping("/foto")
    public String index(){
        return "index";
    }
}
