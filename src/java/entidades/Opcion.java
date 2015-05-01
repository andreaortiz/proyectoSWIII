/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author AnDreiTa
 */
@Entity
@Table(name = "opcion")
@NamedQueries({
    @NamedQuery(name = "Opcion.findAll", query = "SELECT o FROM Opcion o"),
    @NamedQuery(name = "Opcion.findByOpId", query = "SELECT o FROM Opcion o WHERE o.opId = :opId"),
    @NamedQuery(name = "Opcion.findByOpEnun", query = "SELECT o FROM Opcion o WHERE o.opEnun = :opEnun"),
    @NamedQuery(name = "Opcion.findByOpCont", query = "SELECT o FROM Opcion o WHERE o.opCont = :opCont")})
public class Opcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "OP_ID")
    private Integer opId;
    @Size(max = 100)
    @Column(name = "OP_ENUN")
    private String opEnun;
    @Column(name = "OP_CONT")
    private Integer opCont;
    @JoinColumn(name = "PRE_ID", referencedColumnName = "PRE_ID")
    @ManyToOne(optional = false)
    private Pregunta preId;

    public Opcion() {
    }

    public Opcion(Integer opId) {
        this.opId = opId;
    }

    public Integer getOpId() {
        return opId;
    }

    public void setOpId(Integer opId) {
        this.opId = opId;
    }

    public String getOpEnun() {
        return opEnun;
    }

    public void setOpEnun(String opEnun) {
        this.opEnun = opEnun;
    }

    public Integer getOpCont() {
        return opCont;
    }

    public void setOpCont(Integer opCont) {
        this.opCont = opCont;
    }

    public Pregunta getPreId() {
        return preId;
    }

    public void setPreId(Pregunta preId) {
        this.preId = preId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opId != null ? opId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Opcion)) {
            return false;
        }
        Opcion other = (Opcion) object;
        if ((this.opId == null && other.opId != null) || (this.opId != null && !this.opId.equals(other.opId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Opcion[ opId=" + opId + " ]";
    }
    
}
