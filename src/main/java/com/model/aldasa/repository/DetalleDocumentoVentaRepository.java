package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;


public interface DetalleDocumentoVentaRepository extends JpaRepository<DetalleDocumentoVenta, Integer>{
	
	List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);

	//PARA EL REPORTE DE DOCUMENTOS DE VENTAS
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, List<String> codigo, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);


}
