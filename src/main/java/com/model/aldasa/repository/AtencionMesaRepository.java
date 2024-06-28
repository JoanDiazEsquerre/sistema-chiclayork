package com.model.aldasa.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Sucursal;

public interface AtencionMesaRepository extends JpaRepository<AtencionMesa, Integer>{
	
	AtencionMesa findByPisoAndNumMesaAndEstadoAndSucursal(Piso piso, int numMesa, String estado, Sucursal sucursal);
	
	Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);
	Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito, Pageable pageable);
	Page<AtencionMesa> findByEstadoChefAndSucursalAndEstadoIn(String estadoChef,Sucursal sucursal,List<String> estado , Pageable pageable);
	Page<AtencionMesa> findBySucursalAndEstado(Sucursal sucursal, String estado , Pageable pageable);
	
	Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetween(String nombreCliente, String  dniRuc, String estado,Sucursal sucursal,Date fechaIni, Date fechaFin , Pageable pageable);
	Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetweenAndCredito(String nombreCliente, String  dniRuc,String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito, Pageable pageable);

    
	//REPORTE
	   List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin);
	   List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado,Sucursal sucursal,Date fechaIni, Date fechaFin ,boolean credito);

	
//	Page<Usuario> findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);

}
