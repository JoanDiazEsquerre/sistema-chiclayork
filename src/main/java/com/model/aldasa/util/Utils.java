package com.model.aldasa.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.hibernate.criterion.Restrictions;

import com.model.aldasa.entity.Sucursal;

public class Utils {
	 public static final BigDecimal IGV = new BigDecimal(1.18);
	 public static final BigDecimal IGV_PORCENTAJE = new BigDecimal(0.18);

    public static int getDayOfTheWeek(Date d) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int obtenerUltimoDiaMes(int anio, int mes) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String mess = "";
        if (mes < 10) {
            mess = "0" + mes;
        } else {
            mess = String.valueOf(mes);
        }

        Date date = sdf.parse("01/" + mess + "/" + anio);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        /*System.out.println("First Day Of Month : " + calendar.getActualMinimum(Calendar.DAY_OF_MONTH));  
        System.out.println("Last Day of Month  : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));*/
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date crearFechaConHora(Date fecha, Date hora) {
        try {
            Date res = null;
            SimpleDateFormat sdfFechaTotal = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
            String textFechaTotal = "";
            String textTime = sdfTime.format(hora);
            String textFecha = sdfFecha.format(fecha);
            textFechaTotal = textFecha + " " + textTime;
            res = sdfFechaTotal.parse(textFechaTotal);

            return res;
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String diaSemana(Date fecha) {
        try {
            SimpleDateFormat sdfAnio = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdfMes = new SimpleDateFormat("MM");
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
            int año = Integer.parseInt(sdfAnio.format(fecha));
            int mes = Integer.parseInt(sdfMes.format(fecha));
            int dia = Integer.parseInt(sdfDia.format(fecha));
            SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
            String[] dias = {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
            String textDia = "";
            String textMes = "";
            String textFecha = "";
            if (mes < 10) {
                textMes = "0" + mes;
            } else {
                textMes = "" + mes;
            }
            if (dia < 10) {
                textDia = "0" + dia;
            } else {
                textDia = "" + dia;
            }

            textFecha = año + "-" + textMes + "-" + textDia;
            Date hoy;

            hoy = sdfFecha.parse(textFecha);

//	        Date hoy = new Date(añoC, mesC + 1, dia);
            int numeroDia = 0;
            Calendar cal = Calendar.getInstance();
            cal.setTime(hoy);
            numeroDia = cal.get(Calendar.DAY_OF_WEEK);
            String nombreDia = dias[numeroDia - 1];

            return nombreDia;
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static int numDiaSemana(String dia) {

        int numeroDia = 0;
        if (dia.equals("Domingo")) {
            numeroDia = 1;
        } else if (dia.equals("Lunes")) {
            numeroDia = 2;
        } else if (dia.equals("Martes")) {
            numeroDia = 3;
        } else if (dia.equals("Miercoles")) {
            numeroDia = 4;
        } else if (dia.equals("Jueves")) {
            numeroDia = 5;
        } else if (dia.equals("Viernes")) {
            numeroDia = 6;
        } else if (dia.equals("Sabado")) {
            numeroDia = 7;
        }

        return numeroDia;

    }

    public static Date crearFechaDeTIME(Date fecha, Date Time) {
        SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfAnio = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMes = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
        int anio = Integer.parseInt(sdfAnio.format(fecha));
        int mes = Integer.parseInt(sdfMes.format(fecha));
        int dia = Integer.parseInt(sdfDia.format(fecha));
        String time = sdfTime.format(Time);
        String textDia = "";
        String textMes = "";
        String textFecha = "";
        if (mes < 10) {
            textMes = "0" + mes;
        } else {
            textMes = "" + mes;
        }
        if (dia < 10) {
            textDia = "0" + dia;
        } else {
            textDia = "" + dia;
        }

        textFecha = anio + "-" + textMes + "-" + textDia + " " + time;
        Date fechaRes = null;
        try {
            fechaRes = sdfFecha.parse(textFecha);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fechaRes;
    }

    public String nombreMes(int mes) {
        String nameMes = "";
        switch (mes) {
            case 1:
                nameMes = "ENERO";
                break;
            case 2:
                nameMes = "FEBRERO";
                break;
            case 3:
                nameMes = "MARZO";
                break;
            case 4:
                nameMes = "ABRIL";
                break;
            case 5:
                nameMes = "MAYO";
                break;
            case 6:
                nameMes = "JUNIO";
                break;
            case 7:
                nameMes = "JULIO";
                break;
            case 8:
                nameMes = "AGOSTO";
                break;
            case 9:
                nameMes = "SEPTIEMBRE";
                break;
            case 10:
                nameMes = "OCTUBRE";
                break;
            case 11:
                nameMes = "NOVIEMBRE";
                break;
            case 12:
                nameMes = "DICIEMBRE";
                break;
            default:
                nameMes = "-";
                break;
        }

        return nameMes;
    }

    public static DecimalFormatSymbols getPattern() {
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        custom.setGroupingSeparator(' ');
        return custom;
    }

    public static DecimalFormatSymbols getPatternXls() {
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        custom.setGroupingSeparator(',');
        return custom;
    }

//    public static String getImpresora(String impresora) {
//        try {
//            String PATH = "/com/webapp/propiedades/printers.properties";
//            Properties properties = new Properties();
//            properties.load(SessionBean.class.getResourceAsStream(PATH));
//            return properties.getProperty(impresora);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }

    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }

    public static InputStream getRutaGrafico(String rutaGrafico) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getResourceAsStream(rutaGrafico);
    }

    public static String formmatting(BigDecimal valor) {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        custom.setGroupingSeparator(' ');
        format.setDecimalFormatSymbols(custom);
        if (valor.compareTo(BigDecimal.ZERO) > 0) {
            return format.format(valor);
        } else {
            return "";
        }

    }

    public String nombreMes(Integer position) {
        String name = "";
        switch (position) {
            case 1:
                name = "ENERO";
                break;
            case 2:
                name = "FEBRERO";
                break;
            case 3:
                name = "MARZO";
                break;
            case 4:
                name = "ABRIL";
                break;
            case 5:
                name = "MAYO";
                break;
            case 6:
                name = "JUNIO";
                break;
            case 7:
                name = "JULIO";
                break;
            case 8:
                name = "AGOSTO";
                break;
            case 9:
                name = "SEPTIEMBRE";
                break;
            case 10:
                name = "OCTUBRE";
                break;
            case 11:
                name = "NOVIEMBRE";
                break;
            case 12:
                name = "DICIEMBRE";
                break;
        }

        return name;
    }

    public static String quitarCaracteresEspeciales(String cadena) {
        return cadena.replaceAll("á", "a")
                .replaceAll("é", "e")
                .replaceAll("í", "i")
                .replaceAll("ó", "o")
                .replaceAll("ú", "u")
                .replaceAll("Á", "A")
                .replaceAll("É", "E")
                .replaceAll("Í", "I")
                .replaceAll("Ó", "O")
                .replaceAll("Ú", "U")
                .replaceAll("Ñ", "N")
                .replaceAll("ñ", "n");
    }

    public static String numeroARomano(Integer N) {//entre 0 y 99;
        String Unidad[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        String Decena[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        if (N >= 10) {
            int u = N % 10;
            int d = N / 10;
            return (Decena[d] + Unidad[u]);
        } else {
            return (Unidad[N]);
        }
    }

    public static String numeroARomano2(Integer N) {//entre 1000 a  2000
        String Unidad[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        String Decena[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String Centena[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String Mil[] = {"", "M", "MM"};

        int u = N % 10;
        int d = (N / 10) % 10;
        int c = (N / 100) % 10;
        int m = N / 1000;
        if (N >= 1000 && N <= 2000) {
            return (Mil[m] + Centena[c] + Decena[d] + Unidad[u]);
        } else {
            return "";
        }
    }

    public static boolean impar(int numero) {
        if (numero % 2 != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Date sumarDiasCalendarios(Date fecha, int numeroDias) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));

        //Feriados 2014
        List<Date> listaFeriado = new ArrayList();
        listaFeriado.add(sdf.parse("01/11/2013"));
        listaFeriado.add(sdf.parse("08/12/2013"));
        listaFeriado.add(sdf.parse("25/12/2013"));
        listaFeriado.add(sdf.parse("01/01/2014"));
        listaFeriado.add(sdf.parse("17/04/2014"));
        listaFeriado.add(sdf.parse("18/04/2014"));
        listaFeriado.add(sdf.parse("20/04/2014"));
        listaFeriado.add(sdf.parse("01/05/2014"));
        listaFeriado.add(sdf.parse("29/06/2014"));
        listaFeriado.add(sdf.parse("28/07/2014"));
        listaFeriado.add(sdf.parse("29/07/2014"));
        listaFeriado.add(sdf.parse("30/08/2014"));
        listaFeriado.add(sdf.parse("08/10/2014"));
        listaFeriado.add(sdf.parse("01/11/2014"));
        listaFeriado.add(sdf.parse("08/12/2014"));
        listaFeriado.add(sdf.parse("25/12/2014"));
        listaFeriado.add(sdf.parse("01/01/2015"));

        cal.set(año1, mes1 - 1, dia1);
        cal.add(Calendar.DATE, numeroDias);

        for (Date f : listaFeriado) {
            if (sdf.parse(sdf.format(cal.getTime())).compareTo(f) == 0) {
                cal.add(Calendar.DATE, 1);
            }
        }

        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            cal.add(Calendar.DATE, 1);
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
            cal.add(Calendar.DATE, 2);
        }

        return cal.getTime();
    }

    public static Date sumarDiasFecha(Date fecha, int numeroDias) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));

        cal.set(año1, mes1 - 1, dia1);
        cal.add(Calendar.DATE, numeroDias);
        return cal.getTime();
    }

    public static Date sumarDiasFechaSinHora(Date fecha, int numeroDias) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));

        cal.set(año1, mes1 - 1, dia1, 0, 0, 0);
        cal.add(Calendar.DATE, numeroDias);
        return cal.getTime();
    }

    public static Date sumarMinutosFecha(Date fecha, int numeroMinutos) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));

        cal.set(año1, mes1 - 1, dia1);
        cal.add(Calendar.MINUTE, numeroMinutos);
        return cal.getTime();
    }

    public static Date sumarMinutosFechaHora(Date fecha, int numeroMinutos) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormtHora = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormtMin = new SimpleDateFormat("mm");
        SimpleDateFormat dateFormtSeg = new SimpleDateFormat("ss");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));
        Integer hora1 = Integer.parseInt(dateFormtHora.format(fecha));
        Integer minuto1 = Integer.parseInt(dateFormtMin.format(fecha));
        Integer segundo1 = Integer.parseInt(dateFormtSeg.format(fecha));

        cal.set(año1, mes1 - 1, dia1, hora1, minuto1,segundo1);
        cal.add(Calendar.MINUTE, numeroMinutos);
        return cal.getTime();
    }

    public static Date RestarMinutosFechaHora(Date fecha, int numeroMinutos) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormtHora = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormtMin = new SimpleDateFormat("mm");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));
        Integer hora1 = Integer.parseInt(dateFormtHora.format(fecha));
        Integer minuto1 = Integer.parseInt(dateFormtMin.format(fecha));

        int minutosNegativos = numeroMinutos * -1;
        cal.set(año1, mes1 - 1, dia1, hora1, minuto1);
        cal.add(Calendar.MINUTE, minutosNegativos);
        return cal.getTime();
    }

    public static Date sumarMesAnioFecha(Date fecha, char condicion, int cantidad) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatAño = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

        Integer año1 = Integer.parseInt(dateFormatAño.format(fecha));
        Integer mes1 = Integer.parseInt(dateFormatMes.format(fecha));
        Integer dia1 = Integer.parseInt(dateFormatDia.format(fecha));

        cal.set(año1, mes1 - 1, dia1);
        if (condicion == 'A') {
            cal.add(Calendar.YEAR, cantidad);
        }
        if (condicion == 'M') {
            cal.add(Calendar.MONTH, cantidad);
        }
        if (condicion == 'D') {
            cal.add(Calendar.DATE, cantidad);
        }

        return cal.getTime();
    }

    public static int diasEntreFechas(Date fechaIni, Date fechaFi) {
        GregorianCalendar date1 = new GregorianCalendar();
        date1.setTime(fechaIni); //dateIni es el objeto Date

        GregorianCalendar date2 = new GregorianCalendar();
        date2.setTime(fechaFi); //dateFin es el objeto Date
        int rango = 0;

        /* COMPROBAMOS SI ESTAMOS EN EL MISMO AÑO */
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
            rango = date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR);
        } else {
            /* SI ESTAMOS EN DISTINTO AÑO COMPROBAMOS QUE EL AÑO DEL DATEINI NO SEA BISIESTO
            * SI ES BISIESTO SON 366 DIAS EL AÑO
            * SINO SON 365
             */
            int diasAnyo = date1.isLeapYear(date1.get(Calendar.YEAR)) ? 366 : 365;

            /* CALCULAMOS EL RANGO DE AÑOS */
            int rangoAnyos = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);

            /* CALCULAMOS EL RANGO DE DIAS QUE HAY */
            rango = (rangoAnyos * diasAnyo) + (date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR));
        }
        return rango;
    }

    public static String diasEntreHoras_TraerHorasMinutos(Date fechaIni, Date fechaFi) {
        long tiempoInicial = fechaIni.getTime();
        long tiempoFinal = fechaFi.getTime();
        long resta = tiempoFinal - tiempoInicial;
        resta = resta / (1000 * 60);
        int num = Integer.parseInt("" + resta);
        int hor = 0;
        int min = 0;
        hor = num / 60;
        min = num % 60;
        return hor + "&&" + min;
    }

    public static String formatoMensajeHtml(String mensaje, String destinatario, Sucursal sucursal, boolean addSres) {
        String dest = " ";
        if (addSres) {
            dest = ": ";
            if (!destinatario.trim().equals("")) {
                dest = " Señores:<br/> <br/>";
            }
        }

        return "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"
                + "<html lang='en'>"
                + "<head>"
                + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "  <meta http-equiv='X-UA-Compatible' content='IE=edge'>"
                + "  <meta name='format-detection' content='telephone=no'>"
                + "  <title>Single Column</title>"
                + "  "
                + "  <style type='text/css'>"
                + "body {"
                + "  margin: 0;"
                + "  padding: 0;"
                + "  -ms-text-size-adjust: 100%;"
                + "  -webkit-text-size-adjust: 100%;"
                + "}"
                + ""
                + "table {"
                + "  border-spacing: 0;"
                + "}"
                + ""
                + "table td {"
                + "  border-collapse: collapse;"
                + "}"
                + ""
                + ".ExternalClass {"
                + "  width: 100%;"
                + "}"
                + ""
                + ".ExternalClass,"
                + ".ExternalClass p,"
                + ".ExternalClass span,"
                + ".ExternalClass font,"
                + ".ExternalClass td,"
                + ".ExternalClass div {"
                + "  line-height: 100%;"
                + "}"
                + ""
                + ".ReadMsgBody {"
                + "  width: 100%;"
                + "  background-color: #ebebeb;"
                + "}"
                + ""
                + "table {"
                + "  mso-table-lspace: 0pt;"
                + "  mso-table-rspace: 0pt;"
                + "}"
                + ""
                + "img {"
                + "  -ms-interpolation-mode: bicubic;"
                + "}"
                + ""
                + ".yshortcuts a {"
                + "  border-bottom: none !important;"
                + "}"
                + ""
                + "@media screen and (max-width: 599px) {"
                + "  .force-row,"
                + "  .container {"
                + "    width: 100% !important;"
                + "    max-width: 100% !important;"
                + "  }"
                + "}"
                + "@media screen and (max-width: 400px) {"
                + "  .container-padding {"
                + "    padding-left: 12px !important;"
                + "    padding-right: 12px !important;"
                + "  }"
                + "}"
                + ".ios-footer a {"
                + "  color: #aaaaaa !important;"
                + "  text-decoration: underline;"
                + "}"
                + "</style>"
                + "</head>"
                + ""
                + "<body style='font-family:Tahoma,sans-serif; margin:0; padding:0;' bgcolor='#F0F0F0' leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>"
                + "<table border='0' width='100%' height='100%' cellpadding='0' cellspacing='0' bgcolor='#F0F0F0'>"
                + "  <tr>"
                + "    <td align='center' valign='top' bgcolor='#F0F0F0' style='background-color: #F0F0F0;'>"
                + "      <br>"
                + "      <table border='0' width='600' cellpadding='0' cellspacing='0' class='container' style='width:600px;max-width:600px'>"
                + "        <tr>"
                + "          <td class='container-padding header' align='left' style='font-family:Helvetica, Arial, sans-serif;font-size:24px;font-weight:bold;padding-bottom:12px;color:#DF4726;padding-left:24px;padding-right:24px'>"
                + "            <img src='http://polybagsperu.com/sistema/imagenes/logo8.png' height='60' />"
                + "          </td>"
                + "        </tr>"
                + "        <tr>"
                + "          <td class='container-padding content' align='left' style='padding-left:24px;padding-right:24px;padding-top:12px;padding-bottom:12px;background-color:#ffffff'>"
                + "            <span style='font-family: Tahoma,sans-serif; font-size: 14px; font-style: italic; margin-top: 15px;'><br/>Estimado " + dest + " <b>" + destinatario + " </b></span> "
                + "            <br><br>"
                + "            <div class='body-text' style='font-family:Tahoma,sans-serif; font-size:14px;line-height:20px;text-align:left;color:#333333'>" + mensaje + "</div>"
                + "          </td>"
                + "        </tr>"
                + "        <tr>"
                + "          <td class='container-padding footer-text' align='left' style='font-family:Helvetica, Arial, sans-serif;font-size:12px;line-height:16px;color:#aaaaaa;padding-left:24px;padding-right:24px'>"
                + "            <b>POLYBAGS PERÚ SRL &copy; Derechos Reservados</b><br>"
                + "            Planta Lima: Av. Lurigancho 1274 - Z.I. Zárate - San Juan de Lurigancho<br>"
                + "            Planta Chiclayo: Mz.35A - LTS 1-2-3 Chosica del Norte -  La Victoria<br>"
                + "            <a href='ventas.lima@polybagsperu.com' style='color:#aaaaaa'>ventas.lima@polybagsperu.com</a> - (+51)(01)-7390602 <br>"
                + "            <a href='ventas@polybagsperu.com' style='color:#aaaaaa'>ventas@polybagsperu.com</a> - (+51)(074)-215815<br>"
                + "            <a href='http://www.polybagsperu.com' style='color:#aaaaaa'>www.polybagsperu.com</a><br>"
                + "            <br><br>"
                + "          </td>"
                + "        </tr>"
                + "        <tr>"
                + "            <td class='container-padding footer-text' align='center' style='font-family:Helvetica, Arial, sans-serif;font-size:9px;line-height:16px;color:#aaaaaa;padding-left:24px;padding-right:24px'>"
                + "                IMPORTANTE:<br>"
                + "                Este mensaje es enviado de acuerdo con la ley  28493 que regula el uso de correo electrónico comercial no deseado (SPAM) por lo que no puede ser considerado SPAM, Sólo ha sido remitido a los clientes inscritos en el Sistema de Servicio al Cliente de POLYBAGS y no a una base de datos anónima e indiscriminada.<br>"
                + "                Si no desea recibir este correo por favor contáctese con nosotros."
                + "            </td>"
                + "        </tr>"
                + "      </table>"
                + "    </td>"
                + "  </tr>"
                + "</table>"
                + "</body>"
                + "</html>";
    }

    public static String formatoMensajeHtmlMasivo(String mensaje, String destinatario) {
        String dest = ": ";
        if (!destinatario.trim().equals("")) {
            dest = "Señores: ";
        }

        return "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='es' lang='es'>"
                + "<head>"
                + "<meta name='tipo_contenido' http-equiv='Content-Type' content='text/html;'/> "
                + "</head>"
                + "<body>"
                + "<div style='width: 900px; border: 0px solid transparent'>"
                + "<div style='width: 900px; height: 55px; background: #transparent;'>"
                + "<div style='width: 900px; height: 65px; color: #000000; float: left; max-width: 900px;background: transparent; border-bottom: 1px solid #EAEAEA'>"
                + "<div style='width: 150px; height: 55px; color: #FFFFFF; float: right; font-size: 12px'>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div style='width: 900px; padding: 5px; font-family: Arial;' font-size: 10px;>"
                + "<span style='font-family: Arial; font-size: 14px; font-style: italic; margin-top: 15px;'><br/>Estimados " + dest + " <b>" + destinatario + " </b></span>"
                + "<br><span style='font-family: Arial; font-size: 12px; margin-top: 10px;'><br/>"
                + mensaje
                + "</span><br>"
                + "</div>"
                + "<div style='background: #FFFFFF; color: #333333; text-align: center;width: 900px; font-size: 12px; font-family: Arial; border-top: 1px solid #000000;  border-bottom: 1px solid #000000''>"
                + "<b>..:: KEPLER &copy; Derechos Reservados ::..</b><br>"
                + "</div>"
                + "<div style='background: #EAEAEA; color: #000000; text-align: center;width: 900px; height: 55px; font-size: 9px; font-family: Arial;'>"
                + "IMPORTANTE:<br>"
                + "Este mensaje es enviado de acuerdo con la ley  28493 que regula el uso de correo electrónico comercial no deseado (SPAM) por lo que no puede ser considerado SPAM, <br> Sólo ha sido remitido a los clientes inscritos en el Sistema de Servicio al Cliente y no a una base de datos anónima e indiscriminada.<br>"
                + "Si no desea recibir este correo por favor contáctese con nosotros. "
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    public static String calcularDuracion(Date fechaInicio, Date fechaFin) {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);

        long diff1 = fechaFin.getTime() - fechaInicio.getTime();
        Long dias = TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS);
        Long horas = 0L;
        Long minu = 0L;

        if (dias > 0) {
            c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH) - dias.intValue(),
                    c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));

            long diff2 = c2.getTime().getTime() - fechaInicio.getTime();
            horas = TimeUnit.HOURS.convert(diff2, TimeUnit.MILLISECONDS);

            if (horas > 0) {
                c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH),
                        c2.get(Calendar.HOUR_OF_DAY) - horas.intValue(), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
            }
            long diff3 = c2.getTime().getTime() - fechaInicio.getTime();
            minu = TimeUnit.MINUTES.convert(diff3, TimeUnit.MILLISECONDS);
        } else {
            long diff2 = c2.getTime().getTime() - fechaInicio.getTime();
            horas = TimeUnit.HOURS.convert(diff2, TimeUnit.MILLISECONDS);

            if (horas > 0) {
                c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH),
                        c2.get(Calendar.HOUR_OF_DAY) - horas.intValue(), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
            }

            long diff3 = c2.getTime().getTime() - fechaInicio.getTime();
            minu = TimeUnit.MINUTES.convert(diff3, TimeUnit.MILLISECONDS);
        }

        return new StringBuilder().append(dias.compareTo(0L) > 0 ? dias.intValue() + " dias " : "")
                .append(horas.compareTo(0L) > 0 ? horas.intValue() + " horas " : "")
                .append(minu.compareTo(0L) > 0 ? minu.intValue() + " minu " : "")
                .toString().trim();
    }

    public static BigDecimal micrasToMilesimasPulgada(BigDecimal espesorMicras) {
        return espesorMicras.divide(new BigDecimal(25.4), 2, RoundingMode.UP);
    }

    public static BigDecimal milesimasPulgadaToMicras(BigDecimal espesorMilesimasPulgada) {
        return espesorMilesimasPulgada.multiply(new BigDecimal(25.4)).setScale(2, RoundingMode.HALF_UP);
    }

