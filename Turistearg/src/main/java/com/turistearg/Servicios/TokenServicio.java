package com.turistearg.Servicios;

import com.turistearg.Entidades.ConfirmacionToken;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.TokenRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServicio {

	@Autowired
	private TokenRepositorio tokenRepositorio;

	public ConfirmacionToken buscarPorToken(String token) throws ErrorServicio {

		try {
			ConfirmacionToken confirmacionToken = tokenRepositorio.buscarPorToken(token);
			if (confirmacionToken != null) {
				return confirmacionToken;
			} else {
				throw new ErrorServicio("No se encontro el token solicitado");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ErrorServicio(e.getMessage(), e);
		}
	}

	@Transactional
	public void limpiarToken(String token) throws ErrorServicio {
		try {
			int tokenEliminados = tokenRepositorio.limpiarToken(token);

			if (tokenEliminados == 0) {
				throw new ErrorServicio("No se encontró el token ingresado.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ErrorServicio(e.getMessage(), e);
		}
	}

	@Transactional
	public ConfirmacionToken generarToken(Usuario usuario) throws ErrorServicio {
		try {
			// Limpiamos cualquier token asociado al usuario
			tokenRepositorio.limpiarTokensPorUsuario(usuario.getId());

			// Creamos un nuevo token
			ConfirmacionToken confirmacionToken = new ConfirmacionToken(usuario);
			tokenRepositorio.save(confirmacionToken);
			
			return confirmacionToken;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ErrorServicio(e.getMessage(), e);
		}
		
	}
}
