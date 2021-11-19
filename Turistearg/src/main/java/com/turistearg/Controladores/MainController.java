
package com.turistearg.Controladores;

import com.turistearg.Entidades.Lugar;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.LugarRepositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Servicios.UsuarioServicio;


@Controller
public class MainController {        
   
	@Autowired
   private UsuarioServicio usuarioServicio;
	@Autowired
	private LugarRepositorio lugarRepositorio;
   
    @GetMapping("/")
    public String index(ModelMap modelo){
    	List<Lugar> lugares = lugarRepositorio.findAll();
    	modelo.put("lugares", lugares);
        return "index2";
    }
    
    @GetMapping("/login")
        public String login(@RequestParam(required = false) String error,@RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o clave incorrectos.");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro2";
    }       
    
    @PostMapping("/registrar")
    public String registrar(ModelMap model, @RequestParam MultipartFile foto,
            @RequestParam("usuario") String nombreDeUsuario, @RequestParam("correo") String mail,
            @RequestParam("password") String clave1, @RequestParam("password2") String clave2) throws ErrorServicio {
        try {
            
            usuarioServicio.registrar(foto, nombreDeUsuario, mail, clave1, clave2);

        } catch (ErrorServicio e) {
            
            model.put("error", e.getMessage());
            model.put("nombreDeUsuario", nombreDeUsuario);
            model.put("mail", mail);
            model.put("clave1", clave1);
            model.put("clave2", clave2);
            
            return "registro2";
        }

        return "index2";
    }
        
    @GetMapping("/recupass")
    public String recupass(){
        return "recupass";
    }
    
  }
    


