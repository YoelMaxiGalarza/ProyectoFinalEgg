package com.turistearg.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Entidades.ConfirmacionToken;
import com.turistearg.Entidades.Foto;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
import com.turistearg.Repositorios.TokenRepositorio;
import com.turistearg.Repositorios.UsuarioRepositorio;
import org.springframework.scheduling.annotation.Async;

@Service
public class UsuarioServicio implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private FotoServicio servicioFoto;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	TokenRepositorio tokenRepositorio;

        @Async
	public void envioToken(String mail, String urlBase, String token) throws ErrorServicio {
		if (mail == null || mail.trim().isEmpty()) {
			throw new ErrorServicio("El mail no puede estar vacio o ser nulo. COLOCA UN MAIL");
		}
		try {
			SimpleMailMessage mensaje = new SimpleMailMessage();
			mensaje.setTo(mail);
			mensaje.setSubject("Recuperacion de Contraseña Turistearg");
			mensaje.setFrom("grupo2egg@gmail.com");
			mensaje.setText("Haz click para renovar la contraseña " + urlBase
					+ "/usuario/confirmar_cambio_contrasenia?tokenDeConfirmacion=" + token);
			javaMailSender.send(mensaje);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ErrorServicio(e.getMessage(), e);
		}
	}
         @Async
        public void envioTokenAutentificacion(String mail, String urlBase, String token) throws ErrorServicio{
            
            if (mail == null || mail.trim().isEmpty()) {
                throw new ErrorServicio("El mail no puede estar vacio o ser nulo. Coloca un mail");
            }
            
                 
            try {
                    SimpleMailMessage mensaje = new SimpleMailMessage();
                    mensaje.setTo(mail);
                    mensaje.setSubject("Completar Registro Turistearg");
                    mensaje.setFrom("grupo2egg@gmail.com");
                    mensaje.setText("Haz click para completar el registro " + urlBase
                            + "/confirmar_registro?tokenDeConfirmacion=" + token);
                    javaMailSender.send(mensaje);

            } catch (Exception e) {
                System.err.println(e.getMessage());
                throw new ErrorServicio(e.getMessage(), e);
            }
        }

	@Transactional
	public void cambiarContraseña(String clave1, String clave2, String mail) throws ErrorServicio {
		if (mail == null || mail.isEmpty()) {
			throw new ErrorServicio("El mail no puede ser nulo.");
		}

		if (clave1 == null || clave1.isEmpty() || clave1.length() <= 6) {
			throw new ErrorServicio("La clave no puede ser nula y tiene que tener mas de 6 digitos.");
		}

		if (!clave1.equals(clave2)) {
			throw new ErrorServicio("Las claves deben ser iguales");
		}
		Usuario usuario = buscarPorMail(mail);
		String encriptada = new BCryptPasswordEncoder().encode(clave1);
		usuario.setClave(encriptada);
		usuarioRepositorio.save(usuario);
	}

	@Transactional
	public void registrar(MultipartFile archivo, String nombreDeUsuario, String mail, String clave1, String clave2)
			throws ErrorServicio {

		validar(nombreDeUsuario, mail, clave1, clave2);
		try {
			Usuario user = new Usuario();
			user.setNombreDeUsuario(nombreDeUsuario);
			user.setMail(mail);

			String encriptada = new BCryptPasswordEncoder().encode(clave1);
			user.setClave(encriptada);

			user.setAlta(true);

			Foto foto = servicioFoto.guardar(archivo);

			user.setFotoPerfil(foto);

			usuarioRepositorio.save(user);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ErrorServicio(e.getMessage(), e);
		}
	}

	@Transactional
	public void modificar(String id, String nombreDeUsuario, String mail, String clave1, String clave2)
			throws ErrorServicio {

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

			usuarioRepositorio.save(user);
		} else {
			throw new ErrorServicio("No se encontro el usuario solicitado");
		}
	}

	@Transactional
	public void modificarFoto(MultipartFile foto, String idUsuario) throws ErrorServicio {

		Usuario usuario = buscarPorId(idUsuario);
		if (usuario == null) {
			throw new ErrorServicio("El usuario solicitado no se ha encontrado");

		}

		if (foto == null) {
			throw new ErrorServicio("Foto no encontrada");
		}

		String idFoto = usuario.getFotoPerfil().getId();

		Foto nuevaFoto = servicioFoto.actualizar(idFoto, foto);
		usuario.setFotoPerfil(nuevaFoto);
		usuarioRepositorio.save(usuario);
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

	public Usuario buscarPorMail(String mail) throws ErrorServicio {
		Usuario user = usuarioRepositorio.buscarUsuarioPorMail(mail);
		if (user == null) {
			throw new ErrorServicio("El usuario no existe");
		}
		return user;
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

	private void validar(String nombreDeUsuario, String mail, String clave1, String clave2) throws ErrorServicio {

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

		Usuario user = usuarioRepositorio.buscarUsuarioPorMail(mail);

		if (user != null) {
			throw new ErrorServicio("Ya existe un usuario con el email ingresado.");
		}
                
               
	}
}
