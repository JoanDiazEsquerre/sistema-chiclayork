package com.model.aldasa.jrdatasource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.model.aldasa.entity.DetalleAtencionMesa;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DataSourceAtencionMesa implements JRDataSource{
	private List<DetalleAtencionMesa> listaOP = new ArrayList<>();
    private int indice = -1;
    
    

    public boolean next() throws JRException {
        return ++indice < listaOP.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;
//        System.out.println("jrf.getName() "+jrf.getName());
        if ("Fecha".equals(jrf.getName())) {
            valor = listaOP.get(indice).getAtencionMesa().getFechaCobro();
        } else if ("Total".equals(jrf.getName())) {
        	BigDecimal total= BigDecimal.ZERO;
        	for(DetalleAtencionMesa det:listaOP) {
        		total = total.add(det.getTotal());
        	}
            valor = total;
        } else if ("Producto".equals(jrf.getName())) {
            valor = listaOP.get(indice).getPlato().getNombre();
        } else if ("Cantidad".equals(jrf.getName())) {
            valor = listaOP.get(indice).getCantidad();
        } else if ("PrecioUnitario".equals(jrf.getName())) {
            valor = listaOP.get(indice).getPrecioUnitario();
        } else if ("Importe".equals(jrf.getName())) {
            valor = listaOP.get(indice).getTotal(); 
        } 
        return valor;
    }

    public void addCertificadoCalidad(DetalleAtencionMesa op) {
        this.listaOP.add(op);
    }

}
