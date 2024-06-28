package com.model.aldasa.atencion.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.AddReads;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Mesa;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Piso;
import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.fe.ConsumingPostBoImpl;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.jrdatasource.DataSourceAtencionMesa;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.AtencionMesaService;
import com.model.aldasa.service.CajaService;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.DetalleAtencionMesaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.PisoService;
import com.model.aldasa.service.PlatoService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.TipoPlatoType;
import com.model.aldasa.util.Utils;
import com.model.aldasa.ventas.jrdatasource.DataSourceComanda;
import com.model.aldasa.ventas.jrdatasource.DataSourceCuentaDetallada;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVentaElectronico;

@ManagedBean
@ViewScoped
public class AtencionBean extends BaseBean{
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{platoService}")
	private PlatoService platoService;
	
	@ManagedProperty(value = "#{cajaService}")
	private CajaService cajaService;

	@ManagedProperty(value = "#{pisoService}")
	private PisoService pisoService;
	
	@ManagedProperty(value = "#{atencionMesaService}")
	private AtencionMesaService atencionMesaService;
	
	@ManagedProperty(value = "#{detalleAtencionMesaService}")
	private DetalleAtencionMesaService detalleAtencionMesaService;
	
	@ManagedProperty(value = "#{reportGenBo}") //Crea nueva instancia
    private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;
	
	@ManagedProperty(value = "#{serieDocumentoService}")
	private SerieDocumentoService serieDocumentoService;
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService;
	
	@ManagedProperty(value = "#{productoService}")
	private ProductoService productoService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{consumingPostBo}")
	private ConsumingPostBoImpl consumingPostBo;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	

	private List<Piso> lstPisos;
	private List<Mesa> lstMesas;
	private List<Plato> lstPlatoMenu,lstPlatoCarta, lstPlatoEntrada, lstPlatoBebida;
	private List<Plato> lstEntradaEnCarta,lstMenuEnCarta, lstCartaEnCarta, lstBebidaEnCarta, lstPlatoBanderaEnCarta, lstGuarnicionesEnCarta, lstPostresEnCarta, lstOtrosEnCarta;
	private List<DetalleAtencionMesa> lstDetalleAtencionMesaSelected = new ArrayList<>();
	private List<DetalleAtencionMesa> lstDetalleAtencionMesaRegManualSelected = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumento;
	private List<TipoDocumento> lstTipoDocumentoFac;
	private List<SerieDocumento> lstSerieDocumento;
	private List<Cliente> lstCliente;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = new ArrayList<>();
	private List<Producto> lstProducto;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected;

	
	private LazyDataModel<AtencionMesa> lstAtencionLazy, lstAtencionLazyCobrar;
	
	private AtencionMesa atencionSelected;
	private Piso pisoSelected;
	private Mesa mesaSelected;
	private DetalleAtencionMesa detalleSelected;
	private DetalleAtencionMesa detalleRegManSelected;
	private AtencionMesa atencionChefSelected;
	private AtencionMesa atencionCobrarSelected;
	private Plato platoMenu,platoCarta,platoEntrada, platoBebida;
	private Plato entradaSelected, cartaSelected , menuSelected, bebidaSelected, platoBanderaSelected, guarnicionesSelected, postresSelected, otrosSelected;
	private SerieDocumento serieDocumentoSelected ;
	private DetalleDocumentoVenta detalleDocumentoVentaSelected;
	private Caja cajaAbierta;

	
	private Cliente clienteSelected;

	
	private BigDecimal totalDetalle;
	private BigDecimal totalRegManualDetalle;

	private String tituloDialog, montoLetra;
	private String estadoChef="Pendiente";
	private String dni, nombreCliente;
	private String estadoCobrar= "Ocupado";
	private String tipoDocumento="V";
	private Date fechaRegManual;
	private boolean creditoRegManual;
	private String dniRucRegManual;
	private String clienteRegManual;
	private Date fechaEmision;
	private TipoDocumento tipoDocumentoSelected;
	private String numero;
	private String dniRucText, razonSocialText, direccionText, email1Text, email2Text, email3Text;
	private String tipoPago = "Contado";
	private String moneda = "S";
	private boolean incluirIgv = true;
	private boolean porConsumo;


	
	private boolean personaNaturalCliente = true;
	private Persona personSelected;
	
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
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	private NumeroALetra numeroALetra = new  NumeroALetra();
	
	Timer timer = new Timer();

	
	private DataSourceDocumentoVentaElectronico dtAtencion;
	private DataSourceCuentaDetallada dtCuentaDetallada;
	private DataSourceComanda dtComanda;
	Map<String, Object> parametros;

	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	
	@PostConstruct
	public void init() {
		List<String> lstCodigoFB=new ArrayList<>();
		lstCodigoFB.add("00");
		lstCodigoFB.add("03");
		lstCodigoFB.add("01");
		lstTipoDocumentoFac = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoFB);
		tipoDocumentoSelected = lstTipoDocumentoFac.get(0);
		listarSerie();
		
		fechaEmision = new Date();
		lstTipoDocumento = tipoDocumentoService.findByEstado(true);
		lstPisos = pisoService.findBySucursalOrderByNombre(navegacionBean.getSucursalLogin());
		pisoSelected = lstPisos.get(0);
		cambiarPiso();
				
		Date fecha = new Date();
		fecha.setHours(0);
		fecha.setMinutes(0);
		fecha.setSeconds(0);
				
		iniciarLazyAtencion();
		iniciarLazyAtencionCobrar();
		
