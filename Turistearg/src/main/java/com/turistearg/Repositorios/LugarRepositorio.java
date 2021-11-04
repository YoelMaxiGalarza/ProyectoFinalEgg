package com.turistearg.Repositorios;

import com.turistearg.Entidades.Lugar;
import com.turistearg.Enumeraciones.Lugares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LugarRepositorio extends JpaRepository <Lugares, String> {
  
}
