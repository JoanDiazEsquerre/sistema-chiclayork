package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.repository.EmpresaRepository;
import com.model.aldasa.service.EmpresaService;

@Service("empresaService")
public class EmpresaServiceImpl  implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public Optional<Empresa> findById(Integer id) {
		// TODO Auto-generated method stub
		return empresaRepository.findById(id);
	}

	@Override
	public Empresa save(Empresa entity) {
		// TODO Auto-generated method stub
		return empresaRepository.save(entity);
	}

	@Override
	public void delete(Empresa entity) {
		// TODO Auto-generated method stub
		empresaRepository.delete(entity);
	}

	@Override
	public List<Empresa> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return empresaRepository.findByEstado(estado);
	}
}
