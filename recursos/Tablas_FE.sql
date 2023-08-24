/* CREO LOS CAMPOS QUE NECESITO PARA LA VISTA */
ALTER TABLE "DBA"."fac_ventas" ADD "autorizado_sn" char(1) NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."autorizado_sn" IS 'S= Comprobante Aprobado, N=Comprobante no aprobado';
ALTER TABLE "DBA"."fac_ventas" ADD "tipo_autorizado" numeric(2,0) NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."tipo_autorizado" IS 'Tipo de comprobante';
ALTER TABLE "DBA"."fac_ventas" ADD "pto_autorizado" numeric(4,0) NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."pto_autorizado" IS 'Punto de venta autorizado';
ALTER TABLE "DBA"."fac_ventas" ADD "nro_autorizado" numeric(16,0) NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."nro_autorizado" IS 'Numero de Cbte autorizado';
ALTER TABLE "DBA"."fac_ventas" ADD "fe_autorizado" date NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."fe_autorizado" IS 'Fecha de autorizacion';
ALTER TABLE "DBA"."fac_ventas" ADD "CAE" numeric(14,6) NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."CAE" IS 'Numero de CAE';
ALTER TABLE "DBA"."fac_ventas" ADD "fe_Vto" date NULL;
COMMENT ON COLUMN "DBA"."fac_ventas"."fe_Vto" IS 'Fecha vencimiento CAE';

/* CAMPOS EXTRA QUE NO SIEMPRE SE NECESITAN */
ALTER TABLE "DBA"."fac_ventas" ADD "v_perce2459_1" numeric(12,3) NULL;
ALTER TABLE "DBA"."fac_ventas" ADD "v_perce2459_105" numeric(12,3) NULL;

/* CREO LAS TABLAS QUE NECESITO PARA LA FACTURACION ELECTRONICA */
CREATE TABLE "DBA"."afipws_fe_master" (
	"CbteTipo" numeric(2,0) NOT NULL,
	"PtoVta" numeric(4,0) NOT NULL,
	"CbteDesde" numeric(8,0) NOT NULL,
	"Concepto" numeric(1,0) NULL DEFAULT 1,
	"DocTipo" numeric(2,0) NULL,
	"DocNro" numeric(11,0) NULL,
	"CbteHasta" numeric(8,0) NULL,
	"CbteFch" date NULL,
	"ImpTotal" numeric(13,2) NULL DEFAULT 0,
	"ImpTotConc" numeric(13,2) NULL DEFAULT 0,
	"ImpNeto" numeric(13,2) NULL DEFAULT 0,
	"ImpOpEx" numeric(13,2) NULL DEFAULT 0,
	"ImpTrib" numeric(13,2) NULL DEFAULT 0,
	"ImpIVA" numeric(13,2) NULL DEFAULT 0,
	"FchServDesde" date NULL,
	"FchServHasta" date NULL,
	"FchVtoPago" date NULL,
	"MonId" char(3) NULL DEFAULT 'PES',
	"MonCotiz" numeric(13,6) NULL DEFAULT 1,
	"FchProceso" "datetime" NULL,
	"Resultado" char(1) NOT NULL DEFAULT 'X',
	"CAE" numeric(14,0) NULL,
	"CAEFchVto" date NULL,
	"descriTributo" varchar(255) NULL,
	"tipo_comp" numeric(2,0) NULL,
	"pto_emision" numeric(4,0) NULL,
	"v_numero_comprobante" numeric(16,0) NULL,
	PRIMARY KEY ( "CbteTipo", "PtoVta", "CbteDesde" )
);
COMMENT ON TABLE "DBA"."afipws_fe_master" IS 'facturas - master';
COMMENT ON COLUMN "DBA"."afipws_fe_master"."Resultado" IS 'Aprobado, Rechazado, Parcial, X (Pendiente)';
COMMENT ON COLUMN "DBA"."afipws_fe_master"."tipo_comp" IS 'Tipo de comprobante';
COMMENT ON COLUMN "DBA"."afipws_fe_master"."pto_emision" IS 'Punto de venta';
COMMENT ON COLUMN "DBA"."afipws_fe_master"."v_numero_comprobante" IS 'Numero comprobante obtenido desde la AFIP';

