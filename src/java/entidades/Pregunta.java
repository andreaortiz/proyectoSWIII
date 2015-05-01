/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author AnDreiTa
 */
@Entity
@Table(name = "pregunta")
@NamedQueries({
    @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p"),
    @NamedQuery(name = "Pregunta.findByPreId", query = "SELECT p FROM Pregunta p WHERE p.preId = :preId"),
    @NamedQuery(name = "Pregunta.findByPreEnun", query = "SELECT p FROM Pregunta p WHERE p.preEnun = :preEnun"),
    @NamedQuery(name = "Pregunta.findByPreTipo", query = "SELECT p FROM Pregunta p WHERE p.preTipo = :preTipo")})
public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRE_ID")
    private Integer preId;
    @Size(max = 50)
    @Column(name = "PRE_ENUN")
    private String preEnun;
    @Size(max = 50)
    @Column(name = "PRE_TIPO")
    private String preTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preId")
    private Collection<Opcion> opcionCollection;
    @JoinColumn(name = "EN_ID", referencedColumnName = "EN_ID")
    @ManyToOne(optional = false)
    private Encuesta enId;

    public Pregunta() {
    }

    public Pregunta(Integer preId) {
        this.preId = preId;
    }

    public Integer getPreId() {
        return preId;
    }

    public void setPreId(Integer preId) {
        this.preId = preId;
    }

    public String getPreEnun() {
        return preEnun;
    }

    public void setPreEnun(String preEnun) {
        this.preEnun = preEnun;
    }

    public String getPreTipo() {
        return preTipo;
    }

    public void setPreTipo(String preTipo) {
        this.preTipo = preTipo;
    }

    public Collection<Opcion> getOpcionCollection() {
        return opcionCollection;
    }

    public void setOpcionCollection(Collection<Opcion> opcionCollection) {
        this.opcionCollection = opcionCollection;
    }

    public Encuesta getEnId() {
        return enId;
    }

    public void setEnId(Encuesta enId) {
        this.enId = enId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (preId != null ? preId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        if ((this.preId == null && other.preId != null) || (this.preId != null && !this.preId.equals(other.preId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pregunta[ preId=" + preId + " ]";
    }
    
}
