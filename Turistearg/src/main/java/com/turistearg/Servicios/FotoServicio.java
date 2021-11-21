package com.turistearg.Servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Entidades.Foto;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.FotoRepositorio;

@Service
public class FotoServicio {
	@Autowired
	private FotoRepositorio fotoRepositorio;

	@Transactional
	public Foto guardar(MultipartFile archivo) throws ErrorServicio {

		if (archivo != null) {

			try {
				Foto foto = new Foto();

				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());

				return fotoRepositorio.save(foto);

			} catch (Exception e) {
				System.err.println(e.getMessage());
				throw new ErrorServicio(e.getMessage(), e);
			}
		}

		return null;
	}

	@Transactional
	public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {

		if (archivo != null) {

			try {
				Foto foto = new Foto();

				if (idFoto != null) {

					Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);

					if (respuesta.isPresent()) {
						foto = respuesta.get();
					}
				}

				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());

				return fotoRepositorio.save(foto);

			} catch (Exception e) {
				System.err.println(e.getMessage());
				throw new ErrorServicio(e.getMessage(), e);
			}
		}

		return null;
	}

}