CREATE TABLE "DBA"."afipws_fe_detalle" (
	"CbteTipo" numeric(2,0) NOT NULL,
	"PtoVta" numeric(4,0) NOT NULL,
	"CbteDesde" numeric(8,0) NOT NULL,
	"TipoDetalle" numeric(1,0) NOT NULL,
	"Pase" numeric(12,0) NOT NULL DEFAULT autoincrement,
	"Id" varchar(5) NULL,
	"AsocPtoVta" numeric(4,0) NULL DEFAULT 0,
	"AsocNroCbte" numeric(8,0) NULL DEFAULT 0,
	"Valor" varchar(255) NULL,
	"BaseImp" numeric(13,2) NULL DEFAULT 0,
	"Alic" numeric(5,2) NULL DEFAULT 0,
	"Importe" numeric(13,2) NULL DEFAULT 0,
	"descriTributo" varchar(255) NULL,
	"tipo_comp" numeric(2,0) NULL,
	"pto_emision" numeric(4,0) NULL,
	"v_numero_comprobante" numeric(16,0) NULL,
	PRIMARY KEY ( "CbteTipo", "PtoVta", "CbteDesde", "TipoDetalle", "Pase" )
);
COMMENT ON TABLE "DBA"."afipws_fe_detalle" IS 'facturas - detalle (tributos, iva, observa)';
COMMENT ON COLUMN "DBA"."afipws_fe_detalle"."TipoDetalle" IS '1 CbteAsoc, 2 Tributos, 3 IVA, 4 Opcionales, 5 Observaciones';


