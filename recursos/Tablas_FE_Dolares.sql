ALTER TABLE DBA.fac_articulos ADD art_codigo_unidad_afip NUMERIC(3,0) NULL DEFAULT 0;
COMMENT ON COLUMN DBA.fac_articulos.art_codigo_unidad_afip IS 'Codigo de unidad Web services AFIP';
ALTER TABLE DBA.fac_articulos ADD art_unidad_referencia_afip NUMERIC(12,0) NULL;
COMMENT ON COLUMN DBA.fac_articulos.art_unidad_referencia_afip IS 'Unidad de referencia en GS1';


ALTER TABLE DBA.fac_iva ADD iva_afip_inscripto NUMERIC(2,0) NULL DEFAULT 5;
COMMENT ON COLUMN DBA.fac_iva.iva_afip_inscripto IS 'Codigo de la alicuota de IVA para la AFIP';
ALTER TABLE DBA.fac_iva ADD iva_afip_noinscripto NUMERIC(2,0) NULL DEFAULT 4;
COMMENT ON COLUMN DBA.fac_iva.iva_afip_noinscripto IS 'Codigo de la alicuota de IVA para la AFIP';
ALTER TABLE DBA.fac_iva ADD iva_afip_exento NUMERIC(2,0) NULL DEFAULT 2;
COMMENT ON COLUMN DBA.fac_iva.iva_afip_exento IS 'Codigo de la alicuota de IVA para la AFIP';
ALTER TABLE DBA.fac_iva ADD iva_afip_servicio NUMERIC(2,0) NULL DEFAULT 6;
COMMENT ON COLUMN DBA.fac_iva.iva_afip_servicio IS 'Codigo de la alicuota de IVA para la AFIP';

/* ---------------------- SI TIENE FAC_VENTAS_DOLAR ---------------------------------*/
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "autorizado_sn" varchar(1) NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "tipo_autorizado" numeric(2,0) NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "pto_autorizado" numeric(4,0) NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "nro_autorizado" numeric(16,0) NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "fe_autorizado" date NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "CAE" numeric(14,0) NULL;
ALTER TABLE "DBA"."fac_ventas_dolar" ADD "fe_vto" date NULL;

/* ---------------------- SI NO TIENE FAC_VENTAS_DOLAR ---------------------------------*/
CREATE TABLE "DBA"."fac_ventas_dolar" (
	"v_codigo" varchar(14) NOT NULL,
	"v_tipo_operacion" numeric(3,0) NOT NULL,
	"v_fecha_operacion" date NOT NULL,
	"v_tipo_comprobante" numeric(3,0) NOT NULL,
	"v_numero_comprobante" numeric(12,0) NOT NULL,
	"v_numero_ctacte" numeric(5,0) NOT NULL,
	"v_numero_mov" numeric(2,0) NOT NULL,
	"v_forma_pago" numeric(3,0) NULL,
	"v_nombre" varchar(35) NULL,
	"v_numero_cuit" numeric(11,0) NULL,
	"v_cantidad" numeric(12,3) NULL,
	"v_descripcion" varchar(35) NULL,
	"v_precio_unitario" numeric(12,3) NULL,
	"v_retencion1" numeric(12,3) NULL,
	"v_retencion2" numeric(12,3) NULL,
	"v_percepcion1" numeric(12,3) NULL,
	"v_percepcion2" numeric(12,3) NULL,
	"v_impuesto_interno" numeric(12,3) NULL,
	"v_otro_impuesto" numeric(12,3) NULL,
	"v_iva_ri" numeric(12,3) NULL,
	"v_iva_rni" numeric(12,3) NULL,
	"v_descuento" numeric(12,3) NULL,
	"v_bonificacion" numeric(12,3) NULL,
	"v_codigo_relacion" numeric(5,0) NULL,
	"v_fecha_vencimiento" date NULL,
	"v_facturado_sn" char(1) NULL,
	"v_codigo_operador" varchar(5) NULL,
	"v_hora" time NULL,
	"v_condicion_iva" numeric(2,0) NULL,
	"pto_numero" numeric(4,0) NOT NULL,
	"v_tipo_comprobante_asoc" numeric(3,0) NULL,
	"v_numero_comprobante_asoc" numeric(12,0) NULL,
	"v_deposito" integer NULL,
	"v_contabil" char(1) NULL,
	"v_cuotas" numeric(2,0) NULL,
	"v_cuotas_interes" numeric(5,2) NULL,
	"v_dolar" numeric(12,2) NULL,
	"v_perce2459_1" numeric(12,3) NULL,
	"v_perce2459_105" numeric(12,3) NULL,
	"v_canje_sn" varchar(1) NULL,
	"autorizado_sn" varchar(1) NULL,
	"tipo_autorizado" numeric(2,0) NULL,
	"pto_autorizado" numeric(4,0) NULL,
	"fe_autorizado" date NULL,
	"CAE" numeric(14,0) NULL,
	"fe_vto" date NULL,
	"nro_autorizado" numeric(16,0) NULL,
	PRIMARY KEY ( "v_codigo", "v_tipo_operacion", "v_fecha_operacion", "v_tipo_comprobante", "v_numero_comprobante", "v_numero_ctacte", "v_numero_mov", "pto_numero" )
);


