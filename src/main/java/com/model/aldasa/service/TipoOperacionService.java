package com.model.aldasa.service;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.TipoOperacion;

public interface TipoOperacionService {
	
	Optional<TipoOperacion> findById(Integer id);
	TipoOperacion save(TipoOperacion entity);
	void delete(TipoOperacion entity);
	
	List<TipoOperacion> findByEstado(boolean estado);
	

}
