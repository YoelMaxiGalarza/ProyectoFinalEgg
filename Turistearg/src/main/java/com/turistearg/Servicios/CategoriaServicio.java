package com.turistearg.Servicios;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.CategoriaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServicio {
    
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    
    
    
    public Categoria buscarCategoria(String id) throws ErrorServicio {

        Optional<Categoria> respuesta = categoriaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No se encontro la categoria solicitada");
        }

    }
    
	public List<Categoria> buscarCategorias() throws ErrorServicio {
        
        List<Categoria> categorias = categoriaRepositorio.findAll();
        
        return categorias;
        
    }
    
    public List<Categoria> buscarCategoriasPorLugar(String id) throws ErrorServicio {

        if (id == null) {
            throw new ErrorServicio("La id no puede ser nula");
        }
        

        List<Categoria> categorias = categoriaRepositorio.buscarCategoriasPorLugar(id);
        
        if (categorias == null) {
            throw new ErrorServicio("No se encotraron las categorias solicitadas");
        }

        return categorias;

    }
    
    
}