//    public static void resetearFitrosTabla(String id) {
//        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
//        table.reset();
//    }

    public static long getDifferenceMinutosBetwenDates(Date dateInicio, Date dateFinal) {

        long diff1 = dateFinal.getTime() - dateInicio.getTime();
        //Long horas = TimeUnit.HOURS.convert(diff1, TimeUnit.MILLISECONDS);
        long minu = TimeUnit.MINUTES.convert(diff1, TimeUnit.MILLISECONDS);

        return minu;
    }

    public static int calcularDiasLaborables(int messC, int anioC) throws ParseException {

        SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
        String textMes = "";
        String textFechaI = "";
        String textFechaF = "";

        if (messC < 10) {
            textMes = "0" + messC;
        } else {
            textMes = "" + messC;
        }

        textFechaI = anioC + "-" + textMes + "-01";
        Date primerDia = sdfFecha.parse(textFechaI);

        textFechaF = anioC + "-" + textMes + "-" + Utils.obtenerUltimoDiaMes(anioC, messC);
        Date ultimoDia = sdfFecha.parse(textFechaF);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(primerDia);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(ultimoDia);

        List<Date> listaFechasNoLaborables = new ArrayList<>();

        return diasHabiles(c1, c2, listaFechasNoLaborables);
    }

    public static int calcularDiasLaborables(Date fechaInicio, Date fechaFin, List<Date> listaFechasNoLaborables) throws ParseException {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaInicio);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);

        return diasHabiles(c1, c2, listaFechasNoLaborables);
    }

    public static int calcularDiasDomingo(Date fechaInicio, Date fechaFin) throws ParseException {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaInicio);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);

        return diasDomingo(c1, c2);
    }

    public static int diasDomingo(Calendar fechaInicial, Calendar fechaFinal) {
        int diffDays = 0;
        //mientras la fecha inicial sea menor o igual que la fecha final se cuentan los dias
        while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {

            if (fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                //se aumentan los dias de diferencia entre min y max
                diffDays++;
            }

            //se suma 1 dia para hacer la validacion del siguiente dia.
            fechaInicial.add(Calendar.DATE, 1);
        }
        return diffDays;
    }

    public static int diasHabiles(Calendar fechaInicial, Calendar fechaFinal, List<Date> listaFechasNoLaborables) {
        int diffDays = 0;
        boolean diaHabil = false;
        //mientras la fecha inicial sea menor o igual que la fecha final se cuentan los dias
        while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {

            if (!listaFechasNoLaborables.isEmpty()) {
                for (Date date : listaFechasNoLaborables) {
                    Date fechaNoLaborablecalendar = fechaInicial.getTime();
                    //si el dia de la semana de la fecha minima es diferente de sabado o domingo
                    if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !fechaNoLaborablecalendar.equals(date)) {
                        //se aumentan los dias de diferencia entre min y max
                        diaHabil = true;
                    } else {
                        diaHabil = false;
                        break;
                    }
                }
            } else {
                if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    //se aumentan los dias de diferencia entre min y max
                    diffDays++;
                }
            }
            if (diaHabil == true) {
                diffDays++;
            }
            //se suma 1 dia para hacer la validacion del siguiente dia.
            fechaInicial.add(Calendar.DATE, 1);
        }
        return diffDays;
    }

    public static Integer semanaDelAnio(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c1.setMinimalDaysInFirstWeek(4);
        Integer semana = c1.get(Calendar.WEEK_OF_YEAR);
        return semana;
    }
}
