package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Usuario;

public interface UsuarioService {
	
	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario user);
	void delete(Usuario user);
	Usuario findByUsernameAndEstado(String username, boolean status);
	Usuario findByUsername(String username);
	Usuario findByUsernameAndPassword(String username, String pass);
	Usuario findByPersonaDniAndEstadoAndControlarAsistencia(String dni, boolean estado, boolean controlarAsistencia);
	List<Usuario> findAll();
	
	Usuario findByPersona(Persona person);
	Usuario findByPersonaException(int idPersona, int idUser);
	
	Usuario findByUsernameException(String username, int idUser);
	
	Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);


}
