package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.repository.PlatoRepository;
import com.model.aldasa.service.PlatoService;

@Service("platoService")
public class PlatoServiceImpl implements PlatoService {
	
	@Autowired
	private PlatoRepository platoRepository;

	@Override
	public Optional<Plato> findById(Integer id) {
		// TODO Auto-generated method stub
		return platoRepository.findById(id);
	}

	@Override
	public Plato save(Plato plato) {
		// TODO Auto-generated method stub
		return platoRepository.save(plato); 
	}

	@Override
	public void delete(Plato plato) {
		// TODO Auto-generated method stub
		platoRepository.delete(plato);
	}

	@Override
	public List<Plato> findAll() {
		// TODO Auto-generated method stub
		return platoRepository.findAll();
	}


	@Override
	public List<Plato> findByEstadoAndAutomatico(boolean estado, String automatico) {
		// TODO Auto-generated method stub
		return platoRepository.findByEstadoAndAutomatico(estado, automatico);
	}

	@Override
	public List<Plato> findByEstadoAndEnCarta(boolean estado, String enCarta) {
		// TODO Auto-generated method stub
		return platoRepository.findByEstadoAndEnCarta(estado, enCarta);
	}

	@Override
	public Page<Plato> findByEstadoAndProducto(boolean estado, Producto producto, Pageable pageable) {
		// TODO Auto-generated method stub
		return platoRepository.findByEstadoAndProducto(estado, producto, pageable);
	}

	@Override
	public Plato findByNombreException(String nombre, boolean estado, int idProducto, int idPlato) {
		// TODO Auto-generated method stub
		return platoRepository.findByNombreException(nombre, estado, idProducto, idPlato);
	}

	@Override
	public List<Plato> findByProductoDescripcionAndEstado(String productoDescripcion, boolean estado) {
		// TODO Auto-generated method stub
		return platoRepository.findByProductoDescripcionAndEstado(productoDescripcion, estado);
	}

	@Override
	public Plato findByNombreAndEstadoAndProducto(String nombre, boolean estado, Producto producto) {
		// TODO Auto-generated method stub
		return platoRepository.findByNombreAndEstadoAndProducto(nombre, estado, producto);
	}

	@Override
	public Page<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String productoDesc, boolean estado, String enCarta,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return platoRepository.findByProductoDescripcionAndEstadoAndEnCarta(productoDesc, estado, enCarta, pageable);
	}

	@Override
	public List<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String producto, boolean estado, String enCarta) {
		// TODO Auto-generated method stub
		return platoRepository.findByProductoDescripcionAndEstadoAndEnCarta(producto, estado, enCarta);
	}
	
	

}