CREATE TABLE DBA.afipws_fe_CondicionesIVA (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NULL,
	PRIMARY KEY ( id )
) IN system;


CREATE TABLE DBA.afipws_fe_TiposIVA (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NULL,
	fechaDesde DATE NULL,
	fechaHasta DATE NULL,
	PRIMARY KEY ( id )
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.id IS 'Clave Tipo de IVA';
COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.descripcion IS 'Descripcion del tipo de IVA';
COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.fechaDesde IS 'fecha de vigencia desde';
COMMENT ON COLUMN DBA.afipws_fe_TiposIVA.fechaHasta IS 'fecha de vigencia hasta';


CREATE TABLE DBA.afipws_fe_TipoCbte (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NULL,
	fechaDesde DATE NULL,
	fechaHasta DATE NULL,
	PRIMARY KEY ( id )
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.id IS 'Tipo de comprobante';
COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.descripcion IS 'Descripcion del tipo de comprobante';
COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.fechaDesde IS 'fecha de vigencia desde';
COMMENT ON COLUMN DBA.afipws_fe_TipoCbte.fechaHasta IS 'fecha de vigencia hasta';


CREATE TABLE DBA.afipws_fe_TiposMoneda (
	id VARCHAR(10) NOT NULL,
	descripcion VARCHAR(255) NULL,
	fechaDesde DATE NULL,
	fechaHasta DATE NULL
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.id IS 'Clave Tipo de Moneda';
COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.descripcion IS 'Descripcion del tipo de Moneda';
COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.fechaDesde IS 'fecha de vigencia desde';
COMMENT ON COLUMN DBA.afipws_fe_TiposMoneda.fechaHasta IS 'fecha de vigencia hasta';


CREATE TABLE DBA.afipws_fe_TiposUnidadMedida (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	PRIMARY KEY ( id )
) IN system;

CREATE TABLE DBA.afipws_fe_TiposDocs (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NULL,
	fechaDesde DATE NULL,
	fechaHasta DATE NULL,
	PRIMARY KEY ( id )
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.id IS 'Clave Tipo de documento';
COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.descripcion IS 'Descripcion del tipo de documento';
COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.fechaDesde IS 'fecha de vigencia desde';
COMMENT ON COLUMN DBA.afipws_fe_TiposDocs.fechaHasta IS 'fecha de vigencia hasta';


CREATE TABLE DBA.afipws_fe_TiposTributos (
	id INTEGER NOT NULL,
	descripcion VARCHAR(255) NULL,
	fechaDesde DATE NULL,
	fechaHasta DATE NULL,
	PRIMARY KEY ( id )
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.id IS 'Clave Tipo de la condicion de IVA';
COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.descripcion IS 'Descripcion de la condicion de IVA';
COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.fechaDesde IS 'fecha de vigencia desde';
COMMENT ON COLUMN DBA.afipws_fe_TiposTributos.fechaHasta IS 'fecha de vigencia hasta';

CREATE TABLE DBA.afipws_fe_PtoVenta (
	Nro INTEGER NOT NULL UNIQUE,
	EmisionTipo VARCHAR(255) NULL,
	Bloqueado VARCHAR(255) NULL,
	fechaBaja DATE NULL,
	PRIMARY KEY ( Nro )
) IN system;
COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.Nro IS 'Nro del punto de venta';
COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.EmisionTipo IS 'Tipo de comprobante emitidos';
COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.Bloqueado IS 'Estado del punto de venta';
COMMENT ON COLUMN DBA.afipws_fe_PtoVenta.fechaBaja IS 'Fecha de la baja';


CREATE TABLE DBA.afipws_fe_wsaa_TA (
	uid UNSIGNED INT NOT NULL,
	genTime datetime NULL,
	expTime datetime NULL,
	token text NULL,
	sign text NULL,
	source text NULL,
	destination text NULL,
	operador_codigo VARCHAR(5) NULL,
	activo CHAR(1) NULL DEFAULT 'S',
	TestSN CHAR(1) NULL DEFAULT 'S',
	ultActualizacion datetime NOT NULL DEFAULT CURRENT TIMESTAMP,
        servicio VARCHAR(10) NULL,
	PRIMARY KEY ( uid )
) IN system;
COMMENT ON TABLE DBA.afipws_fe_wsaa_TA IS 'AFIP WS - TA (Tickets de Acceso)';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.uid IS 'UniqueID';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.genTime IS 'Generación';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.expTime IS 'Expiración';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.token IS 'Token';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.sign IS 'Signature';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.source IS 'Source';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.destination IS 'Destination';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.operador_codigo IS 'Operador que solicitó el TA';
COMMENT ON COLUMN DBA.afipws_fe_wsaa_TA.activo IS 'TA activo';

CREATE TABLE DBA.afipws_fe_master (
	CbteTipo NUMERIC(2,0) NOT NULL,
	PtoVta NUMERIC(4,0) NOT NULL,
	CbteDesde NUMERIC(8,0) NOT NULL,
	Concepto NUMERIC(1,0) NULL DEFAULT 1,
	DocTipo NUMERIC(2,0) NULL,
	DocNro NUMERIC(11,0) NULL,
    CbteHasta NUMERIC(8,0) NULL,
	CbteFch DATE NULL,
	ImpTotal NUMERIC(13,2) NULL DEFAULT 0,
	ImpTotConc NUMERIC(13,2) NULL DEFAULT 0,
	ImpNeto NUMERIC(13,2) NULL DEFAULT 0,
	ImpOpEx NUMERIC(13,2) NULL DEFAULT 0,
	ImpTrib NUMERIC(13,2) NULL DEFAULT 0,
	ImpIVA NUMERIC(13,2) NULL DEFAULT 0,
	FchServDesde DATE NULL,
	FchServHasta DATE NULL,
	FchVtoPago DATE NULL,
    MonId CHAR(3) NULL DEFAULT 'PES',
	MonCotiz NUMERIC(13,6) NULL DEFAULT 1,
	FchProceso datetime NULL,
	Resultado CHAR(1) NOT NULL DEFAULT 'X',
	CAE NUMERIC(14,0) NULL,
	CAEFchVto DATE NULL,
	descriTributo VARCHAR(255) NULL,
	tipo_comp NUMERIC(2,0) NULL,
	pto_emision NUMERIC(4,0) NULL,
	v_numero_comprobante NUMERIC(16,0) NULL,
	PRIMARY KEY ( CbteTipo, PtoVta, CbteDesde )
) IN system;
COMMENT ON TABLE DBA.afipws_fe_master IS 'facturas - master';
COMMENT ON COLUMN DBA.afipws_fe_master.Resultado IS 'Aprobado, Rechazado, Parcial, X (Pendiente)';
COMMENT ON COLUMN DBA.afipws_fe_master.tipo_comp IS 'Tipo de comprobante';
COMMENT ON COLUMN DBA.afipws_fe_master.pto_emision IS 'Punto de venta';
COMMENT ON COLUMN DBA.afipws_fe_master.v_numero_comprobante IS 'Numero comprobante obtenido desde la AFIP';


CREATE TABLE DBA.afipws_fe_detalle (
	CbteTipo NUMERIC(2,0) NOT NULL,
	PtoVta NUMERIC(4,0) NOT NULL,
	CbteDesde NUMERIC(8,0) NOT NULL,
	TipoDetalle NUMERIC(1,0) NOT NULL,
	Pase NUMERIC(12,0) NOT NULL DEFAULT AUTOINCREMENT,
	Id VARCHAR(5) NULL,
	AsocPtoVta NUMERIC(4,0) NULL DEFAULT 0,
	AsocNroCbte NUMERIC(8,0) NULL DEFAULT 0,
	Valor VARCHAR(255) NULL,
	BaseImp NUMERIC(13,2) NULL DEFAULT 0,
	Alic NUMERIC(5,2) NULL DEFAULT 0,
	Importe NUMERIC(13,2) NULL DEFAULT 0,
	descriTributo VARCHAR(255) NULL,
	tipo_comp NUMERIC(2,0) NULL,
	pto_emision NUMERIC(4,0) NULL,
	v_numero_comprobante NUMERIC(16,0) NULL,
	PRIMARY KEY ( CbteTipo , PtoVta , CbteDesde , TipoDetalle , Pase  )
) IN system;
COMMENT ON TABLE DBA.afipws_fe_detalle IS 'facturas - detalle (tributos, iva, observa)';
COMMENT ON COLUMN DBA.afipws_fe_detalle.TipoDetalle IS '1 CbteAsoc, 2 Tributos, 3 IVA, 4 Opcionales, 5 Observaciones';


CREATE TABLE DBA.afipws_wsaa_TRA_java (
	TestSN CHAR(1) NOT NULL DEFAULT 'S',
	trustStoreFile VARCHAR(100) NULL,
	trustStorePwd VARCHAR(20) NULL,
	keyStoreFile VARCHAR(100) NULL,
	keyStoreSigner VARCHAR(20) NULL,
	keyStorePwd VARCHAR(20) NULL,
	eyStoreTimeout NUMERIC(10,0) NULL DEFAULT 690,
	destino VARCHAR(100) NULL,
	servicio VARCHAR(20) NOT NULL,
	endPoint text NULL,
	ultActualizacion datetime NULL DEFAULT CURRENT TIMESTAMP,
	PRIMARY KEY ( TestSN, servicio )
) IN system;
COMMENT ON TABLE DBA.afipws_wsaa_TRA_java IS 'AFIP WS  - parametros para solicitar TA (java)';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.TestSN IS 'S=test, N=producción';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.trustStoreFile IS 'trustStore path relativo ';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.trustStorePwd IS 'trustStore clave';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.keyStoreFile IS 'keyStore path relativo';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.keyStoreSigner IS 'keyStore signer (1)';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.keyStorePwd IS 'keyStore clave';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.keyStoreTimeout IS 'keyStore Timeout';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.destino IS 'dstn';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.servicio IS 'nombre ws a usar';
COMMENT ON COLUMN DBA.afipws_wsaa_TRA_java.endPoint IS 'url';


INSERT INTO DBA.afipws_wsaa_TRA_java (TestSN, trustStoreFile, trustStorePwd, keyStoreFile, 
keyStoreSigner, keyStorePwd, keyStoreTimeout, destino, servicio,endPoint, ultActualizacion)
VALUES ('S','kernel.jks','kernel','test-keystore.p12','matias','matias',3600000,'CN=wsaahomo,O=AFIP,C=AR,SERIALNUMBER=CUIT 33693450239','wsmtxca','https://wsaahomo.afip.gov.ar/ws/services/LoginCms','2015-07-28 15:52:25.613');
INSERT INTO DBA.afipws_wsaa_TRA_java (TestSN, trustStoreFile, trustStorePwd, keyStoreFile, 
keyStoreSigner, keyStorePwd, keyStoreTimeout, destino, servicio,endPoint, ultActualizacion)
VALUES ('N','afip.jks','kernel','test-keystore.p12','matias','matias',3600000,'cn=wsaa,o=afip,c=ar,serialNumber=CUIT 33693450239','wsmtxca','https://wsaa.afip.gov.ar/ws/services/LoginCms','2015-03-10 00:00:00.000');

CREATE TABLE "DBA"."afipws_wsfe" (
	"servicio" varchar(30) NOT NULL,
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

INSERT INTO  DBA.afipws_wsfe (servicio, url,TestSN, ultActualizacion) VALUES 
('wsmtxca','https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService?wsdl','S','2015-10-14');
INSERT INTO  DBA.afipws_wsfe (servicio, url,TestSN, ultActualizacion) 
VALUES ('wsmtxca','https://serviciosjava.afip.gob.ar/wsmtxca/services/MTXCAService?wsdl','N','2015-10-14');

/*-------------------------  VISTAS OJO ALGUNAS PUEDEN NO ESTAR CREADAS, ASIQUE HAY QUE USAR UN CREATE EN LUGAR DE ALTER --------------------------*/

ALTER VIEW "DBA"."v_fe_detalle_iva" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    (case fac_ventas.v_iva_ri when 0 then 4 else 5 end) as Id,
    //sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) as BaseImp,
    //sum((fac_ventas.v_iva_ri/.21)+(fac_ventas.v_iva_rni/.105)) as Baseimp,
    (case sum(fac_ventas.v_impuesto_interno) when 0 then sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) else sum((fac_ventas.v_iva_ri/.21)+(fac_ventas.v_iva_rni/.105)) end) as Baseimp,
    (case fac_ventas.v_iva_ri when 0 then 10.5 else 21.0 end) as Alic,
    sum(fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as Importe from
    DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and
    (fac_ventas.v_iva_ri <> 0 or fac_ventas.v_iva_rni <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id,Alic

ALTER VIEW "DBA"."v_fe_detalle_iva_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,
    fac_ventas_dolar.pto_numero as PtoVta,
    fac_ventas_dolar.v_numero_comprobante as CbteDesde,
    (case fac_ventas_dolar.v_iva_ri when 0 then 4 else 5 end) as Id,
    //sum(fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad) as BaseImp,
    //sum((fac_ventas_dolar.v_iva_ri/.21)+(fac_ventas_dolar.v_iva_rni/.105)) as Baseimp,
    (case sum(fac_ventas_dolar.v_impuesto_interno) when 0 then sum(fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad) else sum((fac_ventas_dolar.v_iva_ri/.21)+(fac_ventas_dolar.v_iva_rni/.105)) end) as Baseimp,
    (case fac_ventas_dolar.v_iva_ri when 0 then 10.5 else 21.0 end) as Alic,
    sum(fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as Importe from
    DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and
    (fac_ventas_dolar.v_iva_ri <> 0 or fac_ventas_dolar.v_iva_rni <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id,Alic

ALTER VIEW "DBA"."v_fe_detalle_iva_exento" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    3 as Id,
    sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad) as BaseImp,
    0 as Alic,
    sum(fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as Importe from
    DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and
    (fac_ventas.v_iva_ri = 0 and fac_ventas.v_iva_rni = 0) and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id,Alic

ALTER VIEW "DBA"."v_fe_detalle_iva_exento_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,
    fac_ventas_dolar.pto_numero as PtoVta,
    fac_ventas_dolar.v_numero_comprobante as CbteDesde,
    3 as Id,
    sum(fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad) as BaseImp,
    0 as Alic,
    sum(fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as Importe from
    DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and
    (fac_ventas_dolar.v_iva_ri = 0 and fac_ventas_dolar.v_iva_rni = 0) and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id,Alic

ALTER VIEW "DBA"."v_fe_detalle_otros_tributos" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,fac_ventas.pto_numero as PtoVta,fac_ventas.v_numero_comprobante as CbteDesde,99 as Id,0 as BaseImp,0 as Alic,
    SUM(ISNULL(fac_ventas.v_otro_impuesto,0)) as Importe,'Otros Tributos' as Descri from DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and(fac_ventas.v_otro_impuesto <> 0) and fac_ptovta.pto_fe = 'S' and
    fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_otros_tributos_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,fac_ventas_dolar.pto_numero as PtoVta,fac_ventas_dolar.v_numero_comprobante as CbteDesde,99 as Id,0 as BaseImp,0 as Alic,
    SUM(ISNULL(fac_ventas_dolar.v_otro_impuesto,0)) as Importe,'Otros Tributos' as Descri from DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and(fac_ventas_dolar.v_otro_impuesto <> 0) and fac_ptovta.pto_fe = 'S' and
    fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_tributo_impinterno" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    4 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas.v_impuesto_interno,0)) as Importe,'Impuesto Interno' as Descri from
    DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and
    (fac_ventas.v_impuesto_interno <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id


ALTER VIEW "DBA"."v_fe_detalle_tributo_impinterno_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,
    fac_ventas_dolar.pto_numero as PtoVta,
    fac_ventas_dolar.v_numero_comprobante as CbteDesde,
    4 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas_dolar.v_impuesto_interno,0)) as Importe,'Impuesto Interno' as Descri from
    DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and
    (fac_ventas_dolar.v_impuesto_interno <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_tributo_nac" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    1 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas.v_perce2459_1,0)+isnull(fac_ventas.v_perce2459_105,0)) as Importe,'Percepción RG2459' as Descri from
    DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and
    (fac_ventas.v_perce2459_1 <> 0 or fac_ventas.v_perce2459_105 <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_tributo_nac_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,
    fac_ventas_dolar.pto_numero as PtoVta,
    fac_ventas_dolar.v_numero_comprobante as CbteDesde,
    1 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas_dolar.v_perce2459_1,0)+isnull(fac_ventas_dolar.v_perce2459_105,0)) as Importe,'Percepción RG2459' as Descri from
    DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and
    (fac_ventas_dolar.v_perce2459_1 <> 0 or fac_ventas_dolar.v_perce2459_105 <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_tributo_prov" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,
    fac_ventas.pto_numero as PtoVta,
    fac_ventas.v_numero_comprobante as CbteDesde,
    2 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas.v_percepcion1,0)+isnull(fac_ventas.v_percepcion2,0)) as Importe,'Percepción IIBB' as Descri from
    DBA.fac_ventas,DBA.fac_ptovta where
    fac_ventas.v_numero_mov <> 0 and
    (fac_ventas.v_percepcion1 <> 0 or fac_ventas.v_percepcion2 <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_detalle_tributo_prov_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,
    fac_ventas_dolar.pto_numero as PtoVta,
    fac_ventas_dolar.v_numero_comprobante as CbteDesde,
    2 as Id,
    0 as BaseImp,
    0 as Alic,
    sum(isnull(fac_ventas_dolar.v_percepcion1,0)+isnull(fac_ventas_dolar.v_percepcion2,0)) as Importe,'Percepción IIBB' as Descri from
    DBA.fac_ventas_dolar,DBA.fac_ptovta where
    fac_ventas_dolar.v_numero_mov <> 0 and
    (fac_ventas_dolar.v_percepcion1 <> 0 or fac_ventas_dolar.v_percepcion2 <> 0) and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,Id

ALTER VIEW "DBA"."v_fe_master" /* view_column_name, ... */
  as select fac_ventas.v_tipo_comprobante as CbteTipo,fac_ventas.pto_numero as PtoVta,fac_ventas.v_numero_comprobante as CbteDesde,fac_ventas.v_numero_cuit as DocNro,fac_ventas.v_fecha_operacion as CbteFch,
    // ImporteNeto
    (if(SUM(v_iva_ri+v_iva_rni) <> 0) then
      (case sum(fac_ventas.v_impuesto_interno)
      when 0 then sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad)
      else sum((fac_ventas.v_iva_ri/.21)+(fac_ventas.v_iva_rni/.105))
      end)
    else
      0
    endif)+
    // ImpTrib
    sum(isnull(fac_ventas.v_otro_impuesto,0)+isnull(fac_ventas.v_percepcion1,0)+isnull(fac_ventas.v_percepcion2,0)+isnull(fac_ventas.v_impuesto_interno,0)+isnull(fac_ventas.v_perce2459_1,0)+isnull(fac_ventas.v_perce2459_105,0))+
    // ImporteEx
    sum(if fac_ventas.v_iva_ri = 0 and fac_ventas.v_iva_rni = 0 then fac_ventas.v_precio_unitario*fac_ventas.v_cantidad else 0 endif)+
    // Importe IVA
    sum(fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as ImpTotal,
    if(SUM(v_iva_ri+v_iva_rni) <> 0) then
      (case sum(fac_ventas.v_impuesto_interno)
      when 0 then sum(fac_ventas.v_precio_unitario*fac_ventas.v_cantidad)
      else sum((fac_ventas.v_iva_ri/.21)+(fac_ventas.v_iva_rni/.105))
      end)
    else
      0
    endif as ImpNeto,
    sum(isnull(fac_ventas.v_otro_impuesto,0)+isnull(fac_ventas.v_percepcion1,0)+isnull(fac_ventas.v_percepcion2,0)+isnull(fac_ventas.v_impuesto_interno,0)+isnull(fac_ventas.v_perce2459_1,0)+isnull(fac_ventas.v_perce2459_105,0)) as ImpTrib,
    sum(if fac_ventas.v_iva_ri = 0 and fac_ventas.v_iva_rni = 0 then fac_ventas.v_precio_unitario*fac_ventas.v_cantidad else 0 endif) as ImpEx,
    sum(fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as ImpIVA,'PES' as MonID,
    1 as MonCotiz,
    fac_ventas.v_fecha_vencimiento as FchVtoPago from DBA.fac_ventas,DBA.fac_ptovta where fac_ventas.v_numero_mov <> 0 and fac_ptovta.pto_fe = 'S' and fac_ventas.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,DocNro,CbteFch,FchVtoPago


ALTER VIEW "DBA"."v_fe_master_e" /* view_column_name, ... */
  as select fac_ventas_dolar.v_tipo_comprobante as CbteTipo,fac_ventas_dolar.pto_numero as PtoVta,fac_ventas_dolar.v_numero_comprobante as CbteDesde,fac_ventas_dolar.v_numero_cuit as DocNro,fac_ventas_dolar.v_fecha_operacion as CbteFch,
    // ImporteNeto
    (if(SUM(v_iva_ri+v_iva_rni) <> 0) then
      (case sum(fac_ventas_dolar.v_impuesto_interno)
      when 0 then sum(fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad)
      else sum((fac_ventas_dolar.v_iva_ri/.21)+(fac_ventas_dolar.v_iva_rni/.105))
      end)
    else
      0
    endif)+
    // ImpTrib
    sum(isnull(fac_ventas_dolar.v_otro_impuesto,0)+isnull(fac_ventas_dolar.v_percepcion1,0)+isnull(fac_ventas_dolar.v_percepcion2,0)+isnull(fac_ventas_dolar.v_impuesto_interno,0)+isnull(fac_ventas_dolar.v_perce2459_1,0)+isnull(fac_ventas_dolar.v_perce2459_105,0))+
    // ImporteEx
    sum(if fac_ventas_dolar.v_iva_ri = 0 and fac_ventas_dolar.v_iva_rni = 0 then fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad else 0 endif)+
    // Importe IVA
    sum(fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as ImpTotal,
    if(SUM(v_iva_ri+v_iva_rni) <> 0) then
      (case sum(fac_ventas_dolar.v_impuesto_interno)
      when 0 then sum(fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad)
      else sum((fac_ventas_dolar.v_iva_ri/.21)+(fac_ventas_dolar.v_iva_rni/.105))
      end)
    else
      0
    endif as ImpNeto,
    sum(isnull(fac_ventas_dolar.v_otro_impuesto,0)+isnull(fac_ventas_dolar.v_percepcion1,0)+isnull(fac_ventas_dolar.v_percepcion2,0)+isnull(fac_ventas_dolar.v_impuesto_interno,0)+isnull(fac_ventas_dolar.v_perce2459_1,0)+isnull(fac_ventas_dolar.v_perce2459_105,0)) as ImpTrib,
    sum(if fac_ventas_dolar.v_iva_ri = 0 and fac_ventas_dolar.v_iva_rni = 0 then fac_ventas_dolar.v_precio_unitario*fac_ventas_dolar.v_cantidad else 0 endif) as ImpEx,
    sum(fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as ImpIVA,'DOL' as MonID,
    fac_ventas_dolar.v_dolar as MonCotiz,
    fac_ventas_dolar.v_fecha_vencimiento as FchVtoPago from DBA.fac_ventas_dolar,DBA.fac_ptovta where fac_ventas_dolar.v_numero_mov <> 0 and fac_ptovta.pto_fe = 'S' and fac_ventas_dolar.pto_numero = fac_ptovta.pto_numero and fac_ptovta.tipo_comp = 'A'
    group by CbteTipo,PtoVta,CbteDesde,DocNro,CbteFch,FchVtoPago,v_dolar


ALTER VIEW "DBA"."v_feDet_items"
  as select afipws_fe_master.CbteTipo,
    afipws_fe_master.PtoVta,
    afipws_fe_master.CbteDesde,1 as iva_codigo,
    fac_articulos.art_unidad_referencia_afip as unidadesMtx,
    fac_articulos.art_barrAS as codigoMtx,
    fac_articulos.art_codigo as codigo,
    (case when fac_ventas.v_descripcion is null then fac_articulos.art_descri
    when fac_ventas.v_descripcion = '' then fac_articulos.art_descri
    else fac_ventas.v_descripcion
    //fac_ventas.v_cantidad as cantidad,
    end) as descripcion,
    //((fac_ventas.v_iva_ri/.21)/fac_ventas.v_precio_unitario)+((fac_ventas.v_iva_rni/.105)/fac_ventas.v_precio_unitario) as cantidad,
    //(case fac_ventas.v_impuesto_interno when 0 then fac_ventas.v_cantidad else((fac_ventas.v_iva_ri/.21)/fac_ventas.v_precio_unitario)+((fac_ventas.v_iva_rni/.105)/fac_ventas.v_precio_unitario) end) as cantidad,
    fac_ventas.v_cantidad as cantidad,
    fac_articulos.art_codigo_unidad_afip as codigoUnidadMedida,
    //fac_ventas.v_precio_unitario as precioUnitario,
    ((fac_ventas.v_iva_ri/.21)/fac_ventas.v_cantidad)+((fac_ventas.v_iva_rni/.105)/fac_ventas.v_cantidad) as precioUnitario,
    0 as importeBonificacion,
    (case when(fac_articulos.art_iva_ri = 'N' and fac_articulos.art_iva_rni = 'N') then fac_iva.iva_afip_exento
    when(fac_articulos.art_iva_ri = 'S' and fac_articulos.art_iva_rni = 'N') then fac_iva.iva_afip_inscripto
    when(fac_articulos.art_iva_ri = 'N' and fac_articulos.art_iva_rni = 'S') then fac_iva.iva_afip_noinscripto
    when(fac_articulos.art_iva_ri = 'S' and fac_articulos.art_iva_rni = 'S') then fac_iva.iva_afip_servicio
    end) as codigoCondicionIVA,(case when(fac_ventas.v_iva_ri = 0) then fac_ventas.v_iva_rni
    when(fac_ventas.v_iva_rni = 0) then fac_ventas.v_iva_ri
    end) as importeIVA,
    //((fac_ventas.v_cantidad*fac_ventas.v_precio_unitario)+fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as importeItem from
    ((cantidad*precioUnitario)+fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as importeItem from
    //((((fac_ventas.v_iva_ri/.21)/fac_ventas.v_precio_unitario)+((fac_ventas.v_iva_rni/.105)/fac_ventas.v_precio_unitario)*fac_ventas.v_precio_unitario)+fac_ventas.v_iva_ri+fac_ventas.v_iva_rni) as importeItem from
    DBA.fac_articulos join
    DBA.fac_iva on iva_codigo = fac_iva.iva_codigo join
    DBA.fac_ventas on fac_articulos.art_codigo = fac_ventas.v_codigo join
    DBA.afipws_fe_master on afipws_fe_master.CbteTipo = fac_ventas.v_tipo_comprobante and
    afipws_fe_master.PtoVta = fac_ventas.pto_numero and afipws_fe_master.CbteDesde = fac_ventas.v_numero_comprobante



ALTER VIEW "DBA"."v_feDet_items_e"
  as select afipws_fe_master.CbteTipo,
    afipws_fe_master.PtoVta,
    afipws_fe_master.CbteDesde,1 as iva_codigo,
    fac_articulos.art_unidad_referencia_afip as unidadesMtx,
    fac_articulos.art_barrAS as codigoMtx,
    fac_articulos.art_codigo as codigo,
    (case when fac_ventas_dolar.v_descripcion is null then fac_articulos.art_descri
    when fac_ventas_dolar.v_descripcion = '' then fac_articulos.art_descri
    else fac_ventas_dolar.v_descripcion
    //fac_ventas_dolar.v_cantidad as cantidad,
    end) as descripcion,
    //((fac_ventas_dolar.v_iva_ri/.21)/fac_ventas_dolar.v_precio_unitario)+((fac_ventas_dolar.v_iva_rni/.105)/fac_ventas_dolar.v_precio_unitario) as cantidad,
    //(case fac_ventas_dolar.v_impuesto_interno when 0 then fac_ventas_dolar.v_cantidad else((fac_ventas_dolar.v_iva_ri/.21)/fac_ventas_dolar.v_precio_unitario)+((fac_ventas_dolar.v_iva_rni/.105)/fac_ventas_dolar.v_precio_unitario) end) as cantidad,
    fac_ventas_dolar.v_cantidad as cantidad,
    fac_articulos.art_codigo_unidad_afip as codigoUnidadMedida,
    //fac_ventas_dolar.v_precio_unitario as precioUnitario,
    ((fac_ventas_dolar.v_iva_ri/.21)/fac_ventas_dolar.v_cantidad)+((fac_ventas_dolar.v_iva_rni/.105)/fac_ventas_dolar.v_cantidad) as precioUnitario,
    0 as importeBonificacion,
    (case when(fac_articulos.art_iva_ri = 'N' and fac_articulos.art_iva_rni = 'N') then fac_iva.iva_afip_exento
    when(fac_articulos.art_iva_ri = 'S' and fac_articulos.art_iva_rni = 'N') then fac_iva.iva_afip_inscripto
    when(fac_articulos.art_iva_ri = 'N' and fac_articulos.art_iva_rni = 'S') then fac_iva.iva_afip_noinscripto
    when(fac_articulos.art_iva_ri = 'S' and fac_articulos.art_iva_rni = 'S') then fac_iva.iva_afip_servicio
    end) as codigoCondicionIVA,(case when(fac_ventas_dolar.v_iva_ri = 0) then fac_ventas_dolar.v_iva_rni
    when(fac_ventas_dolar.v_iva_rni = 0) then fac_ventas_dolar.v_iva_ri
    end) as importeIVA,
    //((fac_ventas_dolar.v_cantidad*fac_ventas_dolar.v_precio_unitario)+fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as importeItem from
    ((cantidad*precioUnitario)+fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as importeItem from
    //((((fac_ventas_dolar.v_iva_ri/.21)/fac_ventas_dolar.v_precio_unitario)+((fac_ventas_dolar.v_iva_rni/.105)/fac_ventas_dolar.v_precio_unitario)*fac_ventas_dolar.v_precio_unitario)+fac_ventas_dolar.v_iva_ri+fac_ventas_dolar.v_iva_rni) as importeItem from
    DBA.fac_articulos join
    DBA.fac_iva on iva_codigo = fac_iva.iva_codigo join
    DBA.fac_ventas_dolar on fac_articulos.art_codigo = fac_ventas_dolar.v_codigo join
    DBA.afipws_fe_master on afipws_fe_master.CbteTipo = fac_ventas_dolar.v_tipo_comprobante and
    afipws_fe_master.PtoVta = fac_ventas_dolar.pto_numero and afipws_fe_master.CbteDesde = fac_ventas_dolar.v_numero_comprobante


/*------------------------------- PASAJE DE COMPROBANTES A EN DOLARES ---------------------------------*/

ALTER PROCEDURE "DBA"."afipws_pasaje_comprobante_USD"(in cbte_Tipo numeric(2),in pto_Vta numeric(4),in cbte_Desde numeric(8))
begin
  /******************************************/
  /* ACA DECLARO LAS VARIABLES QUE NECESITO */
  /******************************************/
  /* Cursor para la tabla master */
  declare finCursor exception for sqlstate value '02000';
  declare cursorDatos dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,DocNro,CbteFch,ImpTotal,ImpNeto,ImpTrib,
      ImpEx,ImpIVA,MonID,MonCotiz,FchVtoPago from v_fe_master_e where CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;
  /* Cursor para la tabla IVA */
  declare cursorDatos2 dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe from v_fe_detalle_iva_e where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;
  /* union
  select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe from v_fe_detalle_iva_exento where
  CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde;*/
  /* Cursor para la tabla tributos */
  declare cursorDatos3 dynamic scroll cursor for select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_nac_e where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_prov_e where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_tributo_impinterno_e where
      CbteTipo = cbte_tipo and PtoVta = Pto_vta and CbteDesde = cbte_Desde union
    select CbteTipo,PtoVta,CbteDesde,Id,BaseImp,Alic,Importe,Descri from v_fe_detalle_otros_tributos_e where
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
