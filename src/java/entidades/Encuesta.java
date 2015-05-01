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
@Table(name = "encuesta")
@NamedQueries({
    @NamedQuery(name = "Encuesta.findAll", query = "SELECT e FROM Encuesta e"),
    @NamedQuery(name = "Encuesta.findByEnId", query = "SELECT e FROM Encuesta e WHERE e.enId = :enId"),
    @NamedQuery(name = "Encuesta.findByEnTit", query = "SELECT e FROM Encuesta e WHERE e.enTit = :enTit"),
    @NamedQuery(name = "Encuesta.findByEnDes", query = "SELECT e FROM Encuesta e WHERE e.enDes = :enDes"),
    @NamedQuery(name = "Encuesta.findByEnState", query = "SELECT e FROM Encuesta e WHERE e.enState = :enState")})
public class Encuesta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EN_ID")
    private Integer enId;
    @Size(max = 50)
    @Column(name = "EN_TIT")
    private String enTit;
    @Size(max = 200)
    @Column(name = "EN_DES")
    private String enDes;
    @Size(max = 50)
    @Column(name = "EN_STATE")
    private String enState;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enId")
    private Collection<Pregunta> preguntaCollection;

    public Encuesta() {
    }

    public Encuesta(Integer enId) {
        this.enId = enId;
    }

    public Integer getEnId() {
        return enId;
    }

    public void setEnId(Integer enId) {
        this.enId = enId;
    }

    public String getEnTit() {
        return enTit;
    }

    public void setEnTit(String enTit) {
        this.enTit = enTit;
    }

    public String getEnDes() {
        return enDes;
    }

    public void setEnDes(String enDes) {
        this.enDes = enDes;
    }

    public String getEnState() {
        return enState;
    }

    public void setEnState(String enState) {
        this.enState = enState;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    public Collection<Pregunta> getPreguntaCollection() {
        return preguntaCollection;
    }

    public void setPreguntaCollection(Collection<Pregunta> preguntaCollection) {
        this.preguntaCollection = preguntaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enId != null ? enId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encuesta)) {
            return false;
        }
        Encuesta other = (Encuesta) object;
        if ((this.enId == null && other.enId != null) || (this.enId != null && !this.enId.equals(other.enId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Encuesta[ enId=" + enId + " ]";
    }
    
}
