package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	Usuario findByUsernameAndEstado(String username,boolean status);
	Usuario findByUsername(String username);
	Usuario findByUsernameAndPassword(String username, String pass);
	Usuario findByPersonaDniAndEstadoAndControlarAsistencia(String dni, boolean estado, boolean controlarAsistencia);
	
	Usuario findByPersona(Persona person);

	@Query(nativeQuery = true,value = " SELECT * FROM user  WHERE username = :username and id<>:idUser")
    Usuario findByUsernameException(String username, int idUser);
    
    @Query(nativeQuery = true,value = " SELECT * FROM user  WHERE idPersona = :idPersona and id<>:idUser")
    Usuario findByPersonaException(int idPersona, int idUser);
	
	Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);

}
