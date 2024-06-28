package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.Sucursal;

public interface DetalleAtencionMesaService {
 
   Optional<DetalleAtencionMesa> findBy(Integer id);
   DetalleAtencionMesa save(DetalleAtencionMesa entity);
   void delete(DetalleAtencionMesa entity);
   
   List<DetalleAtencionMesa> findByAtencionMesaEstadoAndAtencionMesaSucursalAndAtencionMesaFechaCobroBetweenAndEstado(String atencionMesaEstado, Sucursal sucursal, Date fechaIni, Date fechaFin, boolean estado);
   List<DetalleAtencionMesa> findByAtencionMesaAndEstado(AtencionMesa atencionMesa, boolean estado);
   
   
   BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin);
   BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin, boolean credito);
   
}
