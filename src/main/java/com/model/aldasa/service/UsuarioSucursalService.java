package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.UsuarioSucursal;

public interface UsuarioSucursalService {

	Optional<UsuarioSucursal> findById(Integer id);
	UsuarioSucursal save(UsuarioSucursal entity);
	void delete(UsuarioSucursal entity);
	
	UsuarioSucursal findByUsuarioAndSucursal(Usuario usuario, Sucursal sucursal);
	List<UsuarioSucursal> findByUsuario(Usuario usuario);
}
