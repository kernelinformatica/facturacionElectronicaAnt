package accesoDB;

import java.sql.PreparedStatement;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Esta clase se creo para realizar un testeo en la BD para saber si tiene la 
 * estructura necesaria como para utilizar el web services de la AFIP
 * 
 * @author Matias Panasci
 */
public final class ValidacionBD extends ConexionSyBase {

    private final static Logger log = Logger.getLogger(ValidacionBD.class);        
    
    /**
     * Metodo constructor de la clase
     * 
     * @throws Exception 
     */
    public ValidacionBD() throws Exception {
        
        // Mantengo herencia
        super();
        
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");

    }
    
    /**
     * Este metodo valida la estructura de la base de datos, si falta alguna 
     * tabla o campo los crea
     */
    public void validarBaseDeDatos(){
    
        // Capturo posibles errores
        try {
            
            // Armo una consulta para validar la tabla detalle de comprobantes
            String sentenciaSQL = "SELECT * FROM afipws_fe_detalle";
            
            // Pregunto si la afipws_fe_detalle esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_detalle (" +
                                "	CbteTipo NUMERIC(2,0) NOT NULL," +
                                "	PtoVta NUMERIC(4,0) NOT NULL," +
                                "	CbteDesde NUMERIC(8,0) NOT NULL," +
                                "	TipoDetalle NUMERIC(1,0) NOT NULL," +
                                "	Pase NUMERIC(12,0) NOT NULL DEFAULT AUTOINCREMENT," +
                                "	Id VARCHAR(5) NULL," +
                                "	AsocPtoVta NUMERIC(4,0) NULL DEFAULT 0," +
                                "	AsocNroCbte NUMERIC(8,0) NULL DEFAULT 0," +
                                "	Valor VARCHAR(255) NULL," +
                                "	BaseImp NUMERIC(13,2) NULL DEFAULT 0," +
                                "	Alic NUMERIC(5,2) NULL DEFAULT 0," +
                                "	Importe NUMERIC(13,2) NULL DEFAULT 0," +
                                "	descriTributo VARCHAR(255) NULL," +
                                "	tipo_comp NUMERIC(2,0) NULL," +
                                "	pto_emision NUMERIC(4,0) NULL," +
                                "	v_numero_comprobante NUMERIC(16,0) NULL," +
                                "	PRIMARY KEY ( CbteTipo , PtoVta , CbteDesde , TipoDetalle , Pase  )" +
                                ") IN system;" +
                                "COMMENT ON TABLE DBA.afipws_fe_detalle IS 'facturas - detalle (tributos, iva, observa)';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_detalle.TipoDetalle IS '1 CbteAsoc, 2 Tributos, 3 IVA, 4 Opcionales, 5 Observaciones';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_detalle creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_detalle esta OK!");            
            } 
            
            
            // Armo una consulta para validar la tabla cabecera de comprobantes
            sentenciaSQL = "SELECT * FROM afipws_fe_master";
            
