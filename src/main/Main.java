package main;

import servicios.ServicioAFIP;
import accesoDB.ValidacionBD;
import configuracion.Configuracion;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import wsAFIP.DummyResponse;

/**
 * Esta clase inicia la aplicación
 *
 * @author Panasci Matias
 */
public class Main {

    //--------------------------------------------------------//
    // ACA DECLARO LAS PROPIEDADES QUE NECESITO PARA LA CLASE //
    //--------------------------------------------------------//
    public static Configuracion config;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final static Logger log = Logger.getLogger(Main.class);
    private final static String nroVersion = "8.2";
    private static String operacion;
    private static int tipoComprobante;
    private static int puntoDeVenta;
    private static int nroCbteDesde;
    private static int nroCbteHasta;
    //--------------------------------------------------------//
    private static ServicioAFIP wsAfip;
    //--------------------------------------------------------//

    /**
     * @param args the command line arguments
     *
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");

        // Genero salida en el log
        log.info("--------------- INICIO PROCESO " + sdf.format(new Date()) + "(VERSION: " + nroVersion + ") ---------------");

        // Capturo posibles errores
        try {
            
            // Cargo archivo de configuración para conectar a la base datos
            config = new Configuracion();
            
            // Obtengo los parametros recibidos en la invocacion del programa
            obtenerParametros(args);

            // Instancio el objeto que me permite realizar la conexion a la AFIP
            wsAfip = new ServicioAFIP();
            
            // Valido que exista las conexion
            validarConexiones();

            // Segun la operacion que se quiera realizar
            switch (operacion) {
                
                // Este metodo actualiza los puntos de venta permitidas por la afip                    
                case "FEParamGetPtosVenta":
                    wsAfip.consultarPuntosDeVenta();
                    break;
                    
                // Este metodo actualiza las alitcuotas permitidas por la afip                    
                case "FEParamGetTiposIva":
                    wsAfip.consultarTiposDeIVA();
                    break;

                // Este metodo actualiza los tipos de comprobante permitidos por la afip                    
                case "FEParamGetTiposCbte":
                    wsAfip.consultarTiposDeComprobante();
                    break;

                // Este metodo actualiza los tipos de monedas permitidas por la afip                    
                case "FEParamGetTiposMonedas":
                    wsAfip.consultarTiposDeMonedas();
                    break;

                // Este metodo actualiza los tipos de documentos permitidos por la afip                    
                case "FEParamGetTiposDoc":
                    wsAfip.consultarTiposDeDocumento();
                    break;

                // Este metodo actualiza los tipos de tributos permitidas por la afip                    
                case "FEParamGetTiposTributos":
                    wsAfip.consultarTiposDeTributos();
                    break;
        
                // Este metodo actualiza los tipos de conceptos permitidos por la afip                    
                case "FEParamGetTiposConcepto":
                    wsAfip.consultarTiposDeConceptos();
                    break;       
                    
                // Este metodo actualiza los tipos opcionales permitidos por la afip                    
                case "FEParamGetTiposOpcional":
                    wsAfip.consultarTiposOpcionales();
                    break;               

                // Este metodo actualiza los tipos de paises permitidos por la afip                    
                case "FEParamGetTiposPaises":
                    wsAfip.consultarTiposDePaises();
                    break;               

                // Este metodo obtiene la autorizacion de la AFIP
                case "FeCAESolicitar":
                    wsAfip.autorizarComprobante(tipoComprobante, puntoDeVenta, nroCbteDesde, nroCbteHasta);
                    break;

                // Este metodo obtiene el estado de un comprobante permitidas por la afip                    
                case "FECompConsultar":
                    wsAfip.consultarEstadoComprobante(tipoComprobante, puntoDeVenta, nroCbteDesde);
                    break;

                // Este metodo obtiene el ultimo comprobante autorizado para un tipo y punto de venta    
                case "FECompUltimoAutorizado":
                    wsAfip.consultarUltimoCbte(tipoComprobante, puntoDeVenta);
                    break;

                // Este metodo obtiene el estado de un comprobante permitidas por la afip                    
                case "MENU":
                    pedirOpcion();
                    break;

                // Muestro mensaje de error
                default: log.error("No se reconocieron los parametros...");
            }
            
            // Pregunto si uso pausa
            if(config.getApp_pausa()){
                
                // Genero un mensaje de salida
                System.out.println("Presione <ENTER> para continuar...");
            
                // Le pido que presione una tecla                
                try { System.in.read(); } catch (IOException ex) {}                            
            }

        } catch (FacElectronicaException | IOException e) {

            // Genero salida en el log
            log.error("ATENCION!!! Resultado final con errores: " + e.getMessage());

            // Genero salida en el log
            log.info("------------------- FIN PROCESO " + sdf.format(new Date()) + " -------------------");

            // Salgo del sistema
            System.exit(1);
        }

        // Genero salida en el log
        log.info("------------------- FIN PROCESO " + sdf.format(new Date()) + " -------------------");

        // Salgo del sistema
        System.exit(0);

    } // FIN METODO MAIN

    /**
     * Este metodo extrae los parametros que recibio el programa al ejecutarse
     *
     * @param args
     */
    private static void obtenerParametros(String[] args) throws FacElectronicaException {
        
        // Instancio un arreglo de string donde voy a colocar los parametro
        String[] parametros = new String[1];

        // Defino Cual va a ser el separado
        String delimiter = "-";

        // Valido que los argumentos no vengan vacios
        if (args.length > 0) {

            // Obtengo los parametros            
            parametros = args[0].split(delimiter);
        } else {

            // Le fuerzo un valor
            parametros[0] = "vacio";
        }

        // Muestro mensaje de inicio de la validacion de parametros
        log.info("Validando Parametros...");

        // Capturo posibles errores
        try {

            // Obtengo la operacion que se quiere realizar            
            operacion = parametros[0];

            // Valido que la operacion no sea igual a vacio
            if (!operacion.equalsIgnoreCase("vacio")) {

                // El try es por si no mandan los parametros
                try {

                    // Extrigo los parametros
                    tipoComprobante = Integer.parseInt(parametros[1]);
                    puntoDeVenta = Integer.parseInt(parametros[2]);
                    nroCbteDesde = Integer.parseInt(parametros[3]);
                    nroCbteHasta = Integer.parseInt(parametros[4]);

                } catch (Exception e) {}

                // Completo los datos del log de salida
                log.info("\t Operacion: " + operacion);
                log.info("\t Tipo Comprobante: " + tipoComprobante);
                log.info("\t Punto de Venta: " + puntoDeVenta);
                log.info("\t Cbte Desde: " + nroCbteDesde);
                log.info("\t Cbte Hasta: " + nroCbteHasta);

            } // FIN DEL IF

            // Imprimo el error en la consola
            log.info("\t Parametros OK!");

        } catch (NumberFormatException e) {

            // Pregunto por la clase de error
            if (e.getClass().getName().equalsIgnoreCase("java.lang.NumberFormatException")) {
                // Lanzo un error
                throw new FacElectronicaException("Error intentando obtener parametros de " + operacion + ":\r\n" + e.getMessage());
            } else {
                // Lanzo un error
                throw new FacElectronicaException("Error intentando ejecutar el metodo " + operacion + ":\r\n" + e.getMessage());
            }
        }

    } // FIN METODO OBTENER PARAMETROS    

