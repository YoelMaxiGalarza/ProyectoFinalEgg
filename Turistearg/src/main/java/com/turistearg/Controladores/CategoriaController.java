package com.turistearg.Controladores;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Entidades.Lugar;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Servicios.CategoriaServicio;
import com.turistearg.Servicios.LugarServicio;
import com.turistearg.Servicios.PublicacionServicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriaController {
    
    
    @Autowired
    private CategoriaServicio categoriaServicio;
    
    @Autowired
    private LugarServicio lugarServicio;
    
    
    @GetMapping("/categorias")
    public String categorias(ModelMap model, String idLugar) throws ErrorServicio {

        try {
            List<Categoria> categorias = categoriaServicio.buscarCategoriasPorLugar(idLugar);
            Lugar lugar = lugarServicio.buscarLugar(idLugar);
            model.put("lugar", lugar);
            model.put("categorias", categorias);
            model.put("id", idLugar);

        } catch (ErrorServicio e) {
            e.getMessage();
        }

        return "categorias";

    }
    
    
}