            // Pregunto si la afipws_fe_master esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_master (" +
                                "	CbteTipo NUMERIC(2,0) NOT NULL," +
                                "	PtoVta NUMERIC(4,0) NOT NULL," +
                                "	CbteDesde NUMERIC(8,0) NOT NULL," +
                                "	Concepto NUMERIC(1,0) NULL DEFAULT 1," +
                                "	DocTipo NUMERIC(2,0) NULL," +
                                "	DocNro NUMERIC(11,0) NULL," +
                                "	CbteHasta NUMERIC(8,0) NULL," +
                                "	CbteFch DATE NULL," +
                                "	ImpTotal NUMERIC(13,2) NULL DEFAULT 0," +
                                "	ImpTotConc NUMERIC(13,2) NULL DEFAULT 0," +
                                "	ImpNeto NUMERIC(13,2) NULL DEFAULT 0," +
                                "	ImpOpEx NUMERIC(13,2) NULL DEFAULT 0," +
                                "	ImpTrib NUMERIC(13,2) NULL DEFAULT 0," +
                                "	ImpIVA NUMERIC(13,2) NULL DEFAULT 0," +
                                "	FchServDesde DATE NULL," +
                                "	FchServHasta DATE NULL," +
                                "	FchVtoPago DATE NULL," +
                                "	MonId CHAR(3) NULL DEFAULT 'PES'," +
                                "	MonCotiz NUMERIC(13,6) NULL DEFAULT 1," +
                                "	FchProceso datetime NULL," +
                                "	Resultado CHAR(1) NOT NULL DEFAULT 'X'," +
                                "	CAE NUMERIC(14,0) NULL," +
                                "	CAEFchVto DATE NULL," +
                                "	descriTributo VARCHAR(255) NULL," +
                                "	tipo_comp NUMERIC(2,0) NULL," +
                                "	pto_emision NUMERIC(4,0) NULL," +
                                "	v_numero_comprobante NUMERIC(16,0) NULL," +
                                "	PRIMARY KEY ( CbteTipo, PtoVta, CbteDesde )" +
                                ") IN system;" +
                                "COMMENT ON TABLE DBA.afipws_fe_master IS 'facturas - master';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_master.Resultado IS 'Aprobado, Rechazado, Parcial, X (Pendiente)';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_master.tipo_comp IS 'Tipo de comprobante';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_master.pto_emision IS 'Punto de venta';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_master.v_numero_comprobante IS 'Numero comprobante obtenido desde la AFIP';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_master creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_master esta OK!");            
            } 

            // Armo una consulta para validar la tabla de puntos de venta
            sentenciaSQL = "SELECT * FROM afipws_fe_PtoVenta";
            
            // Pregunto si la afipws_fe_PtoVenta esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_PtoVenta (" +
                                "	Nro INTEGER NOT NULL UNIQUE," +
                                "	EmisionTipo VARCHAR(255) NULL," +
                                "	Bloqueado VARCHAR(255) NULL," +
                                "	fechaBaja DATE NULL," +
                                "	PRIMARY KEY ( Nro )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.Nro IS 'Nro del punto de venta';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.EmisionTipo IS 'Tipo de comprobante emitidos';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.Bloqueado IS 'Estado del punto de venta';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.fechaBaja IS 'Fecha de la baja';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_PtoVenta creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_PtoVenta esta OK!");            
            } 
            
            // Armo una consulta para validar la tabla de tipos de comprobantes
            sentenciaSQL = "SELECT * FROM afipws_fe_TipoCbte";
            
            // Pregunto si la afipws_fe_TipoCbte esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TipoCbte (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.id IS 'Tipo de comprobante';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.descripcion IS 'Descripcion del tipo de comprobante';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TipoCbte creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TipoCbte esta OK!");            
            }  
            
            // Armo una consulta para validar la tabla de tipos de conceptos
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposConceptos";
            
            // Pregunto si la afipws_TiposConceptos esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposConceptos (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposConceptos.id IS 'Clave Tipo de Concepto';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposConceptos.descripcion IS 'Descripcion del tipo de concepto';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposConceptos.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposConceptos.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_TiposConceptos creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_TiposConceptos esta OK!");            
            } 
            
            // Armo una consulta para validar la tabla de tipos de documentos
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposDocs";
            
            // Pregunto si la afipws_fe_TiposDocs esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposDocs (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.id IS 'Clave Tipo de documento';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.descripcion IS 'Descripcion del tipo de documento';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposDocs creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposDocs esta OK!");            
            }  

            // Armo una consulta para validar la tabla de tipos de IVA
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposIVA";
            
            // Pregunto si la afipws_fe_TiposIVA esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposIVA (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.id IS 'Clave Tipo de IVA';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.descripcion IS 'Descripcion del tipo de IVA';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposIVA creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposIVA esta OK!");            
            }            
            
            // Armo una consulta para validar la tabla de tipos de monedas
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposMoneda";
            
            // Pregunto si la afipws_fe_TiposMoneda esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposMoneda (" +
                                "	id VARCHAR(10) NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.id IS 'Clave Tipo de Moneda';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.descripcion IS 'Descripcion del tipo de Moneda';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposMoneda creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposMoneda esta OK!");            
            }  

            // Armo una consulta para validar la tabla de tipos opciones
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposOpcionales";
            
            // Pregunto si la afipws_fe_TiposOpcionales esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposOpcionales (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposOpcionales.id IS 'Clave Tipo de identificador';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposOpcionales.descripcion IS 'Descripcion del tipo de identificador';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposOpcionales.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposOpcionales.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposOpcionales creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposOpcionales esta OK!");            
            }              
            
            // Armo una consulta para validar la tabla de tipos Paises
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposPaises";
            
            // Pregunto si la afipws_fe_TiposPaises esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposPaises (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposPaises.id IS 'Clave Tipo de Paises';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposPaises.descripcion IS 'Descripcion del tipo de pais';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposPaises creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposPaises esta OK!");            
            }       
            
            // Armo una consulta para validar la tabla de tipos de tributos
            sentenciaSQL = "SELECT * FROM afipws_fe_TiposTributos";
            
            // Pregunto si la afipws_fe_TiposDocs esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_TiposTributos (" +
                                "	id INTEGER NOT NULL," +
                                "	descripcion VARCHAR(255) NULL," +
                                "	fechaDesde DATE NULL," +
                                "	fechaHasta DATE NULL," +
                                "	PRIMARY KEY ( id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.id IS 'Clave Tipo de la condicion de IVA';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.descripcion IS 'Descripcion de la condicion de IVA';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.fechaDesde IS 'fecha de vigencia desde';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.fechaHasta IS 'fecha de vigencia hasta';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposTributos creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_TiposTributos esta OK!");            
            }
            
            // Armo una consulta para validar la tabla de tipos de tributos
            sentenciaSQL = "SELECT * FROM afipws_fe_CbtesAsociados";
            
            // Pregunto si la afipws_fe_TiposDocs esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_CbtesAsociados (" +
                                "	CbteTipo NUMERIC(3,0) NOT NULL," +
                                "	PtoVta NUMERIC(5,0) NOT NULL," +
                                "	CbteNro NUMERIC(8,0) NOT NULL," +
                                "	Id INTEGER NOT NULL," +
                                "	CTipoAsoc NUMERIC(3,0) NULL," +
                                "	PVtaAsoc NUMERIC(5,0) NULL," +
                                "	CNroAsoc NUMERIC(8,0) NULL," +
                                "       CuitAsoc NUMERIC(11,0) NULL," +
                                "	PRIMARY KEY ( CbteTipo, PtoVta , CbteNro , Id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_CbtesAsociados.id IS 'Items del comprobante';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_CbtesAsociados IS 'Comprobantes asociados del comprobante a autorizar';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_CbtesAsociados creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_CbtesAsociados esta OK!");            
            }
            
            // Armo una consulta para validar la tabla de tipos de tributos
            sentenciaSQL = "SELECT * FROM afipws_fe_CbtesAsociados_periodo";
            
            // Pregunto si la afipws_fe_TiposDocs esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_CbtesAsociados_periodo (" +
                                "	CbteTipo NUMERIC(3,0) NOT NULL," +
                                "	PtoVta NUMERIC(5,0) NOT NULL," +
                                "	CbteNro NUMERIC(8,0) NOT NULL," +
                                "	FechaDesde DATE NULL," +
                                "	FechaHasta DATE NULL," +
                                "	PRIMARY KEY ( CbteTipo, PtoVta , CbteNro )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_CbtesAsociados IS 'Periodo asociado del comprobante a autorizar';" ;

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_CbtesAsociados_periodo creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_CbtesAsociados_periodo esta OK!");            
            }
            
            // Armo una consulta para validar la tabla de tipos de tributos
            sentenciaSQL = "SELECT * FROM afipws_fe_CbtesAsociados_periodo";
            
            // Pregunto si la afipws_fe_TiposDocs esta ok
            if(!this.validarEstructura(sentenciaSQL)){
                
                // Armo la consulta 
                sentenciaSQL = "CREATE TABLE DBA.afipws_fe_Opcionales (" +
                                "	CbteTipo NUMERIC(3,0) NOT NULL," +
                                "	PtoVta NUMERIC(5,0) NOT NULL," +
                                "	CbteNro NUMERIC(8,0) NOT NULL," +
                                "	Id INTEGER NOT NULL," +
                                "	valor VARCHAR(250) NULL," +
                                "	PRIMARY KEY ( CbteTipo, PtoVta , CbteNro, Id )" +
                                ") IN system;" +
                                "COMMENT ON COLUMN DBA.afipws_fe_CbtesAsociados.id IS 'Items del comprobante';" +
                                "COMMENT ON COLUMN DBA.afipws_fe_CbtesAsociados IS 'Comprobantes asociados del comprobante a autorizar';";

                // Instancio el objeto que me permite ejecutar consultas y le completo los parametros
                PreparedStatement sentencia = crearSentencia(sentenciaSQL);        

                // Ejecuto la instruccion
                actualizar(sentencia);                     
                
                // Genero salida en el log
                log.info("La tabla afipws_fe_Opcionales creada con exito.");
                
            }  else {
        
                // Genero salida en el log
                log.info("La tabla afipws_fe_Opcionales esta OK!");            
            }
            
        } catch (Exception e) {
            
            // Genero salida en el log
            log.fatal("Error actualizando estructura de la base de datos: " + e.getMessage());
            
        }
    }
    
    /**
     * Este metodo ejecuta una instruccion SQL y devuelve el resultado
     * 
     * @return 
     */
    private boolean validarEstructura(String sentenciaSQL){
        
        // Declaro variable auxiliar
        boolean bandera = false;
        
        // Capturo posibles errores
        try {
            
            // Instancio el objeto que me permite ejecutar consultas
            PreparedStatement sentencia = crearSentencia(sentenciaSQL);
            
            // Ejecuto la consulta
            consultar(sentencia);
            
            // Si salio todo OK
            bandera = true;
            
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        } 
        
        
        // Devuelvo el resultado
        return bandera;        

    }
    
  
}