		fechaRegManual= new Date();
		listarPlatos();
		listarPlatosEnCarta();
		
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                // Coloca aquí el código que deseas ejecutar cada 10 segundos
//                // Por ejemplo, actualiza el resultado
//            	cambiarPiso();
//            }
//        }, 0, 5000);
		lstProducto = productoService.findByEstado(true);
		
	}
	
	public void buscar() {		
		
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			if(!dniRucText.equals("")) {
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String apiUrl = "https://api.apis.net.pe/v1/dni?numero="+dniRucText.trim();

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
			if(!dniRucText.equals("")) {
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String apiUrl = "https://api.apis.net.pe/v1/ruc?numero="+dniRucText.trim();

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
	
	public void opcionCobrar() {
		if(atencionCobrarSelected.getEstado().equals("Ocupado")) {
			porConsumo = false;
			validaGeneraDocumento();
		}else if(atencionCobrarSelected.getEstado().equals("Cobrado")) {
			lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionCobrarSelected, true);
			PrimeFaces.current().executeScript("PF('detallePedidoDialog').show();");
		}
	}
	
	public void calcularTipoPago() {
		if(atencionCobrarSelected.getMonto().compareTo(importeTotal)==1) {
			atencionCobrarSelected.setMonto(BigDecimal.ZERO);
			addErrorMessage("El monto ingresado supera al importe TOTAL.");
			calcularTipoPago2();
			return;
		}
		atencionCobrarSelected.setMonto2(importeTotal.subtract(atencionCobrarSelected.getMonto()));
		
	}
	
	public void calcularTipoPago2() {
		if(atencionCobrarSelected.getMonto2().compareTo(importeTotal)==1) {
			atencionCobrarSelected.setMonto2(BigDecimal.ZERO);
			addErrorMessage("El monto ingresado supera al importe TOTAL.");
			calcularTipoPago();
			return;
		}
		atencionCobrarSelected.setMonto(importeTotal.subtract(atencionCobrarSelected.getMonto2()));
		
	}
	
	public void listarDetalleDocumentoVenta( ) {
		montoLetra = numeroALetra.Convertir(atencionCobrarSelected.getDocumentoVenta().getTotal()+"", true, "SOLES");
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(atencionCobrarSelected.getDocumentoVenta(), true);
	}
	
	
	public void saveDocumentoVenta() {
		
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
			BigDecimal sinIGV = d.getTotal().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
			d.setImporteVentaSinIgv(sinIGV); 
			d.setPrecioSinIgv(sinIGV);
		}
		
	//		aqui actualiza los datos del cliente y guarda run razon doreccion
		
		Cliente cliente =null;
	
		cliente = clienteService.findByDniRucAndEstado(dniRucText, true);
		
		
		if(tipoDocumentoSelected.getAbreviatura().equals("B") || tipoDocumentoSelected.getAbreviatura().equals("F")) {
			if(cliente!=null) {
				cliente.setRazonSocial(razonSocialText);
				cliente.setDireccion(direccionText);
				cliente.setEmail1Fe(email1Text);
				cliente.setEmail2Fe(email2Text);
				cliente.setEmail3Fe(email3Text);
			
				if(email1Text!=null) {
					if(email1Text.equals("")) {
						cliente.setEmail1Fe(null);
					}
				}
				if(email2Text!=null) {
					if(email2Text.equals("")) {
						cliente.setEmail2Fe(null);
					}
				}
				if(email3Text!=null) {
					if(email3Text.equals("")) {
						cliente.setEmail3Fe(null);
					}
				}
			}else {
				cliente = new Cliente();
				cliente.setRazonSocial(razonSocialText);
				cliente.setNombreComercial(razonSocialText);
				cliente.setDniRuc(dniRucText);
				cliente.setDireccion(direccionText);
				cliente.setPersonaNatural(tipoDocumentoSelected.getAbreviatura().equals("F")?false:true);
				cliente.setEstado(true);
				cliente.setFechaRegistro(new Date());
				cliente.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
				cliente.setEmail1Fe(email1Text);
				cliente.setEmail2Fe(email2Text);
				cliente.setEmail3Fe(email3Text);
				
				if(email1Text!=null) {
					if(email1Text.equals("")) {
						cliente.setEmail1Fe(null);
					}
				}
				if(email2Text!=null) {
					if(email2Text.equals("")) {
						cliente.setEmail2Fe(null);
					}
				}
				if(email3Text!=null) {
					if(email3Text.equals("")) {
						cliente.setEmail3Fe(null);
					}
				}
				
			}
			clienteService.save(cliente);
			listarClientes();
		}	
		
	
		
		DocumentoVenta documentoVenta = new DocumentoVenta();
		documentoVenta.setCliente(cliente);
		documentoVenta.setDocumentoVentaRef(null);
		documentoVenta.setAtencionMesa(atencionCobrarSelected);
		documentoVenta.setSucursal(navegacionBean.getSucursalLogin());
		documentoVenta.setTipoDocumento(tipoDocumentoSelected);
		documentoVenta.setSerie(serieDocumentoSelected.getSerie());
		documentoVenta.setNumero(""); // vamos a setear el numero despues de haber guardado el documento
		documentoVenta.setRuc(dniRucText);
	
		documentoVenta.setRazonSocial(razonSocialText);
		documentoVenta.setNombreComercial(razonSocialText);
		documentoVenta.setDireccion(direccionText);
		documentoVenta.setFechaEmision(new Date());
		documentoVenta.setFechaVencimiento(new Date());
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
		
			atencionCobrarSelected.setUsuarioCobro(navegacionBean.getUsuarioLogin());
			atencionCobrarSelected.setFechaCobro(new Date());
			atencionCobrarSelected.setEstado("Cobrado");
			atencionCobrarSelected.setDocumentoVenta(documento);
			atencionCobrarSelected.setTipoDocumento(documento.getTipoDocumento());

			atencionMesaService.save(atencionCobrarSelected, cajaAbierta, importeTotal, false); 
						
//			int envio =enviarDocumentoSunat(documento, lstDetalleDocumentoVenta);
			
			lstDetalleDocumentoVenta.clear();// claer es limpiar en ingles prueba
			clienteSelected=null;
//			calcularTotales();
						
		
			iniciarImportesDocVentas();
			listarSerie();
			
//			String addMensaje = envio>0?"Se envio correctamente a SUNAT":"No se pudo enviar a SUNAT";
			addInfoMessage("Se guardó el documento correctamente. ");
			
		}else {
			addErrorMessage("No se puede guardar el documento."); 
			return;
		}
		
	}
	
