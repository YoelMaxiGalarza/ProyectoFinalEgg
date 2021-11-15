package com.turistearg.Controladores;

import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;



@Controller
@RequestMapping("/foto")
public class FotoController {
    @Autowired
    UsuarioServicio usuarioServicio;
    
    
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotosUsuario(@PathVariable String id) {

        try {

            Usuario usuario = usuarioServicio.buscarPorId(id);

            if (usuario.getFotoPerfil() == null) {
                throw new ErrorServicio("El usuario no tiene una foto asignada");
            }
            
            byte[] foto = usuario.getFotoPerfil().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);      
            
        } catch (ErrorServicio ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }

    
}
