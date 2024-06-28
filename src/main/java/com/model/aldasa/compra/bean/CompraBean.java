package com.model.aldasa.compra.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Compra;
import com.model.aldasa.entity.DetalleCompra;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CompraService;
import com.model.aldasa.service.DetalleCompraService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class CompraBean extends BaseBean {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{compraService}")
	private CompraService compraService;
	
	@ManagedProperty(value = "#{detalleCompraService}")
	private DetalleCompraService detalleCompraService;
	
	private LazyDataModel<Compra> lstCompraLazy;
	
	private List<DetalleCompra>  lstDetalleCompra;
	private List<DetalleCompra> lstDetalleCompraSelected;
	
	private DetalleCompra detalleCompraSelected;
	private Compra compraSelected;
	
	private Date fechaIni, fechaFin, fecha;
	private String producto = "";
	private String productoDialog = "";
	private String fechaVista;
	private BigDecimal precio;
	private BigDecimal precioDialog;
	private boolean estado ;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct
	public void init() {
		
		fechaIni = new Date();
		fechaIni.setDate(1);
		fechaFin = new Date();
		fechaIni = sumarDiasAFecha(fechaIni, 1);
		fechaIni = sumarDiasAFecha(fechaIni, -1);
		fechaFin = sumarDiasAFecha(fechaFin, 1);
		fechaFin = sumarDiasAFecha(fechaFin, -1);
		fecha = new Date();
		producto = "";
		estado = true;
		newListaDetalle();
		iniciarLazy();
	}
	
	public void newListaDetalle() {
		lstDetalleCompra = new ArrayList<>();
	}
	
	
	public void deleteDetalle(DetalleCompra detalle) {
		lstDetalleCompra.remove(detalle);
		addInfoMessage("Detalle eliminado correctamente.");
	}
	
	public void deleteDetalleSelected(DetalleCompra detalle) {
		detalle.setEstado(false);
		detalleCompraService.save(detalle);
		updateCompra();
		BigDecimal nuevoTotal = BigDecimal.ZERO;
		for(DetalleCompra c:lstDetalleCompraSelected) {
			nuevoTotal = nuevoTotal.add(c.getPrecio());
		}
		compraSelected.setTotal(nuevoTotal);
		compraService.save(compraSelected);
		addInfoMessage("Eliminado correctamente.");
		
	}
	
	public void updateCompra() {
		productoDialog = "";
		precioDialog = null;
		lstDetalleCompraSelected = detalleCompraService.findByCompraAndEstado(compraSelected, true);
		fechaVista = sdf.format(compraSelected.getFecha());
	}

	public void agregarItem() {
		
		if(producto.equals("")) {
			addErrorMessage("Ingresar producto.");
			return;
		}
		if(precio == null) {
			addErrorMessage("Ingresar precio.");
			return;
		}
		if(precio.compareTo(BigDecimal.ZERO)<=0  ) {
			
			addErrorMessage("Precio tiene que ser mayor que 0.");
			return;
		}
		
		DetalleCompra detalle = new DetalleCompra();	
		detalle.setDescripcionProducto(producto);
		detalle.setPrecio(precio);
		detalle.setEstado(true);
		lstDetalleCompra.add(detalle);
		producto = "";
		precio = null;
		addInfoMessage("Item Agregado.");
		
	}
	
	public void agregarItemDialog() {
		
		if(productoDialog.equals("")) {
			addErrorMessage("Ingresar producto.");
			return;
		}
		if(precioDialog == null) {
			addErrorMessage("Ingresar precio.");
			return;
		}
		if(precioDialog.compareTo(BigDecimal.ZERO)<=0  ) {
			
			addErrorMessage("Precio tiene que ser mayor que 0.");
			return;
		}
		
		DetalleCompra detalle = new DetalleCompra();
		detalle.setCompra(compraSelected);
		detalle.setDescripcionProducto(productoDialog);
		detalle.setPrecio(precioDialog);
		detalle.setEstado(true);
		DetalleCompra guarda = detalleCompraService.save(detalle);
		if(guarda!=null) {
			productoDialog = "";
			precioDialog = null;
			updateCompra();
			BigDecimal total = BigDecimal.ZERO;
			for(DetalleCompra c:lstDetalleCompraSelected) {
				total = total.add(c.getPrecio());
			}
			compraSelected.setTotal(total);
			compraService.save(compraSelected);
			addInfoMessage("Item Agregado.");
		}else {
			addErrorMessage("No se pudo agregar el item.");
		}
		
		
	}
	
	public void saveCompra() {
		if(fecha == null) {
			addErrorMessage("Ingresar fecha.");
			return;
		}

		Compra fechaCompra = compraService.findByfecha(fecha);
		
		if(fechaCompra!=null) {
			addErrorMessage("Ya se encuentra registrado una compra con la fecha " + sdf.format(fecha));
			return;
		}
		
		if(!lstDetalleCompra.isEmpty()) {
			BigDecimal totalDetalle = BigDecimal.ZERO;
			for(DetalleCompra d:lstDetalleCompra) {
				totalDetalle = totalDetalle.add(d.getPrecio());
			}
			Compra compra = new	Compra();
			compra.setFecha(fecha);
			compra.setEstado(true);
			compra.setTotal(totalDetalle);
			compra.setFechaRegistro(new Date());
			compra.setUsuario(navegacionBean.getUsuarioLogin());
			compra.setSucursal(navegacionBean.getSucursalLogin());
			Compra guardar = compraService.save(compra);

			if(guardar!= null) {
				for(DetalleCompra d:lstDetalleCompra) {
					d.setCompra(guardar);
					detalleCompraService.save(d);
				}
				lstDetalleCompra.clear();
				addInfoMessage("Detalle guardado correctamente.");
				
			}else {
				addErrorMessage("No se pudo guardar el detalle");
			}
		}else {
			addErrorMessage("Lista de detalles esta vacía.");
		}
	}
	
	public void iniciarLazy() {

		lstCompraLazy = new LazyDataModel<Compra>() {
			private List<Compra> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Compra getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Compra compra : datasource) {
                    if (compra.getId() == intRowKey) {
                        return compra;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Compra compra) {
                return String.valueOf(compra.getId());
            }

			@Override
			public List<Compra> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fecha").ascending();
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
                
                
                Page<Compra> pageCompra= compraService.findByEstadoAndSucursalAndFechaBetween(estado,navegacionBean.getSucursalLogin(), fechaIni, fechaFin, pageable);
                
                
                setRowCount((int) pageCompra.getTotalElements());
                return datasource = pageCompra.getContent();
            }
		};
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public List<DetalleCompra> getLstDetalleCompra() {
		return lstDetalleCompra;
	}
	public void setLstDetalleCompra(List<DetalleCompra> lstDetalleCompra) {
		this.lstDetalleCompra = lstDetalleCompra;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public DetalleCompra getDetalleCompraSelected() {
		return detalleCompraSelected;
	}
	public void setDetalleCompraSelected(DetalleCompra detalleCompraSelected) {
		this.detalleCompraSelected = detalleCompraSelected;
	}

	public LazyDataModel<Compra> getLstCompraLazy() {
		return lstCompraLazy;
	}
	public void setLstCompraLazy(LazyDataModel<Compra> lstCompraLazy) {
		this.lstCompraLazy = lstCompraLazy;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
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
	public CompraService getCompraService() {
		return compraService;
	}
	public void setCompraService(CompraService compraService) {
		this.compraService = compraService;
	}
	public Compra getCompraSelected() {
		return compraSelected;
	}
	public void setCompraSelected(Compra compraSelected) {
		this.compraSelected = compraSelected;
	}
	public DetalleCompraService getDetalleCompraService() {
		return detalleCompraService;
	}
	public void setDetalleCompraService(DetalleCompraService detalleCompraService) {
		this.detalleCompraService = detalleCompraService;
	}
	public List<DetalleCompra> getLstDetalleCompraSelected() {
		return lstDetalleCompraSelected;
	}
	public void setLstDetalleCompraSelected(List<DetalleCompra> lstDetalleCompraSelected) {
		this.lstDetalleCompraSelected = lstDetalleCompraSelected;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public String getFechaVista() {
		return fechaVista;
	}
	public void setFechaVista(String fechaVista) {
		this.fechaVista = fechaVista;
	}
	public String getProductoDialog() {
		return productoDialog;
	}
	public void setProductoDialog(String productoDialog) {
		this.productoDialog = productoDialog;
	}
	public BigDecimal getPrecioDialog() {
		return precioDialog;
	}
	public void setPrecioDialog(BigDecimal precioDialog) {
		this.precioDialog = precioDialog;
	}
	

}
