package jsf.clases;

import entidades.Usuario;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.UsuarioFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginControlador implements Serializable {

    private Usuario current;
    private Usuario aux;
    private DataModel items = null;
    @EJB
    private beans.sessions.UsuarioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public LoginControlador() {
        current = new Usuario();
    }
    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
            selectedItemIndex = -1;
        }
        return current;
    }
    private UsuarioFacade getFacade() {
        return ejbFacade;
    }
    public void setUsuario(Usuario current)
    {
        this.current = current;
    }
    public Usuario getUsuario()
    {
        return current;
    }
    public void setAux(Usuario aux)
    {
        this.aux = aux;
    }
    public Usuario getAux()
    {
        return aux;
    }
    
    public String autenticar()
    {
        if(buscarNomUsu() && buscarPass())
        {
            aux = getFacade().findNomUsu(current.getUsuNomusu());
            return "listarEncuestas";
        }
        else 
        {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LoginFracaso"));
            current.setUsuNomusu(null);
            current.setUsuPass(null);
            return "/login";
        }
    }
    
    public boolean buscarNomUsu()
    {
        try{
            Usuario aux = getFacade().findNomUsu(current.getUsuNomusu());
            if(aux==null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch(Exception e)
        {
            return false;
        }
        
    }
    
    public boolean buscarPass()
    {
        try{
            Usuario aux = getFacade().findPass(current.getUsuPass());
            if(aux==null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public String cerrarsesion()
    {
        current = null;
        return "index";
    }
    public Usuario obtenerUsuario()
    {
        try{
            Usuario aux = getFacade().findNomUsu(current.getUsuNomusu());
            if(aux==null)
            {
                return null;
            }
            else
            {
                return aux;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
}
