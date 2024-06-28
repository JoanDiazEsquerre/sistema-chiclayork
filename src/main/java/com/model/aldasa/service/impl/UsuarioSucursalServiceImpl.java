package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.UsuarioSucursal;
import com.model.aldasa.repository.UsuarioSucursalRepository;
import com.model.aldasa.service.UsuarioSucursalService;

@Service("usuarioSucursalService")
public class UsuarioSucursalServiceImpl implements UsuarioSucursalService {

	@Autowired
	private UsuarioSucursalRepository usuarioSucursalRepository;

	@Override
	public Optional<UsuarioSucursal> findById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioSucursalRepository.findById(id);
	}

	@Override
	public UsuarioSucursal save(UsuarioSucursal entity) {
		// TODO Auto-generated method stub
		return usuarioSucursalRepository.save(entity);
	}

	@Override
	public void delete(UsuarioSucursal entity) {
		// TODO Auto-generated method stub
		usuarioSucursalRepository.delete(entity);
	}

	@Override
	public UsuarioSucursal findByUsuarioAndSucursal(Usuario usuario, Sucursal sucursal) {
		// TODO Auto-generated method stub
		return usuarioSucursalRepository.findByUsuarioAndSucursal(usuario, sucursal);
	}

	@Override
	public List<UsuarioSucursal> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return usuarioSucursalRepository.findByUsuario(usuario);
	}

}
