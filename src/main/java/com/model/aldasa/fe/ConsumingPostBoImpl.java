/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.aldasa.fe;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

/**
 *
 * @author JHON DIAZ
 */
@Service("consumingPostBo")
public class ConsumingPostBoImpl {

    /*
#########################################################
#### PASO 1: CONSEGUIR LA RUTA Y TOKEN ####
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
# - Regístrate gratis en www.nubefact.com/register
# - Ir la opción API (Integración).
# IMPORTANTE: Para que la opción API esté activada necesitas escribirnos a soporte@nubefact.com o llámanos al teléfono: 01 468 3535 (opción 2) o celular (WhatsApp) 955 598762.
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public JSONObject apiConsume(DocumentoVenta documentoVenta, List<DetalleDocumentoVenta> listDetalleDocumentoVenta) {
        JSONObject json_rspta = null;

        try {

            String RUTA = documentoVenta.getSucursal().getEmpresa().getRutaFe();
            String TOKEN = documentoVenta.getSucursal().getEmpresa().getTokenFe();

            Integer tipoComprobante = 1;
            if (documentoVenta.getTipoDocumento().getAbreviatura().equals("F")) {
                tipoComprobante = 1;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("B")) {
                tipoComprobante = 2;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("C")) {
                tipoComprobante = 3;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("D")) {
                tipoComprobante = 4;
            }

            Integer sunatTransaction = 1;
            /*
            1 = VENTA INTERNA
            2 = EXPORTACIÓN
            4 = VENTA INTERNA – ANTICIPOS
            29 = VENTAS NO DOMICILIADOS QUE NO CALIFICAN COMO   EXPORTACIÓN.
            30 = OPERACIÓN SUJETA A DETRACCIÓN.
            33 = DETRACCIÓN - SERVICIOS DE TRANSPORTE CARGA 
            34 = OPERACIÓN SUJETA A PERCEPCIÓN
            32 = DETRACCIÓN - SERVICIOS DE TRANSPORTE DE PASAJEROS.
            31 = DETRACCIÓN - RECURSOS HIDROBIOLÓGICOS
             */

            String tipoDocumentoCliente = "6";
            if (documentoVenta.getRuc().length() == 11) {
                tipoDocumentoCliente = "6";
            } else if (documentoVenta.getRuc().length() == 8) {
                tipoDocumentoCliente = "1";
            } else {
                tipoDocumentoCliente = "-";
            }

            /*
            6 = RUC - REGISTRO ÚNICO DE CONTRIBUYENTE
            1 = DNI - DOC. NACIONAL DE IDENTIDAD
            - = VARIOS - VENTAS MENORES A S/.700.00 Y OTROS
            4 = CARNET DE EXTRANJERÍA
            7 = PASAPORTE
            A = CÉDULA DIPLOMÁTICA DE IDENTIDAD
            0 = NO DOMICILIADO, SIN RUC (EXPORTACIÓN)
             */
            String moneda = "1";
            if (documentoVenta.getTipoMoneda().equals("S")) {
                moneda = "1";
            } else {
                moneda = "2";
            }

            HttpClient cliente = new DefaultHttpClient();
            HttpPost post = new HttpPost(RUTA);
            post.addHeader("Authorization", "Token token=" + TOKEN); // Cabecera del token
            post.addHeader("Content-Type", "application/json"); // Cabecera del Content-Type

