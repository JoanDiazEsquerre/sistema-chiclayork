package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Persona;
import com.model.aldasa.repository.PersonaRepository;
import com.model.aldasa.service.PersonaService;

@Service("personaService")
public class PersonaServiceImpl implements PersonaService {
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Override
	public Optional<Persona> findById(Integer id) {
		return personaRepository.findById(id);
	}

	@Override
	public Persona save(Persona entity) {
		return personaRepository.save(entity);
	}

	@Override
	public void delete(Persona entity) {
		personaRepository.delete(entity);
	}

	@Override
	public List<Persona> findByEstado(Boolean status) {
		return personaRepository.findByEstado(status);
	}
	
	@Override
	public Page<Persona> findAllByDniLikeAndApellidosLikeAndEstado(String dni, String names, Boolean status, Pageable pageable) {
		return personaRepository.findAllByDniLikeAndApellidosLikeAndEstado(dni, names, status, pageable);
	}
	
	@Override
	public Persona findByDni(String dni) {
		return personaRepository.findByDni(dni);
	}
	
	@Override
	public Persona findByDniException(String dni, int idUser) {
		return personaRepository.findByDniException(dni, idUser);
	}

	@Override
	public List<Persona> findByEstadoAndCredito(Boolean status, boolean credito) {
		// TODO Auto-generated method stub
		return personaRepository.findByEstadoAndCredito(status, credito);
	}

}
