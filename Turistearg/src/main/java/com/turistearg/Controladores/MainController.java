
package com.turistearg.Controladores;

import com.turistearg.Entidades.ConfirmacionToken;
import com.turistearg.Entidades.Lugar;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.LugarRepositorio;
import com.turistearg.Repositorios.RepositorioToken;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Servicios.UsuarioServicio;


@Controller
public class MainController {        
   
	@Autowired
   private UsuarioServicio usuarioServicio;
	
	@Autowired
	private LugarRepositorio lugarRepositorio;
   
	@Autowired
	private RepositorioToken repositorioToken;
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
        
    @GetMapping("/recuperar-contraseña")
    public String recupass(){
        return "recupass";
    }
    
    @PostMapping("/recuperar-contraseña")
    public String recuperarContraseña(ModelMap modelo, @RequestParam("correo") String mail) throws ErrorServicio{
    	if(mail == null || mail.trim().isEmpty()) {
    		throw new ErrorServicio("El mail no puede estar vacio o ser nulo");
    	}
    	try {
    		usuarioServicio.envioToken(mail);
 
    	} catch(ErrorServicio e) {
    		modelo.put("error", e.getMessage());
    	}
    	return "recupass";
    	
    }
    
    @RequestMapping(value="/confirmar_cambio_contraseña", method= {RequestMethod.GET, RequestMethod.POST})
    public String validateResetToken(ModelMap model, @RequestParam String tokenDeConfirmacion) throws ErrorServicio {

        // hacer el service de token
        ConfirmacionToken token = repositorioToken.buscarPorToken(tokenDeConfirmacion);

        if (token != null) {

            Usuario usuario = usuarioServicio.buscarPorMail(token.getUsuario().getMail());
            model.put("usuario", usuario);
            
            
        } else {
            model.put("mensaje", "The link is invalid or broken!");
            return "error";
        }

        return "resetPassword";
    }
    
    @PostMapping("/cambio_contraseña")
    public String cambioContraseña(@RequestParam("password") String clave1, @RequestParam("password2") String clave2, ModelMap modelo,@RequestParam("correo") String mail) throws ErrorServicio {
    	
    	try {
    	usuarioServicio.cambiarContraseña(clave1, clave2, mail);
    	} catch(ErrorServicio e) {
    		modelo.put("error", e.getMessage());
    	}
    	return "login";//PONER FLASH EN EL LOGIN PARA ENVIAR MENSAJE
    }

  }
    


