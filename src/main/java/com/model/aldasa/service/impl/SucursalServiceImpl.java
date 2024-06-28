package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.SucursalRepository;
import com.model.aldasa.service.SucursalService;

@Service("sucursalService")
public class SucursalServiceImpl implements SucursalService {

	@Autowired
	private SucursalRepository sucursalRepository;

	
	@Override
	public Optional<Sucursal> findById(Integer id) {
		// TODO Auto-generated method stub
		return sucursalRepository.findById(id);
	}

	@Override
	public Sucursal save(Sucursal entity) {
		// TODO Auto-generated method stub
		return sucursalRepository.save(entity);
	}

	@Override
	public void delete(Sucursal entity) {
		// TODO Auto-generated method stub
		sucursalRepository.delete(entity);
	}

	@Override
	public List<Sucursal> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return sucursalRepository.findByEstado(estado);
	}

	@Override
	public List<Sucursal> findByEmpresaAndEstado(Empresa empresa, boolean estado) {
		// TODO Auto-generated method stub
		return sucursalRepository.findByEmpresaAndEstado(empresa, estado);
	}



}
