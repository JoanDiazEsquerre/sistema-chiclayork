package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Sucursal;

public interface DetalleAtencionMesaRepository extends JpaRepository<DetalleAtencionMesa, Integer>{
	
	List<DetalleAtencionMesa> findByAtencionMesaEstadoAndAtencionMesaSucursalAndAtencionMesaFechaCobroBetweenAndEstado(String atencionMesaEstado, Sucursal sucursal, Date fechaIni, Date fechaFin, boolean estado);
	List<DetalleAtencionMesa> findByAtencionMesaAndEstado(AtencionMesa atencionMesa, boolean estado);
	
	@Query(nativeQuery = true,value = "SELECT SUM(d.total) FROM detalleatencionmesa d left join atencionmesa a on a.id=d.idAtencionMesa "
			+ "WHERE a.estado=:estado AND a.idSucursal = :idSucursal and d.estado=true and a.fechaCobro between :fechaIni and :fechaFin  ")
	BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin);
	
	@Query(nativeQuery = true,value = "SELECT SUM(d.total) FROM detalleatencionmesa d left join atencionmesa a on a.id=d.idAtencionMesa "
			+ "WHERE a.estado=:estado AND a.idSucursal = :idSucursal and d.estado=true and a.fechaCobro between :fechaIni and :fechaFin and a.credito = :credito")
	BigDecimal getTotalReporte(String estado, int idSucursal, Date fechaIni, Date fechaFin, boolean credito);

}
