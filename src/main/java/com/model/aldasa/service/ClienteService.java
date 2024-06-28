package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;
import com.model.aldasa.entity.Cliente;

public interface ClienteService {
	
	Optional<Cliente> findById(Integer id);
	Cliente save(Cliente entity);
	void delete(Cliente entity);
	
	List<Cliente> findByEstadoAndPersonaNatural (boolean estado, boolean personaNatural);
	List<Cliente> findByEstado (boolean estado);
	Cliente findByDniRucAndEstado (String dni, boolean estado);


}
