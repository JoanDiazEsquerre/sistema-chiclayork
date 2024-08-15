package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;

public interface AsistenciaService {

	Optional<Asistencia> findById(Integer id);
	Asistencia save(Asistencia entity);
	void delete(Asistencia entity);
	
	Asistencia findByUsuarioPersona (Persona persona);
	
	List<Asistencia> findByUsuarioAndHoraBetweenAndEstadoOrderByHoraAsc(Usuario usuario, Date horaIni, Date horaFin, boolean estado);
	List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByUsuarioPersonaDniAndTipoAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	
	List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);

	List<Asistencia> findByUsuarioAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(Usuario usuario, String tipo, Date horaIni, Date horaFin, boolean estado);

	
	Page<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado, Pageable pageable);

}
