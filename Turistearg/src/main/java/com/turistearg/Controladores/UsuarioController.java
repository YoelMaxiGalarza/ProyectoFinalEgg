
package com.turistearg.Controladores;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
     @GetMapping("/usuario")
        public String usuario(){
        return "usuario";
    }
        @GetMapping("/perfil")
        public String perfil(){
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
