package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;

public interface EmpresaService {

	Optional<Empresa> findById(Integer id);
	Empresa save(Empresa entity);
	void delete(Empresa entity);
	
	List<Empresa> findByEstado(boolean estado);

}