//	public void calcularTotales() {
//		anticipos = BigDecimal.ZERO;
//		opInafecta=BigDecimal.ZERO;
//		importeTotal=BigDecimal.ZERO;
//		opGravada=BigDecimal.ZERO;
//		IGV = BigDecimal.ZERO;
//		if(!lstDetalleDocumentoVenta.isEmpty()) {
//			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
//				opInafecta= opInafecta.add(d.getTotal());
//				importeTotal= importeTotal.add(d.getTotal());
////				if(d.getTotalTemp()!=null) {
////					anticipos = anticipos.add(d.getAmortizacion());
////				}
//				
//			}
//		}
//	} 
	

	public void validarFormularioDocumentoVenta() {

		
		if(!importeTotal.equals(atencionCobrarSelected.getMonto().add(atencionCobrarSelected.getMonto2()))) {
			addErrorMessage("Los montos son incorrectos.");
			return;
		}
		
		List<Caja> lstCajaAbierta = cajaService.findBySucursalAndEstado(navegacionBean.getSucursalLogin(), "Abierta");
		if(lstCajaAbierta.isEmpty()) {
			addErrorMessage("Tienes que abrir la caja del dia para poder realizar cobros.");
			return;
		}else {
			cajaAbierta = lstCajaAbierta.get(0);
		}
		
		
		if(tipoDocumentoSelected.getAbreviatura().equals("F")) {

				if(dniRucText.equals("")) {
					addErrorMessage("Ingresar Dni/Ruc para el cliente");
					return;
				}else {
					if(!validarRuc(dniRucText)) {
						return;
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
				
				
//			}		
		}else {

			if(dniRucText.equals("")) {
				addErrorMessage("Ingresar Dni/Ruc para el cliente");
				return;
			}else {
				if(dniRucText.length()!=8) {
					addErrorMessage("El DNI debe tener 8 dígitos.");
					return;
				}
			}
			if(razonSocialText.equals("")) {
				addErrorMessage("Debes ingresar el nombre del cliente.");
				return;
			}
			if(direccionText.equals("")) {
				addErrorMessage("Debes ingresar la dirección del cliente.");
				return;
			}
					
				
			
		}
		
		PrimeFaces.current().executeScript("PF('saveDocumento').show();");

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
	
	public boolean validarDNI(String ruc) {
		if(ruc.length()!=9) {
			addErrorMessage("El DNI debe tener 9 dígitos.");
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
	
	public void iniciarImportesDocVentas() {
		anticipos = BigDecimal.ZERO;
		opGravada = BigDecimal.ZERO;
		opExonerada = BigDecimal.ZERO;
		opInafecta = BigDecimal.ZERO;
		opGratuita = BigDecimal.ZERO;
		descuentos = BigDecimal.ZERO;
		ISC = BigDecimal.ZERO;
		IGV = BigDecimal.ZERO;
		otrosCargos = BigDecimal.ZERO;
		otrosTributos = BigDecimal.ZERO;
		importeTotal = BigDecimal.ZERO;

		dniRucText="";
		razonSocialText="";
		direccionText="";
		email1Text="";
		email2Text="";
		email3Text="";
		incluirIgv=true;


	}
	
	public void eliminarDetalleVenta(int index) {
		lstDetalleDocumentoVenta.remove(index);
		if(lstDetalleDocumentoVenta.isEmpty()) {
			clienteSelected = null;
//			persona=null;
		}
		addInfoMessage("Detalle eliminado");
		
	}
	
	public void agregarDetalle() {
		DetalleDocumentoVenta det = new DetalleDocumentoVenta();
		det.setProducto(lstProducto.get(0));
		det.setTotal(BigDecimal.ZERO);
		det.setEstado(true); 
		det.setImporteVentaSinIgv(BigDecimal.ZERO);
		det.setPrecioSinIgv(BigDecimal.ZERO); 
		lstDetalleDocumentoVenta.add(det);
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
	
	public void validaGeneraDocumento() {
		if(!atencionCobrarSelected.getEstado().equals("Ocupado")) {
			return;
		}
		clienteSelected = null;
		lstDetalleDocumentoVenta = new ArrayList<>();
		
//		if(!atencionCobrarSelected.getTipoDocumento().getAbreviatura().equals("N")) {
//			addErrorMessage("Ya tiene generado un documento de venta");
//			return;
//		}
		
		iniciarImportesDocVentas();

		lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionCobrarSelected, true);
		if (!porConsumo) {
			for (DetalleAtencionMesa d : lstDetalleAtencionMesaSelected) {
				BigDecimal montoSinIgv = d.getTotal().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
				importeTotal = importeTotal.add(d.getTotal());
				
				DetalleDocumentoVenta detalleDoc = new DetalleDocumentoVenta();
				detalleDoc.setDocumentoVenta(null);
				detalleDoc.setProducto(d.getPlato().getProducto());
				detalleDoc.setDescripcion(d.getPlato().getNombre());
				detalleDoc.setCantidad(d.getCantidad());
				detalleDoc.setValorUnitario(d.getPrecioUnitario().divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
				detalleDoc.setPrecioUnitario(d.getPrecioUnitario()); 
				
				detalleDoc.setTotal(d.getTotal());
				detalleDoc.setEstado(true);
				detalleDoc.setImporteVentaSinIgv(montoSinIgv);
				detalleDoc.setPrecioSinIgv(montoSinIgv);
				
				lstDetalleDocumentoVenta.add(detalleDoc);
			
			}
		}else {
			for (DetalleAtencionMesa d : lstDetalleAtencionMesaSelected) {
				importeTotal = importeTotal.add(d.getTotal());
				
			}
			
			DetalleDocumentoVenta detalleDoc = new DetalleDocumentoVenta();
			detalleDoc.setDocumentoVenta(null);
			detalleDoc.setProducto(lstDetalleAtencionMesaSelected.get(0).getPlato().getProducto());
			detalleDoc.setDescripcion("POR CONSUMO");
			detalleDoc.setCantidad(BigDecimal.ONE);
			detalleDoc.setValorUnitario(importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
			detalleDoc.setPrecioUnitario(importeTotal); 
			
			detalleDoc.setTotal(importeTotal);
			detalleDoc.setEstado(true);
			detalleDoc.setImporteVentaSinIgv(importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
			detalleDoc.setPrecioSinIgv(importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP));
			
			lstDetalleDocumentoVenta.add(detalleDoc);
		}
		
		
		BigDecimal montoSinIgv = importeTotal.divide(navegacionBean.getSucursalLogin().getIgv(), 2, RoundingMode.HALF_UP);
		IGV = IGV.add(importeTotal.subtract(montoSinIgv));
		opGravada = opGravada.add(montoSinIgv);

		
		atencionCobrarSelected.setMonto(importeTotal);
		atencionCobrarSelected.setMonto2(BigDecimal.ZERO);

		PrimeFaces.current().executeScript("PF('dialogGeneraDoc').show()");
	}
	
	public void cambiarTipoConsumo() {
		validaGeneraDocumento();
	}
	
	public void eliminarPedido() {
		
		DocumentoVenta docVen = documentoVentaService.findByEstadoAndAtencionMesaAndEnvioSunat(true, atencionCobrarSelected, true);
		if(docVen!=null) {
			addErrorMessage("No se puede eliminar porque el documento de venta ya se envío a SUNAT, comunícate con el area de Sistemas.");
			return;
		}
		
		atencionCobrarSelected.setEstado("Eliminado");
		atencionMesaService.save(atencionCobrarSelected, null, null, false);
		addInfoMessage("Pedido eliminado correctamente.");
	}

	public void listarPlatos() {
		lstPlatoEntrada = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.ENTRADA.getNombre(), true);
		lstPlatoMenu = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.MENU.getNombre(), true);
		lstPlatoCarta = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.CARTA.getNombre(), true);
		lstPlatoBebida = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.BEBIDA.getNombre(), true); 
	}
	
	public void listarPlatosEnCarta() {
		lstEntradaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.ENTRADA.getNombre(), true, "SI");
		lstMenuEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.MENU.getNombre(), true, "SI");
		lstCartaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.CARTA.getNombre(), true, "SI");
		lstBebidaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.BEBIDA.getNombre(), true, "SI"); 
		lstPlatoBanderaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.PLATO_BANDERA.getNombre(), true, "SI"); 
		lstGuarnicionesEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.GUARNICIONES.getNombre(), true, "SI"); 
		lstPostresEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.POSTRES.getNombre(), true, "SI"); 
		lstOtrosEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.OTROS.getNombre(), true, "SI"); 
	
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
        parametros.put("NUMEROOPERACION", atencionCobrarSelected.getDocumentoVenta().getSerie() + "-" + atencionCobrarSelected.getDocumentoVenta().getNumero());
        parametros.put("DNI", atencionCobrarSelected.getDocumentoVenta().getRuc());
        parametros.put("FECHAEMISION",sdf2.format(atencionCobrarSelected.getDocumentoVenta().getFechaEmision()) );
        parametros.put("CAJERO", atencionCobrarSelected.getDocumentoVenta().getUsuarioRegistro().getUsername());
        parametros.put("OPGRAVADA", atencionCobrarSelected.getDocumentoVenta().getOpGravada());
        parametros.put("IGV", atencionCobrarSelected.getDocumentoVenta().getIgv());
        parametros.put("TOTAL", atencionCobrarSelected.getDocumentoVenta().getTotal());
        parametros.put("TIPODOCUMENTO", atencionCobrarSelected.getDocumentoVenta().getTipoDocumento().getDescripcion());
        parametros.put("DIRECCIONCLIENTE", atencionCobrarSelected.getDocumentoVenta().getDireccion()); 
        
        if(atencionCobrarSelected.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")) {
        	parametros.put("TIPODNIRUC", "DNI: ");
        }else if(atencionCobrarSelected.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("F")) {
        	parametros.put("TIPODNIRUC", "RUC: ");
        }else {
        	parametros.put("TIPODNIRUC", "DNI: ");
        }
        
        parametros.put("CLIENTE", atencionCobrarSelected.getDocumentoVenta().getRazonSocial());
        parametros.put("MONTOLETRA", montoLetra);
        parametros.put("QR", navegacionBean.getSucursalLogin().getRuc() + "|" + atencionCobrarSelected.getDocumentoVenta().getTipoDocumento().getCodigo() + "|" + 
        		atencionCobrarSelected.getDocumentoVenta().getSerie() + "|" + atencionCobrarSelected.getDocumentoVenta().getNumero() + "|" + "0" + "|" + atencionCobrarSelected.getDocumentoVenta().getTotal() + "|" + 
	    		sdf2.format(atencionCobrarSelected.getDocumentoVenta().getFechaEmision()) + "|" + (atencionCobrarSelected.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")?"1":"6") + "|" + atencionCobrarSelected.getDocumentoVenta().getRuc() + "|");


        String path = "secured/view/modulos/ventas/reportes/jasper/documentoElectronico.jasper";

        reportGenBo.exportByFormatNotConnectDb(dtAtencion, path, "pdf", parametros, "DOCUMENTO ELECTRÓNICO");
        dtAtencion = null;
        parametros = null;
    }
	
	public void exportarCuentaPDF() {
		lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionCobrarSelected, true);
		
		dtCuentaDetallada = new DataSourceCuentaDetallada();
		BigDecimal total = BigDecimal.ZERO;
		
        for (DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
        	dtCuentaDetallada.addResumenDetalle(detalle);
        	
        	total = total.add(detalle.getTotal());
        }

        parametros = new HashMap<String, Object>();

        parametros.put("PATTERN", Utils.getPattern());
        
        parametros.put("MESA", atencionCobrarSelected.getNumMesa());
        parametros.put("TOTAL", total);
        
     

        String path = "secured/view/modulos/ventas/reportes/jasper/cuentaDetallada.jasper";

        reportGenBo.exportByFormatNotConnectDb(dtCuentaDetallada, path, "pdf", parametros, "CUENTA MESA " + atencionCobrarSelected.getNumMesa());
        dtAtencion = null;
        parametros = null;
    }
	
	public void exportarComandaPDF() {

		dtComanda = new DataSourceComanda();
        for (DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
        	dtComanda.addResumenDetalle(detalle);
        }

        parametros = new HashMap<String, Object>();

        parametros.put("PATTERN", Utils.getPattern());
        
       
        parametros.put("MESA", pisoSelected.getNombre()+": "+ mesaSelected.getNumMesa());
        parametros.put("MESERO", atencionSelected.getUsuario().getPersona().getNombres()+" "+atencionSelected.getUsuario().getPersona().getApellidos());
        parametros.put("FECHA", sdfFull.format(atencionSelected.getFechaRegistro()));
      

        String path = "secured/view/modulos/atencion/reportes/jasper/repComanda.jasper";

        reportGenBo.exportByFormatNotConnectDb(dtComanda, path, "pdf", parametros, "COMANDA "+ pisoSelected.getNombre()+": "+ mesaSelected.getNumMesa());
        dtComanda = null;
        parametros = null;
    }

