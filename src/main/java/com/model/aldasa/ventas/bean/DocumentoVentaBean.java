package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.json.simple.JSONObject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.DetalleCredito;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Identificador;
import com.model.aldasa.entity.MotivoNota;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.TipoOperacion;
import com.model.aldasa.fe.ConsumingPostBoImpl;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVentaElectronico;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.CreditoService;
import com.model.aldasa.service.DetalleCreditoService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.IdentificadorService;
import com.model.aldasa.service.MotivoNotaService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.service.TipoOperacionService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

@ManagedBean
@ViewScoped
public class DocumentoVentaBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{serieDocumentoService}")
	private SerieDocumentoService serieDocumentoService;
	
	@ManagedProperty(value = "#{motivoNotaService}")
	private MotivoNotaService motivoNotaService;
	
	@ManagedProperty(value = "#{consumingPostBo}")
	private ConsumingPostBoImpl consumingPostBo;
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService;
	
	@ManagedProperty(value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;
	
	@ManagedProperty(value = "#{reportGenBo}")
	private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{tipoOperacionService}")
	private TipoOperacionService tipoOperacionService;
	
	@ManagedProperty(value = "#{identificadorService}")
	private IdentificadorService identificadorService;
	
	@ManagedProperty(value = "#{productoService}")
	private ProductoService productoService;
	
	@ManagedProperty(value = "#{creditoService}")
	private CreditoService creditoService;
	
	@ManagedProperty(value = "#{detalleCreditoService}")
	private DetalleCreditoService detalleCreditoService;
	
	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	private LazyDataModel<Credito> lstCreditoLazy;
	
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = new ArrayList<>(); 
	private List<TipoDocumento> lstTipoDocumentoEnvioSunat = new ArrayList<>();
	private List<SerieDocumento> lstSerieNotaDocumento;
	private List<MotivoNota> lstMotivoNota = new ArrayList<>();
	private List<TipoOperacion> lstTipoOperacion = new ArrayList<>();
	private List<Identificador> lstIdentificador = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumento = new ArrayList<>();
	private List<SerieDocumento> lstSerieDocumento;
	private List<Cliente> lstCliente;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = new ArrayList<>();
	private List<Producto> lstProducto;
	private List<TipoDocumento> lstTipoDocumentoNota = new ArrayList<>();
	private List<Credito> lstCreditoSelected;

	
	private DocumentoVenta documentoVentaSelected ;
	private TipoDocumento tipoDocumentoNotaSelected;
	private SerieDocumento serieNotaDocumentoSelected ;
	private TipoOperacion tipoOperacionSelected;
	private Identificador identificadorSelected;
	private TipoDocumento tipoDocumentoSelected, tipoDocumentoEnvioSunat, tipoDocumentoFilter;
	private SerieDocumento serieDocumentoSelected ;
	private Persona personSelected;
	private Cliente clienteSelected;
	private DetalleDocumentoVenta detalleDocumentoVentaSelected;
	private Producto productoVoucher;
	private DetalleDocumentoVenta detalleDocumentoVenta;
	private MotivoNota motivoNotaSelected;


	private boolean personaNaturalCliente = true;
	
	private BigDecimal anticipos = BigDecimal.ZERO;
	private BigDecimal opGravada = BigDecimal.ZERO;
	private BigDecimal opExonerada = BigDecimal.ZERO;
	private BigDecimal opInafecta = BigDecimal.ZERO;
	private BigDecimal opGratuita = BigDecimal.ZERO;
	private BigDecimal descuentos = BigDecimal.ZERO;
	private BigDecimal ISC = BigDecimal.ZERO;
	private BigDecimal IGV = BigDecimal.ZERO;
	private BigDecimal otrosCargos = BigDecimal.ZERO;
	private BigDecimal otrosTributos = BigDecimal.ZERO;
	private BigDecimal importeTotal = BigDecimal.ZERO;



	private boolean estado = true;
	private Boolean estadoSunat;
	private String fechaTextoVista, montoLetra;
	private Date fechaEmisionNotaVenta = new Date() ;
	private String observacion, numero, numeroNota, razonSocialCliente, nombreComercialCliente,rucDniCliente, direccionCliente, email1Cliente, email2Cliente, email3Cliente  ; 
	private Date fechaEnvioSunat ;
	private Date fechaEmision = new Date() ;
	private String numeroDocumentoText, razonSocialText, direccionText, email1Text, email2Text, email3Text;

	private String tipoPago = "Contado";
	private String moneda = "S";
	private boolean incluirIgv = true;
	
    private DataSourceDocumentoVentaElectronico dt; 
	private Map<String, Object> parametros;

	private NumeroALetra numeroALetra = new  NumeroALetra();

	SimpleDateFormat sdf = new SimpleDateFormat("dd 'de'  MMMMM 'del' yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	
	private DataSourceDocumentoVentaElectronico dtAtencion;

	
	@PostConstruct
	public void init() {
		lstCreditoSelected = new ArrayList<>();
		List<String> lstCodigoFB=new ArrayList<>();
		lstCodigoFB.add("03");
		lstCodigoFB.add("01");
		lstTipoDocumento = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoFB);
		tipoDocumentoSelected = lstTipoDocumento.get(0);
		
		List<String> lstCodigoCD=new ArrayList<>();
		lstCodigoCD.add("07");
		lstCodigoCD.add("08");
		lstTipoDocumentoNota = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoCD);
		tipoDocumentoNotaSelected = lstTipoDocumentoNota.get(0);

		
		List<String> lstCodigoSunat=new ArrayList<>();
		lstCodigoSunat.add("03");
		lstCodigoSunat.add("01"); 
		lstCodigoSunat.add("07");
		lstCodigoSunat.add("08");
		lstTipoDocumentoEnvioSunat = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoSunat);
		tipoDocumentoEnvioSunat = lstTipoDocumentoEnvioSunat.get(0);
		tipoDocumentoFilter = null;
		
		iniciarLazy();
		listarSerie();
		iniciarLazyCredito();
		
		lstTipoOperacion = tipoOperacionService.findByEstado(true);
		lstIdentificador = identificadorService.findByEstado(true);
		
		fechaEnvioSunat= new Date();
		lstProducto = productoService.findByEstado(true);

	}
	
	public void buscar() {		
		
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			if(!numeroDocumentoText.equals("")) {
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String apiUrl = "https://api.apis.net.pe/v1/dni?numero="+numeroDocumentoText.trim();

		        HttpGet request = new HttpGet(apiUrl);

		        try {
		            HttpResponse response = httpClient.execute(request);
	 
		            int statusCode = response.getStatusLine().getStatusCode();
		            System.out.println("Status Code: " + statusCode);

		            if (statusCode == 200) {
		                String responseBody = EntityUtils.toString(response.getEntity());
		                System.out.println("Response: " + responseBody);
		                // Aquí puedes procesar la respuesta JSON según lo necesites
		                
		             // Parsear el JSON
		                org.json.JSONObject json = new org.json.JSONObject(responseBody); 

		                // Acceder a los campos del JSON
//		                String nombre = json.getString("nombre");

		                
	                	razonSocialText = json.getString("nombre");
		                direccionText = json.getString("direccion");
		                  
		                
		            } else {
		                System.out.println("Error al obtener la respuesta. Código de estado: " + statusCode);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}else {
			if(!numeroDocumentoText.equals("")) {
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String apiUrl = "https://api.apis.net.pe/v1/ruc?numero="+numeroDocumentoText.trim();

		        HttpGet request = new HttpGet(apiUrl);

		        try {
		            HttpResponse response = httpClient.execute(request);
	 
		            int statusCode = response.getStatusLine().getStatusCode();
		            System.out.println("Status Code: " + statusCode);

		            if (statusCode == 200) {
		                String responseBody = EntityUtils.toString(response.getEntity());
		                System.out.println("Response: " + responseBody);
		                // Aquí puedes procesar la respuesta JSON según lo necesites
		                
		             // Parsear el JSON
		                org.json.JSONObject json = new org.json.JSONObject(responseBody);

		                // Acceder a los campos del JSON
//		                String nombre = json.getString("nombre");

		                razonSocialText = json.getString("nombre");
		                direccionText = json.getString("direccion");
		                
		            } else {
		                System.out.println("Error al obtener la respuesta. Código de estado: " + statusCode);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}
		
	}

	
	public void calcularCantidadDetalle(DetalleDocumentoVenta detalle, int row) {
		
		if(detalle.getPrecioUnitario()!=null) {
			if(detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO)==0) {
				addErrorMessage("El precio unitario debe ser mayor a 0.");
				return;
			}
		}
				
		detalle.setCantidad(detalle.getCantidad());
		detalle.setValorUnitario(detalle.getPrecioUnitario().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
		detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
		
		BigDecimal sinIGV = detalle.getTotal().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
		detalle.setImporteVentaSinIgv(sinIGV); 
		detalle.setPrecioSinIgv(sinIGV);
	
		calcularTotales();
	}
	
	public void importarCredito() {
		
		if(lstCreditoSelected.isEmpty()) {
			addErrorMessage("Debes seleccionar al menos un crédito.");
			return;
		}
		
		for(Credito c : lstCreditoSelected) {
			List<DetalleCredito> lstDetalleCredito = detalleCreditoService.findByCreditoAndEstadoAndEstadoPago(c, true, false);
			for(DetalleCredito d : lstDetalleCredito) {
				BigDecimal montoSinIgv = d.getTotal().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
				
				DetalleDocumentoVenta det = new DetalleDocumentoVenta();
				det.setDocumentoVenta(null);
				det.setProducto(d.getPlato().getProducto());
				det.setDescripcion(d.getPlato().getNombre() + "-" + sdf.format(d.getCredito().getFecha()));
				det.setCantidad(d.getCantidad());
				det.setValorUnitario(d.getPrecioUnitario().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
				det.setPrecioUnitario(d.getPrecioUnitario());
				det.setTotal(d.getTotal());
				det.setEstado(true);
				det.setImporteVentaSinIgv(montoSinIgv);
				det.setPrecioSinIgv(montoSinIgv);
				det.setDetalleCredito(d);
				
				if(!lstDetalleDocumentoVenta.isEmpty()) {
					lstDetalleDocumentoVenta.add(det);
				}else {
					boolean encuentra = false;
					for(DetalleDocumentoVenta ddv:lstDetalleDocumentoVenta) {
						
						if(ddv.getDetalleCredito().getId().equals(d.getId())) {
							encuentra=true;
						}
						
					}
					if(!encuentra) {
						lstDetalleDocumentoVenta.add(det);
					}
				}
				
				
								
			}
			
		}
		calcularTotales();
		
		lstCreditoSelected= new ArrayList<>(); 
		addInfoMessage("Crédito importado correctamente.");
	}
	
	public void enviarDocumentoSunat() {
		if(!documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("N")) {		
			//ENVIAMOS FACTURA A NUBEFACT
	        //JSONObject json_rspta = null;
	        JSONObject json_rspta = consumingPostBo.apiConsume(documentoVentaSelected, lstDetalleDocumentoVentaSelected);
	        System.out.println("Error => " + json_rspta);
	        if (json_rspta != null) {
	            if (json_rspta.get("errors") != null) {
	                System.out.println("Error => " + json_rspta.get("errors"));
	                addErrorMessage("Error al Enviar el comprobante electrónico: " + json_rspta.get("errors"));
	            } else {
	            	documentoVentaSelected.setEnvioSunat(true);
	                /*JSONParser parsearRsptaDetalleOK = new JSONParser();
	                JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());*/
	
	                if (json_rspta.get("aceptada_por_sunat") != null) {
	                    documentoVentaSelected.setEnvioAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
	                }
	
	                if (json_rspta.get("sunat_description") != null) {
	                	documentoVentaSelected.setEnvioSunatDescription(json_rspta.get("sunat_description").toString());
	                }
	
	                if (json_rspta.get("sunat_note") != null) {
	                	documentoVentaSelected.setEnvioSunatNote(json_rspta.get("sunat_note").toString());
	                }
	
	                if (json_rspta.get("sunat_responsecode") != null) {
	                	documentoVentaSelected.setEnvioSunatResponseCode(json_rspta.get("sunat_responsecode").toString());
	                }
	
	                if (json_rspta.get("sunat_soap_error") != null) {
	                	documentoVentaSelected.setEnvioSunatSoapError(json_rspta.get("sunat_soap_error").toString());
	                }
	
	                if (json_rspta.get("enlace_del_pdf") != null) {
	                	documentoVentaSelected.setEnvioEnlaceDelPdf(json_rspta.get("enlace_del_pdf").toString());
	                }
	
	                if (json_rspta.get("enlace_del_xml") != null) {
	                	documentoVentaSelected.setEnvioEnlaceDelXml(json_rspta.get("enlace_del_xml").toString());
	                }
	
	                if (json_rspta.get("enlace_del_cdr") != null) {
	                	documentoVentaSelected.setEnvioEnlaceDelCdr(json_rspta.get("enlace_del_cdr").toString());
	                }
	
	                if (json_rspta.get("cadena_para_codigo_qr") != null) {
	                	documentoVentaSelected.setEnvioCadenaCodigoQr(json_rspta.get("cadena_para_codigo_qr").toString());
	                }
	
	                if (json_rspta.get("codigo_hash") != null) {
	                	documentoVentaSelected.setEnvioCodigoHash(json_rspta.get("codigo_hash").toString());
	                }
	
	                documentoVentaService.save(documentoVentaSelected);
	                addInfoMessage("Documento Electrónico enviado a Sunat Correctamente...");
	            }
	        }
		
		}else {
			addErrorMessage("No se pueden enviar notas de venta a SUNAT.");
		}
	}
	
	public void calcularTotales() {
		anticipos = BigDecimal.ZERO;
		opInafecta=BigDecimal.ZERO;
		importeTotal=BigDecimal.ZERO;
		opGravada=BigDecimal.ZERO;
		IGV = BigDecimal.ZERO;
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				importeTotal= importeTotal.add(d.getTotal());
				
			}
		}
		
		BigDecimal montoSinIgv = importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
		IGV = IGV.add(importeTotal.subtract(montoSinIgv));
		opGravada = opGravada.add(montoSinIgv);
	} 
	
	public void saveDocumentoVenta() {
		
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
			BigDecimal sinIGV = d.getTotal().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
			d.setImporteVentaSinIgv(sinIGV); 
			d.setPrecioSinIgv(sinIGV);
		}
		
		calcularTotales();
//		aqui actualiza los datos del cliente y guarda run razon doreccion
		clienteSelected = clienteService.findByDniRucAndEstado(numeroDocumentoText, true);
		if(clienteSelected==null) {
			clienteSelected = new Cliente();	
			clienteSelected.setEstado(true);
			clienteSelected.setPersonaNatural(tipoDocumentoSelected.getAbreviatura().equals("B")?true:false);
			clienteSelected.setFechaRegistro(new Date());
			clienteSelected.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
		}
		
		clienteSelected.setDniRuc(numeroDocumentoText);
		clienteSelected.setRazonSocial(razonSocialText);
		clienteSelected.setNombreComercial(razonSocialText); 
		clienteSelected.setDireccion(direccionText);
		clienteSelected.setEmail1Fe(email1Text);
		clienteSelected.setEmail2Fe(email2Text);
		clienteSelected.setEmail3Fe(email3Text);
		
		
	
		if(email1Text!=null) {
			if(email1Text.equals("")) {
				clienteSelected.setEmail1Fe(null);
			}
		}
		
		if(email2Text!=null) {
			if(email2Text.equals("")) {
				clienteSelected.setEmail2Fe(null);
			}
		}
		
		if(email3Text!=null) {
			if(email3Text.equals("")) {
				clienteSelected.setEmail3Fe(null);
			}
		}
		
		clienteService.save(clienteSelected);
		listarClientes();
		
		
		DocumentoVenta documentoVenta = new DocumentoVenta();
		documentoVenta.setCliente(clienteSelected);
		documentoVenta.setDocumentoVentaRef(null);
		documentoVenta.setAtencionMesa(null);
		documentoVenta.setSucursal(navegacionBean.getSucursalLogin());
		documentoVenta.setTipoDocumento(tipoDocumentoSelected);
		documentoVenta.setSerie(serieDocumentoSelected.getSerie());
		documentoVenta.setNumero(""); // vamos a setear el numero despues de haber guardado el documento
		documentoVenta.setRuc(clienteSelected.getDniRuc());	
		documentoVenta.setRazonSocial(clienteSelected.getRazonSocial());
		documentoVenta.setNombreComercial(clienteSelected.getNombreComercial());
		documentoVenta.setDireccion(clienteSelected.getDireccion());
		documentoVenta.setFechaEmision(fechaEmision);
		documentoVenta.setFechaVencimiento(fechaEmision);
		documentoVenta.setTipoMoneda(moneda);
		documentoVenta.setObservacion("");
		documentoVenta.setTipoPago(tipoPago);
		if(incluirIgv) {
			documentoVenta.setSubTotal(opGravada);
		}else {
			documentoVenta.setSubTotal(importeTotal);
		}
		documentoVenta.setIgv(IGV);
		documentoVenta.setTotal(importeTotal);
		documentoVenta.setFechaRegistro(new Date());
		documentoVenta.setUsuarioRegistro(navegacionBean.getUsuarioLogin());
		documentoVenta.setEstado(true);
		documentoVenta.setAnticipos(anticipos);
		documentoVenta.setOpGravada(opGravada);
		documentoVenta.setOpExonerada(opExonerada);
		documentoVenta.setOpInafecta(opInafecta);
		documentoVenta.setOpGratuita(opGratuita);
		documentoVenta.setDescuentos(descuentos);
		documentoVenta.setIsc(ISC);
		documentoVenta.setOtrosCargos(otrosCargos);
		documentoVenta.setOtrosTributos(otrosTributos);
		
		
		DocumentoVenta documento = documentoVentaService.save(documentoVenta, lstDetalleDocumentoVenta, serieDocumentoSelected); 
		if(documento != null) {
	//		int envio =enviarDocumentoSunat(documento, lstDetalleDocumentoVenta);
			
			lstDetalleDocumentoVenta.clear();// claer es limpiar en ingles prueba
			clienteSelected=null;
			calcularTotales();
	//		
	//		subirImagenes(documento.getId() + "", documento);
	//		setearInfoVoucher();
			
			numeroDocumentoText="";
			razonSocialText = "";
			direccionText = "";
			email1Text = "";
			email2Text = "";
			email3Text = "";
			incluirIgv=false;
			
	//		String addMensaje = envio>0?"Se envio correctamente a SUNAT":"No se pudo enviar a SUNAT";
			addInfoMessage("Se guardó el documento correctamente. ");
			
		}else {
			addErrorMessage("No se puede guardar el documento."); 
			return;
		}
	
	}
	
	public void eliminarDetalleVenta(int index) {
		lstDetalleDocumentoVenta.remove(index);
		if(lstDetalleDocumentoVenta.isEmpty()) {
			clienteSelected = null;
//			persona=null;
		}
		calcularTotales();
		addInfoMessage("Detalle eliminado");
		
	}
	
	public void anularDocumento() {
		//si es boleta y  anulo el mismo de la emision, mandar mensaje de espererar 24 horas
		if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")) {
			String fechaEmi = sdf.format(documentoVentaSelected.getFechaEmision());
			String fechaactual = sdf.format(new Date());
			if(fechaEmi.equals(fechaactual)) {
				addWarnMessage("Debe esperar 24 horas para poder anular el comprobante."); 
			}
			
		}
		
		if(!documentoVentaSelected.isEnvioSunat()) {
			anulacionFinalDeDocumento();
			return;
		}
		//ENVIAMO LA BAJA A NUBEFACT
        JSONObject json_rspta = consumingPostBo.apiConsumeDelete(documentoVentaSelected);

        if (json_rspta != null) {
            if (json_rspta.get("errors") != null) {
                addErrorMessage("Error al Enviar Anulación de comprobante electrónico: " + json_rspta.get("errors").toString());
            } else {

                if (json_rspta.get("sunat_ticket_numero") != null) {
                    documentoVentaSelected.setAnulacionSunatTicketNumero(json_rspta.get("sunat_ticket_numero").toString());
                    addInfoMessage("Se envió la baja a Sunat correctamente Nro de Ticket:" + json_rspta.get("sunat_ticket_numero").toString());
                }

                if (json_rspta.get("aceptada_por_sunat") != null) {
                    documentoVentaSelected.setAnulacionAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
                }

                if (json_rspta.get("sunat_description") != null) {
                    documentoVentaSelected.setAnulacionSunatDescription(json_rspta.get("sunat_description").toString());
                }

                if (json_rspta.get("sunat_note") != null) {
                    documentoVentaSelected.setAnulacionSunatNote(json_rspta.get("sunat_note").toString());
                }

                if (json_rspta.get("sunat_responsecode") != null) {
                    documentoVentaSelected.setAnulacionSunatResponsecode(json_rspta.get("sunat_responsecode").toString());
                }

                if (json_rspta.get("sunat_soap_error") != null) {
                    documentoVentaSelected.setAnulacionSunatSoapError(json_rspta.get("sunat_soap_error").toString());
                }

                anulacionFinalDeDocumento();
        		
        			
            }
        }
	
	}
	
	private void anulacionFinalDeDocumento() {
		DocumentoVenta doc= documentoVentaService.anular(documentoVentaSelected);
		if(doc!=null) {
			addInfoMessage("Documento de venta anulado.");	
		}else {
			addErrorMessage("No se pudo anular el documento.");
		}
	}
	
	public void saveNota() {
		List<DocumentoVenta> lstBuscarNotaExistente = documentoVentaService.findByDocumentoVentaRefAndTipoDocumentoAndEstado(documentoVentaSelected, tipoDocumentoNotaSelected, true);
		
		if(!lstBuscarNotaExistente.isEmpty()) {
			addErrorMessage( "Ya se registró una " + tipoDocumentoNotaSelected.getDescripcion() + " para la " + documentoVentaSelected.getTipoDocumento().getDescripcion());
			return; 
		}
		
		DocumentoVenta doc = new DocumentoVenta();
		doc.setCliente(documentoVentaSelected.getCliente());
		doc.setDocumentoVentaRef(documentoVentaSelected);
		doc.setSucursal(documentoVentaSelected.getSucursal());
		doc.setTipoDocumento(tipoDocumentoNotaSelected);
		doc.setSerie(serieNotaDocumentoSelected.getSerie());
		doc.setNumero(numeroNota);
		doc.setRuc(documentoVentaSelected.getRuc());
		doc.setRazonSocial(documentoVentaSelected.getRazonSocial());
		doc.setNombreComercial(documentoVentaSelected.getNombreComercial());
		doc.setDireccion(documentoVentaSelected.getDireccion());
		doc.setFechaEmision(fechaEmisionNotaVenta);
		doc.setFechaVencimiento(fechaEmisionNotaVenta);
		doc.setTipoMoneda(documentoVentaSelected.getTipoMoneda());
		doc.setObservacion("");
		doc.setTipoPago(documentoVentaSelected.getTipoPago());
		doc.setSubTotal(documentoVentaSelected.getSubTotal());
		doc.setIgv(documentoVentaSelected.getIgv());
		doc.setTotal(documentoVentaSelected.getTotal());
		doc.setFechaRegistro(new Date());
		doc.setUsuarioRegistro(documentoVentaSelected.getUsuarioRegistro());
		doc.setEstado(true);
		doc.setAnticipos(documentoVentaSelected.getAnticipos());
		doc.setOpGravada(documentoVentaSelected.getOpGravada());
		doc.setOpExonerada(documentoVentaSelected.getOpExonerada());
		doc.setOpInafecta(documentoVentaSelected.getOpInafecta());
		doc.setOpGratuita(documentoVentaSelected.getOpGratuita());
		doc.setDescuentos(documentoVentaSelected.getDescuentos());
		doc.setIsc(documentoVentaSelected.getIsc());
		doc.setOtrosCargos(documentoVentaSelected.getOtrosCargos());
		doc.setOtrosTributos(documentoVentaSelected.getOtrosTributos());
		doc.setMotivoNota(motivoNotaSelected);
		doc.setTipoOperacion(tipoOperacionSelected);
		doc.setIdentificador(identificadorSelected);
		
		DocumentoVenta saveDocNota = documentoVentaService.save(doc);
		if(saveDocNota!=null) {
			SerieDocumento serie = serieDocumentoService.findById(serieNotaDocumentoSelected.getId()).get();
			String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));

			Integer aumento = Integer.parseInt(serie.getNumero())+1;

			serie.setNumero(aumento+"");
			serieDocumentoService.save(serie);

			saveDocNota.setNumero(numeroActual); 
			documentoVentaService.save(saveDocNota);
			
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaSelected) {
				d.setId(null);
				d.setDocumentoVenta(saveDocNota);
				d.setEstado(true);
				detalleDocumentoVentaService.save(d);	
			} 
			
//			aqui actualizamos los campos del documento de origen
			
			if(tipoDocumentoNotaSelected.getAbreviatura().equals("C")) {
				documentoVentaSelected.setNotacredito(true);
				documentoVentaSelected.setNumeroNotaCredito(saveDocNota.getSerie() + "-" + saveDocNota.getNumero());
			}else {
				documentoVentaSelected.setNotaDebito(true);
				documentoVentaSelected.setNumeroNotaDebito(saveDocNota.getSerie() + "-" + saveDocNota.getNumero());
			}
			documentoVentaService.save(documentoVentaSelected);
			
			addInfoMessage("Se guardó el documento correctamente.");
			PrimeFaces.current().executeScript("PF('notaCreditoDebitoDialog').hide();");
		}else {
			addErrorMessage("No se pudo guardar la nota.");
		}
	}
	
	public void exportarPDF() {

		dtAtencion = new DataSourceDocumentoVentaElectronico();
        for (DetalleDocumentoVenta detalle : lstDetalleDocumentoVentaSelected) {
        	dtAtencion.addResumenDetalle(detalle);
        }

        parametros = new HashMap<String, Object>();

        parametros.put("PATTERN", Utils.getPattern());
        
        parametros.put("DIRECCIONSUCURSAL", navegacionBean.getSucursalLogin().getDireccion());
        parametros.put("RUCSUCURSAL", navegacionBean.getSucursalLogin().getRuc());
        parametros.put("TELEFONOSUCURSAL", navegacionBean.getSucursalLogin().getTelefono());
        parametros.put("NUMEROOPERACION", documentoVentaSelected.getSerie() + "-" + documentoVentaSelected.getNumero());
        parametros.put("DNI", documentoVentaSelected.getRuc());
        parametros.put("FECHAEMISION",sdf2.format(documentoVentaSelected.getFechaEmision()) );
        parametros.put("CAJERO", documentoVentaSelected.getUsuarioRegistro().getUsername());
        parametros.put("OPGRAVADA", documentoVentaSelected.getOpGravada());
        parametros.put("IGV", documentoVentaSelected.getIgv());
        parametros.put("TOTAL", documentoVentaSelected.getTotal());
        parametros.put("TIPODOCUMENTO", documentoVentaSelected.getTipoDocumento().getDescripcion());
        parametros.put("DIRECCIONCLIENTE", documentoVentaSelected.getDireccion());
        
        if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")) {
        	parametros.put("TIPODNIRUC", "DNI: ");
        }else if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("F")) {
        	parametros.put("TIPODNIRUC", "RUC: ");
        }else {
        	parametros.put("TIPODNIRUC", "DNI: ");
        }
        
        parametros.put("CLIENTE", documentoVentaSelected.getRazonSocial());
        parametros.put("MONTOLETRA", montoLetra);
        parametros.put("QR", navegacionBean.getSucursalLogin().getRuc() + "|" + documentoVentaSelected.getTipoDocumento().getCodigo() + "|" + 
	    		documentoVentaSelected.getSerie() + "|" + documentoVentaSelected.getNumero() + "|" + "0" + "|" + documentoVentaSelected.getTotal() + "|" + 
	    		sdf2.format(documentoVentaSelected.getFechaEmision()) + "|" + (documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")?"1":"6") + "|" + documentoVentaSelected.getRuc() + "|");


        String path = "secured/view/modulos/ventas/reportes/jasper/documentoElectronico.jasper";

        reportGenBo.exportByFormatNotConnectDb(dtAtencion, path, "pdf", parametros, "DOCUMENTO ELECTRÓNICO");
        dtAtencion = null;
        parametros = null;
    }
	
	
	
	public void editarCorreoCliente() {
		if(documentoVentaSelected.getCliente().getEmail1Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail1Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail1Fe(null);
			}
		}
		
		if(documentoVentaSelected.getCliente().getEmail2Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail2Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail2Fe(null);
			}
		}
		
		if(documentoVentaSelected.getCliente().getEmail3Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail3Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail3Fe(null);
			}
		}
		
		clienteService.save(documentoVentaSelected.getCliente());
		addInfoMessage("Se actualizó el correo correctamente");
	}
	
	public List<Cliente> completeCliente(String query) {
        List<Cliente> lista = new ArrayList<>();
        for (Cliente c : lstCliente) {
    		if (c.getDniRuc().toUpperCase().contains(query.toUpperCase()) ){
                lista.add(c);
            } 
        }
        return lista;
    }
	
	public void cambioIgv() {
		calcularTotales();
		if(importeTotal.compareTo(BigDecimal.ZERO)>0) {
			if(incluirIgv) {
				BigDecimal opGrav = importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
				BigDecimal igv = importeTotal.subtract(opGrav);
				igv = igv.setScale(2, RoundingMode.HALF_UP);
				
				opInafecta = BigDecimal.ZERO;
				opGravada = opGrav;
				IGV = igv;
				
				for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
					BigDecimal sinIGV = d.getTotal().divide(new BigDecimal(1.18), 2, RoundingMode.HALF_UP);
					
					d.setImporteVentaSinIgv(sinIGV); 
					d.setPrecioSinIgv(sinIGV);
				}
				
			}else {
				opInafecta = importeTotal;
				opGravada = BigDecimal.ZERO;
				IGV = BigDecimal.ZERO;
				
				for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
					d.setImporteVentaSinIgv(BigDecimal.ZERO); 
					d.setPrecioSinIgv(BigDecimal.ZERO);
				}
			}
		}
	}
	
	
	
	public void agregarDetalle() {
		DetalleDocumentoVenta det = new DetalleDocumentoVenta();
		det.setDescripcion(""); 
		det.setProducto(lstProducto.get(0));
		det.setTotal(BigDecimal.ZERO);
		det.setCantidad(new BigDecimal(1));
		det.setPrecioUnitario(BigDecimal.ZERO);
		det.setValorUnitario(BigDecimal.ZERO);
		det.setEstado(true); 
		det.setImporteVentaSinIgv(BigDecimal.ZERO);
		det.setPrecioSinIgv(BigDecimal.ZERO); 
		lstDetalleDocumentoVenta.add(det);
		cambioIgv();
	}
	
	public boolean validarRuc(String ruc) {
		if(ruc.length()!=11) {
			addErrorMessage("El RUC debe tener 11 dígitos.");
			return false;
		}
		
		boolean valor = false;
		
		
		String primerosNumeros =ruc.substring(0,2);
		
		if(primerosNumeros.equals("10"))valor = true;
		
		if(primerosNumeros.equals("15"))valor = true;
		
		if(primerosNumeros.equals("17"))valor = true;
		
		if(primerosNumeros.equals("20"))valor = true;
		
		if(!valor) addErrorMessage("Ruc incorrecto, debe iniciar con 10, 15, 17 o 20");
		
		
		
		return valor;
	}
	
	public void validarFormularioDocumentoVenta() {
		if(lstDetalleDocumentoVenta.isEmpty()) { 
			addErrorMessage("Debes haber al menos un detalle.");
			return;
		}else {
			for(DetalleDocumentoVenta det : lstDetalleDocumentoVenta) {
				if(det.getDescripcion().equals("")) {
					addErrorMessage("Todos los detalles deben tener una descripcion.");
					return;
				}
				
				if(det.getPrecioUnitario() == null) {
					addErrorMessage("Todos los precios unitarios de los detalles deben ser mayor que cero.");
					return;
				}else if(det.getPrecioUnitario().compareTo(BigDecimal.ZERO)==0) {
					addErrorMessage("Todos los precios unitarios de los detalles deben ser mayor que cero.");
					return;
				}
			}
		}
		
		
		if(numeroDocumentoText.equals("")) {
			addErrorMessage("Ingresar DNI/RUC");
			return;
		}else {
			if(tipoDocumentoSelected.getAbreviatura().equals("F")) {
				if(!validarRuc(numeroDocumentoText)) {
					return;
				}
			}else {
				if(numeroDocumentoText.length()!=8) {
					addErrorMessage("El DNI debe tener 8 dígitos."); 
					return;
				}
			}
		}
		
		if(razonSocialText.equals("")) {
			addErrorMessage("Ingresar Razon Social para el cliente");
			return;
		}
		if(direccionText.equals("")) {
			addErrorMessage("Ingresar Direccion para el cliente");
			return;
		}
		
		if(importeTotal.compareTo(new BigDecimal(700))>0) {
			if(numeroDocumentoText.equals("99999999")) { 
				addErrorMessage("Los importes mayores a 700 soles, deben tener asigado a una persona o empresa."); 
				return;
			}
		}
		
		
		for(DetalleDocumentoVenta d : lstDetalleDocumentoVenta) {
			if(d.getPrecioUnitario()==null) {
				addErrorMessage("Debes ingresar un importe mayor a 0 en precio unitario en todos los detalles.");
				return;
			}
			if(d.getPrecioUnitario().compareTo(BigDecimal.ZERO)==0) {
				addErrorMessage("Debes ingresar un importe mayor a 0 en precio unitario en todos los detalles.");
				return;
			}
		}
		
		PrimeFaces.current().executeScript("PF('saveDocumento').show();");

	}
	
	public void onChangeCliente() {
		if(clienteSelected !=null) {
			numeroDocumentoText = clienteSelected.getDniRuc();
			razonSocialText = clienteSelected.getRazonSocial();
			direccionText = clienteSelected.getDireccion();
			email1Text = clienteSelected.getEmail1Fe();
			email2Text = clienteSelected.getEmail2Fe();
			email3Text = clienteSelected.getEmail3Fe();
		}else {
			numeroDocumentoText="";
			razonSocialText = "";
			direccionText = "";
			email1Text = "";
			email2Text = "";
			email3Text = "";
		}
	
	}
	
	public void iniciarDatosCliente() {
		personaNaturalCliente = true;
		personSelected=null;
		razonSocialCliente = "";
		nombreComercialCliente = "";
		rucDniCliente = "";
		direccionCliente = "";
		email1Cliente = "";
		email2Cliente = "";
		email3Cliente = "";
	}
	
	public void cambiarSerie() {
		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
	}
	
	public void listarSerie() {
		lstSerieDocumento = serieDocumentoService.findByTipoDocumentoAndSucursal(tipoDocumentoSelected, navegacionBean.getSucursalLogin());
		serieDocumentoSelected=lstSerieDocumento.get(0);

		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
//		changeTipoDocumentoVenta();
		
		listarClientes();
	}
	
	public void listarClientes() {
		if(tipoDocumentoSelected.getAbreviatura().equals("F")) {
			lstCliente = clienteService.findByEstadoAndPersonaNatural(true, false);
		}else {
			lstCliente = clienteService.findByEstadoAndPersonaNatural(true, true);
		}
	}
	
	public void validacionFecha() {
		if(fechaEmision==null) {
			fechaEmision = new Date();
		}
	}
	
	public void enviarDocumentoSunatMasivo() {
		if(fechaEnvioSunat ==  null) {
			addErrorMessage("Seleccione una fecha.");
			return;
		}
		
		List<DocumentoVenta> lstDoc = documentoVentaService.findByEstadoAndSucursalAndFechaEmisionAndEnvioSunatAndTipoDocumento(true, navegacionBean.getSucursalLogin(), fechaEnvioSunat, false, tipoDocumentoEnvioSunat);
		if(!lstDoc.isEmpty()) {
			int cont = 0;
			for(DocumentoVenta d:lstDoc) {
				List<DetalleDocumentoVenta> lstDetalle = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(d, true);
				cont = cont+enviarDocumentoSunat(d, lstDetalle); 
//				consumingPostBo.apiConsume(d, lstDetalle);
				
//				d.setEnvioSunat(true);
//				documentoVentaService.save(d);
				
			}
			addInfoMessage("Se enviaron correctamente "+ cont + " documentos a SUNAT.");
			
		}else {
			addErrorMessage("No se encontraron documentos pendientes de envio para el dia "+ sdf.format(fechaEnvioSunat));
		}
		
	}
	
	public void listarMotivoNota() {
		
		lstMotivoNota = motivoNotaService.findByTipoDocumentoAndEstado(tipoDocumentoNotaSelected.getAbreviatura(), true);
		
	}
	
	public void listarSerieNota() {
		
		if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B") || documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("F")) {
			String anio = sdfYear.format(fechaEmisionNotaVenta);
			
			String codigoInt = "";
			
			if(tipoDocumentoNotaSelected.getAbreviatura().equals("C")) {
				codigoInt = "NC" + documentoVentaSelected.getTipoDocumento().getAbreviatura();
			}else {
				codigoInt = "ND" + documentoVentaSelected.getTipoDocumento().getAbreviatura();
			}
			
			
			lstSerieNotaDocumento = serieDocumentoService.findByTipoDocumentoAndAnioAndSucursalAndCodigoInterno(tipoDocumentoNotaSelected, anio, navegacionBean.getSucursalLogin(), codigoInt);
			serieNotaDocumentoSelected=lstSerieNotaDocumento.get(0);

			numeroNota =  String.format("%0" + serieNotaDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieNotaDocumentoSelected.getNumero()) ); 
			listarMotivoNota();	
			listarDetalleDocumentoVenta();
			tipoOperacionSelected=lstTipoOperacion.get(0);
			identificadorSelected=lstIdentificador.get(0);
		}else {
			addErrorMessage("Debe seleccionar una factura o boleta.");
			return;
		}
		PrimeFaces.current().executeScript("PF('notaCreditoDebitoDialog').show();");
		
	}
	
	public void listarDetalleDocumentoVenta( ) {
		montoLetra = numeroALetra.Convertir(documentoVentaSelected.getTotal()+"", true, "SOLES");
		lstDetalleDocumentoVentaSelected = new ArrayList<>();
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVentaSelected, true);
	}

	public int enviarDocumentoSunat(DocumentoVenta docVenta, List<DetalleDocumentoVenta> lstDetalle) {
		int num = 0;
		//ENVIAMOS FACTURA A NUBEFACT
        //JSONObject json_rspta = null;
        JSONObject json_rspta = consumingPostBo.apiConsume(docVenta, lstDetalle);
        System.out.println("Error => " + json_rspta);
        if (json_rspta != null) {
            if (json_rspta.get("errors") != null) {
                System.out.println("Error => " + json_rspta.get("errors"));
                addErrorMessage("Error al Enviar el comprobante electrónico: " + json_rspta.get("errors"));
            } else {
            	num++;
            	docVenta.setEnvioSunat(true);
                /*JSONParser parsearRsptaDetalleOK = new JSONParser();
                JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());*/

                if (json_rspta.get("aceptada_por_sunat") != null) {
                	docVenta.setEnvioAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
                }

                if (json_rspta.get("sunat_description") != null) {
                	docVenta.setEnvioSunatDescription(json_rspta.get("sunat_description").toString());
                }

                if (json_rspta.get("sunat_note") != null) {
                	docVenta.setEnvioSunatNote(json_rspta.get("sunat_note").toString());
                }

                if (json_rspta.get("sunat_responsecode") != null) {
                	docVenta.setEnvioSunatResponseCode(json_rspta.get("sunat_responsecode").toString());
                }

                if (json_rspta.get("sunat_soap_error") != null) {
                	docVenta.setEnvioSunatSoapError(json_rspta.get("sunat_soap_error").toString());
                }

                if (json_rspta.get("enlace_del_pdf") != null) {
                	docVenta.setEnvioEnlaceDelPdf(json_rspta.get("enlace_del_pdf").toString());
                }

                if (json_rspta.get("enlace_del_xml") != null) {
                	docVenta.setEnvioEnlaceDelXml(json_rspta.get("enlace_del_xml").toString());
                }

                if (json_rspta.get("enlace_del_cdr") != null) {
                	docVenta.setEnvioEnlaceDelCdr(json_rspta.get("enlace_del_cdr").toString());
                }

                if (json_rspta.get("cadena_para_codigo_qr") != null) {
                	docVenta.setEnvioCadenaCodigoQr(json_rspta.get("cadena_para_codigo_qr").toString());
                }

                if (json_rspta.get("codigo_hash") != null) {
                	docVenta.setEnvioCodigoHash(json_rspta.get("codigo_hash").toString());
                }

                documentoVentaService.save(docVenta);
//                addInfoMessage("Documento Electrónico enviado a Sunat Correctamente...");
            }
        }
        return num;
		
	}
	
	public void iniciarLazy() {
		lstDocumentoVentaLazy = new LazyDataModel<DocumentoVenta>() {
			private List<DocumentoVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public DocumentoVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (DocumentoVenta documentoVenta : datasource) {
                    if (documentoVenta.getId() == intRowKey) {
                        return documentoVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(DocumentoVenta documentoVenta) {
                return String.valueOf(documentoVenta.getId());
            }

			@Override
			public List<DocumentoVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String razonSocial = "%" + (filterBy.get("razonSocial") != null ? filterBy.get("razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numero = "%" + (filterBy.get("numero") != null ? filterBy.get("numero").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String ruc = "%" + (filterBy.get("ruc") != null ? filterBy.get("ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("fechaEmision").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<DocumentoVenta> pageDocumentoVenta=null; 
               
                if(estadoSunat==null) {
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, pageable);
                	}else {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter, pageable);
                	}
                }else {
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,estadoSunat, pageable);
                	}else {
                		pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,estadoSunat, tipoDocumentoFilter, pageable);
                	}
                }
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
            }
		};
	}
	
	public void iniciarLazyCredito() {
		lstCreditoLazy = new LazyDataModel<Credito>() {
			private List<Credito> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Credito getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Credito credito : datasource) {
					if (credito.getId() == intRowKey) {
						return credito;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Credito credito) {
				return String.valueOf(credito.getId());
			}

			@Override
			public List<Credito> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				
				String nombres = "%" + (filterBy.get("persona.nombres") != null ? filterBy.get("persona.nombres").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String apellidos = "%" + (filterBy.get("persona.apellidos") != null ? filterBy.get("persona.apellidos").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
			
				Sort sort = Sort.by("id").descending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();

						}

					}
				}
				
				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);
				
				Page<Credito> pageCredito =null;
				
				
				pageCredito = creditoService.findAllByEstadoAndPersonaNombresLikeAndPersonaApellidosLike("Pendiente", nombres, apellidos, pageable);
					
				
				setRowCount((int) pageCredito.getTotalElements());
				return datasource = pageCredito.getContent();
			}
		};
	}

	public Converter getConversorTipoDocumentoSunat() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumentoEnvioSunat) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }

	public Converter getConversorTipoDocumento() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumento) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProducto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Producto c = null;
                    for (Producto si : lstProducto) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Producto) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorSerie() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	SerieDocumento c = null;
                    for (SerieDocumento si : lstSerieDocumento) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((SerieDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorCliente() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Cliente c = null;
                    for (Cliente si : lstCliente) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Cliente) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorNotaDocumento() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumentoNota) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorSerieNota() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	SerieDocumento c = null;
                    for (SerieDocumento si : lstSerieNotaDocumento) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((SerieDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorMotivoNota() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	MotivoNota c = null;
                    for (MotivoNota si : lstMotivoNota) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((MotivoNota) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorTipoOperacion() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoOperacion c = null;
                    for (TipoOperacion si : lstTipoOperacion) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoOperacion) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorIdentificador() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Identificador c = null;
                    for (Identificador si : lstIdentificador) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Identificador) value).getId() + "";
                }
            }
        };
    }

	
	
	
	
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public LazyDataModel<DocumentoVenta> getLstDocumentoVentaLazy() {
		return lstDocumentoVentaLazy;
	}
	public void setLstDocumentoVentaLazy(LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy) {
		this.lstDocumentoVentaLazy = lstDocumentoVentaLazy;
	}
	public TipoDocumento getTipoDocumentoFilter() {
		return tipoDocumentoFilter;
	}
	public void setTipoDocumentoFilter(TipoDocumento tipoDocumentoFilter) {
		this.tipoDocumentoFilter = tipoDocumentoFilter;
	}
	public Boolean getEstadoSunat() {
		return estadoSunat;
	}
	public void setEstadoSunat(Boolean estadoSunat) {
		this.estadoSunat = estadoSunat;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public DocumentoVenta getDocumentoVentaSelected() {
		return documentoVentaSelected;
	}
	public void setDocumentoVentaSelected(DocumentoVenta documentoVentaSelected) {
		this.documentoVentaSelected = documentoVentaSelected;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVentaSelected() {
		return lstDetalleDocumentoVentaSelected;
	}
	public void setLstDetalleDocumentoVentaSelected(List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected) {
		this.lstDetalleDocumentoVentaSelected = lstDetalleDocumentoVentaSelected;
	}
	public String getFechaTextoVista() {
		return fechaTextoVista;
	}
	public void setFechaTextoVista(String fechaTextoVista) {
		this.fechaTextoVista = fechaTextoVista;
	}
	public String getMontoLetra() {
		return montoLetra;
	}
	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}
	public NumeroALetra getNumeroALetra() {
		return numeroALetra;
	}
	public void setNumeroALetra(NumeroALetra numeroALetra) {
		this.numeroALetra = numeroALetra;
	}
	public TipoDocumento getTipoDocumentoSelected() {
		return tipoDocumentoSelected;
	}
	public void setTipoDocumentoSelected(TipoDocumento tipoDocumentoSelected) {
		this.tipoDocumentoSelected = tipoDocumentoSelected;
	}
	public TipoDocumento getTipoDocumentoEnvioSunat() {
		return tipoDocumentoEnvioSunat;
	}
	public void setTipoDocumentoEnvioSunat(TipoDocumento tipoDocumentoEnvioSunat) {
		this.tipoDocumentoEnvioSunat = tipoDocumentoEnvioSunat;
	}
	public List<TipoDocumento> getLstTipoDocumentoEnvioSunat() {
		return lstTipoDocumentoEnvioSunat;
	}
	public void setLstTipoDocumentoEnvioSunat(List<TipoDocumento> lstTipoDocumentoEnvioSunat) {
		this.lstTipoDocumentoEnvioSunat = lstTipoDocumentoEnvioSunat;
	}
	public SerieDocumentoService getSerieDocumentoService() {
		return serieDocumentoService;
	}
	public void setSerieDocumentoService(SerieDocumentoService serieDocumentoService) {
		this.serieDocumentoService = serieDocumentoService;
	}
	public MotivoNotaService getMotivoNotaService() {
		return motivoNotaService;
	}
	public void setMotivoNotaService(MotivoNotaService motivoNotaService) {
		this.motivoNotaService = motivoNotaService;
	}
	public List<SerieDocumento> getLstSerieNotaDocumento() {
		return lstSerieNotaDocumento;
	}
	public void setLstSerieNotaDocumento(List<SerieDocumento> lstSerieNotaDocumento) {
		this.lstSerieNotaDocumento = lstSerieNotaDocumento;
	}
	public List<MotivoNota> getLstMotivoNota() {
		return lstMotivoNota;
	}
	public void setLstMotivoNota(List<MotivoNota> lstMotivoNota) {
		this.lstMotivoNota = lstMotivoNota;
	}
	public TipoDocumento getTipoDocumentoNotaSelected() {
		return tipoDocumentoNotaSelected;
	}
	public void setTipoDocumentoNotaSelected(TipoDocumento tipoDocumentoNotaSelected) {
		this.tipoDocumentoNotaSelected = tipoDocumentoNotaSelected;
	}
	public SerieDocumento getSerieNotaDocumentoSelected() {
		return serieNotaDocumentoSelected;
	}
	public void setSerieNotaDocumentoSelected(SerieDocumento serieNotaDocumentoSelected) {
		this.serieNotaDocumentoSelected = serieNotaDocumentoSelected;
	}
	public Date getFechaEmisionNotaVenta() {
		return fechaEmisionNotaVenta;
	}
	public void setFechaEmisionNotaVenta(Date fechaEmisionNotaVenta) {
		this.fechaEmisionNotaVenta = fechaEmisionNotaVenta;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}
	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}
	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}
	public String getNombreComercialCliente() {
		return nombreComercialCliente;
	}
	public void setNombreComercialCliente(String nombreComercialCliente) {
		this.nombreComercialCliente = nombreComercialCliente;
	}
	public String getRucDniCliente() {
		return rucDniCliente;
	}
	public void setRucDniCliente(String rucDniCliente) {
		this.rucDniCliente = rucDniCliente;
	}
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public String getEmail1Cliente() {
		return email1Cliente;
	}
	public void setEmail1Cliente(String email1Cliente) {
		this.email1Cliente = email1Cliente;
	}
	public String getEmail2Cliente() {
		return email2Cliente;
	}
	public void setEmail2Cliente(String email2Cliente) {
		this.email2Cliente = email2Cliente;
	}
	public String getEmail3Cliente() {
		return email3Cliente;
	}
	public void setEmail3Cliente(String email3Cliente) {
		this.email3Cliente = email3Cliente;
	}
	public List<TipoOperacion> getLstTipoOperacion() {
		return lstTipoOperacion;
	}
	public void setLstTipoOperacion(List<TipoOperacion> lstTipoOperacion) {
		this.lstTipoOperacion = lstTipoOperacion;
	}
	public List<Identificador> getLstIdentificador() {
		return lstIdentificador;
	}
	public void setLstIdentificador(List<Identificador> lstIdentificador) {
		this.lstIdentificador = lstIdentificador;
	}
	public TipoOperacion getTipoOperacionSelected() {
		return tipoOperacionSelected;
	}
	public void setTipoOperacionSelected(TipoOperacion tipoOperacionSelected) {
		this.tipoOperacionSelected = tipoOperacionSelected;
	}
	public Identificador getIdentificadorSelected() {
		return identificadorSelected;
	}
	public void setIdentificadorSelected(Identificador identificadorSelected) {
		this.identificadorSelected = identificadorSelected;
	}
	public Date getFechaEnvioSunat() {
		return fechaEnvioSunat;
	}
	public void setFechaEnvioSunat(Date fechaEnvioSunat) {
		this.fechaEnvioSunat = fechaEnvioSunat;
	}
	public ConsumingPostBoImpl getConsumingPostBo() {
		return consumingPostBo;
	}
	public void setConsumingPostBo(ConsumingPostBoImpl consumingPostBo) {
		this.consumingPostBo = consumingPostBo;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public List<TipoDocumento> getLstTipoDocumento() {
		return lstTipoDocumento;
	}
	public void setLstTipoDocumento(List<TipoDocumento> lstTipoDocumento) {
		this.lstTipoDocumento = lstTipoDocumento;
	}
	public List<SerieDocumento> getLstSerieDocumento() {
		return lstSerieDocumento;
	}
	public void setLstSerieDocumento(List<SerieDocumento> lstSerieDocumento) {
		this.lstSerieDocumento = lstSerieDocumento;
	}
	public List<Cliente> getLstCliente() {
		return lstCliente;
	}
	public void setLstCliente(List<Cliente> lstCliente) {
		this.lstCliente = lstCliente;
	}
	public SerieDocumento getSerieDocumentoSelected() {
		return serieDocumentoSelected;
	}
	public void setSerieDocumentoSelected(SerieDocumento serieDocumentoSelected) {
		this.serieDocumentoSelected = serieDocumentoSelected;
	}
	public Persona getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Persona personSelected) {
		this.personSelected = personSelected;
	}
	public boolean isPersonaNaturalCliente() {
		return personaNaturalCliente;
	}
	public void setPersonaNaturalCliente(boolean personaNaturalCliente) {
		this.personaNaturalCliente = personaNaturalCliente;
	}
	public Cliente getClienteSelected() {
		return clienteSelected;
	}
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}
	public String getRazonSocialText() {
		return razonSocialText;
	}
	public void setRazonSocialText(String razonSocialText) {
		this.razonSocialText = razonSocialText;
	}
	public String getDireccionText() {
		return direccionText;
	}
	public void setDireccionText(String direccionText) {
		this.direccionText = direccionText;
	}
	public String getEmail1Text() {
		return email1Text;
	}
	public void setEmail1Text(String email1Text) {
		this.email1Text = email1Text;
	}
	public String getEmail2Text() {
		return email2Text;
	}
	public void setEmail2Text(String email2Text) {
		this.email2Text = email2Text;
	}
	public String getEmail3Text() {
		return email3Text;
	}
	public void setEmail3Text(String email3Text) {
		this.email3Text = email3Text;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVenta() {
		return lstDetalleDocumentoVenta;
	}
	public void setLstDetalleDocumentoVenta(List<DetalleDocumentoVenta> lstDetalleDocumentoVenta) {
		this.lstDetalleDocumentoVenta = lstDetalleDocumentoVenta;
	}
	public BigDecimal getAnticipos() {
		return anticipos;
	}
	public void setAnticipos(BigDecimal anticipos) {
		this.anticipos = anticipos;
	}
	public BigDecimal getOpGravada() {
		return opGravada;
	}
	public void setOpGravada(BigDecimal opGravada) {
		this.opGravada = opGravada;
	}
	public BigDecimal getOpExonerada() {
		return opExonerada;
	}
	public void setOpExonerada(BigDecimal opExonerada) {
		this.opExonerada = opExonerada;
	}
	public BigDecimal getOpInafecta() {
		return opInafecta;
	}
	public void setOpInafecta(BigDecimal opInafecta) {
		this.opInafecta = opInafecta;
	}
	public BigDecimal getOpGratuita() {
		return opGratuita;
	}
	public void setOpGratuita(BigDecimal opGratuita) {
		this.opGratuita = opGratuita;
	}
	public BigDecimal getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(BigDecimal descuentos) {
		this.descuentos = descuentos;
	}
	public BigDecimal getISC() {
		return ISC;
	}
	public void setISC(BigDecimal iSC) {
		ISC = iSC;
	}
	public BigDecimal getIGV() {
		return IGV;
	}
	public void setIGV(BigDecimal iGV) {
		IGV = iGV;
	}
	public BigDecimal getOtrosCargos() {
		return otrosCargos;
	}
	public void setOtrosCargos(BigDecimal otrosCargos) {
		this.otrosCargos = otrosCargos;
	}
	public BigDecimal getOtrosTributos() {
		return otrosTributos;
	}
	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVentaSelected() {
		return detalleDocumentoVentaSelected;
	}
	public void setDetalleDocumentoVentaSelected(DetalleDocumentoVenta detalleDocumentoVentaSelected) {
		this.detalleDocumentoVentaSelected = detalleDocumentoVentaSelected;
	}
	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	public List<Producto> getLstProducto() {
		return lstProducto;
	}
	public void setLstProducto(List<Producto> lstProducto) {
		this.lstProducto = lstProducto;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public boolean isIncluirIgv() {
		return incluirIgv;
	}
	public void setIncluirIgv(boolean incluirIgv) {
		this.incluirIgv = incluirIgv;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVenta() {
		return detalleDocumentoVenta;
	}
	public void setDetalleDocumentoVenta(DetalleDocumentoVenta detalleDocumentoVenta) {
		this.detalleDocumentoVenta = detalleDocumentoVenta;
	}
	public DataSourceDocumentoVentaElectronico getDt() {
		return dt;
	}
	public void setDt(DataSourceDocumentoVentaElectronico dt) {
		this.dt = dt;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public Producto getProductoVoucher() {
		return productoVoucher;
	}
	public void setProductoVoucher(Producto productoVoucher) {
		this.productoVoucher = productoVoucher;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public List<TipoDocumento> getLstTipoDocumentoNota() {
		return lstTipoDocumentoNota;
	}
	public void setLstTipoDocumentoNota(List<TipoDocumento> lstTipoDocumentoNota) {
		this.lstTipoDocumentoNota = lstTipoDocumentoNota;
	}
	public MotivoNota getMotivoNotaSelected() {
		return motivoNotaSelected;
	}
	public void setMotivoNotaSelected(MotivoNota motivoNotaSelected) {
		this.motivoNotaSelected = motivoNotaSelected;
	}
	public TipoOperacionService getTipoOperacionService() {
		return tipoOperacionService;
	}
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	public IdentificadorService getIdentificadorService() {
		return identificadorService;
	}
	public void setIdentificadorService(IdentificadorService identificadorService) {
		this.identificadorService = identificadorService;
	}
	public ProductoService getProductoService() {
		return productoService;
	}
	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}
	public DataSourceDocumentoVentaElectronico getDtAtencion() {
		return dtAtencion;
	}
	public void setDtAtencion(DataSourceDocumentoVentaElectronico dtAtencion) {
		this.dtAtencion = dtAtencion;
	}
	public LazyDataModel<Credito> getLstCreditoLazy() {
		return lstCreditoLazy;
	}
	public void setLstCreditoLazy(LazyDataModel<Credito> lstCreditoLazy) {
		this.lstCreditoLazy = lstCreditoLazy;
	}
	public List<Credito> getLstCreditoSelected() {
		return lstCreditoSelected;
	}
	public void setLstCreditoSelected(List<Credito> lstCreditoSelected) {
		this.lstCreditoSelected = lstCreditoSelected;
	}
	public CreditoService getCreditoService() {
		return creditoService;
	}
	public void setCreditoService(CreditoService creditoService) {
		this.creditoService = creditoService;
	}
	public DetalleCreditoService getDetalleCreditoService() {
		return detalleCreditoService;
	}
	public void setDetalleCreditoService(DetalleCreditoService detalleCreditoService) {
		this.detalleCreditoService = detalleCreditoService;
	}

	public String getNumeroDocumentoText() {
		return numeroDocumentoText;
	}

	public void setNumeroDocumentoText(String numeroDocumentoText) {
		this.numeroDocumentoText = numeroDocumentoText;
	}
	
	
}
