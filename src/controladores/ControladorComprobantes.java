package controladores;

import accesoDB.ConexionSyBase;
import wsAFIP.AlicIva;
import wsAFIP.FECAEDetRequest;
import wsAFIP.FECAEDetResponse;
import wsAFIP.FECAERequest;
import wsAFIP.FECAEResponse;
import wsAFIP.Tributo;
import wsAFIP.CbteAsoc;
import wsAFIP.Periodo;
import wsAFIP.Opcional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import entidades.Comprobante;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.MyUtil;
import wsAFIP.ArrayOfAlicIva;
import wsAFIP.ArrayOfCbteAsoc;
import wsAFIP.ArrayOfFECAEDetRequest;
import wsAFIP.ArrayOfOpcional;
import wsAFIP.ArrayOfTributo;
import wsAFIP.FECAECabRequest;
import wsAFIP.FECompConsResponse;

/**
 * Esta clase se creo para realizar la solicitud de CAE al Web services de la 
 * AFIP y evitar la problematica de la numeracion de comprobantes defasada
 * 
 * @author Administrador
 */
public class ControladorComprobantes extends ConexionSyBase {

    //----------------------------------------------------//
    // Aca delcaro las variables necesarias para la clase //
    //----------------------------------------------------//    
    private final static Logger log = Logger.getLogger(ControladorComprobantes.class);    
    private List<Comprobante> listaComprobantes;
    private boolean esFacturaEnUSD; 
    private String vacio = "";
    //----------------------------------------------------//
    
    /**
     * Metodo constructor de la clase
     * 
     * @throws Exception 
     */
    public ControladorComprobantes() throws Exception {

        // Mantengo Herencia
        super();
        
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");        
    }
    
    /**
     * Este genera una coleccion de comprobantes para enviar a la AFIP
     * 
     * @param tipoCbte
     * @param ptoVta
     * @param nroCbteDesde
     * @param nroCbteHasta
     * @throws java.lang.Exception
     */
    public void buscarComprobantes(int tipoCbte, int ptoVta, int nroCbteDesde, int nroCbteHasta) throws Exception {
    
        // Inicio la coleccion
        listaComprobantes = new ArrayList();
        
        // Creo la consulta SQL
        String sentenciaSQL = " SELECT DISTINCT fac_ventas.autorizado_sn, afipws_fe_master.* FROM afipws_fe_master " +
                              " INNER JOIN fac_ventas ON afipws_fe_master.CbteTipo = fac_ventas.v_tipo_comprobante " +
                              " AND afipws_fe_master.PtoVta = fac_ventas.pto_numero AND " +
                              " afipws_fe_master.CbteDesde = fac_ventas.v_numero_comprobante  WHERE fac_ventas.autorizado_sn = 'N' " +
                              " AND (CbteTipo = ? AND PtoVta = ? AND CbteDesde >= ? AND CbteDesde <= ?) AND v_fecha_operacion >= ? ";
        
        // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
        PreparedStatement sentencia = crearSentencia(sentenciaSQL);
        sentencia.setInt(1, tipoCbte);
        sentencia.setInt(2, ptoVta);
        sentencia.setInt(3, nroCbteDesde);
        sentencia.setInt(4, nroCbteHasta);
        sentencia.setDate(5, new java.sql.Date((MyUtil.getFechaMinima(new Date())).getTime()));
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(sentenciaSQL);
            log.info("Parametro 1: " + tipoCbte);
            log.info("Parametro 2: " + ptoVta);
            log.info("Parametro 3: " + nroCbteDesde);
            log.info("Parametro 4: " + nroCbteHasta);
            log.info("Parametro 5: " + new java.sql.Date((MyUtil.getFechaMinima(new Date())).getTime()));
        }  
        
        // Ejecuto la consulta
        ResultSet filasConsulta = consultar(sentencia);

        // Instancio un objeto que me permite formatear la fecha
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
        
