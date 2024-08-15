package com.model.aldasa.general.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jdt.internal.compiler.lookup.MostSpecificExceptionMethodBinding;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.AsistenciaService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ReporteAsistenciaBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;

	@ManagedProperty(value = "#{asistenciaService}")
	private AsistenciaService asistenciaService;	
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;


	private LazyDataModel<Asistencia> lstAsistenciaLazy;


	private List<Usuario> lstUsuarioAll;
	private List<Area> lstArea;

	private Usuario usuarioSelected;
	private Area areaSelected;
	private Asistencia asistenciaSelected;


//	private Empleado empleadoDialog;
//	private String tipoDialog;
//	private Date horaDialog;

	private String tipo;
	private String tituloDialog = "";
	private String nombreArchivo = "Reporte de Asistencia.xlsx";
	private Date fechaIni, fechaFin;
	private boolean mostrarBoton = false;
//	private Integer idSelected;
	private boolean estado = true;

	private StreamedContent fileDes;

	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");

	@PostConstruct
	public void init() {
		lstUsuarioAll = usuarioService.findAll();
		fechaIni = new Date();
		fechaFin = new Date();
		tipo = "";
		iniciarLazy();
	}
	
//	public String fechaTexto(Empleado empleado,String tipo, int sumaDia) {
//		String fecha = "-";
//		if(sumaDia==0) {
//			Date fechaIni = sumarDiasAFecha(semanaSelected.getFechaIni(), -1);
//			fechaIni = sumarDiasAFecha(fechaIni, 1);
//			fechaIni.setHours(0);
//			fechaIni.setMinutes(0);
//			fechaIni.setSeconds(0);
//			
//			Date fechaFin = sumarDiasAFecha(semanaSelected.getFechaIni(), -1);
//			fechaFin = sumarDiasAFecha(fechaIni, 1);
//			fechaFin.setHours(23);
//			fechaFin.setMinutes(59);
//			fechaFin.setSeconds(59);
//			
//			List<Asistencia> lstAsist = new ArrayList<>();
//			if(tipo.equals("E")) {
//				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc("%"+empleado.getPerson().getDni()+"%", "%E%", fechaIni, fechaFin, true);
//				if(!lstAsist.isEmpty()) {
//					fecha=sdfTime.format(lstAsist.get(0).getHora());
//				}
//			}else {
//				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc("%"+empleado.getPerson().getDni()+"%", "%S%", fechaIni, fechaFin, true);
//				if(!lstAsist.isEmpty()) {
//					fecha=sdfTime.format(lstAsist.get(0).getHora());
//				}
//			}
//
//		}else {
//			Date fechaIni = sumarDiasAFecha(semanaSelected.getFechaIni(), sumaDia);
//			fechaIni.setHours(0);
//			fechaIni.setMinutes(0);
//			fechaIni.setSeconds(0);
//			
//			Date fechaFin =sumarDiasAFecha(semanaSelected.getFechaIni(), sumaDia);
//			fechaFin.setHours(23);
//			fechaFin.setMinutes(59);
//			fechaFin.setSeconds(59);
//			
//			List<Asistencia> lstAsist = new ArrayList<>();
//			if(tipo.equals("E")) {
//				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc("%"+empleado.getPerson().getDni()+"%", "%E%", fechaIni, fechaFin, true);
//				if(!lstAsist.isEmpty()) {
//					fecha=sdfTime.format(lstAsist.get(0).getHora());
//				}
//			}else {
//				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc("%"+empleado.getPerson().getDni()+"%", "%S%", fechaIni, fechaFin, true);
//				if(!lstAsist.isEmpty()) {
//					fecha=sdfTime.format(lstAsist.get(0).getHora());
//				}
//			}
//		}
//	
//		return fecha;
//	}
	

	public long minutosTardanza(Asistencia asist) {
		long minutosDiferencia = 0;

		if (asist.getTipo().equals("E")) {

			LocalDateTime dateTime1 = LocalDateTime.of(asist.getHora().getYear(), asist.getHora().getMonth() , asist.getHora().getDate(), 8, 45);
	        LocalDateTime dateTime2 = LocalDateTime.of(asist.getHora().getYear(), asist.getHora().getMonth(), asist.getHora().getDate(), asist.getHora().getHours(), asist.getHora().getMinutes());
	        
	        // Calcular la diferencia
	        Duration duration = Duration.between(dateTime1, dateTime2);
	        
	        // Obtener la diferencia en minutos
	        minutosDiferencia = duration.toMinutes();

		}
		return minutosDiferencia;
	}

	

	

	public void updateAsistencia() {
		tituloDialog = "MODIFICAR ASISTENCIA";
		
//		idSelected = asistenciaSelected.getId();
//		empleadoDialog = asistenciaSelected.getEmpleado();
//		tipoDialog = asistenciaSelected.getTipo();
//		horaDialog = asistenciaSelected.getHora();
	}

	public void newAsistencia() {
		tituloDialog = "NUEVA ASISTENCIA";
		asistenciaSelected = new Asistencia();
//		idSelected = null;
//		empleadoDialog = null;
//		tipoDialog = "";
//		horaDialog = null;
	}
	
	public void eliminarAsistencia() {
		
		asistenciaSelected.setEstado(false);
		asistenciaSelected.setUsuarioModifica(navegacionBean.getUsuarioLogin());
		asistenciaSelected.setFechaModifica(new Date());
		asistenciaService.save(asistenciaSelected);
		
		addInfoMessage("Eliminado correctamente.");
		return ;  
		
	}

	public void saveAsistencia() {
		if (asistenciaSelected.getUsuario() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar empleado."));
			return;
		}
		if (asistenciaSelected.getTipo().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar tipo."));
			return;
		}
		if (asistenciaSelected.getHora() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar fecha."));
			return;
		}
		
		if(tituloDialog.equals("MODIFICAR ASISTENCIA")) {
			asistenciaSelected.setUsuarioModifica(navegacionBean.getUsuarioLogin());
			asistenciaSelected.setFechaModifica(new Date());
		}else{
			asistenciaSelected.setUsuarioCrea(navegacionBean.getUsuarioLogin());
			asistenciaSelected.setFechaCrea(new Date());
		}
		
		asistenciaSelected.setEstado(true);
		
		Asistencia asistencia = asistenciaService.save(asistenciaSelected);

		
		if (asistencia == null) {
			addErrorMessage("No se pudo guardar.");
		} else {
			addInfoMessage("Se guardó correctamente.");
			PrimeFaces.current().executeScript("PF('asistenciaNewDialog').hide();"); 
		}

	}