    /**
     * Este metodo valida que la PC tenga acceso a internet, en caso de fallar
     * corta la ejecucion del programa
     *
     * @throws main.FacElectronicaException
     * @throws java.io.IOException
     */
    @SuppressWarnings("resource")
    public static void validarConexiones() throws Exception {
        
        // Muestro mensaje de salida
        log.info("Validando conexion a internet...");

        // Intancio el objero que me permite probar la conexion a internet
        Socket s;

        s = new Socket("www.google.com.ar", 80);

        // Valido que la conexion exista
        if (s.isConnected()) {

            // Cierro la conexion
            s.close();

            // Genero mensaje en el log
            log.info("\t Conexion a internet (puerto 80) OK!");

            // Genero mensaje en el log
            log.info("Validando conexión con la AFIP...");

            // Llamo al metodo que testea la conexion a la AFIP
            DummyResponse estadoServicio = ServicioAFIP.dummy();

            // Pregunto si se pudo establecer la conexion
            if (estadoServicio == null) {

                // Lanzo un error
                throw new FacElectronicaException("La conexión a internet funciono, pero fallo la conexion con la AFIP.");

            } else {

                // Muestro el estado de los servidores
                log.info("\t Servidor de Autenticación: " + estadoServicio.getAuthServer());
                log.info("\t Servidor de Aplicaciones: " + estadoServicio.getAppServer());
                log.info("\t Servidor de Base de datos: " + estadoServicio.getDbServer());

                // Valido que haya conexion a la AFIP
                if (estadoServicio.getAuthServer().equalsIgnoreCase("OK")
                    && estadoServicio.getAppServer().equalsIgnoreCase("OK")
                    && estadoServicio.getDbServer().equalsIgnoreCase("OK")) {

                    // Muestro el estado de los servidores
                    log.info("\t Conexion con la AFIP OK!");

                } else {

                    // Lanzo un error
                    throw new FacElectronicaException("La conexión a internet funciono, pero fallo la conexion con la AFIP.");

                }
            }

        }

        // Cierro el Socket
        s.close();

    } // FIN METODO VALIDAR CONEXION A INTERNET     

