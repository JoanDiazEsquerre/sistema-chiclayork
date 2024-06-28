package com.model.aldasa.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.model.aldasa.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	List<Cliente> findByEstadoAndPersonaNatural (boolean estado, boolean personaNatural);
	List<Cliente> findByEstado (boolean estado);

	Cliente findByDniRucAndEstado (String dni, boolean estado);

}
