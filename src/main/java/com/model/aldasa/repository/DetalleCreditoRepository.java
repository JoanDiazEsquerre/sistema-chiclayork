package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.DetalleCredito;

public interface DetalleCreditoRepository extends JpaRepository<DetalleCredito, Integer>{

	List<DetalleCredito> findByCreditoAndEstadoAndEstadoPago(Credito credito, boolean estado, boolean estadoPago);
	List<DetalleCredito> findByCreditoAndEstado(Credito credito, boolean estado);
}
