package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Identificador;
import com.model.aldasa.repository.IdentificadorRepository;
import com.model.aldasa.service.IdentificadorService;

@Service("identificadorService")
public class IdentificadorServiceImpl implements IdentificadorService{

	@Autowired
	private IdentificadorRepository identificadorRepository;

	@Override
	public Optional<Identificador> findById(Integer id) {
		// TODO Auto-generated method stub
		return identificadorRepository.findById(id);
	}

	@Override
	public Identificador save(Identificador entity) {
		// TODO Auto-generated method stub
		return identificadorRepository.save(entity);
	}

	@Override
	public void delete(Identificador entity) {
		// TODO Auto-generated method stub
		identificadorRepository.delete(entity);
	}

	@Override
	public List<Identificador> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return identificadorRepository.findByEstado(estado);
	}

}
