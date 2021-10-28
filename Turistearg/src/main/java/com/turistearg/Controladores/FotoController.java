
package com.turistearg.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FotoController {
     @GetMapping("")
    public String index(){
        return "index";
    }
}
