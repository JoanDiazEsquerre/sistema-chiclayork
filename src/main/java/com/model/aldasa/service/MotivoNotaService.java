package com.model.aldasa.service;
import java.util.List;
import java.util.Optional;
import com.model.aldasa.entity.MotivoNota;

public interface MotivoNotaService {
	
	Optional<MotivoNota> findById(Integer id);
	MotivoNota save(MotivoNota entity);
	void delete(MotivoNota entity);
	
	List<MotivoNota> findByTipoDocumentoAndEstado(String tipoDocumento, boolean estado);


}
