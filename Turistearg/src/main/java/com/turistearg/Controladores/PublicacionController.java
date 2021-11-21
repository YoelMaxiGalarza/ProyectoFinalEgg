package com.turistearg.Controladores;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Entidades.Publicacion;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.PublicacionRepositorio;
import com.turistearg.Servicios.CategoriaServicio;
import com.turistearg.Servicios.PublicacionServicio;
import com.turistearg.Servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/publicacion")
public class PublicacionController {

	@Autowired
	PublicacionServicio publicacionServicio;

	@Autowired
	UsuarioServicio usuarioServicio;

	@Autowired
	CategoriaServicio categoriaServicio;

	@GetMapping("/publicaciones")
	public String publicaciones(ModelMap model, @RequestParam("id") String idCategoria) throws ErrorServicio {
		try {
			List<Publicacion> publicaciones = publicacionServicio.buscarPublicacionesPorCategoria(idCategoria);

			model.put("publicaciones", publicaciones);
			model.put("idCategoria", idCategoria);

		} catch (ErrorServicio e) {
			model.put("error", e.getMessage());
			return "error";
		}

		return "publicaciones";
	}

	@PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
	@GetMapping("/crear") // CORREGIR URL PARA PASAR idUsuario DESDE PERFIL
	public String displayCrearPublicar(ModelMap model, HttpSession session, @RequestParam String idUsuario) {
		try {
			// Obtener usuario
			Usuario login = (Usuario) session.getAttribute("usuariosession");
			if (login == null || !login.getId().equals(idUsuario)) {
				return "redirect:/";
			}
			model.put("idUsuario", idUsuario);

			// Obtener categorias
			List<Categoria> categorias = categoriaServicio.buscarCategorias();
			model.put("categorias", categorias);
		} catch (Exception e) {
			model.put("error", e.getMessage());
			return "error";
		}

		return "publicar";
	}

	@PostMapping("/publicar")
	public String publicar(ModelMap model, @RequestParam String idUsuario, @RequestParam String idCategoria,
			@RequestParam(required = false) String descripcion, @RequestParam MultipartFile foto) {

		try {
			publicacionServicio.crear(foto, idUsuario, descripcion, idCategoria);
		} catch (ErrorServicio e) {
			model.put("error", e.getMessage());
			return "redirect:/publicacion/crear";
		}
		catch(Exception e) {
			model.put("error", e.getMessage());
			return "error";	
		}

		return "redirect:/publicacion/publicaciones?id=" + idCategoria;
	}
	
	@PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
	@GetMapping("/categoria/crear")
	public String displayCrearPublicacionCategoria(ModelMap model, HttpSession session, @RequestParam String idUsuario, 
			 @RequestParam String idCategoria) {
		try {
			// Obtener usuario
			Usuario login = (Usuario) session.getAttribute("usuariosession");
			if (login == null || !login.getId().equals(idUsuario)) {
				return "redirect:/";
			}
			model.put("idUsuario", idUsuario);

			// Obtener categoria
			Categoria categoria = categoriaServicio.buscarCategoria(idCategoria);
			model.put("idCategoria", categoria.getId());
			
		} catch (Exception e) {
			model.put("error", e.getMessage());
			return "error";
		}

		return "crearPublicacionCategoria";
	}

}
