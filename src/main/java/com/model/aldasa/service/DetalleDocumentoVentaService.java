package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.model.aldasa.entity. DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;

public interface DetalleDocumentoVentaService {

	Optional<DetalleDocumentoVenta> findById(Integer id);
	DetalleDocumentoVenta save(DetalleDocumentoVenta entity);
	void delete(DetalleDocumentoVenta entity);

	List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);

	//PARA EL REPORTE DE DOCUMENTOS DE VENTAS

	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);

}