//	public void procesarExcel() {
//		XSSFWorkbook workbook = new XSSFWorkbook();
//		XSSFSheet sheet = workbook.createSheet("Asistencia");
//
//		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
//		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
//		// CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');
//
////	        Row rowTituloHoja = sheet.createRow(0);
////	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
////	        cellTituloHoja.setCellValue("Reporte de Acciones");
////	        cellTituloHoja.setCellStyle(styleBorder);
////	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo
//
//		Row rowSubTitulo = sheet.createRow(0);
//		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
//		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
//		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("ENTRADA");cellSubEntrada1.setCellStyle(styleTitulo);
//		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("SALIDA");cellSubSalida1.setCellStyle(styleTitulo);
//		Cell cellSubMinTard = rowSubTitulo.createCell(4);cellSubMinTard.setCellValue("MINUTOS DE TARDANZA");cellSubMinTard.setCellStyle(styleTitulo);
////		Cell cellUserCrea = rowSubTitulo.createCell(8);cellUserCrea.setCellValue("USER CREA");cellUserCrea.setCellStyle(styleTitulo);
////		Cell cellFechaCrea = rowSubTitulo.createCell(9);cellFechaCrea.setCellValue("FECHA CREA");cellFechaCrea.setCellStyle(styleTitulo);
////		Cell cellUserModifica = rowSubTitulo.createCell(10);cellUserModifica.setCellValue("USER MODIFICA");cellUserModifica.setCellStyle(styleTitulo);
////		Cell cellFechaModifica = rowSubTitulo.createCell(11);cellFechaModifica.setCellValue("FECHA MODIFICA");cellFechaModifica.setCellStyle(styleTitulo);
//
//		if (fechaFin.before(fechaIni)) {
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//					"La fecha fin debe ser mayor o igual que la fecha inicio");
//			PrimeFaces.current().dialog().showMessageDynamic(message);
//			return;
//		} else {
//			Date fecha1 = fechaIni;
//			Date fecha2 = fechaFin;
//
//			int index = 1;
//			while (fecha1.getTime() <= fecha2.getTime()) {
//
//				List<Empleado> lstempleados = new ArrayList<>();
//				if (empleadoSelected != null) {
//					Empleado emp = empleadoService.findByPersonId(empleadoSelected.getPerson().getId());
//					lstempleados.add(emp);
//				} else {
//					lstempleados = empleadoService.findByEstadoAndExternoOrderByPersonSurnamesAsc(true, false);
//				}
//
//				if (!lstempleados.isEmpty()) {
//					for (Empleado empleado : lstempleados) {
//						Row rowDetail = sheet.createRow(index);
//						Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(fecha1));cellfecha.setCellStyle(styleBorder);
//						Cell cellEmpleado = rowDetail.createCell(1);cellEmpleado.setCellValue(empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames());cellEmpleado.setCellStyle(styleBorder);
//						Cell cellE1 = rowDetail.createCell(2);cellE1.setCellStyle(styleBorder);
//						Cell cellS1 = rowDetail.createCell(3);cellS1.setCellStyle(styleBorder);
//						Cell cellE2 = rowDetail.createCell(4);cellE2.setCellStyle(styleBorder);
//						Cell cellS2 = rowDetail.createCell(5);cellS2.setCellStyle(styleBorder);
//						Cell cellArea = rowDetail.createCell(6);cellArea.setCellValue(empleado.getArea().getNombre());cellArea.setCellStyle(styleBorder);
//						Cell cellMinTard = rowDetail.createCell(7);cellMinTard.setCellStyle(styleBorder);
//						
//
//						Date dia1 = fecha1;
//						String day = sdf.format(dia1);
//						try {
//							dia1 = sdf.parse(day);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Date dia2 = fecha1;
//						dia2.setHours(23);
//						dia2.setMinutes(59);
//						dia2.setSeconds(59);
//						List<Asistencia> lstasistenciasEntrada = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "E", dia1, dia2, true);
//
//						if (!lstasistenciasEntrada.isEmpty()) {
//							Asistencia entrada1 = lstasistenciasEntrada.get(0);
//							cellMinTard.setCellValue(minutosTardanza(entrada1));
//
//							int a = 2;
//							for (Asistencia asistencia : lstasistenciasEntrada) {
//								Cell cellHora = rowDetail.createCell(a);
//								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
//								cellHora.setCellStyle(styleBorder);
//								a = a + 2;
//							}
//
//						}
//
//						List<Asistencia> lstasistenciasSalida = asistenciaService
//								.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "S", dia1,
//										dia2, true);
//
//						if (!lstasistenciasSalida.isEmpty()) {
//							int b = 3;
//							for (Asistencia asistencia : lstasistenciasSalida) {
//								Cell cellHora = rowDetail.createCell(b);
//								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
//								cellHora.setCellStyle(styleBorder);
//								b = b + 2;
//							}
//
//						}
//
//						index++;
//					}
//				}
//				fecha1 = sumarDiasAFecha(fecha1, 1);
//			}
//
//		}
//
//		for (int j = 0; j <= 7; j++) {
//			sheet.autoSizeColumn(j);
//		}
//		try {
//			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
//					.getContext();
//			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
//			File file = new File(filePath);
//			FileOutputStream out = new FileOutputStream(file);
//			workbook.write(out);
//			out.close();
//			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
//					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
//							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
//					.build();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
	
