package com.model.aldasa.ventas.jrdatasource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.model.aldasa.entity.DetalleDocumentoVenta;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Jhon
 */

public class DataSourceDocumentoVentaElectronico implements JRDataSource{
    private List<DetalleDocumentoVenta> listaDDV= new ArrayList<>();
    private int indice = -1;
    
    @Override
    public boolean next() throws JRException {
       return ++indice < listaDDV.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;  // busca el modelo de boleta el pdf

            if("Producto".equals(jrf.getName())) { 
                valor = listaDDV.get(indice).getDescripcion();                        
            }else if ("Cantidad".equals(jrf.getName())){
                valor = listaDDV.get(indice).getCantidad(); 
            }else if ("PrecioUnitario".equals(jrf.getName())){
                valor = listaDDV.get(indice).getPrecioUnitario(); 
            }else if ("Importe".equals(jrf.getName())){
                valor = listaDDV.get(indice).getTotal();
            }     
            return valor; 
    }
    
    public void addResumenDetalle(DetalleDocumentoVenta ddv){
        this.listaDDV.add(ddv);
    }

	


    
    
}
