package com.model.aldasa.reporteDaoImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.model.aldasa.reporteDao.ReportGenDao;


/**
 *
 * @author Jhon
 */
@Repository("reportGenDao")
public class ReportGenDaoImpl extends HibernateDaoSupport implements ReportGenDao{

    @Autowired
    public ReportGenDaoImpl(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
    
    @Override
    public void  exportJoinByFormatNotConnectDb(List<JasperPrint> listJasperPrint, String type, String outputFileName) {     
        try {
          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();               
          //ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
   
          byte[] bytes = null;
          
            try {

                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();             
                         
              JRExporter  exporter =  null ;   
                
                if ("pdf".equals(type)) {
                   exporter  = new JRPdfExporter();
                   response.setContentType("application/pdf");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".pdf");
                   exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, listJasperPrint);                                                                 
                   exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, arrayOutputStream );
                   exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
                   exporter.exportReport();                                                                                                      

                } else if ("xls".equals(type)) {
                   response.setContentType("application/vnd.ms-excel");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".xls");
                   exporter = new JRXlsExporter();
                   exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, listJasperPrint);
                   exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                   exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
                   exporter.exportReport(); 
                }

                  if (exporter != null) {
                      try{
                            bytes = arrayOutputStream.toByteArray();

                            if (bytes != null && bytes.length > 0) {                      

                            response.setHeader("Expires", "0");
                            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                              response.setHeader("Pragma", "public");
                               bytes = arrayOutputStream.toByteArray();                                
                               response.setContentLength(bytes.length);
                               response.getOutputStream().write(bytes, 0, bytes.length);

                            }
                      } catch (Exception e) {
                                    logger.error(e);
                      }                            
                  }                 
            }  catch (JRException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);        
                logger.error(e);
            }  finally {
                  response.getOutputStream().flush();
                  response.getOutputStream().close();                      
                context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        }       
    }
    
    
    @Override //Necesario dos métodos porque para los casos que no se usa la conexion el reporte resulta blanco.
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName,boolean defineAncho,Integer ancho,boolean defineLargo,Integer largo) {     
        try {
          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();               
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
   
          byte[] bytes = null;
          
            try {
                
                Locale locale = new Locale("es", "PE"); //idioma de  Ecuador
                parameters.put(JRParameter.REPORT_LOCALE, locale);
           
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);
                
                if(defineAncho){
                    jasperPrint.setPageWidth(ancho);
                }
                
                if(defineLargo){
                    jasperPrint.setPageHeight(largo);
                }
                
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();             
                         
              JRExporter  exporter =  null ;   
                
                if ("pdf".equals(type)) {
                   exporter  = new JRPdfExporter();
                   response.setContentType("application/pdf");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".pdf");
                   exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);                                                                 
                   exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, arrayOutputStream );
                   exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
                   exporter.exportReport();                                                                                                      

                } else if ("xls".equals(type)) {
                   response.setContentType("application/vnd.ms-excel");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".xls");
                   exporter = new JRXlsExporter();
                   exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                   exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                   exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
                   exporter.exportReport(); 
                }

                  if (exporter != null) {
                      try{
                            bytes = arrayOutputStream.toByteArray();

                            if (bytes != null && bytes.length > 0) {                      

                            response.setHeader("Expires", "0");
                            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                              response.setHeader("Pragma", "public");
                               bytes = arrayOutputStream.toByteArray();                                
                               response.setContentLength(bytes.length);
                               response.getOutputStream().write(bytes, 0, bytes.length);

                            }
                      } catch (Exception e) {
                                    logger.error(e);
                      }                            
                  }                 
            }  catch (JRException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);        
                logger.error(e);
            }  finally {
                  response.getOutputStream().flush();
                  response.getOutputStream().close();                      
                context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        }       
    }
    
    
    @Override //Necesario dos métodos porque para los casos que no se usa la conexion el reporte resulta blanco.
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName) {     
        try {
          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();               
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
   
          byte[] bytes = null;
          
            try {
                
                Locale locale = new Locale("es", "PE"); //idioma de  Ecuador
                parameters.put(JRParameter.REPORT_LOCALE, locale);
           
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();             
                         
              JRExporter  exporter =  null ;   
                
                if ("pdf".equals(type)) {
                   exporter  = new JRPdfExporter();
                   response.setContentType("application/pdf");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".pdf");
                   exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);                                                                 
                   exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, arrayOutputStream );
                   exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
                   exporter.exportReport();                                                                                                      

                } else if ("xls".equals(type)) {
                   response.setContentType("application/vnd.ms-excel");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".xls");
                   exporter = new JRXlsExporter();
                   exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                   exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                   exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
                   exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
                   exporter.exportReport(); 
                }

                  if (exporter != null) {
                      try{
                            bytes = arrayOutputStream.toByteArray();

                            if (bytes != null && bytes.length > 0) {                      

                            response.setHeader("Expires", "0");
                            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                              response.setHeader("Pragma", "public");
                               bytes = arrayOutputStream.toByteArray();                                
                               response.setContentLength(bytes.length);
                               response.getOutputStream().write(bytes, 0, bytes.length);

                            }
                      } catch (Exception e) {
                                    logger.error(e);
                      }                            
                  }                 
            }  catch (JRException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);        
                logger.error(e);
            }  finally {
                  response.getOutputStream().flush();
                  response.getOutputStream().close();                      
                context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        }       
    }
    
    @Override //Necesario dos métodos porque para los casos que no se usa la conexion el reporte resulta blanco.
    public void  exportByFormatNotConnectDb(JRDataSource dt,String path, String type, Map parameters, String outputFileName, String pathOut) {     
        try {
          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();               
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
   
          byte[] bytes = null;
          
            try {
           
                Locale locale = new Locale("es", "PE"); //idioma de  Ecuador
                parameters.put(JRParameter.REPORT_LOCALE, locale);
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();             
                         
              JRExporter  exporter =  null ;   
                
                if ("pdf".equals(type)) {
                   exporter  = new JRPdfExporter();
                   response.setContentType("application/pdf");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".pdf");
                   exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);                                                                 
                   exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, arrayOutputStream );                     
                   //exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE, pathOut + outputFileName + ".pdf");
                   exporter.exportReport();                             
                   JasperExportManager.exportReportToPdfFile(jasperPrint,scontext.getRealPath(pathOut + outputFileName + ".pdf"));
                   // cambia esto
                } else if ("xls".equals(type)) {
                   response.setContentType("application/vnd.ms-excel");
                   response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".xls");
                   exporter = new JRXlsExporter();
                   exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                   exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                   exporter.exportReport(); 
                }
                
             //   JasperPrint print = null;
           // print = JasperFillManager.fillReport(report, parametros,conn);
                 if (exporter != null) {
                      try{
                            bytes = arrayOutputStream.toByteArray();

                            if (bytes != null && bytes.length > 0) {                      

                            response.setHeader("Expires", "0");
                            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                              response.setHeader("Pragma", "public");
                               bytes = arrayOutputStream.toByteArray();                                
                               response.setContentLength(bytes.length);
                               response.getOutputStream().write(bytes, 0, bytes.length);

                            }
                      } catch (Exception e) {
                                    logger.error(e);
                      }                            
                  }                 
            }  catch (JRException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);        
                logger.error(e);
            }  finally {
                  response.getOutputStream().flush();
                  response.getOutputStream().close();                      
                context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        }       
    }
    
    public void formatConConexionDB(String path, String type, Map parameters, String outputFileName) {
     /*    try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            //InputStream inputStream = context.getExternalContext().getResourceAsStream(path);
            //InputStream inputStream = JRLoader.getFileInputStream(context.getExternalContext().getRealPath(path));
            InputStream inputStream = JRLoader.getFileInputStream(context.getExternalContext().getRealPath(path));
            
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = null;

            if (inputStream == null) {
                logger.error("NO CARGA ARCHIVO .JASPER");
                System.exit(2);
            }

            try {
                
                parameters.put(
                        JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
                        getSessionFactory().getCurrentSession());

                // upload the report
                JasperReport report = (JasperReport) JRLoader.loadObject(inputStream);
                JasperPrint jasperPrint = null;
                SessionFactoryImplementor impl = (SessionFactoryImplementor) getSessionFactory();
                ConnectionProvider cp = impl.getConnectionProvider();
                Connection conn = cp.getConnection();
                jasperPrint = JasperFillManager.fillReport(report,
                        parameters, conn);

                JRExporter exporter = null;
                if ("pdf".equals(type)) {
                    response.setContentType("application/pdf");
                    response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".pdf");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                } else if ("xls".equals(type)) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-disposition", "attachment;filename=" + outputFileName + ".xls");
                    exporter = new JRXlsExporter();
                      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                    exporter.setParameter(
                            JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                            Boolean.FALSE);
                    exporter.setParameter(
                            JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
                            Boolean.TRUE);
                    exporter.setParameter(
                            JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                            Boolean.FALSE);
                    exporter.setParameter(
                            JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                            Boolean.TRUE);
                }

                if (exporter != null) {
                    try {
                        
                        response.setHeader("Expires", "0");
                        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                        response.setHeader("Pragma", "public");
                        exporter.exportReport();
                        bytes = arrayOutputStream.toByteArray();
                        response.setContentLength(bytes.length);                                               
                        response.getOutputStream().write(bytes, 0, bytes.length);
                        
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            } catch (JRException e) {
                // display stack trace in the browser
                logger.error(e);
            } catch (HibernateException e) {
                logger.error(e);
                throw e;
            } finally {
                getSessionFactory().getCurrentSession().disconnect();
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        } */
    }
    
    /*public String printJasperNotConnectDb (JRDataSource dt,String path, Map parameters, String namePrinter){
        
       String mensaje="";       
        try {
           FacesContext context = FacesContext.getCurrentInstance();                      
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
          
                    try {   
                   JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);                                                           
                    JRExporter  jrExporter =  null ;                                                                                     
                        try {              
                    PrinterName printerName =new PrinterName(namePrinter, null);    
                        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                        printServiceAttributeSet.add(printerName);                       
                         //PrinterJob job = PrinterJob.getPrinterJob();
                         //PrintService service = PrintServiceLookup.lookupDefaultPrintService();
                         // job.setPrintService(service);
                         //PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                         //MediaSizeName mediaSizeName = MediaSize.ISO.A4.getMediaSizeName();
                         //printRequestAttributeSet.add(mediaSizeName);
                         //printRequestAttributeSet.add(new Copies(1));              
                               if(  !namePrinter.equals("")) {
                                        jrExporter =new JRPrintServiceExporter();                                     
                                        jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                                        //jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, service);
                                        //jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                                       jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
                                                                                      printServiceAttributeSet);                                         
                                        jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
                                                                                        Boolean.FALSE); //who doesn't like self-explanatory attributes!
                                        jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
                                                                                        Boolean.FALSE);                                           
                                        jrExporter.exportReport();                                        
                                        mensaje="Impreso Correctamente";
                                } else {
                                        
                                        System.err.println("Impresora " +  " no encontrada");
                                }
                        }
                        catch(Exception e) {
                                e.printStackTrace();
                                 mensaje="Impresora No Encontrada";
                        }
                    } catch (JRException e) {                
                        logger.error(e);
                    } catch (HibernateException e) {
                        logger.error(e);
                        throw e;
                    } finally {                                                          
                    }
            
        } catch (Exception j) {
            logger.error(j);
        }
        
        return mensaje;

    }*/
    
    public String printJasperNotConnectDb (JRDataSource dt,String path, Map parameters, String enlacePrint,String namePrint, Integer nroCopias){
        
       String mensaje="";       
        try {
           FacesContext context = FacesContext.getCurrentInstance();                      
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
         
                    try {   
                    
                   Locale locale = new Locale("es", "PE"); //idioma de  Ecuador
                   parameters.put(JRParameter.REPORT_LOCALE, locale);
                        
                   JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);                                                           
                    JRExporter  jrExporter =  null ;                                                                                     
                        try {              
                    PrinterName printerName =new PrinterName(enlacePrint, null);   
                        
                        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                        printServiceAttributeSet.add(printerName);
                         PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                        if (nroCopias!=null)
                        printRequestAttributeSet.add(new Copies(nroCopias));                    
                               if( !enlacePrint.equals("")) {
                                        jrExporter =new JRPrintServiceExporter();                                     
                                        jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);                                                                          
                                       jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
                                                                                      printServiceAttributeSet);       
                                       jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                                        jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
                                                                                        Boolean.FALSE); //who doesn't like self-explanatory attributes!
                                        jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
                                                                                        Boolean.FALSE);                                           
                                        jrExporter.exportReport();                                        
                                        mensaje="Impreso Correctamente "+namePrint;
                                } else {
                                        
                                        System.err.println("Impresora " +  " no encontrada "+namePrint);
                                }
                        }
                        catch(Exception e) {
                                e.printStackTrace();
                                mensaje="Impresora No Encontrada "+namePrint;
                        }
                    } catch (JRException e) {                
                        logger.error(e);
                    } catch (HibernateException e) {
                        logger.error(e);
                        throw e;
                    } finally {                                                          
                    }
            
        } catch (Exception j) {
            logger.error(j);
        }
        return mensaje;
    }
    
    @Override //Necesario dos métodos porque para los casos que no se usa la conexion el reporte resulta blanco.
    public byte[] prueba(JRDataSource dt,String path, String type, Map parameters, String outputFileName) {  
         byte[] bytes = null;
        try {
          FacesContext context = FacesContext.getCurrentInstance();                   
          ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
           
          
            try {
           
                Locale locale = new Locale("es", "PE"); //idioma de  Ecuador
                parameters.put(JRParameter.REPORT_LOCALE, locale);
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(path), parameters, dt);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();  
         
                         
              JRExporter  exporter =  null ;   
             
                if ("pdf".equals(type)) {
             //     net.sf.jasperreports.engine.export.JRHtmlExporter();  
                   exporter  = new JRHtmlExporter();
                //      net.sf.jasperreports.engine.export.
                      exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
                     // exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D,);
                     exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print({bUI: true,bSilent: true,bShrinkToFit: false});");                      
                     exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, arrayOutputStream );                 
                      exporter.exportReport();                                                                                                      

                } 

                  if (exporter != null) {
                      try{
                            bytes = arrayOutputStream.toByteArray();
                           
                      } catch (Exception e) {
                                    logger.error(e);
                      }                            
                  }                 
            }  catch (JRException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);        
                logger.error(e);
            }  finally {                                  
              //    context.responseComplete();
            }
        } catch (Exception j) {
            logger.error(j);
        }               
        return bytes;
    }
    
}
