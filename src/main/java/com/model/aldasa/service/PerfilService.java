package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Usuario;

public interface PerfilService {
 
   Optional<Perfil> findBy(Integer id);
   Perfil save(Perfil profile);
   void delete(Perfil profile);
   List<Perfil> findByEstado(boolean status);
   Perfil findByNombre (String name);
   Perfil findByNombreException(String name, int idProfile);
   
	Page<Perfil> findByNombreLikeAndEstado(String name, boolean status, Pageable pageable);

}
