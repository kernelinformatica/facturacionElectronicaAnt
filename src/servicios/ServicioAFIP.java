package servicios;

import wsAFIP.*;
import controladores.*;
import entidades.*;
import java.net.URL;
import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import main.FacElectronicaException;
import main.Main;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.WsHandler;

/**
 * Esta clase se creo para consumir todas las funciones del WS de la AFIP
 *
 * @author Administrador
 */
public class ServicioAFIP {

    //--------------------------------------------------------//
    // ACA DECLARO LAS PROPIEDADES QUE NECESITO PARA LA CLASE //
    //--------------------------------------------------------//    
    private static final Logger log = Logger.getLogger(ServicioAFIP.class);
    private static ControladorTickets catalogoTickets;
    private final FEAuthRequest ticketActual;
    private static URL url = null;
    private static String mensaje="";
    //--------------------------------------------------------//    

    /**
     * Metodo consutructor de la clase
     *
     * @throws java.lang.Exception
     */
    public ServicioAFIP() throws Exception {

        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");

        // Inicio el catalogo de tickets
        catalogoTickets = new ControladorTickets();

        // Obtengo un ticket de acceso al WS
        ticketActual = catalogoTickets.getTicketActivo();

        // Genero la URL del servicio
        url = new URL(catalogoTickets.getEndPointWS());        
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de puntos de venta y
     * actualiza la BD
     */
    public void consultarPuntosDeVenta() {

        // Capturo posibles errores
        try {

            // Obtengo los puntos de venta habilitados
            FEPtoVentaResponse respuesta = ServicioAFIP.feParamGetPtosVenta(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {
                
                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo puntos de venta");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else { 

                    // Instancio el catalogo de puntos de venta
                    ControladorPtoVenta catalogo = new ControladorPtoVenta();

                    // Vacio la tabla
                    catalogo.vaciarTabla();
                
                    // Genero salida en el log            
                    log.info("Puntos de venta obtenidos con exito!");                
                    
                    // Recorro la lista de tipos de puntos de venta
                    for (PtoVenta item : respuesta.getResultGet().getPtoVenta()) {

                        // Instancio un objeto entidad
                        EntidadPtoVenta e = new EntidadPtoVenta(item.getNro(), item.getEmisionTipo(), item.getBloqueado(), item.getFchBaja());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Nro: " + item.getNro() + " - Bloqueado: " + item.getBloqueado() + " - Fecha Baja:" + item.getFchBaja());

                    } // FIN FOR RECORRIDO LISTA                    
                }


            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo puntos de venta: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos de
     * comprobante y actualiza la bd
     */
    public void consultarTiposDeComprobante() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos habilitados 
            CbteTipoResponse respuesta = ServicioAFIP.feParamGetTiposCbte(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {
                
                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de comprobante");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {

                    // Instancio el catalogo de tipos de comprobantes  feParamGetTiposCbte
                    ControladorTiposComprobantes catalogo = new ControladorTiposComprobantes();

                    // Vacio la tabla
                    catalogo.vaciarTabla();
                
                    // Genero salida en el log            
                    log.info("Tipos de Comprobante obtenidos con exito!");

                    // Recorro la lista de tipos de comprobantes
                    for (CbteTipo item : respuesta.getResultGet().getCbteTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoComprobante e = new EntidadTipoComprobante(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA                
                }


            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de comprobante: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos de conceptos
     * y actualiza la bd
     */
    public void consultarTiposDeConceptos() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos habilitados 
            ConceptoTipoResponse respuesta = ServicioAFIP.feParamGetTiposConcepto(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {
                
                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo los tipos de concepto");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else { 
                    
                    // Instancio el catalogo de tipos de comprobantes  feParamGetTiposCbte
                    ControladorTiposConceptos catalogo = new ControladorTiposConceptos();

                    // Vacio la tabla
                    catalogo.vaciarTabla();
                    
                    // Genero salida en el log            
                    log.info("Tipos de conceptos obtenidos con exito!");

                    // Recorro la lista de tipos de comprobantes
                    for (ConceptoTipo item : respuesta.getResultGet().getConceptoTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoConcepto e = new EntidadTipoConcepto(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                    
                }                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de conceptos: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos de
     * documentos y actualiza la BD
     */
    public void consultarTiposDeDocumento() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos de documentos habilitados  
            DocTipoResponse respuesta = ServicioAFIP.feParamGetTiposDoc(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de documentos");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {

                    // Genero salida en el log            
                    log.info("Tipos de documentos obtenidos con exito!");

                    // Instancio el catalogo de tipos de documentos
                    ControladorTiposDocumentos catalogo = new ControladorTiposDocumentos();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de documentos
                    for (DocTipo item : respuesta.getResultGet().getDocTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoDocs e = new EntidadTipoDocs(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                }                
            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de documentos: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de alicuotas de IVA y
     * actualiza la BD
     */
    public void consultarTiposDeIVA() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos de IVA  
            IvaTipoResponse respuesta = ServicioAFIP.feParamGetTiposIva(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de iva");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {

                    // Genero salida en el log            
                    log.info("Tipos de IVA obtenidos con exito!");

                    // Instancio el catalogo de tipos de IVA
                    ControladorTiposIVA catalogo = new ControladorTiposIVA();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de IVA
                    for (IvaTipo item : respuesta.getResultGet().getIvaTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoIVA e = new EntidadTipoIVA(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                    
                }                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de IVA: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de monedas y
     * actualiza la BD
     */
    public void consultarTiposDeMonedas() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos de moneda habilitados   
            MonedaResponse respuesta = ServicioAFIP.feParamGetTiposMonedas(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de monedas");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {
                    
                    // Genero salida en el log            
                    log.info("Tipos de monedas obtenidos con exito!");

                    // Instancio el catalogo de tipos de monedas
                    ControladorTiposMonedas catalogo = new ControladorTiposMonedas();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de monedas
                    for (Moneda item : respuesta.getResultGet().getMoneda()) {

                        // Instancio un objeto entidad
                        EntidadTipoMoneda e = new EntidadTipoMoneda(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                
                }                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de monedas: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos opciones y
     * actualiza la BD
     */
    public void consultarTiposOpcionales() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos opcionales habilitados    
            OpcionalTipoResponse respuesta = ServicioAFIP.feParamGetTiposOpcional(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de opciones");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {
                    
                    // Genero salida en el log            
                    log.info("Tipos opcionales obtenidos con exito!");

                    // Instancio el catalogo de tipos opcionales
                    ControladorTiposOpciones catalogo = new ControladorTiposOpciones();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de monedas
                    for (OpcionalTipo item : respuesta.getResultGet().getOpcionalTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoOpcional e = new EntidadTipoOpcional(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                    
                }                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos opcionales: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos de paises y
     * actualiza la BD
     */
    public void consultarTiposDePaises() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos opcionales habilitados  
            FEPaisResponse respuesta = ServicioAFIP.feParamGetTiposPaises(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {
                
                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de paises");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {
                    
                    // Genero salida en el log            
                    log.info("Tipos de paises obtenidos con exito!");

                    // Instancio el catalogo de tipos opcionales
                    ControladorTiposPaises catalogo = new ControladorTiposPaises();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de monedas
                    for (PaisTipo item : respuesta.getResultGet().getPaisTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoPais e = new EntidadTipoPais(item.getId(), item.getDesc());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                    
                }

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de paises: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene la lista de tipos de tributos
     * y actualiza la BD
     */
    public void consultarTiposDeTributos() {

        // Capturo posibles errores
        try {

            // Obtengo los tipos de tributo habilitados  
            FETributoResponse respuesta = ServicioAFIP.feParamGetTiposTributos(ticketActual);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Valido que la respuesta este OK
                if(respuesta.getResultGet()== null){

                    // Genero salida en el log            
                    log.error("Error obteniendo tipos de tributos");
                    
                    // Imprimo los errores
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
                } else {
                    
                    // Genero salida en el log            
                    log.info("Tipos de tributos obtenidos con exito!");

                    // Instancio el catalogo de tipos de tributos
                    ControladorTiposTributos catalogo = new ControladorTiposTributos();

                    // Vacio la tabla
                    catalogo.vaciarTabla();

                    // Recorro la lista de tipos de tributos
                    for (TributoTipo item : respuesta.getResultGet().getTributoTipo()) {

                        // Instancio un objeto entidad
                        EntidadTipoTributo e = new EntidadTipoTributo(item.getId(), item.getDesc(), item.getFchDesde(), item.getFchHasta());

                        // Guardo el item en la BD
                        catalogo.insertar(e);

                        // Genero una salida en el log
                        log.info("Codigo: " + item.getId() + " - Descripcion: " + item.getDesc());

                    } // FIN FOR RECORRIDO LISTA
                    
                }                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo tipos de tributos: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene el ultimo comprobante
     * autorizado para un tipo de comprobante y un punto de venta
     *
     * @param tipoComprobante
     * @param puntoDeVenta
     */
    public void consultarUltimoCbte(int tipoComprobante, int puntoDeVenta) {

        // Capturo posibles errores
        try {

            // Obtengo los tipos de tributo habilitados  
            FERecuperaLastCbteResponse respuesta = ServicioAFIP.feCompUltimoAutorizado(ticketActual, puntoDeVenta, tipoComprobante);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Genero salida en el log            
                log.info("UltimoComprobante obtenido con exito!");

                // Obtengo el comprobante
                log.info("Nro Cbte: " + respuesta.getCbteNro());

                // Valido que no este en null el listado de errores
                if (respuesta.getErrors() != null) {

                    // Errores
                    log.info("Errores: ");

                    // Recorro la lista de errores
                    for (Err e : respuesta.getErrors().getErr()) {

                        // Genero una salida en el log
                        log.info("Codigo: " + e.getCode() + " - Descripcion: " + e.getMsg());

                    } // FIN FOR RECORRIDO ITEMS

                }

                // Valido que no sea null el evento
                if (respuesta.getEvents() != null) {
                    log.info("Evento: ");

                    for (Evt e : respuesta.getEvents().getEvt()) {

                        log.info("Codigo: " + e.getCode() + " Desc: " + e.getMsg());
                    }

                }

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo el ultimo comprobante: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene un comprobante y su estado
     *
     * @param tipoComprobante
     * @param puntoDeVenta
     * @param nroCbteDesde
     */
    public void consultarEstadoComprobante(int tipoComprobante, int puntoDeVenta, int nroCbteDesde) {

        // Capturo posibles errores
        try {

            // Instancio la cabecera del request
            FECompConsultaReq comprobante = new FECompConsultaReq();
            comprobante.setCbteTipo((short) tipoComprobante);
            comprobante.setPtoVta((short) puntoDeVenta);
            comprobante.setCbteNro(nroCbteDesde);

            // Obtengo los tipos de tributo habilitados
            FECompConsultaResponse respuesta = ServicioAFIP.feCompConsultar(ticketActual, comprobante);

            // Valido que haya una respuesta
            if (respuesta != null) {

                // Genero salida en el log            
                log.info("Estado de Comprobante obtenido con exito!");

                // Valido que haya datos del comprobante
                if (respuesta.getResultGet() != null) {

                    // Genero una salida en el log
                    log.info("\t Tipo Cbte: " + respuesta.getResultGet().getCbteTipo());
                    log.info("\t Pto Venta: " + respuesta.getResultGet().getPtoVta());
                    log.info("\t Nro Cbte: " + respuesta.getResultGet().getCbteDesde());
                    log.info("\t Tipo Doc: " + respuesta.getResultGet().getDocTipo());
                    log.info("\t Nro Doc: " + respuesta.getResultGet().getDocNro());
                    log.info("\t Concepto: " + respuesta.getResultGet().getConcepto());
                    log.info("\t Fecha Emision: " + respuesta.getResultGet().getCbteFch());
                    log.info("\t Fecha Serv. Desde: " + respuesta.getResultGet().getFchServDesde());
                    log.info("\t Fecha Serv. Hasta: " + respuesta.getResultGet().getFchServHasta());
                    log.info("\t Fecha Vencimiento: " + respuesta.getResultGet().getFchVto());
                    log.info("\t Fecha Venc. Pago: " + respuesta.getResultGet().getFchVtoPago());
                    log.info("\t Moneda: " + respuesta.getResultGet().getMonId());
                    log.info("\t Codigo Autorizacion: " + respuesta.getResultGet().getCodAutorizacion());
                    log.info("\t Tipo Aurtizacion: " + respuesta.getResultGet().getEmisionTipo());
                    log.info("\t Importe Gravado: " + respuesta.getResultGet().getImpNeto());
                    log.info("\t Imoprte No Gravado: " + respuesta.getResultGet().getImpOpEx());
                    log.info("\t Importe Tributos: " + respuesta.getResultGet().getImpTrib());
                    log.info("\t Subtotal: " + respuesta.getResultGet().getImpNeto());
                    log.info("\t Importe Total: " + respuesta.getResultGet().getImpTotal());

                    // Valido que haya IVAs
                    if (respuesta.getResultGet().getIva() != null) {

                        // Agrego salida en el LOG
                        log.info("\t Subtotales IVA: ");

                        // Recorro la lista de ivas
                        for (AlicIva i : respuesta.getResultGet().getIva().getAlicIva()) {
                            log.info("\t Codigo: " + i.getId() + " Importe: " + i.getImporte());
                        }
                    }

                    // Valido que tenga tributos
                    if (respuesta.getResultGet().getTributos() != null) {

                        // Agrego salida en el LOG
                        log.info("\t Tributos: ");

                        // Recorro la lista de tributos
                        for (Tributo t : respuesta.getResultGet().getTributos().getTributo()) {
                            log.info("\t Codigo: " + t.getId() + " Descripcion: " + t.getDesc() + " Importe: " + t.getImporte());
                        }
                    }

                    // Valido que tenga comprobantes asociados
                    if (respuesta.getResultGet().getCbtesAsoc() != null) {

                        // Agrega salida en el log
                        log.info("\t Comprobantes Asociados: ");

                        // Recorro la lista de comprobantes Asociados
                        for (CbteAsoc cbteAsoc : respuesta.getResultGet().getCbtesAsoc().getCbteAsoc()) {
                            log.info("\t Tipo: " + cbteAsoc.getTipo() + " Pto Vta: " + cbteAsoc.getPtoVta() + " Nro: " + cbteAsoc.getNro());
                        }
                    }

                    // Imprimo las observaciones
                    ServicioAFIP.imprimirObservacionesCbte(respuesta.getResultGet().getObservaciones());
                }

                // Imprimo los errores
                ServicioAFIP.imprimirErrores(respuesta.getErrors());

                // Imprimo los eventos
                ServicioAFIP.imprimirEventos(respuesta.getEvents());

                // Pregunto si tiene la opcion de actualizacion de comprobantes habilitada
                if(Main.config.getActualizaComprobantes()){

                    // Instancio el catalogo de comprobantess
                    ControladorComprobantes catalogoComprobantes = new ControladorComprobantes();                    

                    // llamo al metodo que actualiza los datos del comprobante en las tablas de la AFIP
                    catalogoComprobantes.actualizarDatosDeComprobanteEnTablasAFIP(respuesta.getResultGet(),
                                                                                  tipoComprobante,  
                                                                                  puntoDeVenta, 
                                                                                  nroCbteDesde);
                            
                }
                

            } // FIN IF VALIDACION NULL

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error obteniendo el estado de un comprobante: " + e.getMessage());
        }
    }

    /**
     * Este metodo se conecta a la AFIP y obtiene autorizacion para comprobante
     *
     * @param tipoComprobante
     * @param puntoDeVenta
     * @param nroCbteDesde
     * @param nroCbteHasta
     * @throws main.FacElectronicaException
     */
    public void autorizarComprobante(int tipoComprobante, int puntoDeVenta, int nroCbteDesde, int nroCbteHasta) throws FacElectronicaException {
        
        // Declaro variable auxiliar
        FECAEResponse respuesta = new FECAEResponse();
                            
        // Capturo posibles errores
        try {
            
            // Muestro mensaje de salida
            log.info("- Buscando comprobantes en la base de datos...");

            // Instancio el catalogo de comprobantess
            ControladorComprobantes catalogoComprobantes = new ControladorComprobantes();

            // Obtengo los comprobantes de la base de datos
            catalogoComprobantes.buscarComprobantes(tipoComprobante, puntoDeVenta, nroCbteDesde, nroCbteHasta);

            // Muestro mensaje de salida
            log.info("- Cantidad de comprobantes encontrados: " + catalogoComprobantes.getListaComprobantes().size());

            // Obtengo el ultimo comprobante facturado
            FERecuperaLastCbteResponse ultimoCbte = ServicioAFIP.feCompUltimoAutorizado(ticketActual, puntoDeVenta, tipoComprobante);

            // Genero salida en el log            
            log.info("Ultimo numero comprobante autorizado para el tipo " + tipoComprobante + " y el punto de venta " + puntoDeVenta + ": " + ultimoCbte.getCbteNro());

            // Pregunto si se encontraron comprobantes
            if (catalogoComprobantes.getListaComprobantes().isEmpty()) {

                // Agrego una linea en el log
                log.error("No se encontraron comprobantes con esos parametros");

                // Lanzo un error
                throw new FacElectronicaException("No se encontraron comprobantes con los parametros recibidos");

            } else {

                // Este metodo cambia los numero de comprobante para que coincidan con los del AFIP
                catalogoComprobantes.corregirNumeracion(ultimoCbte.getCbteNro() + 1);

                // Declaro una variable para contar la cantidad de procesos
                int nroProceso = 1;

                // Realizo el proceso hasta que se vacie la coleccion
                while (!catalogoComprobantes.getListaComprobantes().isEmpty()) {

                    // Concateno mensaje
                    mensaje+= "Resultado Nro Proceso: " + nroProceso + "\r\n";
                            
                    // Obtengo el requerimiento para enviar a la AFIP
                    FECAERequest requerimientoCAE = catalogoComprobantes.getRequerimientoFacturacionDesdeComprobantes();

                    // Llamo al metodo que envia los datos a la AFIP
                    respuesta = ServicioAFIP.fecaeSolicitar(ticketActual, requerimientoCAE);

                    // Agrego una linea al Log
                    log.info("RESULTADO PROCESO NRO " + nroProceso);
                    
                    // Este if es para contener un error que se da cuando falla el ticket
                    if(respuesta.getFeCabResp() != null){
                        
                        // Pregunto por el resultado y genero el log que corresponde
                        switch (respuesta.getFeCabResp().getResultado()) {

                            // En este caso salio todo OK
                            case "A": log.info("\t Todos los comprobantes fueron aprobados.");
                                      mensaje+= "\t Todos los comprobantes fueron aprobados. \r\n";
                                      break;

                            // En este caso salio todo mal
                            case "R": log.error("\t Todos los comprobantes fueron rechazados, a continuacion se detallan los errores.");
                                      mensaje+= "\t Todos los comprobantes fueron rechazados, a continuacion se detallan los errores. \r\n";
                                      // Como fallaron todos, elimino el primer elemento de la lista y lo reintento
                                      catalogoComprobantes.getListaComprobantes().remove(0);
                                      break;

                            // En este caso hay comprobantes aprobados y comprobantes rechazados
                            case "P": log.info("\t Existen comprobantes rechazados y aprobados, a continuacion se detallan los errores y observaciones.");
                                      mensaje+= "\t Existen comprobantes rechazados y aprobados, a continuacion se detallan los errores y observaciones. \r\n";
                                      break;
                        }
                    
                    }

                    // Imprimo los errores, eventos y observaciones
                    ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    ServicioAFIP.imprimirEventos(respuesta.getEvents());
                    ServicioAFIP.imprimirObservaciones(respuesta.getFeDetResp());

                    // Elimino los comprobantes que se facturaron y los que dieron error
                    catalogoComprobantes.descartarComprobantes(respuesta);

                    // Obtengo los datos del ultimo comprobante autorizado
                    ultimoCbte = feCompUltimoAutorizado(ticketActual, puntoDeVenta, tipoComprobante);

                    // Este metodo cambia los numero de comprobante para que coincidan con los del AFIP
                    catalogoComprobantes.corregirNumeracion(ultimoCbte.getCbteNro() + 1);

                    // Incremento el contador
                    nroProceso++;

                } // FIN WHILE

            } // FIN IF

            // Vuelvo a obtengo los datos del ultimo comprobante autorizado despues de ejecutar el proceso
            ultimoCbte = feCompUltimoAutorizado(ticketActual, puntoDeVenta, tipoComprobante);

            // Agrego los datos del ultimo comprobante aprobado
            log.info("Ultimo Comprobante aprobado despues del proceso:");
            log.info("Tipo Cbte: " + ultimoCbte.getCbteTipo() + " - Pto Venta: " + ultimoCbte.getPtoVta() + " - Nro Cbte: " + ultimoCbte.getCbteNro());

        } catch (Exception e) {

            // Genero salida en el log
            log.error("Error autorizando comprobantes: " + e.getMessage());
            mensaje+= "\t Error autorizando comprobantes: " + e.getMessage() + "\r\n";
            
            // Lanzo un error
            throw new FacElectronicaException("Error autorizando comprobantes:");
            
        } finally {
            
            // Imprimo los errores, eventos y observaciones
            ServicioAFIP.imprimirErrores(respuesta.getErrors());
                    
            // Genero el log en la base de datos
            ServicioAFIP.catalogoTickets.generarLogEnBD(tipoComprobante, puntoDeVenta, nroCbteDesde, nroCbteHasta, mensaje);
        }
    }

    //--------------------------------------------------------------------------//
    //                INICIO METODOS DEL WEB SERVICES DE LA AFIP                //
    //--------------------------------------------------------------------------//
    
    
    /**
     * Este metodo devuele el estado del Web services de facturacion electronica
     * detallada
     *
     * @return
     */
    public static DummyResponse dummy() {
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();
        
        // Seteo el timeout para la conexion y el request
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", Main.config.getTimeOutWS()*1000);
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", Main.config.getTimeOutWS()*1000);
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.ws.request.timeout", Main.config.getTimeOutWS()*1000);        
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.ws.connect.timeout", Main.config.getTimeOutWS()*1000);        
        
        return port.feDummy();
    }

    /**
     * Este metodo permite consultar la lista de puntos de venta habilitados en
     * la AFIP
     *
     * @param auth
     * @return
     */
    private static FEPtoVentaResponse feParamGetPtosVenta(wsAFIP.FEAuthRequest auth) {

        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {

            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetPtosVenta"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetPtosVenta(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de Tipos de
     * Comprobantes utilizables en servicio de autorización.
     *
     * @param auth
     * @return
     */
    private static CbteTipoResponse feParamGetTiposCbte(wsAFIP.FEAuthRequest auth) {

        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {

            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposCbte"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposCbte(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de
     * identificadores para el campo Concepto.
     *
     * @param auth
     * @return
     */
    private static ConceptoTipoResponse feParamGetTiposConcepto(wsAFIP.FEAuthRequest auth) {
        
        wsAFIP.Service service = new wsAFIP.Service();
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {

            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposConcepto"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposConcepto(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de Tipos de
     * Documentos utilizables en servicio de autorización.
     *
     * @param auth
     * @return
     */
    private static DocTipoResponse feParamGetTiposDoc(wsAFIP.FEAuthRequest auth) {

        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposDoc"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposDoc(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de Tipos de
     * Iva utilizables en servicio de autorización.
     *
     * @param auth
     * @return
     */
    private static IvaTipoResponse feParamGetTiposIva(wsAFIP.FEAuthRequest auth) {

        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposIva"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposIva(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de monedas
     * utilizables en servicio de autorización
     *
     * @param auth
     * @return
     */
    private static MonedaResponse feParamGetTiposMonedas(wsAFIP.FEAuthRequest auth) {
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposMonedas"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposMonedas(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de
     * identificadores para los campos Opcionales
     *
     * @param auth
     * @return
     */
    private static OpcionalTipoResponse feParamGetTiposOpcional(wsAFIP.FEAuthRequest auth) {
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposOpcional"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposOpcional(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de los
     * diferente paises que pueden ser utilizados en el servicio de autorizacion
     *
     * @param auth
     * @return
     */
    private static FEPaisResponse feParamGetTiposPaises(wsAFIP.FEAuthRequest auth) {
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposPaises"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposPaises(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP el listado de los
     * diferente tributos que pueden ser utilizados en el servicio de
     * autorizacion
     *
     * @param auth
     * @return
     */
    private static FETributoResponse feParamGetTiposTributos(wsAFIP.FEAuthRequest auth) {

        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {
            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feParamGetTiposTributos"));
            binding.setHandlerChain(handlerList);
        }

        return port.feParamGetTiposTributos(auth);
    }

    /**
     * Este metodo consulta al web services de la AFIP Comprobante emitido y su
     * código.
     *
     * @param auth
     * @param feCompConsReq
     * @return
     */
    private static FECompConsultaResponse feCompConsultar(wsAFIP.FEAuthRequest auth, wsAFIP.FECompConsultaReq feCompConsReq) {
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {

            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feCompConsultar"));
            binding.setHandlerChain(handlerList);
        }

        return port.feCompConsultar(auth, feCompConsReq);
    }

    /**
     * Este metodo devuelve los datos del ultimo comprobante autorizado
     *
     * @param auth
     * @param ptoVta
     * @param cbteTipo
     * @return
     */
    private static FERecuperaLastCbteResponse feCompUltimoAutorizado(wsAFIP.FEAuthRequest auth, int ptoVta, int cbteTipo) {
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();

        // Pregunto si se quiere el log de los XML
        if (catalogoTickets.getWs_log()) {

            /* Agrega handler SOAP */
            Binding binding = ((BindingProvider) port).getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(new WsHandler("feCompUltimoAutorizado"));
            binding.setHandlerChain(handlerList);
        }

        return port.feCompUltimoAutorizado(auth, ptoVta, cbteTipo);
    }

    /**
     * Este metodo envia los datos al WEB SERVICES de la AFIP para obtener el
     * CAE
     *
     * @param auth
     * @param feCAEReq
     * @return
     */
    private static FECAEResponse fecaeSolicitar(wsAFIP.FEAuthRequest auth, wsAFIP.FECAERequest feCAEReq) {
        
        
        wsAFIP.Service service = new wsAFIP.Service(url);
        wsAFIP.ServiceSoap port = service.getServiceSoap();
        
        /* Agrega handler SOAP */
        Binding binding = ((BindingProvider) port).getBinding();
        
        // Seteo el timeout para la conexion y el request
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", Main.config.getTimeOutWS()*1000);
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", Main.config.getTimeOutWS()*1000);
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.ws.request.timeout", Main.config.getTimeOutWS()*1000);        
        ((BindingProvider)port).getRequestContext().put("com.sun.xml.ws.connect.timeout", Main.config.getTimeOutWS()*1000);        

        List<Handler> handlerList = binding.getHandlerChain();
        handlerList.add(new WsHandler("fecaeSolicitar"));
        binding.setHandlerChain(handlerList);
        
        return port.fecaeSolicitar(auth, feCAEReq);
    }

    //--------------------------------------------------------------------------//
    //                  FIN METODOS DEL WEB SERVICES DE LA AFIP                 //
    //--------------------------------------------------------------------------//
    
    
    //--------------------------------------------------------------------------//
    //               INICIO METODOS AUXILIARES DE LA APLICACION                 //
    //--------------------------------------------------------------------------//
    /**
     * Este metodo recorre la lista de errores recibida como parametro
     * imprimiendolos en la consola y en el log de errores
     *
     * @param Errores
     */
    private static void imprimirErrores(ArrayOfErr Errores) {
        
        // Valido que la coleccion de errores no este vacia
        if (Errores != null) {

            // Agrego lineas al log 
            log.info("Errores:");

            // Recorro la lista de errores para generar el log y la salida por consola
            for (Err error : Errores.getErr()) {

                log.info("Codigo de error: " + error.getCode() + "\r\n - Descripcion: " + error.getMsg());
                mensaje += "Codigo de error: " + error.getCode() + "\r\n - Descripcion: " + error.getMsg() +"\n\r";
                
            } // FIN FOR

        }
    }  // FIN METODO IMPRIMIR ERRORES

    /**
     * Este metodo recorre la lista de eventos recibida como parametro
     * imprimiendolos en la consola y en el log de errores
     *
     * @param Eventos
     */
    private static void imprimirEventos(ArrayOfEvt Eventos) {
        
        // Valido que la coleccion de errores no este vacia
        if (Eventos != null) {

            // Agrego cabecera al log
            log.info("Eventos:");

            // Recorro la lista de errores para generar el log y la salida por consola
            for (Evt evt : Eventos.getEvt()) {

                // Muestro una linea en la consola
                log.info("Codigo: " + evt.getCode() + " - Descripcion: " + evt.getMsg());

            } // FIN FOR  
        }

    } // FIN METODO IMPRIMIR EVENTOS

    /**
     * Este metodo imprime la lista de observaciones de todos los comprobantes
     * del requerimiento
     *
     * @param feDetResp
     */
    private static void imprimirObservaciones(ArrayOfFECAEDetResponse feDetResp) {
        
        // Recorro la respuesta del WebServices
        for (FEDetResponse cbte : feDetResp.getFECAEDetResponse()) {

            // Pregunto si el comprobante tiene observaciones
            if (cbte.getObservaciones() != null) {
                imprimirObservacionesCbte(cbte.getObservaciones());
            }
        }
    }

    /**
     * Este metodo recorre la lista de eventos recibida como parametro
     * imprimiendolos en la consola y en el log de errores
     *
     * @param observaciones
     */
    private static void imprimirObservacionesCbte(ArrayOfObs observaciones) {
        
        // Valido que la coleccion de errores no este vacia
        if (observaciones != null) {
            // Agrego lineas al log
            log.info("Observaciones:");

            // Recorro la lista de errores para generar el log y la salida por consola
            for (Obs obs : observaciones.getObs()) {

                // Muestro una linea en la consola
                log.info("Codigo: " + obs.getCode() + " - Descripcion: " + obs.getMsg());

            } // FIN FOR   
        }

    } // FIN METODO IMPRIMIR OBSERVACIONES

    //--------------------------------------------------------------------------//
    //                 FIN METODOS AUXILIARES DE LA APLICACION                  //
    //--------------------------------------------------------------------------//    
}
