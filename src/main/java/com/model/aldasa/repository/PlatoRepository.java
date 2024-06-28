package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Producto;

public interface PlatoRepository extends JpaRepository<Plato, Integer>{
	
	Plato findByNombreAndEstadoAndProducto(String nombre, boolean estado,Producto producto);
	List<Plato> findByProductoDescripcionAndEstado(String productoDescripcion, boolean estado); 
	List<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String producto, boolean estado, String enCarta); 
	List<Plato> findByEstadoAndAutomatico(boolean estado, String automatico); 
	List<Plato> findByEstadoAndEnCarta(boolean estado, String enCarta); 
	
	Page<Plato> findByEstadoAndProducto(boolean estado ,Producto producto, Pageable pageable);
	Page<Plato> findByProductoDescripcionAndEstadoAndEnCarta(String productoDesc, boolean estado, String enCarta, Pageable pageable);

	
	@Query(nativeQuery = true,value = " SELECT * FROM plato  WHERE nombre = :nombre and id<>:idPlato and estado=:estado and idProducto=:idProducto")
	Plato findByNombreException(String nombre, boolean estado, int idProducto, int idPlato);
    

	
//	Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);

}
