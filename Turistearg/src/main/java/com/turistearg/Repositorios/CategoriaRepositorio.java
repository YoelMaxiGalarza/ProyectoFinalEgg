
package com.turistearg.Repositorios;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Enumeraciones.Categorias;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepositorio extends JpaRepository <Categoria, String> {
    
    //Categorias : recibe la id de un lugar y devuelve las categorias.
    @Query ("SELECT c From Categoria c Where c.lugar.id = :id" )
    public List<Categoria> buscarCategoriasPorLugar (@Param("id")String id);
                                                                  
    
}
