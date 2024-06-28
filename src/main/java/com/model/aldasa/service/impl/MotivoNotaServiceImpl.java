package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.MotivoNota;
import com.model.aldasa.repository.MotivoNotaRepository;
import com.model.aldasa.service.MotivoNotaService;

@Service("motivoNotaService")
public class MotivoNotaServiceImpl implements MotivoNotaService{

	@Autowired
	private MotivoNotaRepository motivoNotaRepository;

	@Override
	public Optional<MotivoNota> findById(Integer id) {
		// TODO Auto-generated method stub
		return motivoNotaRepository.findById(id);
	}

	@Override
	public MotivoNota save(MotivoNota entity) {
		// TODO Auto-generated method stub
		return motivoNotaRepository.save(entity);
	}

	@Override
	public void delete(MotivoNota entity) {
		// TODO Auto-generated method stub
		motivoNotaRepository.delete(entity);
	}

	@Override
	public List<MotivoNota> findByTipoDocumentoAndEstado(String tipoDocumento, boolean estado) {
		// TODO Auto-generated method stub
		return motivoNotaRepository.findByTipoDocumentoAndEstado(tipoDocumento, estado);
	}


}
