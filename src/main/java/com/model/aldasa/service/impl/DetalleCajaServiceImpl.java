package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.repository.DetalleCajaRepository;
import com.model.aldasa.service.DetalleCajaService;

@Service("detalleCajaService")
public class DetalleCajaServiceImpl implements DetalleCajaService {

	@Autowired
	private DetalleCajaRepository detalleCajaRepository;

	@Override
	public Optional<DetalleCaja> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findById(id);
	}

	@Override
	public DetalleCaja save(DetalleCaja entity) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.save(entity);
	}

	@Override
	public void delete(DetalleCaja entity) {
		// TODO Auto-generated method stub
		detalleCajaRepository.delete(entity); 
	}

	@Override
	public List<DetalleCaja> findByCajaAndEstado(Caja caja, boolean estado) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findByCajaAndEstado(caja, estado);
	}

	@Override
	public List<DetalleCaja> findByAtencionMesa(AtencionMesa atencionMesa) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findByAtencionMesa(atencionMesa);
	}
}