        // Recorro el resultado cargando los objetos en la coleccion
        while (filasConsulta.next()) 
        {
            // Instancio un objeto itemRequerimiento
            FECAEDetRequest itemActual = new FECAEDetRequest();

            // Le completo los datos
            itemActual.setConcepto(filasConsulta.getInt("Concepto"));
            itemActual.setDocTipo(filasConsulta.getInt("DocTipo"));
            itemActual.setDocNro(filasConsulta.getLong("DocNro"));
            itemActual.setCbteDesde(filasConsulta.getLong("CbteDesde"));
            itemActual.setCbteHasta(filasConsulta.getLong("CbteHasta"));
            itemActual.setCbteFch(formatoFecha.format(filasConsulta.getDate("CbteFch")));
            itemActual.setImpTotal(filasConsulta.getDouble("ImpTotal"));
            itemActual.setImpTotConc(filasConsulta.getDouble("ImpTotConc"));
            itemActual.setImpNeto(filasConsulta.getDouble("ImpNeto"));
            itemActual.setImpOpEx(filasConsulta.getDouble("ImpOpEx"));
            itemActual.setImpIVA(filasConsulta.getDouble("ImpIVA"));
            itemActual.setImpTrib(filasConsulta.getDouble("ImpTrib"));
            
            // Valido que sea una factura de servicios
            if(itemActual.getConcepto() == 2 || itemActual.getConcepto() == 3)
            {
                itemActual.setFchServDesde(formatoFecha.format(filasConsulta.getDate("fchServDesde")));
                itemActual.setFchServHasta(formatoFecha.format(filasConsulta.getDate("fchServHasta")));
                itemActual.setFchVtoPago(formatoFecha.format(filasConsulta.getDate("fchVtoPago")));
            }
            
               // Valido que sea una factura de Credito Electronica
            if(tipoCbte == 202 || tipoCbte == 203  )
            {
                itemActual.setFchVtoPago(vacio);
            }

            itemActual.setMonId(filasConsulta.getString("MonID"));
            itemActual.setMonCotiz(filasConsulta.getInt("MonCotiz"));

            // Pregunto si es un comprobante en dolares
            if(itemActual.getMonId().equalsIgnoreCase("DOL")){
                
                // Marco la bandera como que es en dolares
                this.esFacturaEnUSD = true;
                
            } else {
                
                // Marco la bandera como que es en pesos                
                this.esFacturaEnUSD = false;                
            }
            
            // Creo una nueva consulta para para las alicuotas de IVA
            sentenciaSQL = "SELECT * FROM afipws_fe_detalle WHERE CbteTipo = " + tipoCbte + 
                           " AND TipoDetalle = 3 AND PtoVta = " + ptoVta 
                         + " AND CbteDesde = " + itemActual.getCbteDesde();

            // Instancio el objeto que me permite ejecutar consultas
            sentencia = crearSentencia(sentenciaSQL);

            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){

                // Genero salida en la consola
                log.info(sentenciaSQL);
                log.info("Parametro 1: " + tipoCbte);
                log.info("Parametro 2: " + ptoVta);
                log.info("Parametro 3: " + itemActual.getCbteDesde());
            }  
        
            // Ejecuto la consulta
            ResultSet filasConsulta2 = consultar(sentencia);

            // Recorro el resultado cargando los objetos en la coleccion
            while (filasConsulta2.next()) 
            {
                // Instancio un objeto AlicuotaIVA
                AlicIva alicuotaActual = new AlicIva();

                alicuotaActual.setId(filasConsulta2.getInt("Id"));
                alicuotaActual.setBaseImp(filasConsulta2.getDouble("BaseImp"));
                alicuotaActual.setImporte(filasConsulta2.getDouble("Importe"));

                // Valido que no este en null la coleccion
                if(itemActual.getIva() == null){
                    itemActual.setIva(new ArrayOfAlicIva());  
                }

                // Agrego el objeto a la coleccion
                itemActual.getIva().getAlicIva().add(alicuotaActual);
            }

            // Creo una nueva consulta para para las alicuotas de IVA
            sentenciaSQL = "SELECT * FROM afipws_fe_detalle WHERE CbteTipo = " + tipoCbte + 
                           " AND TipoDetalle = 2 AND PtoVta = " + ptoVta 
                         + " AND CbteDesde = " + itemActual.getCbteDesde();
            
