package com.model.aldasa.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Sucursal;

public interface CajaRepository extends JpaRepository<Caja, Integer> {
	
	Caja findFirstBySucursalAndEstadoOrderByIdDesc(Sucursal sucursal,String estado);

	List<Caja> findBySucursalAndEstado(Sucursal sucursal,String estado);
	List<Caja> findBySucursalAndFecha(Sucursal sucursal, Date fecha);
  	
	Page<Caja> findBySucursalOrderByIdDesc(Sucursal sucursal, Pageable pageable);


}