CREATE TABLE "DBA"."afipws_fe_log" (
	"CbteTipo" numeric(2,0) NOT NULL,
	"PtoVta" numeric(4,0) NOT NULL,
	"CbteDesde" numeric(12,0) NOT NULL,
	"FechayHora" "datetime" NOT NULL DEFAULT current timestamp,
	"mensaje" "text" NULL,
	"CbteHasta" numeric(12,0) NOT NULL,
	"tipo_comp" numeric(2,0) NULL,
	"pto_autorizado" numeric(4,0) NULL,
	"v_numero_comprobante" numeric(16,0) NULL,
	PRIMARY KEY ( "CbteTipo", "PtoVta", "CbteDesde", "FechayHora", "CbteHasta" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_log"."tipo_comp" IS 'Tipo de comprobante';
COMMENT ON COLUMN "DBA"."afipws_fe_log"."pto_autorizado" IS 'Punto de venta';

CREATE TABLE "DBA"."afipws_fe_parametros" (
	"Parametro" varchar(30) NOT NULL,
	"CbteTipo" numeric(2,0) NOT NULL,
	"PtoVta" numeric(4,0) NOT NULL,
	"Valor" varchar(255) NOT NULL,
	PRIMARY KEY ( "Parametro", "CbteTipo", "PtoVta" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_parametros"."Parametro" IS 'Nombre del metodo del AFIP que se ejecuta para obtener dicho parametro';
COMMENT ON COLUMN "DBA"."afipws_fe_parametros"."CbteTipo" IS 'Tipo de comprobante para el cual se ejecuta el metodo';
COMMENT ON COLUMN "DBA"."afipws_fe_parametros"."PtoVta" IS 'Punto de venta para el cual se ejecuta el metodo';
COMMENT ON COLUMN "DBA"."afipws_fe_parametros"."Valor" IS 'Valor devuelto por la AFIP';

CREATE TABLE "DBA"."afipws_fe_PtoVenta" (
	"Nro" integer NOT NULL UNIQUE,
	"EmisionTipo" varchar(255) NULL,
	"Bloqueado" varchar(255) NULL,
	"fechaBaja" date NULL,
	PRIMARY KEY ( "Nro" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_PtoVenta"."Nro" IS 'Nro del punto de venta';
COMMENT ON COLUMN "DBA"."afipws_fe_PtoVenta"."EmisionTipo" IS 'Tipo de comprobante emitidos';
COMMENT ON COLUMN "DBA"."afipws_fe_PtoVenta"."Bloqueado" IS 'Estado del punto de venta';
COMMENT ON COLUMN "DBA"."afipws_fe_PtoVenta"."fechaBaja" IS 'Fecha de la baja';

CREATE TABLE "DBA"."afipws_fe_TipoCbte" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TipoCbte"."id" IS 'Tipo de comprobante';
COMMENT ON COLUMN "DBA"."afipws_fe_TipoCbte"."descripcion" IS 'Descripcion del tipo de comprobante';
COMMENT ON COLUMN "DBA"."afipws_fe_TipoCbte"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TipoCbte"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposConceptos" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposConceptos"."id" IS 'Clave Tipo de Concepto';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposConceptos"."descripcion" IS 'Descripcion del tipo de concepto';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposConceptos"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposConceptos"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposDocs" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposDocs"."id" IS 'Clave Tipo de documento';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposDocs"."descripcion" IS 'Descripcion del tipo de documento';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposDocs"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposDocs"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposIVA" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposIVA"."id" IS 'Clave Tipo de IVA';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposIVA"."descripcion" IS 'Descripcion del tipo de IVA';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposIVA"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposIVA"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposMoneda" (
	"id" varchar(10) NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposMoneda"."id" IS 'Clave Tipo de Moneda';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposMoneda"."descripcion" IS 'Descripcion del tipo de Moneda';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposMoneda"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposMoneda"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposOpcionales" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposOpcionales"."id" IS 'Clave Tipo de identificador';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposOpcionales"."descripcion" IS 'Descripcion del tipo de identificador';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposOpcionales"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposOpcionales"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_TiposPaises" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposPaises"."id" IS 'Clave Tipo de Paises';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposPaises"."descripcion" IS 'Descripcion del tipo de pais';

CREATE TABLE "DBA"."afipws_fe_TiposTributos" (
	"id" integer NOT NULL,
	"descripcion" varchar(255) NULL,
	"fechaDesde" date NULL,
	"fechaHasta" date NULL,
	PRIMARY KEY ( "id" )
);
COMMENT ON COLUMN "DBA"."afipws_fe_TiposTributos"."id" IS 'Clave Tipo de Tributos';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposTributos"."descripcion" IS 'Descripcion del tipo de tributo';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposTributos"."fechaDesde" IS 'fecha de vigencia desde';
COMMENT ON COLUMN "DBA"."afipws_fe_TiposTributos"."fechaHasta" IS 'fecha de vigencia hasta';

CREATE TABLE "DBA"."afipws_fe_wsaa_TA" (
	"uid" unsigned int NOT NULL,
	"genTime" "datetime" NULL,
	"expTime" "datetime" NULL,
	"token" "text" NULL,
	"sign" "text" NULL,
	"source" "text" NULL,
	"destination" "text" NULL,
	"operador_codigo" varchar(5) NULL,
	"activo" char(1) NULL DEFAULT 'S',
	"TestSN" char(1) NULL DEFAULT 'S',
	"ultActualizacion" "datetime" NOT NULL DEFAULT current timestamp,
	PRIMARY KEY ( "uid" )
);
COMMENT ON TABLE "DBA"."afipws_fe_wsaa_TA" IS 'AFIP WS - TA (Tickets de Acceso)';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."uid" IS 'UniqueID';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."genTime" IS 'Generación';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."expTime" IS 'Expiración';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."token" IS 'Token';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."sign" IS 'Signature';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."source" IS 'Source';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."destination" IS 'Destination';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."operador_codigo" IS 'Operador que solicitó el TA';
COMMENT ON COLUMN "DBA"."afipws_fe_wsaa_TA"."activo" IS 'TA activo';

CREATE TABLE "DBA"."afipws_wsaa_TRA_java" (
	"TestSN" char(1) NOT NULL DEFAULT 'S',
	"trustStoreFile" varchar(100) NULL,
	"trustStorePwd" varchar(20) NULL,
	"keyStoreFile" varchar(100) NULL,
	"keyStoreSigner" varchar(20) NULL,
	"keyStorePwd" varchar(20) NULL,
	"keyStoreTimeout" numeric(10,0) NULL DEFAULT 3600000,
	"destino" varchar(100) NULL,
	"servicio" varchar(20) NULL,
	"endPoint" "text" NULL,
	"ultActualizacion" "datetime" NULL DEFAULT current timestamp,
	PRIMARY KEY ( "TestSN" )
);
COMMENT ON TABLE "DBA"."afipws_wsaa_TRA_java" IS 'AFIP WS  - parametros para solicitar TA (java)';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."TestSN" IS 'S=test, N=producción';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."trustStoreFile" IS 'trustStore path relativo ';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."trustStorePwd" IS 'trustStore clave';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."keyStoreFile" IS 'keyStore path relativo';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."keyStoreSigner" IS 'keyStore signer (1)';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."keyStorePwd" IS 'keyStore clave';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."keyStoreTimeout" IS 'keyStore Timeout';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."destino" IS 'dstn';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."servicio" IS 'nombre ws a usar';
COMMENT ON COLUMN "DBA"."afipws_wsaa_TRA_java"."endPoint" IS 'url';

CREATE TABLE "DBA"."afipws_wsfe" (
	"servicio" varchar(30) NULL,
	"url" "text" NULL,
	"TestSN" char(1) NOT NULL DEFAULT 'S',
	"ultActualizacion" "datetime" NOT NULL DEFAULT current timestamp,
	PRIMARY KEY ( "TestSN" )
);
COMMENT ON TABLE "DBA"."afipws_wsfe" IS 'AFIP WS - WSCTG Parámetros';
COMMENT ON COLUMN "DBA"."afipws_wsfe"."servicio" IS 'Nombre del Servicio al que apunta';
COMMENT ON COLUMN "DBA"."afipws_wsfe"."url" IS 'URL Endpoint';
COMMENT ON COLUMN "DBA"."afipws_wsfe"."TestSN" IS 'es Testing ?';
COMMENT ON COLUMN "DBA"."afipws_wsfe"."ultActualizacion" IS 'Ultima Edicion';

/* INSERTO LOS REGISTROS QUE NECESITO EN LA TABLA */
INSERT INTO "DBA"."afipws_wsfe" (servicio,url,TestSN,ultActualizacion) VALUES ('wsfe','https://wswhomo.afip.gov.ar/wsfev1/service.asmx?wsdl','S','2015-04-30 12:20:53.420');
INSERT INTO "DBA"."afipws_wsfe" (servicio,url,TestSN,ultActualizacion) VALUES ('wsfe','https://servicios1.afip.gov.ar/wsfev1/service.asmx?WSDL','N','2015-04-30 12:20:53.420');


/* CREO LA VISTA DE FACTURACION ELECTRONICA */
CREATE VIEW "DBA"."v_fe_master" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    fac_ventas.v_numero_cuit as DocNro,
    fac_ventas.v_fecha_operacion as CbteFch,
    //    sum(fac_ventas.v_bonificacion) as ImpTotal,
    sum((fac_ventas.v_precio_unitario*fac_ventas.v_cantidad)+fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as ImpTotal,
    //sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) as ImpNeto,
    //if(v_iva_ri <> 0 or v_iva_rni <> 0) then sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) else 0 endif as ImpNeto,
    sum(if fac_ventas.v_iva_ri <> 0 or fac_ventas.v_iva_rni <> 0 then fac_ventas.v_precio_unitario*fac_ventas.v_cantidad else 0 endif) as ImpNeto,
    //sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) as ImpEx,
    //if(v_iva_ri = 0 and v_iva_rni = 0) then sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) else 0 endif as ImpEx,
    sum(isnull(fac_ventas.v_percepcion1,0)+isnull(fac_ventas.v_percepcion2,0)+isnull(fac_ventas.v_impuesto_interno,0)+isnull(fac_ventas.v_perce2459_1,0)+isnull(fac_ventas.v_perce2459_105,0)) as ImpTrib,
    sum(if fac_ventas.v_iva_ri = 0 and fac_ventas.v_iva_rni = 0 then fac_ventas.v_precio_unitario*fac_ventas.v_cantidad else 0 endif) as ImpEx,
    sum(fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as ImpIVA,'PES' as MonID,
    1 as MonCotiz,
    fac_ventas.v_fecha_vencimiento as FchVtoPago from
    DBA.fac_ventas where
    fac_ventas.v_numero_mov <> 0 //and cbtedesde = 3877
    group by CbteTipo,PtoVta,CbteDesde,DocNro,CbteFch,FchVtoPago;

CREATE PROCEDURE "DBA"."afipws_pasaje_comprobante"(in cbte_Tipo numeric(2),in pto_Vta numeric(4),in cbte_Desde numeric(8))
begin
  /******************************************/
  /* ACA DECLARO LAS VARIABLES QUE NECESITO */
  /******************************************/
  /* Cursor para la tabla master */
  declare finCursor exception for sqlstate value '02000';
  declare cursorDatos dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,DocNro,CbteFch,ImpTotal,ImpNeto,ImpTrib,
      ImpEx,ImpIVA,MonID,MonCotiz,FchVtoPago from v_fe_master where CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;
  /* Cursor para la tabla IVA */
  declare cursorDatos2 dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe from v_fe_detalle_iva where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe from v_fe_detalle_iva_exento where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;
  /* Cursor para la tabla tributos */
  declare cursorDatos3 dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_nac where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_prov where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_impinterno where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;
  /******************************/
  /* VARIABLES PARA EL CURSOR 1 */
  /******************************/
  declare cbtetipo_temp numeric(3);declare ptoVta_temp numeric(4);declare CbteDesde_temp numeric(8);
  declare docNro_temp numeric(11);declare CbteFch_temp date;declare ImpTotal_temp numeric(20,3);
  declare ImpNeto_temp numeric(20,3);declare ImpTrib_temp numeric(24,3);declare ImpEx_temp numeric(30,6);
  declare ImpIva_temp numeric(21,3);declare MonID_temp varchar(3);declare MonCotiz_temp smallint;declare FchVtoPago_temp date;
  /****************************/
  /**********************************/
  /* VARIABLES PARA EL CURSOR 2 y 3 */
  /**********************************/
  declare cbtetipo_temp2 numeric(3);declare ptoVta_temp2 numeric(4);declare CbteDesde_temp2 numeric(8);
  declare id_temp smallint;declare baseImp_temp numeric(20,3);declare Importe_temp numeric(21,3);
  declare Alic_temp2 numeric(3,1);declare DescTrib_temp varchar(255);
  /****************************/
  // Abro el cursor y lo recorro para ir generando los registros en la tabla afip_fe_master
  open cursorDatos;
  salgoloop: loop
    // Muevo los datos al cursor    
    fetch next cursorDatos into cbtetipo_temp,ptoVta_temp,CbteDesde_temp,docNro_temp,CbteFch_temp,
      ImpTotal_temp,ImpNeto_temp,ImpTrib_temp,ImpEx_temp,ImpIva_temp,MonID_temp,MonCotiz_temp,FchVtoPago_temp;
    // Valido final del cursor
    if sqlstate = finCursor then
      leave salgoloop
    end if;
    /************************************************************************************************************/
    /*                                             ACLARACIONES                                                 */
    /************************************************************************************************************/
    /* Concepto: Admite los siguientes valores: 1-Productos, 2-Servicios, 3-Productos y Servicios               */
    /* DocTipo: Si la longitud de DocNro es igual a 11 es CUIT si no es DNI                                     */
    /* ImpTotConc: Importe neto no gravado, Debe ser menor o igual a Importe total y no puede ser menor a cero. */
    /* ImpOpEx: Importe exento, debe ser menor o igual a Importe total y no puede ser menor a cero.             */
    /* ImpTrib: Suma de los importes del array de tributos (Todo lo que no es IVA)                              */
    /* FechaServDesde: Fecha inicio del servicio, por ahora le fuerzo la fecha del comprobante                  */
    /* FechaServHasta: Fecha fin del servicio, por ahora le fuerzo la fecha del comprobante                     */
    /************************************************************************************************************/
    // Inserto el registro encontrado en la tabla afipes_fe_master
    insert into afipws_fe_master( CbteTipo,PtoVta,CbteDesde,Concepto,DocTipo,DocNro,CbteHasta,CbteFch,ImpTotal,ImpTotConc,
      ImpNeto,ImpOpEx,ImpTrib,ImpIva,FchServDesde,FchServHasta,FchVtoPago,MonId,MonCotiz,FchProceso,Resultado,CAE,CAEFchVto) values( 
      cbtetipo_temp,ptoVta_temp,CbteDesde_temp,3,if(LENGTH(DocNro_temp) = 11) then 80 else 96 endif, /*concepto*/ /*docTipo*/
      docNro_temp,CbteDesde_temp,CbteFch_temp,ImpTotal_temp,0,ImpNeto_temp,ImpEx_temp,ImpTrib_temp,ImpIva_temp, /*ImpTotConc*/
      CbteFch_temp,CbteFch_temp,FchVtoPago_temp,MonID_temp,MonCotiz_temp,null,'','', /*FechaServDesde*/ /*FechServHasta*/ /*FchProceso*/
      null)  /*Resultado*/ /*CAE*/ /*CAEFchVto*/
  end loop salgoloop;
  // Fin del loop
  // Abro el cursor y lo recorro para ir generando los registros en la tabla afip_fe_detalle
  open cursorDatos2;
  salgoloop2: loop
    // Muevo los datos al cursor 2    
    fetch next cursorDatos2 into cbtetipo_temp2,ptoVta_temp2,CbteDesde_temp2,id_temp,baseImp_temp,Alic_temp2,Importe_temp;
    // Valido final del cursor
    if sqlstate = finCursor then
      leave salgoloop2
    end if;
    /************************************************************************************************************/
    /*                                             ACLARACIONES                                                 */
    /************************************************************************************************************/
    /* TipoDetalle: Admite los siguientes valores: 1 CbteAsoc, 2 Tributos, 3 IVA, 4 Opcionales, 5 Observaciones */
    /************************************************************************************************************/
    // Inserto el registro encontrado en la tabla afipes_fe_detalle
    insert into afipws_fe_detalle( CbteTipo,PtoVta,CbteDesde,TipoDetalle,Id,AsocPtoVta,AsocNroCbte,Valor,BaseImp,Alic,Importe,descriTributo) values( 
      CbteTipo_temp2,PtoVta_temp2,CbteDesde_temp2,3,Id_temp,0,0,null,baseImp_temp, /*TipoDetalle=IVA*/ /*AsocPtoVenta*/ /*AsocNroCbte*/ /*Valor*/
      Alic_temp2,importe_temp,if(id_temp = 4) then 'IVA 10.5%' else 'IVA 21%' endif)  /*descriTributo*/
  end loop salgoloop2;
  // Fin del loop
  // Abro el cursor y lo recorro para ir generando los registros en la tabla afip_fe_detalle
  open cursorDatos3;
  salgoloop3: loop
    // Muevo los datos al cursor 3    
    fetch next cursorDatos3 into cbtetipo_temp2,ptoVta_temp2,CbteDesde_temp2,id_temp,baseImp_temp,Alic_temp2,Importe_temp,DescTrib_temp;
    // Valido final del cursor
    if sqlstate = finCursor then
      leave salgoloop3
    end if;
    /************************************************************************************************************/
    /*                                             ACLARACIONES                                                 */
    /************************************************************************************************************/
    /* TipoDetalle: Admite los siguientes valores: 1 CbteAsoc, 2 Tributos, 3 IVA, 4 Opcionales, 5 Observaciones */
    /************************************************************************************************************/
    // Inserto el registro encontrado en la tabla afipes_fe_detalle
    insert into afipws_fe_detalle( CbteTipo,PtoVta,CbteDesde,TipoDetalle,Id,AsocPtoVta,AsocNroCbte,Valor,BaseImp,Alic,Importe,descriTributo) values( 
      CbteTipo_temp2,PtoVta_temp2,CbteDesde_temp2,2,Id_temp,0,0,null,baseImp_temp, /*TipoDetalle= Tributos*/ /*AsocPtoVenta*/ /*AsocNroCbte*/ /*Valor*/
      Alic_temp2,importe_temp,DescTrib_temp) 
  end loop salgoloop3
// Fin del loop
end

