package com.model.aldasa.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer>{
	
	Compra findByfecha(Date fecha);
	Compra findByfechaAndSucursalAndEstado(Date fecha, Sucursal sucursal, boolean estado);
	
	Page<Compra> findByEstadoAndSucursalAndFechaBetween(boolean estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);


}
