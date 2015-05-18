package jsf.clases;

import entidades.Pregunta;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.PreguntaFacade;
import entidades.Encuesta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "preguntaController")
@SessionScoped
public class PreguntaController implements Serializable {

    private Pregunta current;
    private DataModel items = null;
    @EJB
    private beans.sessions.PreguntaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String opcion;
    private String opc1, opc2, opc3, opc4, disable;
    private Map<String,String> opciones; 
    private Map<String,Map<String,String>> data = new HashMap<String, Map<String,String>>();
    public PreguntaController() {
        opciones  = new HashMap<String, String>();
        opciones.put("Respuesta única", "Respuesta única");
        opciones.put("Multiple respuesta", "Multiple respuesta");
        opciones.put("Respuesta desplegable", "Respuesta desplegable");
        opciones.put("Respuesta abierta", "Respuesta abierta");
    }
    
    public Pregunta getSelected() {
        if (current == null) {
            current = new Pregunta();
            selectedItemIndex = -1;
        }
        return current;
    }
////////////////////////////////////////////////////
    
    public Map<String, String> getOpciones() {
        return opciones;
    }
    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public String getOpc1() {
        return opc1;
    }

    public void setOpc1(String opc1) {
        this.opc1 = opc1;
    }

    public String getOpc2() {
        return opc2;
    }

    public void setOpc2(String opc2) {
        this.opc2 = opc2;
    }

    public String getOpc3() {
        return opc3;
    }

    public void setOpc3(String opc3) {
        this.opc3 = opc3;
    }

    public String getOpc4() {
        return opc4;
    }

    public void setOpc4(String opc4) {
        this.opc4 = opc4;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }
    
    public void show()
    {
        if(current.getPreTipo().equals("Respuesta única"))
        {
            setOpc1("true");
            setOpc2("false");
            setOpc3("false");
            setOpc4("false");
            setDisable("true");
        }
        if(current.getPreTipo().equals("Multiple respuesta"))
        {
            setOpc1("false");
            setOpc2("true");
            setOpc3("false");
            setOpc4("false");
            setDisable("true");
        }
        if(current.getPreTipo().equals("Respuesta desplegable"))
        {
            setOpc1("false");
            setOpc2("false");
            setOpc3("true");
            setOpc4("false");
            setDisable("true");
        }
        if(current.getPreTipo().equals("Respuesta abierta"))
        {
            setOpc1("false");
            setOpc2("false");
            setOpc3("false");
            setOpc4("false");
            setDisable("true");
        } 
        if(current.getPreTipo()==null)
        {
            setOpc1("false");
            setOpc2("false");
            setOpc3("false");
            setOpc4("false");
        }
    }
   
///////////////////////////////////
    private PreguntaFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Pregunta) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pregunta();
        selectedItemIndex = -1;
        return "editarEncuestas";
    }

    public String create(Encuesta Enc) {
        try {
            current.setEnId(Enc);
            getFacade().create(current);
            show();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PreguntaCreated"));
            return "editarEncuestas";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareEdit() {
        current = (Pregunta) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PreguntaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Pregunta) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PreguntaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Pregunta.class)
    public static class PreguntaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PreguntaController controller = (PreguntaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "preguntaController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Pregunta) {
                Pregunta o = (Pregunta) object;
                return getStringKey(o.getPreId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pregunta.class.getName());
            }
        }

    }

}
