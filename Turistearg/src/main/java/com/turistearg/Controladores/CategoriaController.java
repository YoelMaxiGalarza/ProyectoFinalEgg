package com.turistearg.Controladores;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.CategoriaRepositorio;
import com.turistearg.Servicios.CategoriaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoriaController {
    
    
    @Autowired
    CategoriaServicio categoriaServicio;
    
    @GetMapping("/categorias")
    public String categorias(ModelMap model, @RequestParam("id") String idLugar) throws ErrorServicio {

        try {
            List<Categoria> categorias = categoriaServicio.buscarCategoriasPorLugar(idLugar);
            model.put("categorias", categorias);
            model.put("id", idLugar);

        } catch (ErrorServicio e) {
            e.getMessage();
        }

        return "categorias";

    }
    
}
