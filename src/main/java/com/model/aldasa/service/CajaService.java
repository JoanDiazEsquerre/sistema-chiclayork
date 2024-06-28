package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Sucursal;


public interface CajaService {
 
   Optional<Caja> findById(Integer id);
   Caja save(Caja entity);
   void delete(Caja entity);
   
   Caja findFirstBySucursalAndEstadoOrderByIdDesc(Sucursal sucursal,String estado);
   
   List<Caja> findBySucursalAndEstado(Sucursal sucursal,String estado);
   List<Caja> findBySucursalAndFecha(Sucursal sucursal, Date fecha);
  
   Page<Caja> findBySucursalOrderByIdDesc(Sucursal sucursal, Pageable pageable);

}