            JSONObject objetoCabecera = new JSONObject(); // Instancear el  segundario
            objetoCabecera.put("operacion", "generar_comprobante");
            objetoCabecera.put("tipo_de_comprobante", tipoComprobante);
            objetoCabecera.put("serie", documentoVenta.getSerie());
            objetoCabecera.put("numero", Integer.parseInt(documentoVenta.getNumero()));
            objetoCabecera.put("sunat_transaction", sunatTransaction);
            objetoCabecera.put("cliente_tipo_de_documento", tipoDocumentoCliente);
            objetoCabecera.put("cliente_numero_de_documento", documentoVenta.getRuc());
            objetoCabecera.put("cliente_denominacion", documentoVenta.getRazonSocial());
            objetoCabecera.put("cliente_direccion", documentoVenta.getDireccion());
            objetoCabecera.put("cliente_email", documentoVenta.getCliente().getEmail1Fe());
            objetoCabecera.put("cliente_email_1", documentoVenta.getCliente().getEmail2Fe());
            objetoCabecera.put("cliente_email_2", documentoVenta.getCliente().getEmail3Fe());
            objetoCabecera.put("fecha_de_emision", sdf.format(documentoVenta.getFechaEmision()));
            objetoCabecera.put("fecha_de_vencimiento", sdf.format(documentoVenta.getFechaVencimiento()));
            objetoCabecera.put("moneda", moneda);
            objetoCabecera.put("tipo_de_cambio", documentoVenta.getTasaCambio());
            objetoCabecera.put("porcentaje_de_igv", "18.00");
            objetoCabecera.put("descuento_global", "");
            objetoCabecera.put("total_descuento", "");
            objetoCabecera.put("total_anticipo", "");
            objetoCabecera.put("total_gravada", documentoVenta.getOpGravada());
            objetoCabecera.put("total_inafecta", documentoVenta.getOpInafecta());
            objetoCabecera.put("total_exonerada", "");
            objetoCabecera.put("total_igv", documentoVenta.getIgv());
            objetoCabecera.put("total_gratuita", "");
            objetoCabecera.put("total_otros_cargos", "");
            objetoCabecera.put("total", documentoVenta.getTotal());
            objetoCabecera.put("percepcion_tipo", "");
            objetoCabecera.put("percepcion_base_imponible", "");
            objetoCabecera.put("total_percepcion", "");
            objetoCabecera.put("total_incluido_percepcion", "");
            objetoCabecera.put("detraccion", "false");
            objetoCabecera.put("observaciones", documentoVenta.getObservacion());

            if (documentoVenta.getTipoDocumento().getAbreviatura().equals("C")) {

                if (documentoVenta.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("F")) {
                    objetoCabecera.put("documento_que_se_modifica_tipo", 1);
                } else {
                    objetoCabecera.put("documento_que_se_modifica_tipo", 2);
                }

                objetoCabecera.put("documento_que_se_modifica_serie", documentoVenta.getDocumentoVentaRef().getSerie());
                objetoCabecera.put("documento_que_se_modifica_numero", Integer.parseInt(documentoVenta.getDocumentoVentaRef().getNumero()));
                objetoCabecera.put("tipo_de_nota_de_credito", Integer.parseInt(documentoVenta.getMotivoNota().getCodigo()));
                objetoCabecera.put("tipo_de_nota_de_debito", "");
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("D")) {
                if (documentoVenta.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("F")) {
                    objetoCabecera.put("documento_que_se_modifica_tipo", 1);
                } else {
                    objetoCabecera.put("documento_que_se_modifica_tipo", 2);
                }
                objetoCabecera.put("documento_que_se_modifica_serie", documentoVenta.getDocumentoVentaRef().getSerie());
                objetoCabecera.put("documento_que_se_modifica_numero", Integer.parseInt(documentoVenta.getDocumentoVentaRef().getNumero()));
                objetoCabecera.put("tipo_de_nota_de_credito", "");
                objetoCabecera.put("tipo_de_nota_de_debito", Integer.parseInt(documentoVenta.getMotivoNota().getCodigo()));
            } else {
                objetoCabecera.put("documento_que_se_modifica_tipo", "");
                objetoCabecera.put("documento_que_se_modifica_serie", "");
                objetoCabecera.put("documento_que_se_modifica_numero", "");
                objetoCabecera.put("tipo_de_nota_de_credito", "");
                objetoCabecera.put("tipo_de_nota_de_debito", "");
            }

            objetoCabecera.put("enviar_automaticamente_a_la_sunat", "true");
            objetoCabecera.put("enviar_automaticamente_al_cliente", "true");
            objetoCabecera.put("codigo_unico", "");
            objetoCabecera.put("condiciones_de_pago", "");
            objetoCabecera.put("medio_de_pago", "");
            objetoCabecera.put("placa_vehiculo", "");
            objetoCabecera.put("orden_compra_servicio", "");
            objetoCabecera.put("tabla_personalizada_codigo", "");
            objetoCabecera.put("formato_de_pdf", "");

            JSONArray lista = new JSONArray();

