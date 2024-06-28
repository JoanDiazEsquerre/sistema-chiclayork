package com.model.aldasa.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleCaja;

public interface DetalleCajaRepository extends JpaRepository<DetalleCaja, Integer> {
	
	List<DetalleCaja> findByCajaAndEstado(Caja caja, boolean estado);
	List<DetalleCaja> findByAtencionMesa(AtencionMesa atencionMesa);


}
