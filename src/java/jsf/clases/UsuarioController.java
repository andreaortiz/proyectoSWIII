package jsf.clases;

import entidades.Usuario;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.UsuarioFacade;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import javax.servlet.ServletContext;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    private Usuario current;
    private DataModel items = null;
    @EJB
    private beans.sessions.UsuarioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private UploadedFile imp;
    private String v1, v2, v3,v4,v5;
    public UsuarioController() {
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
    public void setV1(String v1)
    {
        this.v1 = v1;
    }
    public String getV1()
    {
        return v1;
    }
    public void setV2(String v2)
    {
        this.v2 = v2;
    }
    public String getV2()
    {
        return v2;
    }
    public void setV3(String v3)
    {
        this.v3 = v3;
    }
    public String getV3()
    {
        return v3;
    }
    public void setV4(String v4)
    {
        this.v4 = v4;
    }
    public String getV4()
    {
        return v4;
    }
    public void setV5(String v5)
    {
        this.v5 = v5;
    }
    public String getV5()
    {
        return v5;
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
        public void validar1()
    {
        if(current.getUsuNomusu().equals(""))
        {
                setV1("Incompleto");
        }
        else
        {
            setV1("");
        }
    }
    public void validar2()
    {
        if(current.getUsuPass().equals(""))
        {
                setV2("Incompleto");
        }
        else
        {
            setV2("");
        }
    }
    public void validar3()
    {
        if(current.getUsuNomb().equals(""))
        {
                setV3("Incompleto");
        }
        else
        {
            setV3("");
        }
    }
    public void validar4()
    {
        if(current.getUsuApel().equals(""))
        {
                setV4("Incompleto");
        }
        else
        {
            setV4("");
        }
    }
    public void validar5()
    {
        if(current.getUsuEmail().equals(""))
        {
                setV5("Incompleto");
        }
        else
        {
             setV5("");
        }
    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Usuario();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            int contador = getFacade().count();
            current.setUsuId(contador+1);
            actualizar();
            byte[] bi = Imagen_A_Bytes();
            current.setUsuFoto(bi);
            validar1();
            validar2();
            validar3();
            validar4();
            validar5();
            
           
           
            if(v1.equals("")&& v2.equals("") && v3.equals("") && v4.equals("") && v5.equals(""))
            {
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
                return prepareCreate();
            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Usuario) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
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

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getUsuId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }

    }
    
    public void actualizar() throws IOException 
    {
     InputStream inputStream = null;
     OutputStream outputStream = null;
     try{
         ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
         String realPath=(String) servletContext.getRealPath("/perfiles"); // Sustituye "/" por el directorio ej: "/upload"
         outputStream = new FileOutputStream(new File(realPath+"/"+current.getUsuId()+".png"));
         inputStream = this.imp.getInputstream();
         int read=0;
         byte[] bytes = new byte[1024];
         while((read=inputStream.read(bytes))!=-1)
         {
             outputStream.write(bytes, 0, read);
         }
     }
     catch(Exception e){
         
     }
     finally
     {
          if(inputStream!=null)
          {
              inputStream.close();
          }
          if(outputStream!=null)
          {
              outputStream.close();
          }
     }
     //return bytes;
    }
    public byte[] Imagen_A_Bytes() throws FileNotFoundException
    {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String realPath = (String) servletContext.getRealPath("/perfiles");
        File file = new File(realPath+"/"+current.getUsuId()+".png");
 
        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.
 
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum); 
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        //Logger.getLogger(this.getSelected).log(Level.SEVERE, null, ex);
            System.out.println("error");
        }
 
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
    
    public UploadedFile getImp() {
        return imp;
    }

    public void setImp(UploadedFile imp) {
        this.imp = imp;
    }

}
