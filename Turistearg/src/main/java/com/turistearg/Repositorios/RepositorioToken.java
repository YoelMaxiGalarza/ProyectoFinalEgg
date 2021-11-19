package com.turistearg.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.turistearg.Entidades.ConfirmacionToken;


public interface RepositorioToken extends JpaRepository <ConfirmacionToken, String>{
	
	@Query ("SELECT c FROM ConfirmacionToken c where c.token = :token")
    public ConfirmacionToken buscarPorToken(@Param("token")String token);
	
	/*
	 * @Modifying
	 * 
	 * @Query ("DELETE FROM ConfirmacionToken c WHERE c.usuario.id=?id") public void
	 * eliminarTokenPorUsuario(@Param("id")String id);
	 */
}
