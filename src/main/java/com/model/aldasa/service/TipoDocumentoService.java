package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.TipoDocumento;

public interface TipoDocumentoService {

	Optional<TipoDocumento> findById(Integer id);
	TipoDocumento save(TipoDocumento entity);
	void delete(TipoDocumento entity);
	
	List<TipoDocumento> findByEstado(boolean estado);
	List<TipoDocumento> findByEstadoAndCodigoIn(boolean estado, List<String> codigo);
}
