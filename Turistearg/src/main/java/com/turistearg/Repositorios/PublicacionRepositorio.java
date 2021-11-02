package com.turistearg.Repositorios;

import com.turistearg.Entidades.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepositorio extends JpaRepository <Publicacion, String> {
    
}
