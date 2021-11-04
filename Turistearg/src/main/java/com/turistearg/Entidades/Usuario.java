package com.turistearg.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	@OneToOne
	private Foto fotoPerfil;
	
	private String nombreDeUsuario;
	private String mail;
	private String clave;
	private boolean alta;
	
	public Usuario() {
	
	}

	public Usuario(String id, Foto fotoPerfil, String nombreDeUsuario, String mail, String clave, boolean alta) {
		super();
		this.id = id;
		this.fotoPerfil = fotoPerfil;
		this.nombreDeUsuario = nombreDeUsuario;
		this.mail = mail;
		this.clave = clave;
		this.alta = alta;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombreDeUsuario() {
		return nombreDeUsuario;
	}

	public void setNombreDeUsuario(String nombreDeUsuario) {
		this.nombreDeUsuario = nombreDeUsuario;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isAlta() {
		return alta;
	}

	public void setAlta(boolean alta) {
		this.alta = alta;
	}

	public Foto getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(Foto fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	
	
}
