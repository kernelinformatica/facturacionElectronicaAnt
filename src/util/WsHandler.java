/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Carlos
 */
public class WsHandler implements SOAPHandler<SOAPMessageContext> {

    String tipoMensajeSoap = null;
    private String metodoWS = null;

    public WsHandler() {
    }

    /**
     * Este metodo seta la propiedad que contiene el nombre del metodo qe se va a ejacutar
     * 
     * @param nombreMetod 
     */
    public WsHandler(String nombreMetod) {
        
        // Seteo el nombre
        this.metodoWS = nombreMetod;
    }

    
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean isOutboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (isOutboundMessage) {
            //Agrega headers de seguridad solo en el request
            tipoMensajeSoap = "request";
        } else {
            tipoMensajeSoap = "response";
        }
        
        //Grabo XML en archivo (siempre)
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            context.getMessage().writeTo(os);
            String sTemp = new String(os.toByteArray(), "UTF-8");
            if(this.metodoWS != null){
                MyUtil.grabarArchivo("logs/" + MyUtil.fechaYHoraAString() + "_" + this.metodoWS + "_" + tipoMensajeSoap + ".xml", sTemp);
            } else {
                MyUtil.grabarArchivo("logs/" + MyUtil.fechaYHoraAString() + "_" + tipoMensajeSoap + ".xml", sTemp);            
            }

        } catch (SOAPException | IOException ex) {
            MyUtil.LogError("Error log xml " + ex.getMessage(), true);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public void close(MessageContext context) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public String getMetodoWS() {
        return metodoWS;
    }

    public void setMetodoWS(String metodoWS) {
        this.metodoWS = metodoWS;
    }
}