            for (DetalleDocumentoVenta ddv : listDetalleDocumentoVenta) {
                JSONObject detalle_linea_1 = new JSONObject();               
                 if (documentoVenta.getIgv().compareTo(BigDecimal.ZERO)==1) {
                	BigDecimal subtotalItem = ddv.getImporteVentaSinIgv();
                    BigDecimal igvItem = ddv.getTotal().subtract(subtotalItem);
                    BigDecimal totalItem = subtotalItem.add(igvItem);

                    BigDecimal valorUnitario = ddv.getValorUnitario();
                    BigDecimal precioUnitario = ddv.getPrecioUnitario();

                    detalle_linea_1.put("unidad_de_medida", "NIU");
                    detalle_linea_1.put("codigo", ddv.getProducto().getId());
                    detalle_linea_1.put("descripcion", ddv.getDescripcion());
                    detalle_linea_1.put("cantidad", ddv.getCantidad().intValue());
                    detalle_linea_1.put("valor_unitario", valorUnitario);
                    detalle_linea_1.put("precio_unitario", precioUnitario);
                    detalle_linea_1.put("descuento", "");
                    detalle_linea_1.put("subtotal", subtotalItem);
                    detalle_linea_1.put("tipo_de_igv", "1");
                    detalle_linea_1.put("igv", igvItem);
                    detalle_linea_1.put("total", totalItem);
                    detalle_linea_1.put("anticipo_regularizacion", "false");
                    detalle_linea_1.put("anticipo_serie", "");
                    detalle_linea_1.put("anticipo_documento_numero", "");
                    lista.add(detalle_linea_1);
                }else {
                	BigDecimal subtotalItem = ddv.getTotal();
                    BigDecimal igvItem = BigDecimal.ZERO;
                    BigDecimal totalItem = ddv.getTotal();

                    BigDecimal valorUnitario = ddv.getTotal();
                    BigDecimal precioUnitario = ddv.getTotal();

                    detalle_linea_1.put("unidad_de_medida", "NIU"); //ZZ servicio  NIU producto
                    detalle_linea_1.put("codigo", ddv.getProducto().getId());
                    detalle_linea_1.put("descripcion", ddv.getDescripcion());
                    detalle_linea_1.put("cantidad", ddv.getCantidad());
                    detalle_linea_1.put("valor_unitario", valorUnitario);
                    detalle_linea_1.put("precio_unitario", precioUnitario);
                    detalle_linea_1.put("descuento", "");
                    detalle_linea_1.put("subtotal", subtotalItem);
                    detalle_linea_1.put("tipo_de_igv", "9"); //inafecto
                    detalle_linea_1.put("igv", igvItem);
                    detalle_linea_1.put("total", totalItem);
                    detalle_linea_1.put("anticipo_regularizacion", "false");
                    detalle_linea_1.put("anticipo_serie", "");
                    detalle_linea_1.put("anticipo_documento_numero", "");
                    lista.add(detalle_linea_1);
                    
                }
                
                
                
            }

            objetoCabecera.put("items", lista);
            /*
                #########################################################
                #### PASO 3: ENVIAR EL ARCHIVO A NUBEFACT ####
                +++++++++++++++++++++++++++++++++++++++++++++++++++++++
                # SI ESTÁS TRABAJANDO CON ARCHIVO JSON
                # - Debes enviar en el HEADER de tu solicitud la siguiente lo siguiente:
                # Authorization = Token token="8d19d8c7c1f6402687720eab85cd57a54f5a7a3fa163476bbcf381ee2b5e0c69"
                # Content-Type = application/json
                # - Adjuntar en el CUERPO o BODY el archivo JSON o TXT
                # SI ESTÁS TRABAJANDO CON ARCHIVO TXT
                # - Debes enviar en el HEADER de tu solicitud la siguiente lo siguiente:
                # Authorization = Token token="8d19d8c7c1f6402687720eab85cd57a54f5a7a3fa163476bbcf381ee2b5e0c69"
                # Content-Type = text/plain
                # - Adjuntar en el CUERPO o BODY el archivo JSON o TXT
                +++++++++++++++++++++++++++++++++++++++++++++++++++++++
             */

            System.out.println("-- Trama:" + objetoCabecera.toString());

