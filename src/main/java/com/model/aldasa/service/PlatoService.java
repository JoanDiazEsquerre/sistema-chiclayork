package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.Sucursal;

public interface PlatoService {
	
	Optional<Plato> findById(Integer id);
	Plato save(Plato plato);
	void delete(Plato plato);
	Plato findByNombreAndEstadoAndProducto(String nombre, boolean estado, Producto producto);

	
	List<Plato> findAll();
	List<Plato> findByProductoDescripcionAndEstado(String productoDescripcion, boolean estado); 
	List<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String producto, boolean estado, String enCarta); 
	List<Plato> findByEstadoAndAutomatico(boolean estado, String automatico); 
	List<Plato> findByEstadoAndEnCarta(boolean estado, String enCarta); 
	
	Page<Plato> findByEstadoAndProducto(boolean estado , Producto producto, Pageable pageable);
	Page<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String productoDesc, boolean estado, String enCarta, Pageable pageable);


	
	
	Plato findByNombreException(String nombre, boolean estado, int idProducto, int idPlato);
	
//	Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);


}
