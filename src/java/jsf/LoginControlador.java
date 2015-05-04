package jsf;

import entidades.Usuario;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import bean.UsuarioFacade;

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
    private DataModel items = null;
    @EJB
    private bean.UsuarioFacade ejbFacade;
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
    public String autenticar()
    {
        if(buscarNomUsu() && buscarPass())
        {
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LoginExito"));
            return "login";
        }
        else 
        {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LoginFracaso"));
            current.setUsuNomusu(null);
            current.setUsuPass(null);
            return null;
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
    
}
