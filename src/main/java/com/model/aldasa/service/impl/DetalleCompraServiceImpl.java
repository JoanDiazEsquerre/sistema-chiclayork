package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.DetalleCompra;
import com.model.aldasa.repository.DetalleCompraRepository;
import com.model.aldasa.service.DetalleCompraService;

@Service("detalleCompraService")
public class DetalleCompraServiceImpl implements DetalleCompraService{
	
	@Autowired
	private DetalleCompraRepository detalleCompraRepository;

	@Override
	public Optional<DetalleCompra> findBy(Integer id) {
		// TODO Auto-generated method stub
		return detalleCompraRepository.findById(id);
	}

	@Override
	public DetalleCompra save(DetalleCompra entity) {
		// TODO Auto-generated method stub
		return detalleCompraRepository.save(entity);
	}

	@Override
	public void delete(DetalleCompra entity) {
		// TODO Auto-generated method stub
		detalleCompraRepository.delete(entity);
	}

	@Override
	public List<DetalleCompra> findByCompraAndEstado(Compra compra, boolean estado) {
		// TODO Auto-generated method stub
		return detalleCompraRepository.findByCompraAndEstado(compra, estado);
	}




}
