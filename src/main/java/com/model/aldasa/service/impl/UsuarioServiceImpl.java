package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.UsuarioRepository;
import com.model.aldasa.service.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario entity) {
		return usuarioRepository.save(entity);
	}

	@Override
	public void delete(Usuario entity) {
		usuarioRepository.delete(entity);
	}
	
	@Override
	public Usuario findByUsernameAndEstado(String username, boolean status) {
		return usuarioRepository.findByUsernameAndEstado(username,status);
	}
	

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario findByUsername(String username) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsername(username);
	}

	@Override
	public Usuario findByUsernameAndPassword(String username, String pass) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsernameAndPassword(username, pass); 
	}

	@Override
	public Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(
			String profileName, String personSurnames, String password, String username, boolean status,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(profileName, personSurnames, password, username, status, pageable); 
	}

	@Override
	public Usuario findByPersona(Persona person) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByPersona(person);
	}

	@Override
	public Usuario findByPersonaException(int idPersona, int idUser) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByPersonaException(idPersona, idUser); 
	}

	@Override
	public Usuario findByUsernameException(String username, int idUser) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsernameException(username, idUser); 
	}
	

}
