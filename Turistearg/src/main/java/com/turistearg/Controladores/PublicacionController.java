
package com.turistearg.Controladores;

import com.turistearg.Entidades.Publicacion;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.PublicacionRepositorio;
import com.turistearg.Servicios.PublicacionServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/publicacion")
public class PublicacionController {
    
    
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @GetMapping("/publicaciones")
    public String publicaciones(ModelMap model, @RequestParam("id") String idCategoria) throws ErrorServicio {

        try {
            List<Publicacion> publicaciones = publicacionServicio.buscarPublicacionesPorCategoria(idCategoria);

            model.put("publicaciones", publicaciones);

            model.put("idCategoria", idCategoria);

        } catch (ErrorServicio e) {
            throw new ErrorServicio(e.getMessage());

        }

        return "publicaciones";

    }
    
}
