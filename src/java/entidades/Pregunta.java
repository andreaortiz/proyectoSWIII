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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author AnDeiiTa
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pre_id")
    private Integer preId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pre_enun")
    private String preEnun;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pre_tipo")
    private String preTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preId")
    private Collection<Opcion> opcionCollection;
    @JoinColumn(name = "en_id", referencedColumnName = "en_id")
    @ManyToOne(optional = false)
    private Encuesta enId;

    public Pregunta() {
    }

    public Pregunta(Integer preId) {
        this.preId = preId;
    }

    public Pregunta(Integer preId, String preEnun, String preTipo) {
        this.preId = preId;
        this.preEnun = preEnun;
        this.preTipo = preTipo;
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
