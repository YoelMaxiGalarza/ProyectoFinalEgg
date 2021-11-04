
package com.turistearg.Controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.turistearg.Servicios.UsuarioServicio;


@Controller
public class MainController {        
   
	@Autowired
   UsuarioServicio usuarioServicio;
   
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
     @PostMapping("/registrar")
     public String registrar(String nombreDeUsuario, String mail, String clave1, String clave2) {
        try {
        	usuarioServicio.registrar(null, nombreDeUsuario, mail, clave1, clave2);
        } catch (Exception e) {
			System.out.println(e.getMessage());
			return "registro.html";
		}
    	 
        return "index.html";
	}
        
    @GetMapping("/recupass")
    public String recupass(){
        return "recupass.html";
    }
    
  }
    