            // Instancio el objeto que me permite ejecutar consultas
            sentencia = crearSentencia(sentenciaSQL);

            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){

                // Genero salida en la consola
                log.info(sentenciaSQL);
                log.info("Parametro 1: " + tipoCbte);
                log.info("Parametro 2: " + ptoVta);
                log.info("Parametro 3: " + itemActual.getCbteDesde());
            }  
            
            // Ejecuto la consulta
            filasConsulta2 = consultar(sentencia);

            // Recorro el resultado cargando los objetos en la coleccion
            while (filasConsulta2.next()) 
            {
                // Instancio un objeto Tributo
                Tributo tributoAtual = new Tributo();

                tributoAtual.setAlic(filasConsulta2.getDouble("Alic"));
                tributoAtual.setBaseImp(filasConsulta2.getDouble("BaseImp"));
                tributoAtual.setDesc(filasConsulta2.getString("descriTributo"));
                tributoAtual.setId(filasConsulta2.getShort("Id"));
                tributoAtual.setImporte(filasConsulta2.getDouble("Importe"));

                // Valido que no este en null la coleccion
                if(itemActual.getTributos() == null){
                    itemActual.setTributos(new ArrayOfTributo());
                }

                // Agrego el objeto a la coleccion
                itemActual.getTributos().getTributo().add(tributoAtual);
            }
            
                //-------------------------------------------//
                // COMPLETO OPCIONALES                       //
                //-------------------------------------------//

                // Creo una nueva consulta para los Opcionales
                sentenciaSQL = "SELECT * FROM afipws_fe_Opcionales WHERE CbteTipo = " + tipoCbte  
                             +  " AND PtoVta = " + ptoVta 
                             + " AND CbteNro = " + itemActual.getCbteDesde();

                // Instancio el objeto que me permite ejecutar consultas
                sentencia = crearSentencia(sentenciaSQL);

                // Ejecuto la consulta
                ResultSet filasConsulta3 = consultar(sentencia);

                // Recorro el resultado cargando los objetos en la coleccion
                while (filasConsulta3.next()) 
                {
                    // Instancio un objeto Opcional
                    Opcional opcionalActual = new Opcional();
                    
                    opcionalActual.setId(filasConsulta3.getString("Id"));
                    opcionalActual.setValor(filasConsulta3.getString("valor"));
                    
                    // Valido que no este en null la coleccion
                    if(itemActual.getOpcionales() == null){
                        itemActual.setOpcionales(new ArrayOfOpcional());
                    }
                
                   // Agrego el objeto a la coleccion
                   itemActual.getOpcionales().getOpcional().add(opcionalActual);
      
                }
            
                //-------------------------------------------//
                // COMPLETO CBTES ASOCIADOS                  //
                //-------------------------------------------//

                // Creo una nueva consulta para los Opcionales
                sentenciaSQL = "SELECT * FROM afipws_fe_CbtesAsociados WHERE CbteTipo = " + tipoCbte  
                             +  " AND PtoVta = " + ptoVta 
                             + " AND CbteNro = " + itemActual.getCbteDesde();

                // Instancio el objeto que me permite ejecutar consultas
                sentencia = crearSentencia(sentenciaSQL);

                // Ejecuto la consulta
                filasConsulta3 = consultar(sentencia);

                // Recorro el resultado cargando los objetos en la coleccion
                while (filasConsulta3.next()) 
                {
                    // Instancio un objeto Opcional
                    CbteAsoc cbteasocActual = new CbteAsoc();
                    
                    cbteasocActual.setTipo(filasConsulta3.getInt("CTipoAsoc"));
                    cbteasocActual.setPtoVta(filasConsulta3.getInt("PVtaAsoc"));
                    cbteasocActual.setNro(filasConsulta3.getLong("CNroAsoc"));
                    cbteasocActual.setCuit(filasConsulta3.getString("CuitAsoc"));
                    
                    if(filasConsulta3.getDate("FechaAsoc") != null){
                       cbteasocActual.setCbteFch(formatoFecha.format(filasConsulta3.getDate("FechaAsoc")));
                    }
                    
                    // Valido que no este en null la coleccion
                    if(itemActual.getCbtesAsoc() == null){
                        itemActual.setCbtesAsoc(new ArrayOfCbteAsoc());
                    }
                   
                    // Agrego el objeto a la coleccion
                   itemActual.getCbtesAsoc().getCbteAsoc().add(cbteasocActual);
             
                }
                
                //-------------------------------------------//
                // COMPLETO PERIODO CBTES ASOCIADOS          //
                //-------------------------------------------//

                // Creo una nueva consulta para los Opcionales
                sentenciaSQL = "SELECT * FROM afipws_fe_CbtesAsociados_periodo WHERE CbteTipo = " + tipoCbte  
                             +  " AND PtoVta = " + ptoVta 
                             + " AND CbteNro = " + itemActual.getCbteDesde();

                // Instancio el objeto que me permite ejecutar consultas
                sentencia = crearSentencia(sentenciaSQL);

                // Ejecuto la consulta
                filasConsulta3 = consultar(sentencia);

                // Recorro el resultado cargando los objetos en la coleccion
                while (filasConsulta3.next()) 
                {
                    // Instancio un objeto Opcional
                   Periodo periodoActual = new Periodo();
                   
                   periodoActual.setFchDesde(formatoFecha.format(filasConsulta3.getDate("fechadesde")));
                   periodoActual.setFchHasta(formatoFecha.format(filasConsulta3.getDate("fechahasta")));
                                
                   itemActual.setPeriodoAsoc(periodoActual);
                   
                }
            
            // Agrego el Comprobante a la coleccion
            listaComprobantes.add(new Comprobante(tipoCbte, ptoVta, itemActual.getCbteDesde(), itemActual));            
        }
                
        // Realizo la desconexion
        desconectar();
    }

    /**
     * Este metodo recorre la lista de comprobante corrigiendo la numeracion defasada
     * 
     * @param proximoNumero 
     */
    public void corregirNumeracion(int proximoNumero) {

        // Inicio el contador interno
        int numeroActual = proximoNumero;
        
        for (Comprobante comprobante : listaComprobantes) {
            
            // Le cambio el numero al que tiene que llevar
            comprobante.setCbteDesde(numeroActual);
            
            // Lo incremento
            numeroActual++;
        }
    }
    
    /**
     * Este metodo devuelve un requerimiento de facturacion a apartir de los comprobantes encontrados
     * 
     * @return 
     */
    public FECAERequest getRequerimientoFacturacionDesdeComprobantes() {
        
        // Instacio el objeto que almacena los datos del requerimiento
        FECAERequest requerimientoFacturacion = new FECAERequest();
        requerimientoFacturacion.setFeCabReq(new FECAECabRequest());
        requerimientoFacturacion.setFeDetReq(new ArrayOfFECAEDetRequest());
        
        // Genero la coleccion de comprobantes
        for (Comprobante comprobante : listaComprobantes) {
            
            // Agrego el objeto a la coleccion
            requerimientoFacturacion.getFeDetReq().getFECAEDetRequest().add(comprobante.getComprobanteAFIP());
        }
        
        // Completo los datos de la cabecera
        requerimientoFacturacion.getFeCabReq().setCantReg(requerimientoFacturacion.getFeDetReq().getFECAEDetRequest().size());
        requerimientoFacturacion.getFeCabReq().setPtoVta(listaComprobantes.get(0).getPto_numero());
        requerimientoFacturacion.getFeCabReq().setCbteTipo(listaComprobantes.get(0).getV_tipo_comprobante());        
        
        // Devuelvo el requerimiento
        return requerimientoFacturacion;
    }
    

    /**
     * Este metodo elimina de la lista de comprobantes a facturar todos aquellos 
     * comprobantes que ya fueron facturados o tienen errores
     * 
     * @param respuesta 
     * @throws java.lang.Exception 
     */
    public void descartarComprobantes(FECAEResponse respuesta) throws Exception {
        
        // Recorro la lista de respuestas actualizando los comprobantes
        for (FECAEDetResponse cbteAFIP : respuesta.getFeDetResp().getFECAEDetResponse()) {
            
            // Valido que el comprobante tenga CAE (Si no tiene es por que lo rechazaron)
            if(!cbteAFIP.getCAE().isEmpty()){
                
                // Obtengo el comprobante interno
                Comprobante cbteInterno = getComprobante(cbteAFIP);
                        
                // Llamo al metodo que persiste los datos en la base de datos
                actualizarDatosComprobante(cbteInterno, cbteAFIP);    
                
                // Eliminino el comprobante facturado
                this.listaComprobantes.remove(cbteInterno);
            }            
            
            // Valido que el comprobante tenga errores y que tenga almenos una observacion, esto se hace por que si falla
            // un comprobante del lote los marca a todos como rechazados, pero solo el que falla tiene observaciones
            if( cbteAFIP.getResultado().equalsIgnoreCase("R")){
                
                // Valido que las observaciones no sean null
                if(cbteAFIP.getObservaciones() != null){
                
                    // Valido la cantidad de observaciones
                    if(cbteAFIP.getObservaciones().getObs().size() > 0 ) {
                        
                        // Obtengo el comprobante interno
                        Comprobante cbteInterno = getComprobante(cbteAFIP);

                        // Eliminino el comprobante para poder facturar los que restan del lote
                        this.listaComprobantes.remove(cbteInterno);                    
                    }
                }

            }                        
        }
    }
    
    /**
     * Este metodo devuelve el comprobate que conincide con el recbido por parametro
     * 
     * @return 
     */
    private Comprobante getComprobante(FECAEDetResponse cbte) {
        
        // Recorro la coleccion buscando el comprobante
        for (Comprobante c : listaComprobantes) {
            
            // Si el numero coincide con el que busco
            if(c.getCbteDesde() == cbte.getCbteDesde()){
                
                // Devuelvo el comprobante
                return c;
            }
        }
    
        // Si llego aca es por que algo fallo
        return null;
    }
    
    /**
     * Este metodo actualiza el CAE, fecha de vencimiento del CAE y la numeracion 
     * del comprobante segun la AFIP en la base de datos
     * 
     * @param cbteInterno
     * @param cbteAFIP
     * @throws Exception 
     */
    public void actualizarDatosComprobante(Comprobante cbteInterno, FECAEDetResponse cbteAFIP) throws Exception {
        
        // Armo la instruccion SQL para actualizar la tabla cabecera
        String instruccionSQL = "UPDATE afipws_fe_master SET  "
                                    + "CAE = ?, "                   // 1
                                    + "CAEFchVto = ?, "             // 2
                                    + "Resultado = ?, "              // 3
                                    + "tipo_Comp = ?, "              // 4
                                    + "pto_emision = ?, "            // 5 
                                    + "v_numero_comprobante = ? "   // 6
                              + "WHERE CbteTipo = ? AND PtoVta = ? AND CbteDesde = ? "; // 7 - 8 -9
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setString(1, cbteAFIP.getCAE());
        sentencia.setString(2, cbteAFIP.getCAEFchVto());
        sentencia.setString(3, cbteAFIP.getResultado());
        sentencia.setInt(4, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(5, cbteInterno.getPto_numero());
        sentencia.setLong(6, cbteAFIP.getCbteDesde());        
        sentencia.setInt(7, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(8, cbteInterno.getPto_numero());
        sentencia.setLong(9, cbteInterno.getV_numero_comprobante());
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + cbteAFIP.getCAE());
            log.info("Parametro 2: " + cbteAFIP.getCAEFchVto());
            log.info("Parametro 3: " + cbteAFIP.getResultado());
            log.info("Parametro 4: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 5: " + cbteInterno.getPto_numero());
            log.info("Parametro 6: " + cbteAFIP.getCbteDesde());
            log.info("Parametro 7: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 8: " + cbteInterno.getPto_numero());
            log.info("Parametro 9: " + cbteInterno.getV_numero_comprobante());
        }  
        
        // Ejecuto el update
        actualizar(sentencia);     
        
        
        // Armo la instruccion SQL para actualizar la tabla detalle
        instruccionSQL = "UPDATE afipws_fe_detalle SET  "
                       + "tipo_Comp = ?, " // 1
                       + "pto_emision = ?, " // 2 
                       + "v_numero_comprobante = ? " // 3
                       + "WHERE CbteTipo = ? AND PtoVta = ? AND CbteDesde = ? "; // 4 - 5 - 6
        
        // Creo la nueva instruccion
        sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setInt(1, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(2, cbteInterno.getPto_numero());
        sentencia.setLong(3, cbteAFIP.getCbteDesde());    
        sentencia.setInt(4, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(5, cbteInterno.getPto_numero());
        sentencia.setLong(6, cbteInterno.getV_numero_comprobante());
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 2: " + cbteInterno.getPto_numero());
            log.info("Parametro 3: " + cbteAFIP.getCbteDesde());
            log.info("Parametro 4: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 5: " + cbteInterno.getPto_numero());
            log.info("Parametro 6: " + cbteInterno.getV_numero_comprobante());
        } 
        
        // Ejecuto el update
        actualizar(sentencia);            
        
        // Instancio el objeto que me permite formatear fechas
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        
        // Armo la instruccion SQL para actualizar la fac ventas
        instruccionSQL = "UPDATE fac_ventas SET  "
                       + "autorizado_sn = 'S', "     
                       + "tipo_autorizado = ?, "    // 1
                       + "pto_autorizado = ?, "     // 2
                       + "nro_autorizado = ?, "     // 3
                       + "fe_autorizado = ?, "      // 4
                       + "CAE= ?, "                 // 5 
                       + "fe_vto = ? "              // 6
                       + "WHERE v_tipo_comprobante = ? AND Pto_numero = ? AND v_numero_comprobante = ? "; // 7 - 8 - 9
        
        // Instancio el objeto que me permite ejecutar la instruccion
        sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setInt(1, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(2, cbteInterno.getPto_numero());
        sentencia.setLong(3, cbteAFIP.getCbteDesde());
        sentencia.setString(4, sdf.format(new Date()));
        sentencia.setString(5, cbteAFIP.getCAE());
        sentencia.setString(6, cbteAFIP.getCAEFchVto());
        sentencia.setInt(7, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(8, cbteInterno.getPto_numero());
        sentencia.setLong(9, cbteInterno.getV_numero_comprobante());
        //sentencia.setDate(10, new java.sql.Date((new Date()).getTime()));
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 2: " + cbteInterno.getPto_numero());
            log.info("Parametro 3: " + cbteAFIP.getCbteDesde());
            log.info("Parametro 4: " + sdf.format(new Date()));
            log.info("Parametro 5: " + cbteAFIP.getCAE());
            log.info("Parametro 6: " + cbteAFIP.getCAEFchVto());
            log.info("Parametro 7: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 8: " + cbteInterno.getPto_numero());
            log.info("Parametro 9: " + cbteInterno.getV_numero_comprobante());
            //log.info("Parametro 10: " + new java.sql.Date((new Date()).getTime()));            
        } 
        
        // Ejecuto el update
        actualizar(sentencia);             
        
        // Armo la instruccion SQL para actualizar la fac ventas en dolares
        instruccionSQL = "UPDATE fac_ventas_dolar SET  "
                       + "autorizado_sn = 'S', "     
                       + "tipo_autorizado = ?, "    // 1
                       + "pto_autorizado = ?, "     // 2
                       + "nro_autorizado = ?, "     // 3
                       + "fe_autorizado = ?, "      // 4
                       + "CAE= ?, "                 // 5 
                       + "fe_vto = ? "              // 6
                       + "WHERE v_tipo_comprobante = ? AND Pto_numero = ? AND v_numero_comprobante = ?  "; // 7 - 8 - 9 
        
        // Instancio el objeto que me permite ejecutar la instruccion
        sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setInt(1, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(2, cbteInterno.getPto_numero());
        sentencia.setLong(3, cbteAFIP.getCbteDesde());
        sentencia.setString(4, sdf.format(new Date()));
        sentencia.setString(5, cbteAFIP.getCAE());
        sentencia.setString(6, cbteAFIP.getCAEFchVto());
        sentencia.setInt(7, cbteInterno.getV_tipo_comprobante());
        sentencia.setInt(8, cbteInterno.getPto_numero());
        sentencia.setLong(9, cbteInterno.getV_numero_comprobante());
        //sentencia.setDate(10, new java.sql.Date((new Date()).getTime()));
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 2: " + cbteInterno.getPto_numero());
            log.info("Parametro 3: " + cbteAFIP.getCbteDesde());
            log.info("Parametro 4: " + sdf.format(new Date()));
            log.info("Parametro 5: " + cbteAFIP.getCAE());
            log.info("Parametro 6: " + cbteAFIP.getCAEFchVto());
            log.info("Parametro 7: " + cbteInterno.getV_tipo_comprobante());
            log.info("Parametro 8: " + cbteInterno.getPto_numero());
            log.info("Parametro 9: " + cbteInterno.getV_numero_comprobante());
            //log.info("Parametro 10: " + new java.sql.Date((new Date()).getTime()));            
        } 
        
        // Ejecuto el update
        actualizar(sentencia);             
        
    }    
    
    /**
     * Este metodo se ejeucta cuando se consulta el estado de un comprobante en 
     * el Web services de la AFIP, la idea es poder recuperar los datos de un 
     * comprobante cuando un proceso se corta a la mitad
     * 
     * @param respuestaAFIP
     * @param tipoComprobante
     * @param puntoDeVenta
     * @param nroCbteDesde
     * @throws java.lang.Exception
     */
    public void actualizarDatosDeComprobanteEnTablasAFIP(FECompConsResponse respuestaAFIP, 
                                                        int tipoComprobante, 
                                                        int puntoDeVenta, 
                                                        int nroCbteDesde ) throws Exception {
        
        // Armo la instruccion SQL para actualizar la tabla cabecera
        String instruccionSQL = "UPDATE afipws_fe_master SET  "
                                    + "CAE = ?, "                   // 1
                                    + "CAEFchVto = ?, "             // 2
                                    + "Resultado = ?, "             // 3
                                    + "tipo_Comp = ?, "             // 4
                                    + "pto_emision = ?, "           // 5 
                                    + "v_numero_comprobante = ? "   // 6
                              + "WHERE CbteTipo = ? AND PtoVta = ? AND CbteDesde = ? "; // 7 - 8 -9
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setString(1, respuestaAFIP.getCodAutorizacion());
        sentencia.setString(2, respuestaAFIP.getFchVto());
        sentencia.setString(3, respuestaAFIP.getResultado());
        sentencia.setInt(4, tipoComprobante);
        sentencia.setInt(5, puntoDeVenta);
        sentencia.setLong(6, nroCbteDesde);        
        sentencia.setInt(7, tipoComprobante);
        sentencia.setInt(8, puntoDeVenta);
        sentencia.setLong(9, nroCbteDesde);
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + respuestaAFIP.getCodAutorizacion());
            log.info("Parametro 2: " + respuestaAFIP.getFchVto());
            log.info("Parametro 3: " + respuestaAFIP.getResultado());
            log.info("Parametro 4: " + tipoComprobante);
            log.info("Parametro 5: " + puntoDeVenta);
            log.info("Parametro 6: " + nroCbteDesde);
            log.info("Parametro 7: " + tipoComprobante);
            log.info("Parametro 8: " + puntoDeVenta);
            log.info("Parametro 9: " + nroCbteDesde);
        }  
        
        // Ejecuto el update
        actualizar(sentencia);     
        
        
        // Armo la instruccion SQL para actualizar la tabla detalle
        instruccionSQL = "UPDATE afipws_fe_detalle SET  "
                       + "tipo_Comp = ?, " // 1
                       + "pto_emision = ?, " // 2 
                       + "v_numero_comprobante = ? " // 3
                       + "WHERE CbteTipo = ? AND PtoVta = ? AND CbteDesde = ? "; // 4 - 5 - 6
        
        // Creo la nueva instruccion
        sentencia = crearSentencia(instruccionSQL);
        
        // Completo los datos del objeto
        sentencia.setInt(1, tipoComprobante);
        sentencia.setInt(2, puntoDeVenta);
        sentencia.setLong(3, nroCbteDesde);    
        sentencia.setInt(4, tipoComprobante);
        sentencia.setInt(5, puntoDeVenta);
        sentencia.setLong(6, nroCbteDesde);
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + tipoComprobante);
            log.info("Parametro 2: " + puntoDeVenta);
            log.info("Parametro 3: " + nroCbteDesde);
            log.info("Parametro 4: " + tipoComprobante);
            log.info("Parametro 5: " + puntoDeVenta);
            log.info("Parametro 6: " + nroCbteDesde);
        } 
        
        // Ejecuto el update
        actualizar(sentencia);            
        
    }
    
    
    //---------------------------------------------------------------//
    //------------------- METODOS ENCAPSULAMIENTO -------------------//
    //---------------------------------------------------------------//
    
    /**
     * Metodo que otorga vibilidad al objeto
     * 
     * @return 
     */
    public List<Comprobante> getListaComprobantes() {
        return listaComprobantes;
    }

    /**
     * Metodo que otorga visibilidad al objeto
     * 
     * @param listaComprobantes 
     */
    public void setListaComprobantes(List<Comprobante> listaComprobantes) {
        this.listaComprobantes = listaComprobantes;
    }    
}
