package com.turistearg.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Entidades.Foto;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoServicio serviciosFoto;

    @Transactional
    public void registrar(MultipartFile archivo, String nombreDeUsuario, String mail, String clave1, String clave2) throws ErrorServicio {

        validar(nombreDeUsuario, mail, clave1, clave2);

        Usuario user = new Usuario();
        user.setNombreDeUsuario(nombreDeUsuario);
        user.setMail(mail);

        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        user.setClave(encriptada);

        user.setAlta(true);

        Foto foto = serviciosFoto.guardar(archivo);

        user.setFotoPerfil(foto);

        usuarioRepositorio.save(user);
    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombreDeUsuario, String mail, String clave1,
            String clave2) throws ErrorServicio {

        System.out.println(clave2);
        System.out.println(clave1);
        validar(nombreDeUsuario, mail, clave1, clave2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = usuarioRepositorio.findById(id).get();
            user.setNombreDeUsuario(nombreDeUsuario);
            user.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            user.setClave(encriptada);

            String idFoto = null;

            if (user.getFotoPerfil() != null) {
                idFoto = user.getFotoPerfil().getId();
            }

            Foto foto = serviciosFoto.actualizar(idFoto, archivo);
            user.setFotoPerfil(foto);

            usuarioRepositorio.save(user);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = respuesta.get();
            user.setAlta(false);
            usuarioRepositorio.save(user);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = respuesta.get();
            user.setAlta(true);
            usuarioRepositorio.save(user);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    public Usuario buscarPorId(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = respuesta.get();
            return user;
        } else {
            throw new ErrorServicio("No se encontro al usuario solicitado.");
        }
    }

    private void validar(String nombreDeUsuario, String mail, String clave1, String clave2)
            throws ErrorServicio {

        if (nombreDeUsuario == null || nombreDeUsuario.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo.");
        }

        if (clave1 == null || clave1.isEmpty() || clave1.length() <= 6) {
            throw new ErrorServicio("La clave no puede ser nula y tiene que tener mas de 6 digitos.");
        }

        if (!clave1.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
    }

    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarUsuarioPorMail(mail);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;
        } else {
            return null;
        }

    }
}
