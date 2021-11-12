
package com.turistearg.Controladores;


import com.turistearg.Entidades.Usuario;
import com.turistearg.Servicios.UsuarioServicio;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
     @GetMapping("/usuario")
        public String usuario(){
        return "usuario";
    }
        @Autowired
    private UsuarioServicio usuarioServicio;
        
       @GetMapping("/perfil")
        public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model){
          Usuario login = (Usuario) session.getAttribute("usuariosession");
          
            if (login == null || !login.getId().equals(id)) {
            return "redirect:/index";
        }
            try {
          Usuario usuario = usuarioServicio.buscarPorId(id);
           
            } 
              catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
            
        return "perfil";
    }
        
        
        
        
        
        @GetMapping("/editar-perfil")
        public String editarPerfil(){
        return "editarPerfil";    
        }

    
        @GetMapping("/cambiarfoto")
        public String cambiarfoto(){
        return "cambiarfoto";
        }
  }
