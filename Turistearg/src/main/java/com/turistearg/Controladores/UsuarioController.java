package com.turistearg.Controladores;

import com.turistearg.Entidades.ConfirmacionToken;
import com.turistearg.Entidades.Publicacion;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Servicios.PublicacionServicio;
import com.turistearg.Servicios.TokenServicio;
import com.turistearg.Servicios.UsuarioServicio;

import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends BaseController {

	@Autowired
	UsuarioServicio usuarioServicio;

	@Autowired
	private TokenServicio tokenServicio;

	@Autowired
	PublicacionServicio publicacionServicio;

	@PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
	@GetMapping("/perfil")
	public String perfil(HttpSession session, @RequestParam String id, ModelMap model) {
		Usuario login = (Usuario) session.getAttribute("usuariosession");

		if (login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}
		try {
			Usuario usuario = usuarioServicio.buscarPorId(id);
			model.addAttribute("usuario", usuario);

			List<Publicacion> publicaciones = publicacionServicio.buscarPublicacionesPorUsuario(id);
			model.addAttribute("publicaciones", publicaciones);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
		}
		return "perfil";
	}

	@PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
	@GetMapping("/editar-perfil")
	public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");

		if (login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}
		try {
			Usuario usuario = usuarioServicio.buscarPorId(id);
			model.addAttribute("usuario", usuario);

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
		}
		return "editar-perfil";
	}

	@PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
	@PostMapping("/actualizar-perfil")
	public String actualizarperfil(ModelMap model, HttpSession session, @RequestParam String id,
			@RequestParam String nombreDeUsuario, @RequestParam String clave1, @RequestParam String clave2,
			@RequestParam String mail) {

		Usuario usuario = null;
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if (login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}

		try {
			usuario = usuarioServicio.buscarPorId(id);
			usuarioServicio.modificar(id, nombreDeUsuario, mail, clave1, clave2);
			session.setAttribute("usuariosession", usuario);

			return "redirect:/perfil";

		} catch (ErrorServicio ex) {

			model.put("error", ex.getMessage());

			Usuario usuarioActual = (Usuario) session.getAttribute("usuariosession");

			model.put("usuario", usuarioActual); // probar que devuelva el usuario no modificado al dar un tipo de error
		}
		return "perfil";

	}

	@GetMapping("/editar-fotoPerfil")
	public String editarFoto(ModelMap model, HttpSession session, @RequestParam String id) {

		Usuario login = (Usuario) session.getAttribute("usuariosession");

		if (login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}
		try {
			Usuario usuario = usuarioServicio.buscarPorId(id);
			model.addAttribute("usuario", usuario);

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
		}
		return "editar-foto";
	}

	@PostMapping("/actualizar-fotoPerfil")
	public String actualizarFotoPerfil(ModelMap model, HttpSession session, @RequestParam String id,
			@RequestParam MultipartFile fotoPerfil) {

		Usuario login = (Usuario) session.getAttribute("usuariosession");

		if (login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}
		try {
			Usuario usuario = usuarioServicio.buscarPorId(id);
			usuarioServicio.modificarFoto(fotoPerfil, id);
			session.setAttribute("usuariosession", usuario);

			return "redirect:/perfil";

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
		}
		return "perfil";
	}

	@GetMapping("/recuperar-contraseña")
	public String recupass() {
		return "recupass";
	}

	@PostMapping("/recuperar-contraseña")
	public String recuperarContraseña(HttpServletRequest httpRequest, ModelMap modelo,
			@RequestParam(name = "correo", required = true) String mail) throws ErrorServicio {
		try {
			String urlBase = this.getURLBase(httpRequest);
			Usuario usuario = usuarioServicio.buscarPorMail(mail);
			ConfirmacionToken token = tokenServicio.generarToken(usuario);
			usuarioServicio.envioToken(mail, urlBase, token.getToken());
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "error";
		} catch (MalformedURLException e) {
			modelo.put("error", e.getMessage());
			return "error";
		}
		return "recupass";
	}

	@RequestMapping(value = "/confirmar_cambio_contraseña", method = { RequestMethod.GET, RequestMethod.POST })
	public String validateResetToken(ModelMap model, @RequestParam String tokenDeConfirmacion) throws ErrorServicio {
		ConfirmacionToken token = tokenServicio.buscarPorToken(tokenDeConfirmacion);

		if (token != null) {
			model.put("tokenDeConfirmacion", tokenDeConfirmacion);
			model.put("usuario", token.getUsuario().getId());
		} else {
			model.put("error", "The link is invalid or broken!");
			return "error";
		}

		return "resetPassword";
	}

	@PostMapping("/cambio_contraseña")
	public String cambioContraseña(@RequestParam("password") String clave1, @RequestParam("password2") String clave2,
			ModelMap modelo, @RequestParam("correo") String mail, @RequestParam String tokenDeConfirmacion)
			throws ErrorServicio {
		try {
			usuarioServicio.cambiarContraseña(clave1, clave2, mail);
			tokenServicio.limpiarToken(tokenDeConfirmacion);
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "error";
		}
		return "redirect:/login"; // TODO: PONER FLASH EN EL LOGIN PARA ENVIAR MENSAJE
	}
}
