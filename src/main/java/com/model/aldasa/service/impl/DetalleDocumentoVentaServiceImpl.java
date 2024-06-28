package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.repository. DetalleDocumentoVentaRepository;
import com.model.aldasa.service.DetalleDocumentoVentaService;

@Service("detalleDocumentoVentaService")
public class DetalleDocumentoVentaServiceImpl implements DetalleDocumentoVentaService{

	@Autowired
	private  DetalleDocumentoVentaRepository  detalleDocumentoVentaRepository;

	@Override
	public Optional<DetalleDocumentoVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findById(id);
	}

	@Override
	public DetalleDocumentoVenta save(DetalleDocumentoVenta entity) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.save(entity);
	}

	@Override
	public void delete(DetalleDocumentoVenta entity) {
		// TODO Auto-generated method stub
		detalleDocumentoVentaRepository.delete(entity);
	}

	@Override
	public List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findByDocumentoVentaAndEstado(documentoVenta, estado);
	}

	@Override
	public List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(
			Boolean estado, Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(estado, sucursal, codigo, fechaIni, fechaFin);
	}

	@Override
	public List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(
			Boolean estado, Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(estado, sucursal, tipoDocumento, fechaIni, fechaFin);
	}

	@Override
	public List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(
			Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(sucursal, codigo, fechaIni, fechaFin);
	}

	@Override
	public List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(
			Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(sucursal, tipoDocumento, fechaIni, fechaFin);
	}

	


	
	
}
