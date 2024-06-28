package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
	
	List<Empresa> findByEstado(boolean estado);


}
