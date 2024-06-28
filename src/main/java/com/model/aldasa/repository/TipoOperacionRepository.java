package com.model.aldasa.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.model.aldasa.entity.TipoOperacion;

public interface TipoOperacionRepository extends JpaRepository<TipoOperacion, Integer>{
	
	List<TipoOperacion> findByEstado(boolean estado);

}
