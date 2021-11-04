
package com.turistearg.Repositorios;

import com.turistearg.Entidades.Categoria;
import com.turistearg.Enumeraciones.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepositorio extends JpaRepository <Categoria, String> {
    
}
