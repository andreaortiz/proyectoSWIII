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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author AnDeiiTa
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "op_id")
    private Integer opId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "op_enun")
    private String opEnun;
    @Basic(optional = false)
    @NotNull
    @Column(name = "op_cont")
    private int opCont;
    @JoinColumn(name = "pre_id", referencedColumnName = "pre_id")
    @ManyToOne(optional = false)
    private Pregunta preId;

    public Opcion() {
    }

    public Opcion(Integer opId) {
        this.opId = opId;
    }

    public Opcion(Integer opId, String opEnun, int opCont) {
        this.opId = opId;
        this.opEnun = opEnun;
        this.opCont = opCont;
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

    public int getOpCont() {
        return opCont;
    }

    public void setOpCont(int opCont) {
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
