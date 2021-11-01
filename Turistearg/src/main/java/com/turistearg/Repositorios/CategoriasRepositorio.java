
package com.turistearg.Repositorios;

import com.turistearg.Enumeraciones.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasRepositorio extends JpaRepository <Categorias, String> {
    
}
