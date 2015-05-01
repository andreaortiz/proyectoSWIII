package entidades;

import entidades.Encuesta;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-30T17:11:30")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> usuNomb;
    public static volatile SingularAttribute<Usuario, String> usuNomusu;
    public static volatile CollectionAttribute<Usuario, Encuesta> encuestaCollection;
    public static volatile SingularAttribute<Usuario, byte[]> usuFoto;
    public static volatile SingularAttribute<Usuario, Integer> usuId;
    public static volatile SingularAttribute<Usuario, String> usuPass;
    public static volatile SingularAttribute<Usuario, String> usuEmail;
    public static volatile SingularAttribute<Usuario, String> usuApel;

}