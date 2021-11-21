
package com.turistearg.Repositorios;

import com.turistearg.Entidades.Publicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepositorio extends JpaRepository <Publicacion, String> {
    
    @Query ("SELECT c From Publicacion c Where c.categoria.id = :id")
    public List <Publicacion> buscarPublicacionesPorCategoria(@Param("id")String id);
                  
    @Query ("SELECT c From Publicacion c Where c.usuario.id = :id ORDER BY c.fechaPublicacion DESC"
    		+ "")
    public List <Publicacion> buscarPublicacionesPorUsuario(@Param("id")String id);
    
}
