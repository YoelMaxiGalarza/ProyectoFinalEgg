package com.turistearg.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turistearg.Entidades.Lugar;


@Repository
public interface LugarRepositorio extends JpaRepository <Lugar, String> {
  
}
