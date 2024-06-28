package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;

public interface AtencionMesaService {
 
   Optional<AtencionMesa> findBy(Integer id);
   
   AtencionMesa save(AtencionMesa entity);
   AtencionMesa save(AtencionMesa entity, Caja caja, BigDecimal totalDetalle, boolean cobroCredito);
   void delete(AtencionMesa entity);
   AtencionMesa findByPisoAndNumMesaAndEstadoAndSucursal(Piso piso, int numMesa, String estado, Sucursal sucursal);
   
   Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);
   Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito, Pageable pageable);
   Page<AtencionMesa> findByEstadoChefAndSucursalAndEstadoIn(String estadoChef,Sucursal sucursal,List<String> estado , Pageable pageable);
   Page<AtencionMesa> findBySucursalAndEstado(Sucursal sucursal, String estado , Pageable pageable);
   
   Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetween(String nombreCliente, String  dniRuc, String estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);
   Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetweenAndCredito(String nombreCliente, String  dniRuc,String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito, Pageable pageable);


   //REPORTE
   List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin);
   List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito);

}
