package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.CompraRepository;
import com.model.aldasa.service.CompraService;

@Service("compraService")
public class CompraServiceImpl implements CompraService {

	@Autowired
	private CompraRepository compraRepository;

	@Override
	public Optional<Compra> findBy(Integer id) {
		// TODO Auto-generated method stub
		return compraRepository.findById(id);
	}

	@Override
	public Compra save(Compra entity) {
		// TODO Auto-generated method stub
		return compraRepository.save(entity);
	}

	@Override
	public void delete(Compra entity) {
		// TODO Auto-generated method stub
		compraRepository.delete(entity);
	}

	@Override
	public Page<Compra> findByEstadoAndSucursalAndFechaBetween(boolean estado, Sucursal sucursal, Date fechaIni,
			Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return compraRepository.findByEstadoAndSucursalAndFechaBetween(estado, sucursal, fechaIni, fechaFin, pageable);
	}

	@Override
	public Compra findByfecha(Date fecha) {
		// TODO Auto-generated method stub
		return compraRepository.findByfecha(fecha);
	}

	@Override
	public Compra findByfechaAndSucursalAndEstado(Date fecha, Sucursal sucursal, boolean estado) {
		// TODO Auto-generated method stub
		return compraRepository.findByfechaAndSucursalAndEstado(fecha, sucursal, estado);
	}


}
