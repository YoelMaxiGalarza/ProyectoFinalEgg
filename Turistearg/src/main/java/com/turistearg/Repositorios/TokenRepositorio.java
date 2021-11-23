package com.turistearg.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.turistearg.Entidades.ConfirmacionToken;


public interface TokenRepositorio extends JpaRepository <ConfirmacionToken, String>{
	
	@Query ("SELECT c FROM ConfirmacionToken c WHERE c.token = :token")
    public ConfirmacionToken buscarPorToken(@Param("token")String token);
	
	@Transactional
	@Modifying
	@Query ("DELETE FROM ConfirmacionToken c WHERE c.token = :token")
	public int limpiarToken(@Param("token") String token);
	
	@Transactional
	@Modifying
	@Query ("DELETE FROM ConfirmacionToken c WHERE c.usuario.id = :usuarioId")
	public int limpiarTokensPorUsuario(@Param("usuarioId") String usuarioId);
}