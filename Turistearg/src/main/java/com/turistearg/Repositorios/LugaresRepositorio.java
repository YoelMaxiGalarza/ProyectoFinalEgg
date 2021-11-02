package com.turistearg.Repositorios;

import com.turistearg.Enumeraciones.Lugares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LugaresRepositorio extends JpaRepository <Lugares, String> {
    
}
