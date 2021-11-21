package com.turistearg.Servicios;

import com.turistearg.Entidades.Lugar;
import com.turistearg.Excepciones.ErrorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.turistearg.Repositorios.LugarRepositorio;
import java.util.List;
import java.util.Optional;

@Service
public class LugarServicio {

	@Autowired
	private LugarRepositorio lugarRepositorio;

	public Lugar buscarLugar(String id) throws ErrorServicio {

		Optional<Lugar> respuesta = lugarRepositorio.findById(id);

		if (respuesta.isPresent()) {
			return respuesta.get();
		} else {
			throw new ErrorServicio("No se encontro el lugar solicitado");
		}
	}

	public List<Lugar> buscarLugares() {

		List<Lugar> lugares = lugarRepositorio.findAll();

		return lugares;
	}

}
