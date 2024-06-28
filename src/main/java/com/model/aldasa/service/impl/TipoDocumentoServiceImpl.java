package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.repository.TipoDocumentoRepository;
import com.model.aldasa.service.TipoDocumentoService;

@Service("tipoDocumentoService")
public class TipoDocumentoServiceImpl implements TipoDocumentoService{
	
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Override
	public Optional<TipoDocumento> findById(Integer id) {
		// TODO Auto-generated method stub
		return tipoDocumentoRepository.findById(id);
	}

	@Override
	public TipoDocumento save(TipoDocumento entity) {
		// TODO Auto-generated method stub
		return tipoDocumentoRepository.save(entity);
	}

	@Override
	public void delete(TipoDocumento entity) {
		// TODO Auto-generated method stub
		tipoDocumentoRepository.delete(entity);
	}

	@Override
	public List<TipoDocumento> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return tipoDocumentoRepository.findByEstado(estado);
	}

	@Override
	public List<TipoDocumento> findByEstadoAndCodigoIn(boolean estado, List<String> codigo) {
		// TODO Auto-generated method stub
		return tipoDocumentoRepository.findByEstadoAndCodigoIn(estado, codigo);
	}


}
