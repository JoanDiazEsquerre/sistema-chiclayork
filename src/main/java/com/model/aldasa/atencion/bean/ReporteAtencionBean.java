package com.model.aldasa.atencion.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.jrdatasource.DataSourceAtencionMesa;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.AtencionMesaService;
import com.model.aldasa.service.CajaService;
import com.model.aldasa.service.DetalleAtencionMesaService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;
import com.model.aldasa.util.Utils;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;



@ManagedBean
@ViewScoped
public class ReporteAtencionBean extends BaseBean{
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{atencionMesaService}")
	private AtencionMesaService atencionMesaService;
	
	@ManagedProperty(value = "#{detalleAtencionMesaService}")
	private DetalleAtencionMesaService detalleAtencionMesaService;
	
	@ManagedProperty(value = "#{reportGenBo}") //Crea nueva instancia
    private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{cajaService}")
	private CajaService cajaService;
	
	private List<DetalleAtencionMesa> lstDetalleAtencionMesaSelected;
	
	private String estado = "Cobrado";
	private String numMesaSelected;
	private BigDecimal totalDetalle, totalReporte, totalCreditoReporte;
	private Date fechaIni = new Date();
	private Date fechaFin = new Date();	
	private Boolean credito;
	
	private StreamedContent fileDes;
												
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private LazyDataModel<AtencionMesa> lstAtencionLazy;
	
	private AtencionMesa atencionMesaSelected;
	private DataSourceAtencionMesa dtAtencion;
	Map<String, Object> parametros;
	
	@PostConstruct
	public void init() {
		fechaIni=sumarDiasAFecha(fechaIni, 1);
		fechaIni=sumarDiasAFecha(fechaIni, -1);
		fechaFin=sumarDiasAFecha(fechaFin, 1);
		fechaFin=sumarDiasAFecha(fechaFin, -1);

		iniciarLazy();
		calcularTotalReporte();
		
	}
	
