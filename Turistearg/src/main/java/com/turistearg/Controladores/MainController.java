
package com.turistearg.Controladores;

import com.turistearg.Entidades.ConfirmacionToken;
import com.turistearg.Entidades.Lugar;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.LugarRepositorio;
import com.turistearg.Servicios.TokenServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Servicios.UsuarioServicio;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController extends BaseController{

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private LugarRepositorio lugarRepositorio;
        
        @Autowired
        private TokenServicio tokenServicio;

	@GetMapping("/")
	public String index(ModelMap modelo) {
		List<Lugar> lugares = lugarRepositorio.findAll();
		modelo.put("lugares", lugares);
		return "index";
	}

	// TODO: mover endpoints de login y registro a UsuarioController?
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
			ModelMap modelo) {
		if (error != null) {
			modelo.put("error", "usuario o clave incorrectos.");
		}
		if (logout != null) {
			modelo.put("logout", "Ha salido correctamente de la plataforma");
		}
		return "login";
	}

	@GetMapping("/registro")
	public String registro() {
		return "registro2";
	}

	@PostMapping("/registrar")
	public String registrar(ModelMap model, HttpServletRequest httpRequest, @RequestParam MultipartFile foto,
			@RequestParam("usuario") String nombreDeUsuario, @RequestParam("correo") String mail,
			@RequestParam("password") String clave1, @RequestParam("password2") String clave2) throws ErrorServicio, MalformedURLException {
		try {

			usuarioServicio.registrar(foto, nombreDeUsuario, mail, clave1, clave2);
                        String urlBase = this.getURLBase(httpRequest);
                        Usuario registrado = usuarioServicio.buscarPorMail(mail);
                        ConfirmacionToken token = tokenServicio.generarToken(registrado);
                        usuarioServicio.envioTokenAutentificacion(mail, urlBase, token.getToken());
		} catch (ErrorServicio e) {

			model.put("error", e.getMessage());
			model.put("nombreDeUsuario", nombreDeUsuario);
			model.put("mail", mail);
			model.put("clave1", clave1);
			model.put("clave2", clave2);

			return "registro2";
		} catch (MalformedURLException e) {
                        model.put("error", e.getMessage());
                        return "error";
            }

		return "redirect:/"; // Devolver vista o mensaje que el mail para completar el registro fue enviado
	}
        
        @RequestMapping(value = "/usuario/confirmar_registro", method = {RequestMethod.GET, RequestMethod.POST})
        public String confirmacionRegistro(ModelMap model, @RequestParam String tokenDeConfirmacion) throws ErrorServicio {
        
        ConfirmacionToken token = tokenServicio.buscarPorToken(tokenDeConfirmacion);
        
        if (token != null) {
            Usuario usuario = usuarioServicio.buscarPorMail(token.getUsuario().getMail());
            usuarioServicio.habilitar(usuario.getId());
            tokenServicio.limpiarToken(tokenDeConfirmacion);
            model.put("mensaje", "Registro completado!");           
        } else {
            model.put("error", "El link es inavalido o esta roto");
        }
        return "redirect:/";
    }
}
