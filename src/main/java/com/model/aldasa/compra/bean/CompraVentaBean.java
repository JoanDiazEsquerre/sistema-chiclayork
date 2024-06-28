package com.model.aldasa.compra.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.CompraVenta;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.DetalleCompra;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CompraService;
import com.model.aldasa.service.DetalleAtencionMesaService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoAtencionMesaType;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class CompraVentaBean extends BaseBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{compraService}")
	private CompraService compraService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{detalleAtencionMesaService}")
	private DetalleAtencionMesaService detalleAtencionMesaService;
	
	private List<CompraVenta>  lstCompraVenta;
	
	private Date fechaIni, fechaFin = new Date();
	private String nombreArchivo = "Compra Venta.xlsx";
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private StreamedContent fileDes;

	
	@PostConstruct
	public void init() {
		fechaIni = new Date();
		fechaIni.setDate(1);
	}
	
	public void buscar() throws ParseException {
		
		if (fechaFin.before(fechaIni)) {
			addErrorMessage("La fecha fin debe ser mayor o igual que la fecha inicio");
			return;
		} 
		
		lstCompraVenta = new ArrayList<>();
		
		String fechaIniTexto = sdf.format(fechaIni);
		String fechaFinTexto = sdf.format(fechaFin);
		Date fechaI = sdf.parse(fechaIniTexto);
		Date fechaF = sdf.parse(fechaFinTexto);
		
		while (fechaI.getTime() <= fechaF.getTime()) {
			String ini = sdf.format(fechaI);
			
			Date fechaBusquedaIni = sdf.parse(ini);
			fechaBusquedaIni.setHours(0);
			fechaBusquedaIni.setMinutes(0);
			fechaBusquedaIni.setSeconds(0);
			Date fechaBusquedaFin = sdf.parse(ini);
			fechaBusquedaFin.setHours(23);
			fechaBusquedaFin.setMinutes(59);
			fechaBusquedaFin.setSeconds(59);
			
			
			Compra compra = compraService.findByfechaAndSucursalAndEstado(fechaI, navegacionBean.getSucursalLogin(), true);
			List<DetalleAtencionMesa> lstDetalle = detalleAtencionMesaService.findByAtencionMesaEstadoAndAtencionMesaSucursalAndAtencionMesaFechaCobroBetweenAndEstado(EstadoAtencionMesaType.COBRADO.getName(), navegacionBean.getSucursalLogin(), fechaBusquedaIni, fechaBusquedaFin, true);
			BigDecimal totalVenta = BigDecimal.ZERO;
			BigDecimal totalVentaCredito = BigDecimal.ZERO;
			BigDecimal totalVentaContado = BigDecimal.ZERO;			
			if(!lstDetalle.isEmpty()) {
				for(DetalleAtencionMesa d:lstDetalle) {
					totalVenta = totalVenta.add(d.getTotal());
					if(lstDetalle.get(0).getAtencionMesa().isCredito()) {
						totalVentaCredito=totalVentaCredito.add(d.getTotal());
					}else {
						totalVentaContado=totalVentaContado.add(d.getTotal());						
					}
				}
			}
			
			CompraVenta compraVenta = new CompraVenta();
			compraVenta.setFecha(fechaI);
			compraVenta.setMontoCompra(compra == null ? BigDecimal.ZERO : compra.getTotal());
			compraVenta.setMontoVenta(totalVenta);
			compraVenta.setMontoVentaCredito(totalVentaCredito);
			compraVenta.setMontoVentaContado(totalVentaContado);
			compraVenta.setCalculo(totalVenta.subtract(compraVenta.getMontoCompra()));
			lstCompraVenta.add(compraVenta);
			
			fechaI = sumarDiasAFecha(fechaI, 1);
			
		}
		addInfoMessage("Se buscó correctamente el reporte.");
	}
	
	public Date sumarDiasAFecha(Date fecha, int dias) {
		if (dias == 0)
			return fecha;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		Date date = calendar.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Compra Venta");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		// CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//	        Row rowTituloHoja = sheet.createRow(0);
//	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//	        cellTituloHoja.setCellValue("Reporte de Acciones");
//	        cellTituloHoja.setCellStyle(styleBorder);
//	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubMontoCompra = rowSubTitulo.createCell(1);cellSubMontoCompra.setCellValue("MONTO COMPRA");cellSubMontoCompra.setCellStyle(styleTitulo);
		Cell cellSubMontoVenta = rowSubTitulo.createCell(2);cellSubMontoVenta.setCellValue("MONTO VENTA");cellSubMontoVenta.setCellStyle(styleTitulo);
		Cell cellSubMontoVentaCredito = rowSubTitulo.createCell(3);cellSubMontoVentaCredito.setCellValue("MONTO VENTA CREDITO");cellSubMontoVentaCredito.setCellStyle(styleTitulo);
		Cell cellSubMontoVentaContado = rowSubTitulo.createCell(4);cellSubMontoVentaContado.setCellValue("MONTO VENTA CONTADO");cellSubMontoVentaContado.setCellStyle(styleTitulo);
		Cell cellSubGanancia = rowSubTitulo.createCell(5);cellSubGanancia.setCellValue("GANANCIA");cellSubGanancia.setCellStyle(styleTitulo);

		int index = 1;
		if(lstCompraVenta.isEmpty()) {
			addErrorMessage("Lista vacía.");
			return;
		}
		
		BigDecimal totalMontoCompra = BigDecimal.ZERO;
		BigDecimal totalMontoVenta = BigDecimal.ZERO;
		BigDecimal totalMontoVentaCredito = BigDecimal.ZERO;
		BigDecimal totalMontoVentaContado = BigDecimal.ZERO;
		BigDecimal totalGanancia = BigDecimal.ZERO;

		for(CompraVenta c:lstCompraVenta) {
			Row rowDetail = sheet.createRow(index);
			Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(c.getFecha()));cellfecha.setCellStyle(styleBorder);
			Cell cellMontoCompra = rowDetail.createCell(1);cellMontoCompra.setCellValue(c.getMontoCompra() + "");cellMontoCompra.setCellStyle(styleBorder);
			Cell cellMontoVenta = rowDetail.createCell(2);cellMontoVenta.setCellValue(c.getMontoVenta() + "");cellMontoVenta.setCellStyle(styleBorder);
			Cell cellMontoVentaCredito = rowDetail.createCell(3);cellMontoVentaCredito.setCellValue(c.getMontoVentaCredito() + "");cellMontoVentaCredito.setCellStyle(styleBorder);
			Cell cellMontoVentaContado = rowDetail.createCell(4);cellMontoVentaContado.setCellValue(c.getMontoVentaContado() + "");cellMontoVentaContado.setCellStyle(styleBorder);
			Cell cellGanancia = rowDetail.createCell(5);cellGanancia.setCellValue(c.getCalculo() + "");cellGanancia.setCellStyle(styleBorder);
			
			index++;
			
			totalMontoCompra = totalMontoCompra.add(c.getMontoCompra());
			totalMontoVenta = totalMontoVenta.add(c.getMontoVenta());
			totalMontoVentaCredito = totalMontoVentaCredito.add(c.getMontoVentaCredito());
			totalMontoVentaContado = totalMontoVentaContado.add(c.getMontoVentaContado());
			totalGanancia = totalGanancia.add(c.getCalculo());

		}
		
		
		Row rowDetail2 = sheet.createRow(index);
		Cell cellTotales = rowDetail2.createCell(0);cellTotales.setCellValue("TOTALES");cellTotales.setCellStyle(styleBorder);
		Cell cellTotalMontoCompra = rowDetail2.createCell(1);cellTotalMontoCompra.setCellValue(totalMontoCompra + "");cellTotalMontoCompra.setCellStyle(styleBorder);
		Cell cellTotalMontoVenta = rowDetail2.createCell(2);cellTotalMontoVenta.setCellValue(totalMontoVenta + "");cellTotalMontoVenta.setCellStyle(styleBorder);
		Cell cellTotalMontoVentaCredito = rowDetail2.createCell(3);cellTotalMontoVentaCredito.setCellValue(totalMontoVentaCredito + "");cellTotalMontoVentaCredito.setCellStyle(styleBorder);
		Cell cellTotalMontoVentaContado = rowDetail2.createCell(4);cellTotalMontoVentaContado.setCellValue(totalMontoVentaContado + "");cellTotalMontoVentaContado.setCellStyle(styleBorder);
		Cell cellTotalGanancia = rowDetail2.createCell(5);cellTotalGanancia.setCellValue(totalGanancia + "");cellTotalGanancia.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 5; j++) {
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
	public List<CompraVenta> getLstCompraVenta() {
		return lstCompraVenta;
	}
	public void setLstCompraVenta(List<CompraVenta> lstCompraVenta) {
		this.lstCompraVenta = lstCompraVenta;
	}
	public CompraService getCompraService() {
		return compraService;
	}
	public void setCompraService(CompraService compraService) {
		this.compraService = compraService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public DetalleAtencionMesaService getDetalleAtencionMesaService() {
		return detalleAtencionMesaService;
	}
	public void setDetalleAtencionMesaService(DetalleAtencionMesaService detalleAtencionMesaService) {
		this.detalleAtencionMesaService = detalleAtencionMesaService;
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
	
	
	
}