//	public void exportarPDF() {
//
//		dtAtencion = new DataSourceAtencionMesa();
//        for (DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
//        	dtAtencion.addCertificadoCalidad(detalle);
//        }
//
//        parametros = new HashMap<String, Object>();
//
//        parametros.put("PATTERN", Utils.getPattern());
//        
//        parametros.put("DIRECCIONSUCURSAL", navegacionBean.getSucursalLogin().getDireccion());
//        parametros.put("RUCSUCURSAL", navegacionBean.getSucursalLogin().getRuc());
//        parametros.put("TELEFONOSUCURSAL", navegacionBean.getSucursalLogin().getTelefono());
//        parametros.put("MESERO", atencionCobrarSelected.getUsuario().getPersona().getNombres()+" "+atencionCobrarSelected.getUsuario().getPersona().getApellidos());
//        parametros.put("MESA", atencionCobrarSelected.getNumMesa());
//        parametros.put("NUMEROOPERACION", atencionCobrarSelected.getId());
//
//
//        String path = "secured/view/modulos/atencion/reportes/jasper/repDocumentoVentaTicket.jasper";
//        reportGenBo.exportByFormatNotConnectDb(dtAtencion, path, "pdf", parametros, "PRE-CUENTA" + atencionCobrarSelected.getNumMesa());
//        dtAtencion = null;
//        parametros = null;
//    }
	
	public void actualizarTotal(DetalleAtencionMesa detalle) {
		if(detalle.getPrecioUnitario()==null) {
			addErrorMessage("Ingresar precio unitario.");
			return;
		}
		if(detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO)==0) {
			addErrorMessage("El precio unitario debe ser diferente de 0.");
			return;
		}
		detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
		calcularTotalRegManual();
	}
	
	public void pasarAtendido() {
		atencionChefSelected.setEstadoChef("Atendido");
		atencionMesaService.save(atencionChefSelected);
		
		addInfoMessage("Se pasó a Atendido correctamente.");
		PrimeFaces.current().executeScript("PF('atencionDialogChef').hide();");
		
	}
	
	public void pasarPendiente() {
		atencionChefSelected.setEstadoChef("Pendiente");
		atencionMesaService.save(atencionChefSelected);
		
		addInfoMessage("Se pasó a Pendiente correctamente.");
	}
	
	public void iniciarLazyAtencion() {

		lstAtencionLazy = new LazyDataModel<AtencionMesa>() {
			private List<AtencionMesa> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public AtencionMesa getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (AtencionMesa empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(AtencionMesa empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<AtencionMesa> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fechaRegistro").descending();
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

                List<String> estados = new ArrayList<>();
                estados.add("Ocupado"); estados.add("Cobrado");
                
                Page<AtencionMesa> pageEmpleado= atencionMesaService.findByEstadoChefAndSucursalAndEstadoIn(estadoChef,navegacionBean.getSucursalLogin(), estados, pageable);
                
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	public void iniciarLazyAtencionCobrar() {

		lstAtencionLazyCobrar = new LazyDataModel<AtencionMesa>() {
			private List<AtencionMesa> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public AtencionMesa getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (AtencionMesa empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(AtencionMesa empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<AtencionMesa> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fechaCobro").descending();
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
                
                Page<AtencionMesa> pageEmpleado= atencionMesaService.findBySucursalAndEstado(navegacionBean.getSucursalLogin(), estadoCobrar, pageable);
                
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	public void verDetallesChef() {
		lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionChefSelected, true);
		totalDetalle = calcularTotalChef();
		
		tituloDialog = atencionChefSelected.getPiso().getNombre()+": "+ atencionChefSelected.getNumMesa()
		+" / Mesero(a): "+ atencionChefSelected.getUsuario().getPersona().getNombres()+" "+atencionChefSelected.getUsuario().getPersona().getApellidos();
	}
	
	public void verDetallesCobrar() {
		lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionCobrarSelected, true);
		
		calcularTotal();
		
		tituloDialog = atencionCobrarSelected.getPiso().getNombre()+": "+ atencionCobrarSelected.getNumMesa()
		+" / Mesero(a): "+ atencionCobrarSelected.getUsuario().getPersona().getNombres()+" "+atencionCobrarSelected.getUsuario().getPersona().getApellidos();
		
	}
	
	public BigDecimal calcularTotalChef() {
		 BigDecimal totalDetalle = BigDecimal.ZERO;
		if(!lstDetalleAtencionMesaSelected.isEmpty()) {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
				totalDetalle = totalDetalle.add(detalle.getTotal());
			}
		}
		
		return totalDetalle;
	}
	
	public BigDecimal calcularTotalVistaReporte(AtencionMesa atencion) {
		 BigDecimal totalDetalle = BigDecimal.ZERO;
		
		 List<DetalleAtencionMesa> lstDetalle = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencion,true);
		 for(DetalleAtencionMesa det: lstDetalle) {
			 totalDetalle = totalDetalle.add(det.getTotal());
		 }
		
		return totalDetalle;
	}
	
	public String convertirHora(Date hora) {
		String a="";
		if(hora!=null) {
			a = sdfFull.format(hora);

		}
		return a;
	}
	
	public void eliminarDetalle() {
		if(detalleSelected.getId()==null) {
			lstDetalleAtencionMesaSelected.remove(detalleSelected);
		}else {
			detalleSelected.setEstado(false);
			detalleAtencionMesaService.save(detalleSelected);
			lstDetalleAtencionMesaSelected.remove(detalleSelected);
		}
		
		calcularTotal();
		
		addInfoMessage("Se eliminó correctamente."); 
	}
	
	public void eliminarDetalleManual() {
		if(detalleRegManSelected.getId()==null) {
			lstDetalleAtencionMesaRegManualSelected.remove(detalleRegManSelected);
		}else {
			detalleRegManSelected.setEstado(false);
			detalleAtencionMesaService.save(detalleRegManSelected);
			lstDetalleAtencionMesaRegManualSelected.remove(detalleRegManSelected);
		}
		
		calcularTotal();
		calcularTotalRegManual();
		
		addInfoMessage("Se eliminó correctamente."); 
	}
	
	public void verDetalles() {
		totalDetalle = BigDecimal.ZERO;

		
		atencionSelected = atencionMesaService.findByPisoAndNumMesaAndEstadoAndSucursal(pisoSelected,mesaSelected.getNumMesa(), "Ocupado", navegacionBean.getSucursalLogin());
		lstDetalleAtencionMesaSelected = new ArrayList<>();
		
		if(atencionSelected!=null) {
			lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionSelected, true);
			calcularTotal();
		}
		
		tituloDialog = pisoSelected.getNombre()+": "+ mesaSelected.getNumMesa()+"";
		if(atencionSelected != null) {
			tituloDialog = tituloDialog + " / Mesero(a): "+ atencionSelected.getUsuario().getPersona().getNombres()+" "+atencionSelected.getUsuario().getPersona().getApellidos();
		}
		
		Date fecha = new Date();
		fecha.setHours(0);
		fecha.setMinutes(0);
		fecha.setSeconds(0);
//		List<Carta> lstEntradas= cartaService.findByFechaAndEstadoAndSucursalAndPlatoTipoAndAgotado(fecha, true, navegacionBean.getSucursalLogin(), TipoPlatoType.ENTRADA.getNombre(), false);
//		lstPlatoEntradaDetalle= new ArrayList<>();
//		if(!lstEntradas.isEmpty()) {
//			for(Carta c: lstEntradas) {
//				lstPlatoEntradaDetalle.add(c.getPlato());
//			}
//		}
	}
	
	public void calcularTotal() {
		totalDetalle = BigDecimal.ZERO;
		if(!lstDetalleAtencionMesaSelected.isEmpty()) {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
				totalDetalle = totalDetalle.add(detalle.getTotal());
			}
		}
	}
	
	public void calcularTotalRegManual() {
		totalRegManualDetalle = BigDecimal.ZERO;
		if(!lstDetalleAtencionMesaRegManualSelected.isEmpty()) {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaRegManualSelected) {
				totalRegManualDetalle = totalRegManualDetalle.add(detalle.getTotal());
			}
		}
	}
	
	public void eliminarAtencionMesa() {
		atencionSelected.setEstado("Eliminado");
		atencionMesaService.save(atencionSelected);
		atencionSelected = null;
		lstDetalleAtencionMesaSelected = new ArrayList<>();
		calcularTotal();
		addInfoMessage("Eliminado correctamente."); 
	}
	
	public void mensajesCobrarAtencionMesa() {
		if(atencionCobrarSelected.isCredito()) {
			if(!atencionCobrarSelected.getTipoPago().equals("")) {
				addErrorMessage("El tipo de pago de ser [-Ninguno-]");
				return;
			}
			
			if(atencionCobrarSelected.getDniRuc().equals("")) {
				addErrorMessage("Ingresar el Dni/Ruc.");
				return;
			}
			if(atencionCobrarSelected.getNombreCliente().equals("")) {
				addErrorMessage("Ingresar el nombre del cliente.");
				return;
			}
		}else {
			if(atencionCobrarSelected.getTipoDocumento() == null) {
				addErrorMessage("Seleccione un tipo de documento.");
				return;
			}
			
			if(atencionCobrarSelected.getTipoPago().equals("")) {
				addErrorMessage("Seleccione un tipo de pago.");
				return;
			}
		}
		
		PrimeFaces.current().executeScript("PF('cobrarDialog').show()");
	}
	
	public void cobrarAtencionMesa() {
		List<Caja> lstCajaAbierta = cajaService.findBySucursalAndEstado(navegacionBean.getSucursalLogin(), "Abierta");
		if(lstCajaAbierta.isEmpty()) {
			addErrorMessage("Tienes que abrir la caja del dia para poder realizar cobros.");
			return;
		}
		
		
		
		atencionCobrarSelected.setUsuarioCobro(navegacionBean.getUsuarioLogin());
		atencionCobrarSelected.setFechaCobro(new Date());
		atencionCobrarSelected.setEstado("Cobrado");
		atencionMesaService.save(atencionCobrarSelected, lstCajaAbierta.get(0), totalDetalle, false); 
		
		PrimeFaces.current().executeScript("PF('atencionDialogCobrar').hide()");
		addInfoMessage("Cobrado correctamente."); 
		
	}
	
	
	public void mensajeSaveRegistroManual() {
		
		if(lstDetalleAtencionMesaRegManualSelected.isEmpty()) {
			addErrorMessage("Ingresar ventas del registro manual.");
			return;
		}
		
		if(fechaRegManual==null) {
			addErrorMessage("Ingresar fecha.");
			return;
		}
		
		if(creditoRegManual) {
			if(dniRucRegManual.equals("")) {
				addErrorMessage("Ingresar DNI/RUC.");
				return;
			}
			
			if(clienteRegManual.equals("")) {
				addErrorMessage("Ingresar cliente.");
				return;
			}
		}
		
		PrimeFaces.current().executeScript("PF('saveRegistroManualDialog').show()");
	}
	
	public void saveRegistroManual() {
	
		Piso piso = new Piso();
		piso.setId(1);
		AtencionMesa atencionSaveReqManual = new AtencionMesa();
		atencionSaveReqManual.setPiso(piso);
		atencionSaveReqManual.setNumMesa(101);
		atencionSaveReqManual.setEstado("Cobrado");
		atencionSaveReqManual.setSucursal(navegacionBean.getSucursalLogin());
		atencionSaveReqManual.setFechaCobro(fechaRegManual);
		atencionSaveReqManual.setUsuario(navegacionBean.getUsuarioLogin());
		atencionSaveReqManual.setEstadoChef(estadoChef);
		atencionSaveReqManual.setFechaRegistro(new Date());
		atencionSaveReqManual.setCredito(creditoRegManual);
		atencionSaveReqManual.setDniRuc(dniRucRegManual);
		atencionSaveReqManual.setNombreCliente(clienteRegManual);
		atencionSaveReqManual = atencionMesaService.save(atencionSaveReqManual);
		
		if(atencionSaveReqManual==null) {
			addErrorMessage("No se pudo guardar");
		}else {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaRegManualSelected) {
				detalle.setAtencionMesa(atencionSaveReqManual);
				detalleAtencionMesaService.save(detalle);
			}
			creditoRegManual=false;
			dniRucRegManual="";
			clienteRegManual="";
			lstDetalleAtencionMesaRegManualSelected.clear();
			totalRegManualDetalle=null;
			addInfoMessage("Cobrado correctamente."); 
		}
		
	}
	
	
	public void saveAtencionMesa() {
		
		if(lstDetalleAtencionMesaSelected.isEmpty()) {
			addErrorMessage("Lista vacia.");
			return;
		}
		
		if(atencionSelected == null) {
			if(lstDetalleAtencionMesaSelected.isEmpty()) {
				addErrorMessage("La lista de pedido esta vacío."); 
				return;
			}
			
			AtencionMesa atencionMew = new AtencionMesa();
			atencionMew.setPiso(pisoSelected);
			atencionMew.setDniRuc("");
			atencionMew.setNombreCliente("");
			atencionMew.setNumMesa(mesaSelected.getNumMesa()); 
			atencionMew.setEstado("Ocupado");
			atencionMew.setSucursal(navegacionBean.getSucursalLogin()); 
			atencionMew.setUsuario(navegacionBean.getUsuarioLogin());
			atencionMew.setEstadoChef("Pendiente"); 
			atencionMew.setFechaRegistro(new Date());
			atencionMew.setCredito(false);
			atencionMew.setTipoPago("");
			
			atencionSelected = atencionMesaService.save(atencionMew);
			if(atencionSelected==null) {
				addErrorMessage("No se pudo guardar."); 
			}else {
				for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
					detalle.setAtencionMesa(atencionSelected);
					detalleAtencionMesaService.save(detalle);
				}
				addInfoMessage("Se guardó correctamente el pedido.");
			}
		}else {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
				detalle.setAtencionMesa(atencionSelected);
				detalleAtencionMesaService.save(detalle);
			}
			addInfoMessage("Se guardó correctamente el pedido.");
		}
	}
	
	
	public void cambiarPiso() {
		System.out.println("esta entrando");
		lstMesas = new ArrayList<>();
		int cantidad = pisoSelected.getCantidadMesa();
		char letraUno=pisoSelected.getNombre().charAt(pisoSelected.getNombre().length()-1);
		
		if(cantidad != 0) {
			for(int i = 1; i<= cantidad;i++) {
				Mesa mesa = new Mesa();
				String tamanioCantida = "0";
				if(i>9) {
					tamanioCantida = "";
				}
				
				mesa.setNumMesa(Integer.parseInt(letraUno + tamanioCantida+i+"")); 				
				lstMesas.add(mesa);
			}
		}
	}
	
	public void agregarDetalle(Plato plato) {
		if(plato != null) {
			DetalleAtencionMesa detalle = new DetalleAtencionMesa();
			detalle.setAtencionMesa(atencionSelected);
			detalle.setPlato(plato); 
			detalle.setPlatoEntrada(null);
			detalle.setCantidad(BigDecimal.ONE); 
			detalle.setPrecioUnitario(plato.getPrecio());
			detalle.setTotal(plato.getPrecio());
			detalle.setObservacion(null);
			detalle.setEstado(true);
			lstDetalleAtencionMesaSelected.add(detalle);
			calcularTotal();
		}
		
		entradaSelected=null;
		menuSelected=null;
		cartaSelected=null;
		bebidaSelected=null;
		platoBanderaSelected=null;
		guarnicionesSelected=null;
		postresSelected=null;
		otrosSelected=null;
		
	}
	
	public void agregarDetalleManualEntrada(Plato platoSelected) {
		
		for(DetalleAtencionMesa d:lstDetalleAtencionMesaRegManualSelected) {
			if(d.getPlato()==platoSelected) {
				addErrorMessage("El plato ya esta seleccionado.");
				return;
			}
		}
			if(platoSelected!=null) {
				DetalleAtencionMesa detalle = new DetalleAtencionMesa();
				detalle.setAtencionMesa(null);
				detalle.setPlato(platoSelected);
				detalle.setPlatoEntrada(null);
				detalle.setCantidad(new BigDecimal(1));
				detalle.setPrecioUnitario(platoSelected.getPrecio());
				detalle.setTotal(detalle.getCantidad().multiply(detalle.getPrecioUnitario()));
				detalle.setObservacion("");
				detalle.setEstado(true);
				lstDetalleAtencionMesaRegManualSelected.add(detalle);
			}
		
		calcularTotalRegManual();
	}
	
	public String buscaEstadoMesa(int numMesa) {
		String color = "green";
		
		AtencionMesa atencion = atencionMesaService.findByPisoAndNumMesaAndEstadoAndSucursal(pisoSelected,numMesa, "Ocupado", navegacionBean.getSucursalLogin());
		if(atencion != null) {
			color = "red";
		}
		
		return color;
	}
	
	public void aumentarMesa() {
		int cantidadActual = pisoSelected.getCantidadMesa();
		pisoSelected.setCantidadMesa(cantidadActual+1); 
		pisoService.save(pisoSelected);
		cambiarPiso();
		addInfoMessage("Se agregó una mesa.");
	}
	
	public void disminuyeMesa() {
		int cantidadActual = pisoSelected.getCantidadMesa();
		if(cantidadActual!=0) {
			pisoSelected.setCantidadMesa(cantidadActual-1); 
			pisoService.save(pisoSelected);
			cambiarPiso();
			addInfoMessage("Se eliminó una mesa.");
		}else {
			addErrorMessage("Ya no se puede eliminar.");
		}
	}
	
	public void aumentarDetalle(DetalleAtencionMesa detalle) {
		detalle.setCantidad(detalle.getCantidad().add(BigDecimal.ONE));
		detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
		calcularTotal();
	}
	
	public void disminuyeDetalle(DetalleAtencionMesa detalle) {
		if(detalle.getCantidad().compareTo(BigDecimal.ONE) >0) {
			detalle.setCantidad(detalle.getCantidad().subtract(BigDecimal.ONE));
			detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
			calcularTotal();
		}
		
		
	}
	
	public void cantidadDetalle(DetalleAtencionMesa detalle) {
		detalle.setCantidad(detalle.getCantidad());
		detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
		calcularTotal();
		calcularTotalRegManual();
	}

	public Converter getConversorPiso() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Piso c = null;
                    for (Piso si : lstPisos) {
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
                    return ((Piso) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoEntrada() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoEntrada) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoMenu() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoMenu) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoBebida() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoBebida) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorEntradaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstEntradaEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	public Converter getConversorMenuEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstMenuEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorCartaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstCartaEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorBebidaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstBebidaEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoBanderaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoBanderaEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorGuarnicionesEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstGuarnicionesEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPostresEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPostresEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorOtrosEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstOtrosEnCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorTipoDocumentoFac() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumentoFac) {
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
	
	public List<Cliente> completeCliente(String query) {
        List<Cliente> lista = new ArrayList<>();
        for (Cliente c : lstCliente) {
    		if (c.getDniRuc().toUpperCase().contains(query.toUpperCase()) ){
                lista.add(c);
            } 
        }
        return lista;
    }
	
	public void onChangeCliente() {

		if(clienteSelected !=null) {
			dniRucText = clienteSelected.getDniRuc();
			razonSocialText = clienteSelected.getRazonSocial();
			direccionText = clienteSelected.getDireccion();
			email1Text = clienteSelected.getEmail1Fe();
			email2Text = clienteSelected.getEmail2Fe();
			email3Text = clienteSelected.getEmail3Fe();
		}else {
			dniRucText = "";
			razonSocialText = "";
			direccionText = "";
			email1Text = "";
			email2Text = "";
			email3Text = "";
		}
	
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
	
	
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public PlatoService getPlatoService() {
		return platoService;
	}
	public void setPlatoService(PlatoService platoService) {
		this.platoService = platoService;
	}
	public PisoService getPisoService() {
		return pisoService;
	}
	public void setPisoService(PisoService pisoService) {
		this.pisoService = pisoService;
	}
	public List<Piso> getLstPisos() {
		return lstPisos;
	}
	public void setLstPisos(List<Piso> lstPisos) {
		this.lstPisos = lstPisos;
	}
	public Piso getPisoSelected() {
		return pisoSelected;
	}
	public void setPisoSelected(Piso pisoSelected) {
		this.pisoSelected = pisoSelected;
	}
	public List<Mesa> getLstMesas() {
		return lstMesas;
	}
	public void setLstMesas(List<Mesa> lstMesas) {
		this.lstMesas = lstMesas;
	}
	public AtencionMesaService getAtencionMesaService() {
		return atencionMesaService;
	}
	public void setAtencionMesaService(AtencionMesaService atencionMesaService) {
		this.atencionMesaService = atencionMesaService;
	}
	public Mesa getMesaSelected() {
		return mesaSelected;
	}
	public void setMesaSelected(Mesa mesaSelected) {
		this.mesaSelected = mesaSelected;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DetalleAtencionMesaService getDetalleAtencionMesaService() {
		return detalleAtencionMesaService;
	}
	public void setDetalleAtencionMesaService(DetalleAtencionMesaService detalleAtencionMesaService) {
		this.detalleAtencionMesaService = detalleAtencionMesaService;
	}
	public List<DetalleAtencionMesa> getLstDetalleAtencionMesaSelected() {
		return lstDetalleAtencionMesaSelected;
	}
	public void setLstDetalleAtencionMesaSelected(List<DetalleAtencionMesa> lstDetalleAtencionMesaSelected) {
		this.lstDetalleAtencionMesaSelected = lstDetalleAtencionMesaSelected;
	}
	public DetalleAtencionMesa getDetalleSelected() {
		return detalleSelected;
	}
	public void setDetalleSelected(DetalleAtencionMesa detalleSelected) {
		this.detalleSelected = detalleSelected;
	}
	public AtencionMesa getAtencionSelected() {
		return atencionSelected;
	}
	public void setAtencionSelected(AtencionMesa atencionSelected) {
		this.atencionSelected = atencionSelected;
	}
	public BigDecimal getTotalDetalle() {
		return totalDetalle;
	}
	public void setTotalDetalle(BigDecimal totalDetalle) {
		this.totalDetalle = totalDetalle;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<AtencionMesa> getLstAtencionLazy() {
		return lstAtencionLazy;
	}
	public void setLstAtencionLazy(LazyDataModel<AtencionMesa> lstAtencionLazy) {
		this.lstAtencionLazy = lstAtencionLazy;
	}
	public String getEstadoChef() {
		return estadoChef;
	}
	public void setEstadoChef(String estadoChef) {
		this.estadoChef = estadoChef;
	}
	public AtencionMesa getAtencionChefSelected() {
		return atencionChefSelected;
	}
	public void setAtencionChefSelected(AtencionMesa atencionChefSelected) {
		this.atencionChefSelected = atencionChefSelected;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public String getEstadoCobrar() {
		return estadoCobrar;
	}
	public void setEstadoCobrar(String estadoCobrar) {
		this.estadoCobrar = estadoCobrar;
	}
	public LazyDataModel<AtencionMesa> getLstAtencionLazyCobrar() {
		return lstAtencionLazyCobrar;
	}
	public void setLstAtencionLazyCobrar(LazyDataModel<AtencionMesa> lstAtencionLazyCobrar) {
		this.lstAtencionLazyCobrar = lstAtencionLazyCobrar;
	}
	public AtencionMesa getAtencionCobrarSelected() {
		return atencionCobrarSelected;
	}
	public void setAtencionCobrarSelected(AtencionMesa atencionCobrarSelected) {
		this.atencionCobrarSelected = atencionCobrarSelected;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public Date getFechaRegManual() {
		return fechaRegManual;
	}
	public void setFechaRegManual(Date fechaRegManual) {
		this.fechaRegManual = fechaRegManual;
	}
	public boolean isCreditoRegManual() {
		return creditoRegManual;
	}
	public void setCreditoRegManual(boolean creditoRegManual) {
		this.creditoRegManual = creditoRegManual;
	}
	public String getDniRucRegManual() {
		return dniRucRegManual;
	}
	public void setDniRucRegManual(String dniRucRegManual) {
		this.dniRucRegManual = dniRucRegManual;
	}
	public String getClienteRegManual() {
		return clienteRegManual;
	}
	public void setClienteRegManual(String clienteRegManual) {
		this.clienteRegManual = clienteRegManual;
	}
	public List<DetalleAtencionMesa> getLstDetalleAtencionMesaRegManualSelected() {
		return lstDetalleAtencionMesaRegManualSelected;
	}
	public void setLstDetalleAtencionMesaRegManualSelected(
			List<DetalleAtencionMesa> lstDetalleAtencionMesaRegManualSelected) {
		this.lstDetalleAtencionMesaRegManualSelected = lstDetalleAtencionMesaRegManualSelected;
	}	
	public DetalleAtencionMesa getDetalleRegManSelected() {
		return detalleRegManSelected;
	}
	public void setDetalleRegManSelected(DetalleAtencionMesa detalleRegManSelected) {
		this.detalleRegManSelected = detalleRegManSelected;
	}
	public List<Plato> getLstPlatoEntrada() {
		return lstPlatoEntrada;
	}
	public void setLstPlatoEntrada(List<Plato> lstPlatoEntrada) {
		this.lstPlatoEntrada = lstPlatoEntrada;
	}
	public Plato getPlatoMenu() {
		return platoMenu;
	}
	public void setPlatoMenu(Plato platoMenu) {
		this.platoMenu = platoMenu;
	}
	public Plato getPlatoCarta() {
		return platoCarta;
	}
	public void setPlatoCarta(Plato platoCarta) {
		this.platoCarta = platoCarta;
	}
	public Plato getPlatoEntrada() {
		return platoEntrada;
	}
	public void setPlatoEntrada(Plato platoEntrada) {
		this.platoEntrada = platoEntrada;
	}
	public Plato getPlatoBebida() {
		return platoBebida;
	}
	public void setPlatoBebida(Plato platoBebida) {
		this.platoBebida = platoBebida;
	}
	public List<Plato> getLstPlatoMenu() {
		return lstPlatoMenu;
	}
	public void setLstPlatoMenu(List<Plato> lstPlatoMenu) {
		this.lstPlatoMenu = lstPlatoMenu;
	}
	public List<Plato> getLstPlatoCarta() {
		return lstPlatoCarta;
	}
	public void setLstPlatoCarta(List<Plato> lstPlatoCarta) {
		this.lstPlatoCarta = lstPlatoCarta;
	}
	public List<Plato> getLstPlatoBebida() {
		return lstPlatoBebida;
	}
	public void setLstPlatoBebida(List<Plato> lstPlatoBebida) {
		this.lstPlatoBebida = lstPlatoBebida;
	}
	public BigDecimal getTotalRegManualDetalle() {
		return totalRegManualDetalle;
	}
	public void setTotalRegManualDetalle(BigDecimal totalRegManualDetalle) {
		this.totalRegManualDetalle = totalRegManualDetalle;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	public List<TipoDocumento> getLstTipoDocumento() {
		return lstTipoDocumento;
	}
	public void setLstTipoDocumento(List<TipoDocumento> lstTipoDocumento) {
		this.lstTipoDocumento = lstTipoDocumento;
	}
	public CajaService getCajaService() {
		return cajaService;
	}
	public void setCajaService(CajaService cajaService) {
		this.cajaService = cajaService;
	}
	public List<Plato> getLstEntradaEnCarta() {
		return lstEntradaEnCarta;
	}
	public void setLstEntradaEnCarta(List<Plato> lstEntradaEnCarta) {
		this.lstEntradaEnCarta = lstEntradaEnCarta;
	}
	public List<Plato> getLstMenuEnCarta() {
		return lstMenuEnCarta;
	}
	public void setLstMenuEnCarta(List<Plato> lstMenuEnCarta) {
		this.lstMenuEnCarta = lstMenuEnCarta;
	}
	public List<Plato> getLstCartaEnCarta() {
		return lstCartaEnCarta;
	}
	public void setLstCartaEnCarta(List<Plato> lstCartaEnCarta) {
		this.lstCartaEnCarta = lstCartaEnCarta;
	}
	public List<Plato> getLstBebidaEnCarta() {
		return lstBebidaEnCarta;
	}
	public void setLstBebidaEnCarta(List<Plato> lstBebidaEnCarta) {
		this.lstBebidaEnCarta = lstBebidaEnCarta;
	}
	public Plato getEntradaSelected() {
		return entradaSelected;
	}

	public void setEntradaSelected(Plato entradaSelected) {
		this.entradaSelected = entradaSelected;
	}
	public Plato getCartaSelected() {
		return cartaSelected;
	}
	public void setCartaSelected(Plato cartaSelected) {
		this.cartaSelected = cartaSelected;
	}
	public Plato getMenuSelected() {
		return menuSelected;
	}
	public void setMenuSelected(Plato menuSelected) {
		this.menuSelected = menuSelected;
	}
	public Plato getBebidaSelected() {
		return bebidaSelected;
	}
	public void setBebidaSelected(Plato bebidaSelected) {
		this.bebidaSelected = bebidaSelected;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public TipoDocumento getTipoDocumentoSelected() {
		return tipoDocumentoSelected;
	}
	public void setTipoDocumentoSelected(TipoDocumento tipoDocumentoSelected) {
		this.tipoDocumentoSelected = tipoDocumentoSelected;
	}
	public SerieDocumentoService getSerieDocumentoService() {
		return serieDocumentoService;
	}
	public void setSerieDocumentoService(SerieDocumentoService serieDocumentoService) {
		this.serieDocumentoService = serieDocumentoService;
	}
	public List<TipoDocumento> getLstTipoDocumentoFac() {
		return lstTipoDocumentoFac;
	}
	public void setLstTipoDocumentoFac(List<TipoDocumento> lstTipoDocumentoFac) {
		this.lstTipoDocumentoFac = lstTipoDocumentoFac;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
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
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public boolean isPersonaNaturalCliente() {
		return personaNaturalCliente;
	}
	public void setPersonaNaturalCliente(boolean personaNaturalCliente) {
		this.personaNaturalCliente = personaNaturalCliente;
	}
	public Persona getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Persona personSelected) {
		this.personSelected = personSelected;
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
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVenta() {
		return lstDetalleDocumentoVenta;
	}
	public void setLstDetalleDocumentoVenta(List<DetalleDocumentoVenta> lstDetalleDocumentoVenta) {
		this.lstDetalleDocumentoVenta = lstDetalleDocumentoVenta;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVentaSelected() {
		return detalleDocumentoVentaSelected;
	}
	public void setDetalleDocumentoVentaSelected(DetalleDocumentoVenta detalleDocumentoVentaSelected) {
		this.detalleDocumentoVentaSelected = detalleDocumentoVentaSelected;
	}
	public ProductoService getProductoService() {
		return productoService;
	}
	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
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
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public String getDniRucText() {
		return dniRucText;
	}
	public void setDniRucText(String dniRucText) {
		this.dniRucText = dniRucText;
	}
	public ConsumingPostBoImpl getConsumingPostBo() {
		return consumingPostBo;
	}
	public void setConsumingPostBo(ConsumingPostBoImpl consumingPostBo) {
		this.consumingPostBo = consumingPostBo;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public NumeroALetra getNumeroALetra() {
		return numeroALetra;
	}
	public void setNumeroALetra(NumeroALetra numeroALetra) {
		this.numeroALetra = numeroALetra;
	}
	public DataSourceDocumentoVentaElectronico getDtAtencion() {
		return dtAtencion;
	}
	public void setDtAtencion(DataSourceDocumentoVentaElectronico dtAtencion) {
		this.dtAtencion = dtAtencion;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVentaSelected() {
		return lstDetalleDocumentoVentaSelected;
	}
	public void setLstDetalleDocumentoVentaSelected(List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected) {
		this.lstDetalleDocumentoVentaSelected = lstDetalleDocumentoVentaSelected;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	public String getMontoLetra() {
		return montoLetra;
	}
	public void setMontoLetra(String montoLetra) { 
		this.montoLetra = montoLetra;
	}
	public Caja getCajaAbierta() {
		return cajaAbierta;
	}
	public void setCajaAbierta(Caja cajaAbierta) {
		this.cajaAbierta = cajaAbierta;
	}

	public DataSourceComanda getDtComanda() {
		return dtComanda;
	}

	public void setDtComanda(DataSourceComanda dtComanda) {
		this.dtComanda = dtComanda;
	}

	public Plato getPlatoBanderaSelected() {
		return platoBanderaSelected;
	}

	public void setPlatoBanderaSelected(Plato platoBanderaSelected) {
		this.platoBanderaSelected = platoBanderaSelected;
	}

	public List<Plato> getLstPlatoBanderaEnCarta() {
		return lstPlatoBanderaEnCarta;
	}

	public void setLstPlatoBanderaEnCarta(List<Plato> lstPlatoBanderaEnCarta) {
		this.lstPlatoBanderaEnCarta = lstPlatoBanderaEnCarta;
	}

	public Plato getGuarnicionesSelected() {
		return guarnicionesSelected;
	}

	public void setGuarnicionesSelected(Plato guarnicionesSelected) {
		this.guarnicionesSelected = guarnicionesSelected;
	}

	public List<Plato> getLstGuarnicionesEnCarta() {
		return lstGuarnicionesEnCarta;
	}

	public void setLstGuarnicionesEnCarta(List<Plato> lstGuarnicionesEnCarta) {
		this.lstGuarnicionesEnCarta = lstGuarnicionesEnCarta;
	}

	public List<Plato> getLstPostresEnCarta() {
		return lstPostresEnCarta;
	}

	public void setLstPostresEnCarta(List<Plato> lstPostresEnCarta) {
		this.lstPostresEnCarta = lstPostresEnCarta;
	}

	public List<Plato> getLstOtrosEnCarta() {
		return lstOtrosEnCarta;
	}

	public void setLstOtrosEnCarta(List<Plato> lstOtrosEnCarta) {
		this.lstOtrosEnCarta = lstOtrosEnCarta;
	}

	public Plato getPostresSelected() {
		return postresSelected;
	}

	public void setPostresSelected(Plato postresSelected) {
		this.postresSelected = postresSelected;
	}

	public Plato getOtrosSelected() {
		return otrosSelected;
	}

	public void setOtrosSelected(Plato otrosSelected) {
		this.otrosSelected = otrosSelected;
	}

	public boolean isPorConsumo() {
		return porConsumo;
	}

	public void setPorConsumo(boolean porConsumo) {
		this.porConsumo = porConsumo;
	}

	public DataSourceCuentaDetallada getDtCuentaDetallada() {
		return dtCuentaDetallada;
	}

	public void setDtCuentaDetallada(DataSourceCuentaDetallada dtCuentaDetallada) {
		this.dtCuentaDetallada = dtCuentaDetallada;
	}
	
	
}
