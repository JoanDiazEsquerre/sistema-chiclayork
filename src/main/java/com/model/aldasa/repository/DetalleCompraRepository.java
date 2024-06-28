package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer>{

	List<DetalleCompra> findByCompraAndEstado(Compra compra, boolean estado);

}
