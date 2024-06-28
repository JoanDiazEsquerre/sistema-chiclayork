package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.repository.CreditoRepository;
import com.model.aldasa.service.CreditoService;

@Service("creditoService")
public class CreditoServiceImpl  implements CreditoService{

	@Autowired
	private CreditoRepository creditoRepository;

	@Override
	public Optional<Credito> findById(Integer id) {
		// TODO Auto-generated method stub
		return creditoRepository.findById(id);
	}

	@Override
	public Credito save(Credito entity) {
		// TODO Auto-generated method stub
		return creditoRepository.save(entity);
	}

	@Override
	public void delete(Credito entity) {
		// TODO Auto-generated method stub
		creditoRepository.delete(entity);
	}



	@Override
	public List<Credito> findAllByEstadoAndPersona(String estado, Persona persona) {
		// TODO Auto-generated method stub
		return creditoRepository.findAllByEstadoAndPersona(estado, persona);
	}

	@Override
	public Page<Credito> findAllByEstadoAndPersona(String estado, Persona persona, Pageable pageable) {
		// TODO Auto-generated method stub
		return creditoRepository.findAllByEstadoAndPersona(estado, persona, pageable);
	}

	@Override
	public Page<Credito> findAllByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return creditoRepository.findAllByEstado(estado, pageable);
	}

	@Override
	public BigDecimal sumaTotal(String estado) {
		// TODO Auto-generated method stub
		return creditoRepository.sumaTotal(estado);
	}

	@Override
	public BigDecimal sumaTotal(String estado, int idPersona) {
		// TODO Auto-generated method stub
		return creditoRepository.sumaTotal(estado, idPersona);
	}

	@Override
	public Page<Credito> findAllByEstadoAndPersonaNombresLikeAndPersonaApellidosLike(String estado, String nombres,
			String apellidos, Pageable pageable) {
		// TODO Auto-generated method stub
		return creditoRepository.findAllByEstadoAndPersonaNombresLikeAndPersonaApellidosLike(estado, nombres, apellidos, pageable);
	}

	

	
}
