package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.DetalleCredito;
import com.model.aldasa.repository.DetalleCreditoRepository;
import com.model.aldasa.service.DetalleCreditoService;

@Service("detalleCreditoService")
public class DetalleCreditoServiceImpl implements DetalleCreditoService{
	
	@Autowired
	private DetalleCreditoRepository detalleCreditoRepository;

	@Override
	public Optional<DetalleCredito> findBy(Integer id) {
		// TODO Auto-generated method stub
		return detalleCreditoRepository.findById(id);
	}

	@Override
	public DetalleCredito save(DetalleCredito entity) {
		// TODO Auto-generated method stub
		return detalleCreditoRepository.save(entity);
	}

	@Override
	public void delete(DetalleCredito entity) {
		// TODO Auto-generated method stub
		detalleCreditoRepository.delete(entity);
	}

	@Override
	public List<DetalleCredito> findByCreditoAndEstadoAndEstadoPago(Credito credito, boolean estado,
			boolean estadoPago) {
		// TODO Auto-generated method stub
		return detalleCreditoRepository.findByCreditoAndEstadoAndEstadoPago(credito, estado, estadoPago);
	}

	@Override
	public List<DetalleCredito> findByCreditoAndEstado(Credito credito, boolean estado) {
		// TODO Auto-generated method stub
		return detalleCreditoRepository.findByCreditoAndEstado(credito, estado);
	}

	


}
