package com.model.aldasa.reporteBoImpl;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.reporteDao.ReportGenDao;
import com.model.aldasa.reporteDaoImpl.ReportGenDaoImpl;

/**
 *
 * @author Jhon
 */
@Service("reportGenBo")
public class ReportGenBoImpl implements ReportGenBo{
    
    @Autowired
    private ReportGenDaoImpl reportGenDao;
    
    public void exportByFormatNotConnectDb(JRDataSource dt, String path, String type, Map parameters, String outputFileName) {
        reportGenDao.exportByFormatNotConnectDb(dt, path, type, parameters, outputFileName);
    }

    public void exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName, String pathOut){
        reportGenDao.exportByFormatNotConnectDb(dt,path, type, parameters, outputFileName, pathOut);
    }
    
    public void exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName,boolean defineAncho,Integer ancho,boolean defineLargo,Integer largo) {
        reportGenDao.exportByFormatNotConnectDb(dt, path, type, parameters, outputFileName, defineAncho, ancho, defineLargo, largo);
    }
            
    public void formatConConexionDB(String path, String type, Map parameters, String outputFileName) {
        reportGenDao.formatConConexionDB(path, type, parameters, outputFileName);
    }
    
     public String printJasperNotConnectDb (JRDataSource dt,String path, Map parameters, String enlacePrint,String namePrint, Integer nroCopias){
        return reportGenDao.printJasperNotConnectDb(dt, path, parameters,enlacePrint,namePrint, nroCopias);
     }
     
     public byte[] prueba(JRDataSource dt,String path, String type, Map parameters, String outputFileName){
         return prueba(dt,path, type, parameters, outputFileName);
     }

    public void exportJoinByFormatNotConnectDb(List<JasperPrint> listJasperPrint, String type, String outputFileName) {
         reportGenDao.exportJoinByFormatNotConnectDb(listJasperPrint, type, outputFileName);
    }
    
}

