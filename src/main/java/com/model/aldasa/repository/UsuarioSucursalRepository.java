package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.UsuarioSucursal;

public interface UsuarioSucursalRepository extends JpaRepository<UsuarioSucursal, Integer> {
	
	UsuarioSucursal findByUsuarioAndSucursal(Usuario usuario, Sucursal sucursal);
	List<UsuarioSucursal> findByUsuario(Usuario usuario);


}
