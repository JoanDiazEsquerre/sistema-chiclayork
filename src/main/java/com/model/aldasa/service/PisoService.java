package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;

public interface PisoService {
 
   Optional<Piso> findBy(Integer id);
   Piso save(Piso entity);
   void delete(Piso entity);
   List<Piso> findBySucursalOrderByNombre(Sucursal sucursal);
  
}
