
package wsAFIP;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ArrayOfDocTipo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfDocTipo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocTipo" type="{http://ar.gov.afip.dif.FEV1/}DocTipo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDocTipo", propOrder = {
    "docTipo"
})
public class ArrayOfDocTipo {

    @XmlElement(name = "DocTipo", nillable = true)
    protected List<DocTipo> docTipo;

    /**
     * Gets the value of the docTipo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docTipo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocTipo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocTipo }
     * 
     * 
     */
    public List<DocTipo> getDocTipo() {
        if (docTipo == null) {
            docTipo = new ArrayList<DocTipo>();
        }
        return this.docTipo;
    }

}
