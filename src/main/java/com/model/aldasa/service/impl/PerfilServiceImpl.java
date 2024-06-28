package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.PerfilRepository; 
import com.model.aldasa.service.PerfilService;

@Service("perfilService")
public class PerfilServiceImpl implements PerfilService {

	@Autowired
	private PerfilRepository perfilRepository;
	
	@Override
	public Optional<Perfil> findBy(Integer id) {
		return perfilRepository.findById(id);
	}

	
	@Override
	public Perfil save(Perfil entity) {
		return perfilRepository.save(entity);
	}
	
	@Override
	public void delete(Perfil entity) {
		perfilRepository.delete(entity);
	}
	
	@Override
	public List<Perfil> findByEstado(boolean status) {
		return perfilRepository.findByEstado(status);
	}
	
	@Override
	public Perfil findByNombre(String name) {
		return perfilRepository.findByNombre(name);
	}
	
	@Override
	public  Perfil findByNombreException(String name, int idProfile) {
		return perfilRepository.findByNombreException(name, idProfile);
	}


	@Override
	public Page<Perfil> findByNombreLikeAndEstado(String name, boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return perfilRepository.findByNombreLikeAndEstado(name, status, pageable);
	}
	
}
