package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ReporteDocumentoVentaBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
   	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;

	
	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	
	private List<TipoDocumento> lstTipoDocumentoEnvioSunat;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected; 
	private List<DocumentoVenta> lstDocumentoVentaReporte = new ArrayList<>(); 

	private NumeroALetra numeroALetra = new  NumeroALetra();
	
	private StreamedContent fileDes;
	
	private TipoDocumento tipoDocumentoFilter;
	private DocumentoVenta documentoVentaSelected;

	
	private Boolean estado;
	private String nombreArchivo = "Reporte de Documento de Ventas.xlsx";
	private String montoLetra;
	private Date fechaIni, fechaFin;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct
	public void init() {
		
		List<String> lstCodigoSunat=new ArrayList<>();
		lstCodigoSunat.add("03");
		lstCodigoSunat.add("01");
		lstCodigoSunat.add("07");
		lstCodigoSunat.add("08");
		lstTipoDocumentoEnvioSunat = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoSunat);
		
		
		fechaIni = new Date();
		fechaFin = new Date();
		iniciarLazy();
		
		
	}
	
	public void listarDetalleDocumentoVenta( ) {
		montoLetra = numeroALetra.Convertir(documentoVentaSelected.getTotal()+"", true, "SOLES");
		lstDetalleDocumentoVentaSelected = new ArrayList<>();
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVentaSelected, true);
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
				String userReg = "%" + (filterBy.get("usuarioRegistro.username") != null ? filterBy.get("usuarioRegistro.username").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

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
               
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                if(estado!=null) { 
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,fechaIni, fechaFin, userReg, pageable);
                        lstDocumentoVentaReporte = documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,fechaIni, fechaFin, userReg);
                	}else {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter,fechaIni, fechaFin, userReg, pageable);
                        lstDocumentoVentaReporte = documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter,fechaIni, fechaFin, userReg);
                	}
                }else {
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,fechaIni, fechaFin, userReg, pageable);
                        lstDocumentoVentaReporte = documentoVentaService.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,fechaIni, fechaFin, userReg);
                	}else {
                        pageDocumentoVenta= documentoVentaService.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter,fechaIni, fechaFin, userReg, pageable);
                        lstDocumentoVentaReporte = documentoVentaService.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter,fechaIni, fechaFin, userReg);
                	}
                }
     
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
            }
		};
	}
	
	
	public void iniciarLazy2() {

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
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("estado").ascending();
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
                
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
               
                pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndFechaEmisionBetween(estado, navegacionBean.getSucursalLogin(), fechaIni, fechaFin, pageable);
                
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
            }
		};
	}

	public void procesarExcelSunat() {
		PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').show();"); 
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Documento Venta");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("FECHA DE EMISION DEL COMPROBANTE DE PAGO O DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("FECHA DE VENCIMIENTO O FECHA DE PAGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("COMPROBANTE DE PAGO O DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("INFORMACION DEL CLIENTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("VALOR FACTURADO DE LA EXPORTACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("BASE IMPONIBLE DE LA OPERACION GRAVADA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("IMPORTE TOTAL DE LA OPERACION EXONERADA O INAFECTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("ISC");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("IGV Y/O IPM");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("OTROS TRIBUTOS Y CARGOS QUE NO FORMAN PARTE DE LA BASE IMPONIBLE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue("IMPORTE TOTAL DEL COMPROBANTE DE PAGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue("TIPO DE CAMBIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue("REFERENCIA DEL COMPROBANTE DE PAGO O DOCUMENTO ORIGINAL QUE SE MODIFICA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(21);cellSub1.setCellValue("MONEDA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(22);cellSub1.setCellValue("EQUIVALENTE EN DOLARES AMERICANOS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(23);cellSub1.setCellValue("FECHA VENCIMIENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(24);cellSub1.setCellValue("CONDICION CONTADO/CREDITO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(25);cellSub1.setCellValue("CODIGO CENTRO DE COSTOS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(26);cellSub1.setCellValue("CODIGO CENTRO DE COSTOS 2");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(27);cellSub1.setCellValue("CUENTA CONTABLE BASE IMPONIBLE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(28);cellSub1.setCellValue("CUENTA CONTABLE OTROS TRIBUTOS Y CARGOS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(29);cellSub1.setCellValue("CUENTA CONTABLE TOTAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(30);cellSub1.setCellValue("REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(31);cellSub1.setCellValue("PORCENTAJE REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(32);cellSub1.setCellValue("IMPORTE REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(33);cellSub1.setCellValue("SERIE DOCUMENTO REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(34);cellSub1.setCellValue("NUMERO DOCUMENTO REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(35);cellSub1.setCellValue("FECHA DOCUMENTO REGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(36);cellSub1.setCellValue("CODIGO PRESUPUESTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(37);cellSub1.setCellValue("PORCENTAJE I.G.V.");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(38);cellSub1.setCellValue("GLOSA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(39);cellSub1.setCellValue("MEDIO DE PAGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(40);cellSub1.setCellValue("CONDICIÓN DE PERCEPCIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(41);cellSub1.setCellValue("IMPORTE PARA CALCULO RÉGIMEN ESPECIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(42);cellSub1.setCellValue("IMPUESTO AL CONSUMO DE LAS BOLSAS DE PLASTICO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(43);cellSub1.setCellValue("CUENTA CONTABLE ICBPER");cellSub1.setCellStyle(styleTitulo);
		
		rowSubTitulo = sheet.createRow(1);
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("DOCUMENTO IDENTIDAD");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("APELLIDOS Y NOMBRES, DENOMINACION O RAZON SOCIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(18);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(19);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(20);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(21);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(22);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(23);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(24);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(25);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(26);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(27);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(28);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(29);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(30);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(31);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(32);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(33);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(34);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(35);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(36);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(37);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(38);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(39);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(40);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(41);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(42);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(43);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		
		rowSubTitulo = sheet.createRow(2);
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("TIPO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("N° SERIE/N° SERIE MAQ REGIS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("NUMERO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("TIPO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("NUMERO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("EXONERADA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("INAFECTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue("FECHA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(18);cellSub1.setCellValue("TIPO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(19);cellSub1.setCellValue("SERIE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(20);cellSub1.setCellValue("N° COMPROBANTE PAGO O DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(21);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(22);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(23);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(24);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(25);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(26);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(27);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(28);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(29);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(30);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(31);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(32);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(33);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(34);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(35);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(36);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(37);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(38);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(39);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(40);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(41);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(42);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(43);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
		

		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0)); //combinar Celdas para titulo
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 7, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 11));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 12, 12));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 13, 13));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 14, 14));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 15, 15));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 16, 16));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 17, 20));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 21, 21));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 22, 22));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 23, 23));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 24, 24));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 25, 25));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 26, 26));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 27, 27));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 28, 28));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 29, 29));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 30, 30));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 31, 31));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 32, 32));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 33, 33));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 34, 34));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 35, 35));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 36, 36));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 37, 37));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 38, 38));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 39, 39));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 40, 40));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 41, 41));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 42, 42));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 43, 43));
		
		
		
		List<DetalleDocumentoVenta> lstDetalle = new ArrayList<>();
		fechaIni.setHours(0);
        fechaIni.setMinutes(0);
        fechaIni.setSeconds(0);
        fechaFin.setHours(23);
        fechaFin.setMinutes(59);
        fechaFin.setSeconds(59);
		
		List<String> lstCodigoFB=new ArrayList<>();
		lstCodigoFB.add("03");
		lstCodigoFB.add("01");
		lstCodigoFB.add("07");
		lstCodigoFB.add("08");
		
		if(estado!=null) { 
        	if(tipoDocumentoFilter==null) {
        		lstDetalle= detalleDocumentoVentaService.findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(estado, navegacionBean.getSucursalLogin(), lstCodigoFB, fechaIni, fechaFin);
        	}else {
        		lstDetalle= detalleDocumentoVentaService.findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(estado, navegacionBean.getSucursalLogin(), tipoDocumentoFilter,fechaIni, fechaFin);
        	}
        }else {
        	if(tipoDocumentoFilter==null) {
        		lstDetalle= detalleDocumentoVentaService.findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoCodigoInAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(navegacionBean.getSucursalLogin(), lstCodigoFB, fechaIni, fechaFin);
        	}else {
        		lstDetalle= detalleDocumentoVentaService.findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(navegacionBean.getSucursalLogin(), tipoDocumentoFilter,fechaIni, fechaFin);
        	}
        }
		
		
		int index = 3;

		if (!lstDetalle.isEmpty()) {
			for (DetalleDocumentoVenta d : lstDetalle) {
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(sdf.format(d.getDocumentoVenta().getFechaEmision()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(d.getDocumentoVenta().getTipoDocumento().getCodigo());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(d.getDocumentoVenta().getSerie());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(d.getDocumentoVenta().getNumero());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(d.getDocumentoVenta().getTipoDocumento().getCodigo().equals("01")?"06":"01");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(d.getDocumentoVenta().isEstado()? d.getDocumentoVenta().getRuc(): d.getDocumentoVenta().getTipoDocumento().getCodigo().equals("03")? "00000000":"00000000000");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(d.getDocumentoVenta().isEstado()? d.getDocumentoVenta().getRazonSocial().toUpperCase(): "ANULADA");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(!d.getDocumentoVenta().isEstado()?"" : d.getDocumentoVenta().getOpGravada().compareTo(BigDecimal.ZERO)!=0? d.getImporteVentaSinIgv()+"":"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(!d.getDocumentoVenta().isEstado()?"" :d.getDocumentoVenta().getOpGravada().compareTo(BigDecimal.ZERO)==0?d.getTotal()+"":"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue(!d.getDocumentoVenta().isEstado()?"" :d.getDocumentoVenta().getOpGravada().compareTo(BigDecimal.ZERO)!=0? d.getTotal().subtract(d.getImporteVentaSinIgv())+"":"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue(!d.getDocumentoVenta().isEstado()?"" : d.getTotal()+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				
				String fechaRef="";
				String tipoRef="";
				String serieRef="";
				String numeroRef="";
				if(d.getDocumentoVenta().getDocumentoVentaRef()!=null) {
					fechaRef = sdf.format(d.getDocumentoVenta().getDocumentoVentaRef().getFechaEmision());
					tipoRef = d.getDocumentoVenta().getDocumentoVentaRef().getTipoDocumento().getCodigo();
					serieRef = d.getDocumentoVenta().getDocumentoVentaRef().getSerie();
					numeroRef = d.getDocumentoVenta().getDocumentoVentaRef().getNumero();
				}
				cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue(fechaRef);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(18);cellSub1.setCellValue(tipoRef);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(19);cellSub1.setCellValue(serieRef);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(20);cellSub1.setCellValue(numeroRef);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(21);cellSub1.setCellValue("s");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(22);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(23);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(24);cellSub1.setCellValue("CON");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(25);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(26);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(27);cellSub1.setCellValue("70121");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(28);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(29);cellSub1.setCellValue("1212");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(30);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(31);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(32);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(33);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(34);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(35);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(36);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(37);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(38);cellSub1.setCellValue(d.getDocumentoVenta().isEstado()? d.getDescripcion(): "ANULADA");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(39);cellSub1.setCellValue("001");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(40);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(41);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(42);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(43);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				
				
				
//				Row rowDetail = sheet.createRow(index);
//				Cell cell = null;
//				Cell cellfechaEmision = rowDetail.createCell(0);cellfechaEmision.setCellValue(sdf.format(d.getFechaEmision()));cellfechaEmision.setCellStyle(styleBorder);
//				Cell cellFechaVencimiento = rowDetail.createCell(1);cellFechaVencimiento.setCellValue(sdf.format(d.getFechaEmision()));cellFechaVencimiento.setCellStyle(styleBorder);
//				Cell cellTipo = rowDetail.createCell(2);cellTipo.setCellValue(d.getTipoDocumento().getCodigo());cellTipo.setCellStyle(styleBorder);
//				Cell cellSerie = rowDetail.createCell(3);cellSerie.setCellValue(d.getSerie());cellSerie.setCellStyle(styleBorder);
//				Cell cellNumero = rowDetail.createCell(4);cellNumero.setCellValue(d.getNumero());cellNumero.setCellStyle(styleBorder);
//				Cell cellTipo2 = rowDetail.createCell(5);cellTipo2.setCellValue(d.getTipoDocumento().getAbreviatura());cellTipo2.setCellStyle(styleBorder);
//				Cell cellRuc = rowDetail.createCell(6);cellRuc.setCellValue(d.getRuc());cellRuc.setCellStyle(styleBorder);
//				Cell cellRazonSocial = rowDetail.createCell(7);cellRazonSocial.setCellValue(d.getRazonSocial());cellRazonSocial.setCellStyle(styleBorder);
//				Cell cellTotal = rowDetail.createCell(8);cellTotal.setCellValue(d.getTotal().doubleValue());cellTotal.setCellStyle(styleBorder);
				
				index++;
			}
		}
		
		
		for (int j = 0; j <= 9; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();
			
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 


		} catch (FileNotFoundException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		} catch (IOException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		}
	}
	
	public void procesarExcel() {
		PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').show();"); 
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Clientes");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("TIPO DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("NÚMERO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("FECHA EMISIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("PRODUCTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("DESCRIPCIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("CANTIDAD");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("PRECIO UNITARIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TOTAL");cellSub1.setCellStyle(styleTitulo);
		
		
		
		int index = 1;
		
		BigDecimal total = BigDecimal.ZERO;
		
		if (!lstDocumentoVentaReporte.isEmpty()) {
			for (DocumentoVenta d : lstDocumentoVentaReporte) {
				List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(d, true);
				for(DetalleDocumentoVenta dt : lstDet) {
					rowSubTitulo = sheet.createRow(index);
					cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(dt.getDocumentoVenta().getTipoDocumento().getDescripcion());cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(dt.getDocumentoVenta().getSerie() + "-" + dt.getDocumentoVenta().getNumero());cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(sdf.format(dt.getDocumentoVenta().getFechaEmision()));cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(dt.getProducto()==null ? "" : dt.getProducto().getDescripcion());cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(dt.getDescripcion() == null ? "" : dt.getDescripcion());cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(dt.getCantidad() == null ? "" : dt.getCantidad() + "");cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(dt.getPrecioUnitario() == null ? "" : dt.getPrecioUnitario() + "");cellSub1.setCellStyle(styleBorder);
					cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(dt.getTotal() == null ? "" : dt.getTotal() + "");cellSub1.setCellStyle(styleBorder);
					total = total.add(dt.getTotal());
					
					index++;
				}

				
			}
		}
		
		sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 6));
		rowSubTitulo = sheet.createRow(index);
        cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("TOTAL");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("");cellSub1.setCellStyle(styleTitulo);
        cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(total + "");cellSub1.setCellStyle(styleBorder);
        
       
		
		for (int j = 0; j <= 7; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();
			
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 


		} catch (FileNotFoundException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		} catch (IOException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		}
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
	
	
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public LazyDataModel<DocumentoVenta> getLstDocumentoVentaLazy() {
		return lstDocumentoVentaLazy;
	}
	public void setLstDocumentoVentaLazy(LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy) {
		this.lstDocumentoVentaLazy = lstDocumentoVentaLazy;
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
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public TipoDocumento getTipoDocumentoFilter() {
		return tipoDocumentoFilter;
	}
	public void setTipoDocumentoFilter(TipoDocumento tipoDocumentoFilter) {
		this.tipoDocumentoFilter = tipoDocumentoFilter;
	}
	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	public List<TipoDocumento> getLstTipoDocumentoEnvioSunat() {
		return lstTipoDocumentoEnvioSunat;
	}
	public void setLstTipoDocumentoEnvioSunat(List<TipoDocumento> lstTipoDocumentoEnvioSunat) {
		this.lstTipoDocumentoEnvioSunat = lstTipoDocumentoEnvioSunat;
	}
	public DocumentoVenta getDocumentoVentaSelected() {
		return documentoVentaSelected;
	}
	public void setDocumentoVentaSelected(DocumentoVenta documentoVentaSelected) {
		this.documentoVentaSelected = documentoVentaSelected;
	}
	public String getMontoLetra() {
		return montoLetra;
	}
	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
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
	public NumeroALetra getNumeroALetra() {
		return numeroALetra;
	}
	public void setNumeroALetra(NumeroALetra numeroALetra) {
		this.numeroALetra = numeroALetra;
	}
	public List<DocumentoVenta> getLstDocumentoVentaReporte() {
		return lstDocumentoVentaReporte;
	}
	public void setLstDocumentoVentaReporte(List<DocumentoVenta> lstDocumentoVentaReporte) {
		this.lstDocumentoVentaReporte = lstDocumentoVentaReporte;
	}

	
}
