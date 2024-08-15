package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.AsistenciaRepository;
import com.model.aldasa.service.AsistenciaService;

@Service("asistenciaService")
public class AsistenciaServiceImpl  implements AsistenciaService  {


	@Autowired
	private AsistenciaRepository asistenciaRepository;
	
	@Override
	public Optional<Asistencia> findById(Integer id) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findById(id);
	}

	@Override
	public Asistencia save(Asistencia entity) {
		// TODO Auto-generated method stub
		return asistenciaRepository.save(entity);
	}

	@Override
	public void delete(Asistencia entity) {
		// TODO Auto-generated method stub
		asistenciaRepository.delete(entity);
	}

	@Override
	public Asistencia findByUsuarioPersona(Persona persona) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersona(persona);
	}

	@Override
	public List<Asistencia> findByUsuarioAndHoraBetweenAndEstadoOrderByHoraAsc(Usuario usuario, Date horaIni,
			Date horaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioAndHoraBetweenAndEstadoOrderByHoraAsc(usuario, horaIni, horaFin, estado);
	}

	@Override
	public List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo,
			Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByUsuarioPersonaDniAndTipoAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni,
			Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersonaDniAndTipoAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(String dni,
			String tipo, Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(String dni,
			String tipo, Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByUsuarioAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(Usuario usuario, String tipo,
			Date horaIni, Date horaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(usuario, tipo, horaIni, horaFin, estado);
	}

	@Override
	public Page<Asistencia> findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo,
			Date fechaIni, Date fechaFin, boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado, pageable);
	}


}
