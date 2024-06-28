package com.model.aldasa.service;


import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.DetalleCredito;

public interface DetalleCreditoService {
	
	Optional<DetalleCredito> findBy(Integer id);
	DetalleCredito save(DetalleCredito entity);
	void delete(DetalleCredito entity);
	
	List<DetalleCredito> findByCreditoAndEstadoAndEstadoPago(Credito credito, boolean estado, boolean estadoPago);
	List<DetalleCredito> findByCreditoAndEstado(Credito credito, boolean estado);


}
