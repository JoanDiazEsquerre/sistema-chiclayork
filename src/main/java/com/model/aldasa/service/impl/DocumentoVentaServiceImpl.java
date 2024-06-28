package com.model.aldasa.service.impl;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.repository.AtencionMesaRepository;
import com.model.aldasa.repository.CreditoRepository;
import com.model.aldasa.repository.DetalleCreditoRepository;
import com.model.aldasa.repository.DetalleDocumentoVentaRepository;
import com.model.aldasa.repository.DocumentoVentaRepository;
import com.model.aldasa.repository.SerieDocumentoRepository;
import com.model.aldasa.service.DocumentoVentaService;

@Service("documentoVentaService")
public class DocumentoVentaServiceImpl implements DocumentoVentaService{
	
	@Autowired
	private DocumentoVentaRepository documentoVentaRepository;
	
	@Autowired
	private SerieDocumentoRepository serieDocumentoRepository;
	
	@Autowired
	private DetalleDocumentoVentaRepository detalleDocumentoVentaRepository;
	
	@Autowired
	private AtencionMesaRepository atencionMesaRepository;
	
	@Autowired
	private CreditoRepository creditoRepository;
	
	@Autowired
	private DetalleCreditoRepository detalleCreditoRepository;

	@Override
	public Optional<DocumentoVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findById(id);
	}

	@Override
	public DocumentoVenta save(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.save(entity);
	}

	@Override
	public void delete(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		documentoVentaRepository.delete(entity);
	}
	
	@Transactional
	@Override
	public DocumentoVenta save(DocumentoVenta entity, List<DetalleDocumentoVenta> lstDetalleDocumentoVenta, SerieDocumento serieDocumento) {
		SerieDocumento serie = serieDocumentoRepository.findById(serieDocumento.getId()).get();
		String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));
		Integer aumento = Integer.parseInt(serie.getNumero())+1;
		
		
		entity.setNumero(numeroActual); 
		documentoVentaRepository.save(entity);
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
			
//			1. verificar si el detalle de venta tiene detalle credito
//			2. cambiar el estado del detralle credito (estadopagadi)
//			3. sumar a montoPagado (de la cabecera) el monto del detallecredito
//			4. validas si el montoPagado es igual a Total(de credito), cambias el estado a "Pagado" 
			
			if(d.getDetalleCredito()!=null) {
				d.getDetalleCredito().setEstadoPago(true);
				d.getDetalleCredito().getCredito().setMontoPagado(d.getDetalleCredito().getCredito().getMontoPagado().add(d.getDetalleCredito().getTotal()));
				
				if(d.getDetalleCredito().getCredito().getTotal().compareTo(d.getDetalleCredito().getCredito().getMontoPagado())==0) {
					d.getDetalleCredito().getCredito().setEstado("Cobrado");
				}
				detalleCreditoRepository.save(d.getDetalleCredito());
				creditoRepository.save(d.getDetalleCredito().getCredito());
			}
			
			d.setDocumentoVenta(entity);
			d.setEstado(true);
			detalleDocumentoVentaRepository.save(d);
			
				
		}  
		
		serie.setNumero(aumento+"");
		serieDocumentoRepository.save(serie);
		
		
//		if(entity.getAtencionMesa()!=null) {
//			entity.getAtencionMesa().setEstado("Cobrado");
//			entity.getAtencionMesa().setUsuarioCobro(entity.getUsuarioRegistro());
//			entity.getAtencionMesa().setFechaCobro(new Date());
//			entity.getAtencionMesa().setTipoDocumento(entity.getTipoDocumento());
//			entity.getAtencionMesa().setDocumentoVenta(entity);
//			atencionMesaRepository.save(entity.getAtencionMesa());
//		}
		
		

		return entity;

		
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(boolean estado,
			Sucursal sucursal, String razonSocial, String numero, String ruc, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(estado, sucursal, razonSocial, numero, ruc, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(estado, sucursal, razonSocial, numero, ruc, envioSunat, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat,
			TipoDocumento tipoDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(estado, sucursal, razonSocial, numero, ruc, envioSunat, tipoDocumento, pageable);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionAndEnvioSunatAndTipoDocumento(boolean estado, Sucursal sucursal, Date fechaEmision, boolean envioSunat, TipoDocumento tipoDoc) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionAndEnvioSunatAndTipoDocumento(estado, sucursal, fechaEmision, envioSunat, tipoDoc);
	}

	@Override
	public List<DocumentoVenta> findByDocumentoVentaRefAndTipoDocumentoAndEstado(DocumentoVenta documentoVenta,
			TipoDocumento tipoDocumento, boolean estado) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByDocumentoVentaRefAndTipoDocumentoAndEstado(documentoVenta, tipoDocumento, estado);
	}

	@Override
	public DocumentoVenta anular(DocumentoVenta entity) {
		if(entity.getDocumentoVentaRef()!=null) {
			if(entity.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("C")) {
				entity.getDocumentoVentaRef().setNotacredito(false);
				entity.getDocumentoVentaRef().setNumeroNotaCredito(null);

			}
			if(entity.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("D")) {
				entity.getDocumentoVentaRef().setNotaDebito(false);
				entity.getDocumentoVentaRef().setNumeroNotaDebito(null);
			}
			documentoVentaRepository.save(entity.getDocumentoVentaRef());
		}
		
		entity.setEstado(false);
		documentoVentaRepository.save(entity);
	
		
		return entity;
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni,
			Date fechaFin, String user, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento,
			Date fechaIni, Date fechaFin, String user, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni,
			Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin,
			String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento,
			Date fechaIni, Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal,
			Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionBetween(estado, sucursal, fechaIni, fechaFin, pageable);
	}

	@Override
	public DocumentoVenta findByEstadoAndAtencionMesaAndEnvioSunat(boolean estado, AtencionMesa atencionMesa,
			boolean envioSunat) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndAtencionMesaAndEnvioSunat(estado, atencionMesa, envioSunat); 
	}

	

}
