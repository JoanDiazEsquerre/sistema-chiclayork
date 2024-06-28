package com.model.aldasa.ventas.jrdatasource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.model.aldasa.entity.DetalleAtencionMesa;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Jhon
 */

public class DataSourceComanda implements JRDataSource{
    private List<DetalleAtencionMesa> listaDDV= new ArrayList<>();
    private int indice = -1;
    
    @Override
    public boolean next() throws JRException {
       return ++indice < listaDDV.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;  // busca el modelo de boleta el pdf

            if("Plato".equals(jrf.getName())) { 
                valor = listaDDV.get(indice).getPlato().getNombre();                        
            }else if ("Entrada".equals(jrf.getName())){
            	if(listaDDV.get(indice).getPlatoEntrada() !=null) {
            		valor = listaDDV.get(indice).getPlatoEntrada().getNombre();     
            	}else {
            		valor = "";
            	}
                
            }else if ("Observacion".equals(jrf.getName())){
            	if(listaDDV.get(indice).getObservacion() != null) {
            		valor = listaDDV.get(indice).getObservacion();
            	}else {
            		valor = "";
            	}
                
            }    
            return valor; 
    }
    
    public void addResumenDetalle(DetalleAtencionMesa ddv){
        this.listaDDV.add(ddv);
    }

	


    
    
}
