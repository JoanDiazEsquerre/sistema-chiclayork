package com.model.aldasa.repository;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.model.aldasa.entity.MotivoNota;

public interface MotivoNotaRepository extends PagingAndSortingRepository<MotivoNota, Integer>{
	
	List<MotivoNota> findByTipoDocumentoAndEstado(String tipoDocumento, boolean estado);

}
