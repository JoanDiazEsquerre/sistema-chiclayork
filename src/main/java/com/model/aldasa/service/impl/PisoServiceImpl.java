package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.PisoRepository; 
import com.model.aldasa.service.PerfilService;
import com.model.aldasa.service.PisoService;

@Service("pisoService")
public class PisoServiceImpl implements PisoService {

	@Autowired
	private PisoRepository pisoRepository;

	@Override
	public Optional<Piso> findBy(Integer id) {
		// TODO Auto-generated method stub
		return pisoRepository.findById(id);
	}

	@Override
	public Piso save(Piso entity) {
		// TODO Auto-generated method stub
		return pisoRepository.save(entity);
	}

	@Override
	public void delete(Piso entity) {
		// TODO Auto-generated method stub
		pisoRepository.delete(entity);
	}

	@Override
	public List<Piso> findBySucursalOrderByNombre(Sucursal sucursal) {
		// TODO Auto-generated method stub
		return pisoRepository.findBySucursalOrderByNombre(sucursal); 
	}
	
	
	
}
