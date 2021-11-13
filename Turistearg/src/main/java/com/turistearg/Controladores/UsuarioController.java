
package com.turistearg.Controladores;

import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;






@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    UsuarioServicio usuarioServicio;
        
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
        
        @PreAuthorize(("hasAnyRole('ROLE_USUARIO_REGISTRADO')"))
        @PostMapping("/actualizar-perfil")
        public String actualizarperfil(ModelMap model,HttpSession session,@RequestParam String id, @RequestParam String nombreDeUsuario,
            @RequestParam String apellido,
            @RequestParam MultipartFile foto,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam String mail)
            {
            
            Usuario usuario = null;
             try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            
            usuario = usuarioServicio.buscarPorId(id);
            usuarioServicio.modificar(foto, id, nombreDeUsuario, mail, clave1, clave2);
            session.setAttribute("usuariosession", usuario);
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            
            
            model.put("error", ex.getMessage());
            model.put("usuario", usuario);
        }
        return "perfil";

        } 
       }
