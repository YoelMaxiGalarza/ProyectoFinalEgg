package com.turistearg.Controladores;

import com.turistearg.Entidades.Publicacion;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
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
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    PublicacionServicio publicacionServicio;

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
    public String actualizarperfil(ModelMap model, HttpSession session,
            @RequestParam String id, @RequestParam String nombreDeUsuario,
            @RequestParam String clave1, @RequestParam String clave2,
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
            model.put("usuario", usuario);
        }
        return "perfil";

    }
}