    /**
     * Este metodo muestra las opciones en la pantall
     */
    private static void mostrarMenu() {

        System.out.println("");
        System.out.println("==============================");
        System.out.println(" Ingrese operacion a realizar ");
        System.out.println("==============================");
        System.out.println("0 - Consultar puntos de Venta (Metodo: FEParamGetPtosVenta)");
        System.out.println("1 - Consultar tipos de Comprobante (Metodo: FEParamGetTiposCbte)");        
        System.out.println("2 - Consultar tipos de Conceptos (Metodo: FEParamGetTiposConcepto)");
        System.out.println("3 - Consultar tipos de Documentos (Metodo: FEParamGetTiposDoc)");        
        System.out.println("4 - Consultar tipos de IVA (Metodo: FEParamGetTiposIva)");        
        System.out.println("5 - Consultar tipos de Monedas (Metodo: FEParamGetTiposMonedas)");        
        System.out.println("6 - Consultar tipos Opcionales (Metodo: FEParamGetTiposOpcional)");
        System.out.println("7 - Consultar tipos de Paises (Metodo: FEParamGetTiposPaises)");
        System.out.println("8 - Consultar tipos de Tributos (Metodo: FEParamGetTiposTributos)");
        System.out.println("==============================");        
        System.out.println("9 - Consultar Ultimo Comprobante Autrizado");        
        System.out.println("10 - Consultar Estado Comprobante");
        System.out.println("11 - VALIDAR BASE DE DATOS");        
        System.out.println("12 - Salir");
        System.out.println("==============================");
        System.out.print("operacion: ");
    }

    /**
     * Este metodo muestra las operaciones manuales que se pueden realizar
     */
    private static void pedirOpcion() throws Exception {

        // Declaro variable auxiliar
        boolean bandera = true;

        // Hasta que no elija la opcion salir
        while (bandera) {

            // Muestro el menu por pantalla
            mostrarMenu();

            // sc se usará para leer los datos
            Scanner lecturaTeclado = new Scanner(System.in);

            // Ejecuto la operacion segun el ingreso del usuario por teclado
            switch (lecturaTeclado.nextInt()) {

                // Si estoy consultado los puntos de venta
                case 0: operacion = "FEParamGetPtosVenta";
                        wsAfip.consultarPuntosDeVenta();
                    break;

                // Si estoy consultado los tipos de comprobante utilizables en el WS
                case 1: operacion = "FEParamGetTiposCbte";
                        wsAfip.consultarTiposDeComprobante();
                    break;

                // Si estoy consultando por los tipos de conceptos
                case 2: operacion = "FEParamGetTiposConcepto";
                        wsAfip.consultarTiposDeConceptos();
                    break;

                // Si estoy consultando por los tipos de documentos
                case 3: operacion = "FEParamGetTiposDoc";
                        wsAfip.consultarTiposDeDocumento();
                    break;

                // Si estoy consultando por los tipos de IVA
                case 4: operacion = "FEParamGetTiposIva";
                        wsAfip.consultarTiposDeIVA();
                    break;

                // Si estoy consultando por los tipos de monedas
                case 5: operacion = "FEParamGetTiposMonedas";
                        wsAfip.consultarTiposDeMonedas();
                    break;
                    
                // Si estoy consultando por los tipos opcionales
                case 6: operacion = "FEParamGetTiposOpcional";
                        wsAfip.consultarTiposOpcionales();
                    break;

                // Si estoy consultando por los tipos de paises
                case 7: operacion = "FEParamGetTiposPaises";
                        wsAfip.consultarTiposDePaises();
                    break;

                // Si estoy consultando por los tipos de tributos
                case 8: operacion = "FEParamGetTiposTributos";
                        wsAfip.consultarTiposDeTributos();
                    break;
                    
                // Si estoy consultando por el ultimo comprobante autorizado
                case 9: operacion = "Consultar Ultimo Comprobante Autrizado";
                        wsAfip.consultarUltimoCbte(tipoComprobante, puntoDeVenta);
                    break;

                // Si estoy consultando por es estado de un comprobante
                case 10: operacion = "Consultar Estado Comprobante";
                         wsAfip.consultarEstadoComprobante(tipoComprobante, puntoDeVenta, nroCbteDesde);
                    break;                    

                // Opcion Testear DB
                case 11: operacion = "Testear DB"; ValidacionBD validador = new ValidacionBD(); validador.validarBaseDeDatos();
                         
                        // Genero salida en el log
                        log.info("Opción Validar Base de datos seleccionada");                         
                        
                    break;                    

                // Opcion Salir
                case 12: operacion = "SALIR";
                         bandera = false;
                         
                        // Genero salida en el log
                        log.info("Opción salir seleccionada");                         
                        
                    break;

                // Operacion por defecto
                default: System.out.println("Opreacion no reconocida");
                    break;
            }
        }
    }
}
