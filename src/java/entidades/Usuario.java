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
import javax.persistence.Lob;
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
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsuId", query = "SELECT u FROM Usuario u WHERE u.usuId = :usuId"),
    @NamedQuery(name = "Usuario.findByUsuNomusu", query = "SELECT u FROM Usuario u WHERE u.usuNomusu = :usuNomusu"),
    @NamedQuery(name = "Usuario.findByUsuPass", query = "SELECT u FROM Usuario u WHERE u.usuPass = :usuPass"),
    @NamedQuery(name = "Usuario.findByUsuNomb", query = "SELECT u FROM Usuario u WHERE u.usuNomb = :usuNomb"),
    @NamedQuery(name = "Usuario.findByUsuApel", query = "SELECT u FROM Usuario u WHERE u.usuApel = :usuApel"),
    @NamedQuery(name = "Usuario.findByUsuEmail", query = "SELECT u FROM Usuario u WHERE u.usuEmail = :usuEmail")})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usu_id")
    private Integer usuId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "usu_nomusu")
    private String usuNomusu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usu_pass")
    private String usuPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usu_nomb")
    private String usuNomb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usu_apel")
    private String usuApel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usu_email")
    private String usuEmail;
    @Lob
    @Column(name = "usu_foto")
    private byte[] usuFoto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuId")
    private Collection<Encuesta> encuestaCollection;

    public Usuario() {
    }

    public Usuario(Integer usuId) {
        this.usuId = usuId;
    }

    public Usuario(Integer usuId, String usuNomusu, String usuPass, String usuNomb, String usuApel, String usuEmail) {
        this.usuId = usuId;
        this.usuNomusu = usuNomusu;
        this.usuPass = usuPass;
        this.usuNomb = usuNomb;
        this.usuApel = usuApel;
        this.usuEmail = usuEmail;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

    public String getUsuNomusu() {
        return usuNomusu;
    }

    public void setUsuNomusu(String usuNomusu) {
        this.usuNomusu = usuNomusu;
    }

    public String getUsuPass() {
        return usuPass;
    }

    public void setUsuPass(String usuPass) {
        this.usuPass = usuPass;
    }

    public String getUsuNomb() {
        return usuNomb;
    }

    public void setUsuNomb(String usuNomb) {
        this.usuNomb = usuNomb;
    }

    public String getUsuApel() {
        return usuApel;
    }

    public void setUsuApel(String usuApel) {
        this.usuApel = usuApel;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public byte[] getUsuFoto() {
        return usuFoto;
    }

    public void setUsuFoto(byte[] usuFoto) {
        this.usuFoto = usuFoto;
    }

    public Collection<Encuesta> getEncuestaCollection() {
        return encuestaCollection;
    }

    public void setEncuestaCollection(Collection<Encuesta> encuestaCollection) {
        this.encuestaCollection = encuestaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuId != null ? usuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuId == null && other.usuId != null) || (this.usuId != null && !this.usuId.equals(other.usuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuario[ usuId=" + usuId + " ]";
    }
    
}