            StringEntity parametros = new StringEntity(objetoCabecera.toString(), "UTF-8");
            post.setEntity(parametros);
            HttpResponse response = cliente.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String linea = "";
            if ((linea = rd.readLine()) != null) {

                JSONParser parsearRsptaJson = new JSONParser();
                json_rspta = (JSONObject) parsearRsptaJson.parse(linea);

                /*
                    #########################################################
                    #### PASO 4: LEER RESPUESTA DE NUBEFACT ####
                    +++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    # Recibirás una respuesta de NUBEFACT inmediatamente lo cual se debe leer, verificando que no haya errores.
                    # Debes guardar en la base de datos la respuesta que te devolveremos.
                    # Escríbenos a soporte@nubefact.com o llámanos al teléfono: 01 468 3535 (opción 2) o celular (WhatsApp) 955 598762
                    # Puedes imprimir el PDF que nosotros generamos como también generar tu propia representación impresa previa coordinación con nosotros.
                    # La impresión del documento seguirá haciéndose desde tu sistema. Enviaremos el documento por email a tu cliente si así lo indicas en el archivo JSON o TXT.
                    +++++++++++++++++++++++++++++++++++++++++++++++++++++++               
                 */
                /*if (json_rspta.get("errors") != null) {
                        System.out.println("Error => " + json_rspta.get("errors"));
                        return json_rspta;
                    } else {

                    JSONParser parsearRsptaDetalleOK = new JSONParser();
                    JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());

                    System.out.println(json_rspta_ok.get("tipo_de_comprobante"));
                    System.out.println(json_rspta_ok.get("serie"));
                    System.out.println(json_rspta_ok.get("numero"));
                    System.out.println(json_rspta_ok.get("enlace"));
                    System.out.println(json_rspta_ok.get("aceptada_por_sunat"));
                    System.out.println(json_rspta_ok.get("sunat_description"));
                    System.out.println(json_rspta_ok.get("sunat_note"));
                    System.out.println(json_rspta_ok.get("sunat_responsecode"));
                    System.out.println(json_rspta_ok.get("sunat_soap_error"));
                    System.out.println(json_rspta_ok.get("pdf_zip_base64"));
                    System.out.println(json_rspta_ok.get("xml_zip_base64"));
                    System.out.println(json_rspta_ok.get("cdr_zip_base64"));
                    System.out.println(json_rspta_ok.get("cadena_para_codigo_qr"));
                    System.out.println(json_rspta_ok.get("codigo_hash"));
                    
                    return json_rspta_ok;
                }*/
            }
        } catch (UnsupportedEncodingException ex1) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.getMessage());
        } catch (IOException ex2) {
            System.err.println("Error IOException: " + ex2.getMessage());
        } catch (Exception ex3) {
            System.err.println("Exepction: " + ex3.getMessage());
        }

        return json_rspta;
    }
    
    public JSONObject apiConsumeConsulta(DocumentoVenta documentoVenta) {
        JSONObject json_rspta = null;
        try {

            String RUTA = documentoVenta.getSucursal().getEmpresa().getRutaFe();
            String TOKEN = documentoVenta.getSucursal().getEmpresa().getTokenFe();

            Integer tipoComprobante = 1;
            if (documentoVenta.getTipoDocumento().getAbreviatura().equals("F")) {
                tipoComprobante = 1;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("B")) {
                tipoComprobante = 2;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("C")) {
                tipoComprobante = 3;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("D")) {
                tipoComprobante = 4;
            }

            HttpClient cliente = new DefaultHttpClient();
            HttpPost post = new HttpPost(RUTA);
            JSONObject objetoCabecera = new JSONObject(); // Instancear el  segundario
            objetoCabecera.put("operacion", "consultar_comprobante");
            objetoCabecera.put("tipo_de_comprobante", tipoComprobante);
            objetoCabecera.put("serie", documentoVenta.getSerie());
            objetoCabecera.put("numero", Integer.parseInt(documentoVenta.getNumero()));

            System.out.println(objetoCabecera.toString());

            post.addHeader("Authorization", "Token token=" + TOKEN); // Cabecera del token
            post.addHeader("Content-Type", "application/json"); // Cabecera del Content-Type
            StringEntity parametros = new StringEntity(objetoCabecera.toString(), "UTF-8");
            post.setEntity(parametros);
            HttpResponse response = cliente.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String linea = "";
            if ((linea = rd.readLine()) != null) {
                JSONParser parsearRsptaJson = new JSONParser();
                json_rspta = (JSONObject) parsearRsptaJson.parse(linea);
            }

        } catch (UnsupportedEncodingException ex1) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.getMessage());
        } catch (IOException ex2) {
            System.err.println("Error IOException: " + ex2.getMessage());
        } catch (Exception ex3) {
            System.err.println("Exepction: " + ex3.getMessage());
        }
        return json_rspta;
    }

    public JSONObject apiConsumeDelete(DocumentoVenta documentoVenta) {
        JSONObject json_rspta = null;
        try {

            String RUTA = documentoVenta.getSucursal().getEmpresa().getRutaFe();
            String TOKEN = documentoVenta.getSucursal().getEmpresa().getTokenFe();

            Integer tipoComprobante = 1;
            if (documentoVenta.getTipoDocumento().getAbreviatura().equals("F")) {
                tipoComprobante = 1;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("B")) {
                tipoComprobante = 2;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("C")) {
                tipoComprobante = 3;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("D")) {
                tipoComprobante = 4;
            }

            HttpClient cliente = new DefaultHttpClient();
            HttpPost post = new HttpPost(RUTA);
            JSONObject objetoCabecera = new JSONObject(); // Instancear el  segundario
            objetoCabecera.put("operacion", "generar_anulacion");
            objetoCabecera.put("tipo_de_comprobante", tipoComprobante);
            objetoCabecera.put("serie", documentoVenta.getSerie());
            objetoCabecera.put("numero", Integer.parseInt(documentoVenta.getNumero()));
            objetoCabecera.put("motivo", "ERROR");
            objetoCabecera.put("codigo_unico", "");

            System.out.println(objetoCabecera.toString());

            post.addHeader("Authorization", "Token token=" + TOKEN); // Cabecera del token
            post.addHeader("Content-Type", "application/json"); // Cabecera del Content-Type
            StringEntity parametros = new StringEntity(objetoCabecera.toString(), "UTF-8");
            post.setEntity(parametros);
            HttpResponse response = cliente.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String linea = "";
            if ((linea = rd.readLine()) != null) {
                JSONParser parsearRsptaJson = new JSONParser();
                json_rspta = (JSONObject) parsearRsptaJson.parse(linea);
            }

        } catch (UnsupportedEncodingException ex1) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.getMessage());
        } catch (IOException ex2) {
            System.err.println("Error IOException: " + ex2.getMessage());
        } catch (Exception ex3) {
            System.err.println("Exepction: " + ex3.getMessage());
        }
        return json_rspta;
    }

    public JSONObject apiConsumeConsultaDelete(DocumentoVenta documentoVenta) {
        JSONObject json_rspta = null;
        try {

            String RUTA = documentoVenta.getSucursal().getEmpresa().getRutaFe();
            String TOKEN = documentoVenta.getSucursal().getEmpresa().getTokenFe();

            Integer tipoComprobante = 1;
            if (documentoVenta.getTipoDocumento().getAbreviatura().equals("F")) {
                tipoComprobante = 1;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("B")) {
                tipoComprobante = 2;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("C")) {
                tipoComprobante = 3;
            } else if (documentoVenta.getTipoDocumento().getAbreviatura().equals("D")) {
                tipoComprobante = 4;
            }

            HttpClient cliente = new DefaultHttpClient();
            HttpPost post = new HttpPost(RUTA);
            JSONObject objetoCabecera = new JSONObject(); // Instancear el  segundario
            objetoCabecera.put("operacion", "consultar_anulacion");
            objetoCabecera.put("tipo_de_comprobante", tipoComprobante);
            objetoCabecera.put("serie", documentoVenta.getSerie());
            objetoCabecera.put("numero", Integer.parseInt(documentoVenta.getNumero()));

            System.out.println(objetoCabecera.toString());

            post.addHeader("Authorization", "Token token=" + TOKEN); // Cabecera del token
            post.addHeader("Content-Type", "application/json"); // Cabecera del Content-Type
            StringEntity parametros = new StringEntity(objetoCabecera.toString(), "UTF-8");
            post.setEntity(parametros);
            HttpResponse response = cliente.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String linea = "";
            if ((linea = rd.readLine()) != null) {
                JSONParser parsearRsptaJson = new JSONParser();
                json_rspta = (JSONObject) parsearRsptaJson.parse(linea);
            }

        } catch (UnsupportedEncodingException ex1) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.getMessage());
        } catch (IOException ex2) {
            System.err.println("Error IOException: " + ex2.getMessage());
        } catch (Exception ex3) {
            System.err.println("Exepction: " + ex3.getMessage());
        }
        return json_rspta;
    }

}