	public void procesarExcel() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Atencion");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("CLIENTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("DNI/RUC");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("TIPO DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("SERIE-NUMERO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("NUMERO MESA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("TOTAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("FECHA COBRO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TIPO PAGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("MESERO");cellSub1.setCellStyle(styleTitulo);
	
		
		int index = 1;
		
		fechaIni.setHours(0);
        fechaIni.setMinutes(0);
        fechaIni.setSeconds(0);
       
        fechaFin.setHours(23);
        fechaFin.setMinutes(59);
        fechaFin.setSeconds(59);
        
        List<AtencionMesa> lstAtencion= new ArrayList<>();
        
        
        if(credito == null) {
        	lstAtencion= atencionMesaService.findByEstadoAndSucursalAndFechaCobroBetween(estado,navegacionBean.getSucursalLogin(), fechaIni, fechaFin);
        }else {
        	lstAtencion= atencionMesaService.findByEstadoAndSucursalAndFechaCobroBetweenAndCredito(estado,navegacionBean.getSucursalLogin(), fechaIni, fechaFin, credito);
        }
		
		
		if (!lstAtencion.isEmpty()) {
			for (AtencionMesa d : lstAtencion) {
				
				
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(d.getDocumentoVenta()!=null?d.getDocumentoVenta().getRazonSocial():"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(d.getDocumentoVenta()!=null?d.getDocumentoVenta().getRuc():"");cellSub1.setCellStyle(styleBorder);				
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(d.getTipoDocumento()!=null?d.getTipoDocumento().getDescripcion():"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(d.getDocumentoVenta()!=null?d.getDocumentoVenta().getSerie()+"-"+d.getDocumentoVenta().getNumero():"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(d.getNumMesa());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(calcularTotalVistaReporte(d)+"");cellSub1.setCellStyle(styleBorder); 
				cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(d.getFechaCobro() == null ? "" : sdfFull.format(d.getFechaCobro()));cellSub1.setCellStyle(styleBorder);
				
				String tipoPago = "";
				if(d.getMonto().compareTo(BigDecimal.ZERO)>0) {
					tipoPago = tipoPago + d.getTipoPago() +" : "+ d.getMonto()+ "\n";
				}
				
				if(d.getMonto2().compareTo(BigDecimal.ZERO)>0) {
					tipoPago = tipoPago + d.getTipoPago2() +" : "+ d.getMonto2();
				}
				
				cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(tipoPago);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(d.getUsuario().getUsername());cellSub1.setCellStyle(styleBorder); 
				
				
				index++;
			}
		}
		
		
		for (int j = 0; j <= 8; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + "Reporte de atencion.xlsx");
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name("Reporte de atencion.xlsx").contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + "Reporte de atencion.xlsx"))
					.build();
			


		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public void eliminarPedido() {
		atencionMesaSelected.setEstado("Eliminado");
		atencionMesaSelected = atencionMesaService.save(atencionMesaSelected, null, null, false);
				
		if(atencionMesaSelected!=null) {
			calcularTotalReporte();
			addInfoMessage("Pedido Eliminado.");
		}else {
			addErrorMessage("No se pudo eliminar el pedido.");
		}
		
	
	}
	
	public void updatePedido() {
		if(!atencionMesaSelected.isCredito()){
			addErrorMessage("El pedido ya se cobró.");
			return;
		}
		
		List<Caja> lstCajaAbierta = cajaService.findBySucursalAndEstado(navegacionBean.getSucursalLogin(), "Abierta");
		if(lstCajaAbierta.isEmpty()) {
			addErrorMessage("Tienes que abrir la caja del dia para poder realizar cobros.");
			return;
		}
		
		atencionMesaSelected.setFechaCobro(new Date()); 
		atencionMesaSelected.setCredito(false);
		atencionMesaSelected = atencionMesaService.save(atencionMesaSelected, lstCajaAbierta.get(0), totalDetalle, true); 
		if(atencionMesaSelected!=null) {
			calcularTotalReporte();
			addInfoMessage("Se cobró el pedido correctamente."); 
		}else {
			addErrorMessage("No se pudo cobrar el pedido.");
		}
	}
	
//	public Date sumarDiasAFecha(Date fecha, int dias) {
//		if (dias == 0)
//			return fecha;
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(fecha);
//		calendar.add(Calendar.DAY_OF_YEAR, dias);
//		Date date = calendar.getTime();
//		date.setHours(0);
//		date.setMinutes(0);
//		date.setSeconds(0);
//		return date;
//	}

	
	public void iniciarLazy() {

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
               
				String dni = "%" + (filterBy.get("documentoVenta.ruc") != null ? filterBy.get("documentoVenta.ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String names = "%" + (filterBy.get("documentoVenta.razonSocial") != null ? filterBy.get("documentoVenta.razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				
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
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
               
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                Page<AtencionMesa> pageEmpleado=null;
                
                calcularTotalReporte();
                
                if(credito == null) {
                    pageEmpleado= atencionMesaService.findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetween(names, dni, estado,navegacionBean.getSucursalLogin(), fechaIni, fechaFin, pageable);
                }else {
                    pageEmpleado= atencionMesaService.findByDocumentoVentaRazonSocialLikeAndDocumentoVentaRucLikeAndEstadoAndSucursalAndFechaCobroBetweenAndCredito(names, dni,estado,navegacionBean.getSucursalLogin(), fechaIni, fechaFin, credito, pageable);
                }
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	public void calcularTotalReporte() {
		fechaIni.setHours(0);
        fechaIni.setMinutes(0);
        fechaIni.setSeconds(0);
       
        fechaFin.setHours(23);
        fechaFin.setMinutes(59);
        fechaFin.setSeconds(59);
        
        if(credito == null) {
        	totalReporte = detalleAtencionMesaService.getTotalReporte(estado, navegacionBean.getSucursalLogin().getId(), fechaIni, fechaFin); 
        	totalCreditoReporte = detalleAtencionMesaService.getTotalReporte(estado, navegacionBean.getSucursalLogin().getId(), fechaIni, fechaFin, true);
        }else {
        	totalReporte = detalleAtencionMesaService.getTotalReporte(estado, navegacionBean.getSucursalLogin().getId(), fechaIni, fechaFin, credito); 
        	 if(credito) {
        		 totalCreditoReporte = totalReporte;
             }
      
             if(!credito) {
             	totalCreditoReporte = BigDecimal.ZERO;
             }
        }
	}
	
	public String renombrarNumMesa(AtencionMesa atencion) {
//		char letraUno=atencion.getPiso().getNombre().charAt(atencion.getPiso().getNombre().length()-1);
		
		String nombre = atencion.getNumMesa()+"";
		return nombre;
		
	}
	
	
	public void verDetalles() {
		lstDetalleAtencionMesaSelected = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencionMesaSelected, true);
		totalDetalle = calcularTotal();
		numMesaSelected = renombrarNumMesa(atencionMesaSelected);
	}
	
	public BigDecimal calcularTotal() {
		 BigDecimal totalDetalle = BigDecimal.ZERO;
		if(!lstDetalleAtencionMesaSelected.isEmpty()) {
			for(DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
				totalDetalle = totalDetalle.add(detalle.getTotal());
			}
		}
		
		return totalDetalle;
	}
	
	public String convertirHora(Date hora) {
		String a ="";
		if(hora!=null) { 
			a = sdfFull.format(hora);
		}
		
		return a;
	}
	
	public BigDecimal calcularTotalVistaReporte(AtencionMesa atencion) {
		 BigDecimal totalDetalle = BigDecimal.ZERO;
		
		 List<DetalleAtencionMesa> lstDetalle = detalleAtencionMesaService.findByAtencionMesaAndEstado(atencion,true);
		 for(DetalleAtencionMesa det: lstDetalle) {
			 totalDetalle = totalDetalle.add(det.getTotal());
		 }
		
		return totalDetalle;
	}
	
	
	public void exportarPDF() {

		dtAtencion = new DataSourceAtencionMesa();
        for (DetalleAtencionMesa detalle : lstDetalleAtencionMesaSelected) {
        	dtAtencion.addCertificadoCalidad(detalle);
        }

        parametros = new HashMap<String, Object>();

        parametros.put("PATTERN", Utils.getPattern());
        
        parametros.put("DIRECCIONSUCURSAL", navegacionBean.getSucursalLogin().getDireccion());
        parametros.put("RUCSUCURSAL", navegacionBean.getSucursalLogin().getRuc());
        parametros.put("TELEFONOSUCURSAL", navegacionBean.getSucursalLogin().getTelefono());
        parametros.put("MESERO", atencionMesaSelected.getUsuario().getPersona().getNombres()+" "+atencionMesaSelected.getUsuario().getPersona().getApellidos());
        parametros.put("MESA", atencionMesaSelected.getNumMesa());
        parametros.put("NUMEROOPERACION", atencionMesaSelected.getId());


        String path = "secured/view/modulos/atencion/reportes/jasper/repDocumentoVentaTicket.jasper";

        reportGenBo.exportByFormatNotConnectDb(dtAtencion, path, "pdf", parametros, "PRE-CUENTA" + atencionMesaSelected.getNumMesa());
        dtAtencion = null;
        parametros = null;
    }
	
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public AtencionMesaService getAtencionMesaService() {
		return atencionMesaService;
	}
	public void setAtencionMesaService(AtencionMesaService atencionMesaService) {
		this.atencionMesaService = atencionMesaService;
	}
	public DetalleAtencionMesaService getDetalleAtencionMesaService() {
		return detalleAtencionMesaService;
	}
	public void setDetalleAtencionMesaService(DetalleAtencionMesaService detalleAtencionMesaService) {
		this.detalleAtencionMesaService = detalleAtencionMesaService;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<DetalleAtencionMesa> getLstDetalleAtencionMesaSelected() {
		return lstDetalleAtencionMesaSelected;
	}
	public void setLstDetalleAtencionMesaSelected(List<DetalleAtencionMesa> lstDetalleAtencionMesaSelected) {
		this.lstDetalleAtencionMesaSelected = lstDetalleAtencionMesaSelected;
	}
	public LazyDataModel<AtencionMesa> getLstAtencionLazy() {
		return lstAtencionLazy;
	}
	public void setLstAtencionLazy(LazyDataModel<AtencionMesa> lstAtencionLazy) {
		this.lstAtencionLazy = lstAtencionLazy;
	}
	public AtencionMesa getAtencionMesaSelected() {
		return atencionMesaSelected;
	}
	public void setAtencionMesaSelected(AtencionMesa atencionMesaSelected) {
		this.atencionMesaSelected = atencionMesaSelected;
	}
	public BigDecimal getTotalDetalle() {
		return totalDetalle;
	}
	public void setTotalDetalle(BigDecimal totalDetalle) {
		this.totalDetalle = totalDetalle;
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
	public String getNumMesaSelected() {
		return numMesaSelected;
	}
	public void setNumMesaSelected(String numMesaSelected) {
		this.numMesaSelected = numMesaSelected;
	}
	public BigDecimal getTotalReporte() {
		return totalReporte;
	}
	public void setTotalReporte(BigDecimal totalReporte) {
		this.totalReporte = totalReporte;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public Boolean getCredito() {
		return credito;
	}
	public void setCredito(Boolean credito) {
		this.credito = credito;
	}
	public BigDecimal getTotalCreditoReporte() {
		return totalCreditoReporte;
	}
	public void setTotalCreditoReporte(BigDecimal totalCreditoReporte) {
		this.totalCreditoReporte = totalCreditoReporte;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public DataSourceAtencionMesa getDtAtencion() {
		return dtAtencion;
	}
	public void setDtAtencion(DataSourceAtencionMesa dtAtencion) {
		this.dtAtencion = dtAtencion;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public CajaService getCajaService() {
		return cajaService;
	}
	public void setCajaService(CajaService cajaService) {
		this.cajaService = cajaService;
	}

	public StreamedContent getFileDes() {
		return fileDes;
	}

	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	

	
	
}
