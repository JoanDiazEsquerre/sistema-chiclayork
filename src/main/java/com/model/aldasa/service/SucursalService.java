package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;

public interface SucursalService {

	Optional<Sucursal> findById(Integer id);
	Sucursal save(Sucursal entity);
	void delete(Sucursal entity);
	
	List<Sucursal> findByEstado(boolean estado);
	List<Sucursal> findByEmpresaAndEstado (Empresa empresa, boolean estado);


	
}
