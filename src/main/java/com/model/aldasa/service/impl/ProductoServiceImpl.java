package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Producto;
import com.model.aldasa.repository.ProductoRepository;
import com.model.aldasa.service.ProductoService;

@Service("productoService")
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public Optional<Producto> findById(Integer id) {
		// TODO Auto-generated method stub
		return productoRepository.findById(id);
	}

	@Override
	public Producto save(Producto entity) {
		// TODO Auto-generated method stub
		return productoRepository.save(entity);
	}

	@Override
	public void delete(Producto entity) {
		// TODO Auto-generated method stub
		productoRepository.delete(entity);
	}

	@Override
	public List<Producto> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return productoRepository.findByEstado(estado);
	}
}
