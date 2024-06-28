package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{

	List<Producto> findByEstado(boolean estado);

}
