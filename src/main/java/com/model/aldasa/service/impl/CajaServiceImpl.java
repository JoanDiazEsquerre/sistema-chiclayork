package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.CajaRepository;
import com.model.aldasa.service.CajaService;

@Service("cajaService")
public class CajaServiceImpl implements CajaService {

	@Autowired
	private CajaRepository cajaRepository;

	@Override
	public Optional<Caja> findById(Integer id) {
		// TODO Auto-generated method stub
		return cajaRepository.findById(id);
	}

	@Override
	public Caja save(Caja entity) {
		// TODO Auto-generated method stub
		return cajaRepository.save(entity);
	}

	@Override
	public void delete(Caja entity) {
		// TODO Auto-generated method stub
		cajaRepository.delete(entity); 
	}

	@Override
	public Page<Caja> findBySucursalOrderByIdDesc(Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return cajaRepository.findBySucursalOrderByIdDesc(sucursal, pageable);
	}

	@Override
	public List<Caja> findBySucursalAndEstado(Sucursal sucursal, String estado) {
		// TODO Auto-generated method stub
		return cajaRepository.findBySucursalAndEstado(sucursal, estado);
	}

	@Override
	public Caja findFirstBySucursalAndEstadoOrderByIdDesc(Sucursal sucursal, String estado) {
		// TODO Auto-generated method stub
		return cajaRepository.findFirstBySucursalAndEstadoOrderByIdDesc(sucursal, estado); 
	}

	@Override
	public List<Caja> findBySucursalAndFecha(Sucursal sucursal, Date fecha) {
		// TODO Auto-generated method stub
		return cajaRepository.findBySucursalAndFecha(sucursal, fecha);
	}

	
	
	
}
