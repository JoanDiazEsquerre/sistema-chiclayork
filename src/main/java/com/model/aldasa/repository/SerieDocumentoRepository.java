package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;

public interface SerieDocumentoRepository extends JpaRepository<SerieDocumento, Integer>{

	List<SerieDocumento> findByTipoDocumentoAndSucursal(TipoDocumento tipoDocumento, Sucursal sucural);
	List<SerieDocumento> findByTipoDocumentoAndAnioAndSucursalAndCodigoInterno(TipoDocumento tipoDocumento,String anio, Sucursal sucural, String codigoInterno);

}
