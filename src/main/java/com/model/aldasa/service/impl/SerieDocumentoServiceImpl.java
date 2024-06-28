package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.repository.SerieDocumentoRepository;
import com.model.aldasa.service.SerieDocumentoService;

@Service("serieDocumentoService")
public class SerieDocumentoServiceImpl implements SerieDocumentoService{

	@Autowired
	private SerieDocumentoRepository serieDocumentoRepository;

	@Override
	public Optional<SerieDocumento> findById(Integer id) {
		// TODO Auto-generated method stub
		return serieDocumentoRepository.findById(id);
	}

	@Override
	public SerieDocumento save(SerieDocumento entity) {
		// TODO Auto-generated method stub
		return serieDocumentoRepository.save(entity);
	}

	@Override
	public void delete(SerieDocumento entity) {
		// TODO Auto-generated method stub
		serieDocumentoRepository.delete(entity);
	}

	@Override
	public List<SerieDocumento> findByTipoDocumentoAndSucursal(TipoDocumento tipoDocumento, Sucursal sucural) {
		// TODO Auto-generated method stub
		return serieDocumentoRepository.findByTipoDocumentoAndSucursal(tipoDocumento, sucural);
	}

	@Override
	public List<SerieDocumento> findByTipoDocumentoAndAnioAndSucursalAndCodigoInterno(TipoDocumento tipoDocumento,
			String anio, Sucursal sucural, String codigoInterno) {
		// TODO Auto-generated method stub
		return serieDocumentoRepository.findByTipoDocumentoAndAnioAndSucursalAndCodigoInterno(tipoDocumento, anio, sucural, codigoInterno);
	}
}
