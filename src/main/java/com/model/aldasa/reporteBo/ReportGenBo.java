package com.model.aldasa.reporteBo;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public interface ReportGenBo {
	public void exportJoinByFormatNotConnectDb(List<JasperPrint> listJasperPrint, String type, String outputFileName);
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName); 
    public void exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName,boolean defineAncho,Integer ancho,boolean defineLargo,Integer largo) ;
    public void formatConConexionDB(String path, String type, Map parameters, String outputFileName);
    public void exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName, String pathOut);
    public String printJasperNotConnectDb (JRDataSource dt,String path, Map parameters, String enlacePrint,String namePrint, Integer nroCopias);
    public byte[] prueba(JRDataSource dt,String path, String type, Map parameters, String outputFileName);
}
