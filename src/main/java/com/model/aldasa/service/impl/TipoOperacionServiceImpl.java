package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.TipoOperacion;
import com.model.aldasa.repository.TipoOperacionRepository;
import com.model.aldasa.service.TipoOperacionService;

@Service("tipoOperacionService")
public class TipoOperacionServiceImpl implements TipoOperacionService{

	@Autowired
	private TipoOperacionRepository tipoOperacionRepository;

	@Override
	public Optional<TipoOperacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return tipoOperacionRepository.findById(id);
	}

	@Override
	public TipoOperacion save(TipoOperacion entity) {
		// TODO Auto-generated method stub
		return tipoOperacionRepository.save(entity);
	}

	@Override
	public void delete(TipoOperacion entity) {
		// TODO Auto-generated method stub
		tipoOperacionRepository.delete(entity);
	}

	@Override
	public List<TipoOperacion> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return tipoOperacionRepository.findByEstado(estado);
	}

}
