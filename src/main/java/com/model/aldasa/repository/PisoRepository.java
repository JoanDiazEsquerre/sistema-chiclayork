package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;

public interface PisoRepository extends JpaRepository<Piso, Integer> {

	List<Piso> findBySucursalOrderByNombre(Sucursal sucursal);
}
