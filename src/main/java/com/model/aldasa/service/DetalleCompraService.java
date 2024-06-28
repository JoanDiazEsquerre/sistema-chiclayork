package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.DetalleCompra;

public interface DetalleCompraService {
	
	Optional<DetalleCompra> findBy(Integer id);
	DetalleCompra save(DetalleCompra entity);
	void delete(DetalleCompra entity);

	List<DetalleCompra> findByCompraAndEstado(Compra compra, boolean estado);
}
