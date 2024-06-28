package com.model.aldasa.reporteDao;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jhon
 */

public interface ReportGenDao {
    public void exportJoinByFormatNotConnectDb(List<JasperPrint> listJasperPrint, String type, String outputFileName);
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName); 
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName, String pathOut);
    public void exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName,boolean defineAncho,Integer ancho,boolean defineLargo,Integer largo) ;
    public void formatConConexionDB(String path, String type, Map parameters, String outputFileName);
    public String printJasperNotConnectDb (JRDataSource dt,String path, Map parameters, String enlacePrint,String namePrint, Integer nroCopias);
    public byte[] prueba(JRDataSource dt,String path, String type, Map parameters, String outputFileName);
}
