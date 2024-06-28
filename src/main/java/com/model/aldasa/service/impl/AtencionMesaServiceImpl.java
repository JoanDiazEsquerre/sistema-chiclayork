package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.AtencionMesaRepository;
import com.model.aldasa.repository.CajaRepository;
import com.model.aldasa.repository.DetalleCajaRepository;
import com.model.aldasa.repository.DocumentoVentaRepository;
import com.model.aldasa.repository.PisoRepository;
import com.model.aldasa.service.AtencionMesaService;
import com.model.aldasa.service.PerfilService;
import com.model.aldasa.service.PisoService;

@Service("atencionMesaService")
public class AtencionMesaServiceImpl implements AtencionMesaService {

	@Autowired
	private AtencionMesaRepository atencionMesaRepository;
	
	@Autowired
	private DocumentoVentaRepository documentoVentaRepository;
	
	@Autowired
	private DetalleCajaRepository detalleCajaRepository;

	@Override
	public Optional<AtencionMesa> findBy(Integer id) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findById(id);
	}

	@Override
	public AtencionMesa save(AtencionMesa atencionMesa, Caja caja, BigDecimal totalDetalle, boolean cobroCredito) {
		atencionMesaRepository.save(atencionMesa);
		
		if(atencionMesa.getEstado().equals("Eliminado")) {
			List<DetalleCaja> lstDet = detalleCajaRepository.findByAtencionMesa(atencionMesa);
			for(DetalleCaja d : lstDet) {
				d.setEstado(false);
				detalleCajaRepository.save(d);
			}
			
			if(atencionMesa.getDocumentoVenta()!=null) {
				atencionMesa.getDocumentoVenta().setEstado(false);
				documentoVentaRepository.save(atencionMesa.getDocumentoVenta());
			}
			
		}
		
		if(!atencionMesa.getTipoPago().equals("")) {
			if(atencionMesa.getEstado().equals("Cobrado")) {
				if(atencionMesa.getMonto().compareTo(BigDecimal.ZERO)!=0) {
					DetalleCaja detalle = new DetalleCaja();
					detalle.setCaja(caja);
					detalle.setTipoMovimiento("Ingreso");
					detalle.setDescripcion("Cobro de mesa #" + atencionMesa.getNumMesa()+", correlativo "+ atencionMesa.getId());
					detalle.setMonto(atencionMesa.getMonto());
					detalle.setFecha(new Date());
					detalle.setEstado(true);
					detalle.setAtencionMesa(atencionMesa);
					detalle.setOrigen("Efectivo");
					detalleCajaRepository.save(detalle);
				}
				
				if(atencionMesa.getMonto2().compareTo(BigDecimal.ZERO)!=0) {
					DetalleCaja detalle = new DetalleCaja();
					detalle.setCaja(caja);
					detalle.setTipoMovimiento("Ingreso");
					detalle.setDescripcion("Cobro de mesa #" + atencionMesa.getNumMesa()+", correlativo "+ atencionMesa.getId());
					detalle.setMonto(atencionMesa.getMonto2());
					detalle.setFecha(new Date());
					detalle.setEstado(true);
					detalle.setAtencionMesa(atencionMesa);
					detalle.setOrigen("POS");
					detalleCajaRepository.save(detalle);
				}
				
			}
			
		}
		
		return atencionMesa;
	}

	@Override
	public void delete(AtencionMesa entity) {
		// TODO Auto-generated method stub
		atencionMesaRepository.delete(entity);
	}

	@Override
	public AtencionMesa findByPisoAndNumMesaAndEstadoAndSucursal(Piso piso, int numMesa,String estado, Sucursal sucursal) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByPisoAndNumMesaAndEstadoAndSucursal(piso,numMesa, estado, sucursal);
	}

	@Override
	public Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado,Sucursal sucursal, Date fechaIni, Date fechaFin,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByEstadoAndSucursalAndFechaCobroBetween(estado, sucursal,fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<AtencionMesa> findByEstadoChefAndSucursalAndEstadoIn(String estadoChef, Sucursal sucursal,
			List<String> estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByEstadoChefAndSucursalAndEstadoIn(estadoChef, sucursal, estado, pageable);
	}

	@Override
	public Page<AtencionMesa> findBySucursalAndEstado(Sucursal sucursal, String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findBySucursalAndEstado(sucursal, estado, pageable);
	}

	@Override
	public Page<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado, Sucursal sucursal,
			Date fechaIni, Date fechaFin, boolean credito, Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(estado, sucursal, fechaIni, fechaFin, credito, pageable);
	}

	@Override
	public Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetween(
			String nombreCliente, String dniRuc, String estado, Sucursal sucursal, Date fechaIni, Date fechaFin,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetween(nombreCliente, dniRuc, estado, sucursal, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<AtencionMesa> findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetweenAndCredito(
			String nombreCliente, String dniRuc, String estado, Sucursal sucursal, Date fechaIni, Date fechaFin,
			boolean credito, Pageable pageable) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetweenAndCredito(nombreCliente, dniRuc, estado, sucursal, fechaIni, fechaFin, credito, pageable);
	}

	@Override
	public AtencionMesa save(AtencionMesa entity) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.save(entity);
	}

	@Override
	public List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetween(String estado, Sucursal sucursal,
			Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByEstadoAndSucursalAndFechaCobroBetween(estado, sucursal, fechaIni, fechaFin);
	}

	@Override
	public List<AtencionMesa> findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(String estado, Sucursal sucursal,
			Date fechaIni, Date fechaFin, boolean credito) {
		// TODO Auto-generated method stub
		return atencionMesaRepository.findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(estado, sucursal, fechaIni, fechaFin, credito); 
	}


}