//	public void procesarExcelExternos() {
//		XSSFWorkbook workbook = new XSSFWorkbook();
//		XSSFSheet sheet = workbook.createSheet("Asistencia");
//
//		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
//		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
//		// CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');
//
////	        Row rowTituloHoja = sheet.createRow(0);
////	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
////	        cellTituloHoja.setCellValue("Reporte de Acciones");
////	        cellTituloHoja.setCellStyle(styleBorder);
////	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo
//
//		Row rowSubTitulo = sheet.createRow(0);
//		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
//		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
//		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("ENTRADA");cellSubEntrada1.setCellStyle(styleTitulo);
//		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("SALIDA");cellSubSalida1.setCellStyle(styleTitulo);
//		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("ENTRADA");cellSubEntrada2.setCellStyle(styleTitulo);
//		Cell cellSubSalida2 = rowSubTitulo.createCell(5);cellSubSalida2.setCellValue("SALIDA");cellSubSalida2.setCellStyle(styleTitulo);
//		Cell cellSubArea = rowSubTitulo.createCell(6);cellSubArea.setCellValue("ÁREA");cellSubArea.setCellStyle(styleTitulo);
//		Cell cellSubMinTard = rowSubTitulo.createCell(7);cellSubMinTard.setCellValue("MINUTOS DE TARDANZA");cellSubMinTard.setCellStyle(styleTitulo);
////		Cell cellUserCrea = rowSubTitulo.createCell(8);cellUserCrea.setCellValue("USER CREA");cellUserCrea.setCellStyle(styleTitulo);
////		Cell cellFechaCrea = rowSubTitulo.createCell(9);cellFechaCrea.setCellValue("FECHA CREA");cellFechaCrea.setCellStyle(styleTitulo);
////		Cell cellUserModifica = rowSubTitulo.createCell(10);cellUserModifica.setCellValue("USER MODIFICA");cellUserModifica.setCellStyle(styleTitulo);
////		Cell cellFechaModifica = rowSubTitulo.createCell(11);cellFechaModifica.setCellValue("FECHA MODIFICA");cellFechaModifica.setCellStyle(styleTitulo);
//
//		if (fechaFin.before(fechaIni)) {
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//					"La fecha fin debe ser mayor o igual que la fecha inicio");
//			PrimeFaces.current().dialog().showMessageDynamic(message);
//			return;
//		} else {
//			Date fecha1 = fechaIni;
//			Date fecha2 = fechaFin;
//
//			int index = 1;
//			while (fecha1.getTime() <= fecha2.getTime()) {
//
//				List<Empleado> lstempleados = new ArrayList<>();
//				if (empleadoSelected != null) {
//					Empleado emp = empleadoService.findByPersonId(empleadoSelected.getPerson().getId());
//					lstempleados.add(emp);
//				} else {
//					lstempleados = empleadoService.findByEstadoAndExternoOrderByPersonSurnamesAsc(true, true);
//				}
//
//				if (!lstempleados.isEmpty()) {
//					for (Empleado empleado : lstempleados) {
//						Row rowDetail = sheet.createRow(index);
//						Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(fecha1));cellfecha.setCellStyle(styleBorder);
//						Cell cellEmpleado = rowDetail.createCell(1);cellEmpleado.setCellValue(empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames());cellEmpleado.setCellStyle(styleBorder);
//						Cell cellE1 = rowDetail.createCell(2);cellE1.setCellStyle(styleBorder);
//						Cell cellS1 = rowDetail.createCell(3);cellS1.setCellStyle(styleBorder);
//						Cell cellE2 = rowDetail.createCell(4);cellE2.setCellStyle(styleBorder);
//						Cell cellS2 = rowDetail.createCell(5);cellS2.setCellStyle(styleBorder);
//						Cell cellArea = rowDetail.createCell(6);cellArea.setCellValue(empleado.getArea().getNombre());cellArea.setCellStyle(styleBorder);
//						Cell cellMinTard = rowDetail.createCell(7);cellMinTard.setCellStyle(styleBorder);
//						
//
//						Date dia1 = fecha1;
//						String day = sdf.format(dia1);
//						try {
//							dia1 = sdf.parse(day);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Date dia2 = fecha1;
//						dia2.setHours(23);
//						dia2.setMinutes(59);
//						dia2.setSeconds(59);
//						List<Asistencia> lstasistenciasEntrada = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "E", dia1, dia2, true);
//
//						if (!lstasistenciasEntrada.isEmpty()) {
//							Asistencia entrada1 = lstasistenciasEntrada.get(0);
//							cellMinTard.setCellValue(minutosTardanza(entrada1));
//
//							int a = 2;
//							for (Asistencia asistencia : lstasistenciasEntrada) {
//								Cell cellHora = rowDetail.createCell(a);
//								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
//								cellHora.setCellStyle(styleBorder);
//								a = a + 2;
//							}
//
//						}
//
//						List<Asistencia> lstasistenciasSalida = asistenciaService
//								.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "S", dia1,
//										dia2, true);
//
//						if (!lstasistenciasSalida.isEmpty()) {
//							int b = 3;
//							for (Asistencia asistencia : lstasistenciasSalida) {
//								Cell cellHora = rowDetail.createCell(b);
//								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
//								cellHora.setCellStyle(styleBorder);
//								b = b + 2;
//							}
//
//						}
//
//						index++;
//					}
//				}
//				fecha1 = sumarDiasAFecha(fecha1, 1);
//			}
//
//		}
//
//		for (int j = 0; j <= 7; j++) {
//			sheet.autoSizeColumn(j);
//		}
//		try {
//			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
//					.getContext();
//			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
//			File file = new File(filePath);
//			FileOutputStream out = new FileOutputStream(file);
//			workbook.write(out);
//			out.close();
//			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
//					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
//							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
//					.build();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

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
	
	public void iniciarLazy() {

		lstAsistenciaLazy = new LazyDataModel<Asistencia>() {
			private List<Asistencia> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Asistencia getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Asistencia asistencia : datasource) {
					if (asistencia.getId() == intRowKey) {
						return asistencia;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Asistencia asistencia) {
				return String.valueOf(asistencia.getId());
			}

			@Override
			public List<Asistencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("hora").ascending();
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

				Page<Asistencia> pageAsistencia = null;

				String dni = "%%";

				if (usuarioSelected != null) {
					dni = "%" + usuarioSelected.getPersona().getDni() + "%";
				}

				fechaIni.setHours(0);
				fechaIni.setMinutes(0);
				fechaIni.setSeconds(0);
				fechaFin.setHours(23);
				fechaFin.setMinutes(59);
				fechaFin.setSeconds(59);

				pageAsistencia = asistenciaService.findByUsuarioPersonaDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, "%" + tipo + "%", fechaIni, fechaFin, estado, pageable);

				setRowCount((int) pageAsistencia.getTotalElements());
				return datasource = pageAsistencia.getContent();
			}
		};
	}


	public String convertirHora(Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}

	public String convertirTipo(String tipo) {
		String valor = "";
		if (tipo.equals("E")) {
			valor = "ENTRADA";
		} else {
			valor = "SALIDA";
		}

		return valor;
	}

	public Converter getConversorUsuario() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Usuario c = null;
					for (Usuario si : lstUsuarioAll) {
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
					return ((Usuario) value).getId() + "";
				}
			}
		};
	}

	public List<Usuario> completeUsuario(String query) {
		List<Usuario> lista = new ArrayList<>();
		for (Usuario usuario : lstUsuarioAll) {
			if (usuario.getPersona().getApellidos().toUpperCase().contains(query.toUpperCase())
					|| usuario.getPersona().getNombres().toUpperCase().contains(query.toUpperCase())) {
				lista.add(usuario);
			}
		}
		return lista;
	}
	
	
	
	
	public Asistencia getAsistenciaSelected() {
		return asistenciaSelected;
	}
	public void setAsistenciaSelected(Asistencia asistenciaSelected) {
		this.asistenciaSelected = asistenciaSelected;
	}
	public LazyDataModel<Asistencia> getLstAsistenciaLazy() {
		return lstAsistenciaLazy;
	}
	public void setLstAsistenciaLazy(LazyDataModel<Asistencia> lstAsistenciaLazy) {
		this.lstAsistenciaLazy = lstAsistenciaLazy;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	public AsistenciaService getAsistenciaService() {
		return asistenciaService;
	}
	public void setAsistenciaService(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
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
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isMostrarBoton() {
		return mostrarBoton;
	}
	public void setMostrarBoton(boolean mostrarBoton) {
		this.mostrarBoton = mostrarBoton;
	}
	public List<Area> getLstArea() {
		return lstArea;
	}
	public void setLstArea(List<Area> lstArea) {
		this.lstArea = lstArea;
	}
	public Area getAreaSelected() {
		return areaSelected;
	}
	public void setAreaSelected(Area areaSelected) {
		this.areaSelected = areaSelected;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public List<Usuario> getLstUsuarioAll() {
		return lstUsuarioAll;
	}
	public void setLstUsuarioAll(List<Usuario> lstUsuarioAll) {
		this.lstUsuarioAll = lstUsuarioAll;
	}
	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}
	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfTime() {
		return sdfTime;
	}
	public void setSdfTime(SimpleDateFormat sdfTime) {
		this.sdfTime = sdfTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
