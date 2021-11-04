
package com.turistearg.Controladores;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class MainController {        
   
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/login")
        public String login(@RequestParam(required = false) String error,@RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o clave incorrectos.");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }       
//    /* @PostMapping(/regitrar)
//     public String registrar(String nombreDeUsuario, String mail, String clave);
//        usuarioServicio.registrar(null, nombreDeUsuario, mail, clave);
//        return "registro.html";*/
        
    @GetMapping("/recupass")
    public String recupass(){
        return "recupass.html";
    }
    
  }
    


