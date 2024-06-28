package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.Sucursal;

public interface CompraService {
	
	Optional<Compra> findBy(Integer id);
	Compra save(Compra entity);
	void delete(Compra entity);
	
	Compra findByfecha(Date fecha);
	Compra findByfechaAndSucursalAndEstado(Date fecha, Sucursal sucursal, boolean estado);
	
	Page<Compra> findByEstadoAndSucursalAndFechaBetween(boolean estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);


}
