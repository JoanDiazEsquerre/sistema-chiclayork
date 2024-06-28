package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Usuario;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	List<Perfil> findByEstado(boolean status);
	Perfil findByNombre(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM perfil WHERE nombre=:name AND id <> :idProfile ")
	Perfil findByNombreException(String name, int idProfile);
	
	Page<Perfil> findByNombreLikeAndEstado(String name, boolean status, Pageable pageable);

}
