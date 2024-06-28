package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.DetalleAtencionMesaRepository;
import com.model.aldasa.service.DetalleAtencionMesaService;

@Service("detalleAtencionMesaService")
public class DetalleAtencionMesaServiceImpl implements DetalleAtencionMesaService {

	@Autowired
	private DetalleAtencionMesaRepository detalleAtencionMesaRepository;

	@Override
	public Optional<DetalleAtencionMesa> findBy(Integer id) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.findById(id); 
	}

	@Override
	public DetalleAtencionMesa save(DetalleAtencionMesa entity) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.save(entity); 
	}

	@Override
	public void delete(DetalleAtencionMesa entity) {
		// TODO Auto-generated method stub
		detalleAtencionMesaRepository.delete(entity); 
	}

	@Override
	public List<DetalleAtencionMesa> findByAtencionMesaAndEstado(AtencionMesa atencionMesa, boolean estado) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.findByAtencionMesaAndEstado(atencionMesa, estado); 
	}

	@Override
	public BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.getTotalReporte(estado, idSucursal, fechaIni, fechaFin); 
	}

	@Override
	public List<DetalleAtencionMesa> findByAtencionMesaEstadoAndAtencionMesaSucursalAndAtencionMesaFechaCobroBetweenAndEstado(
			String atencionMesaEstado, Sucursal sucursal, Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.findByAtencionMesaEstadoAndAtencionMesaSucursalAndAtencionMesaFechaCobroBetweenAndEstado(atencionMesaEstado, sucursal, fechaIni, fechaFin, estado);
	}

	@Override
	public BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin, boolean credito) {
		// TODO Auto-generated method stub
		return detalleAtencionMesaRepository.getTotalReporte(estado, idSucursal, fechaIni, fechaFin, credito);
	}

	



	
}
