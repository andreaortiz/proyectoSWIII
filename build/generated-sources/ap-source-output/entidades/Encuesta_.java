package entidades;

import entidades.Pregunta;
import entidades.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-30T17:11:30")
@StaticMetamodel(Encuesta.class)
public class Encuesta_ { 

    public static volatile SingularAttribute<Encuesta, String> enTit;
    public static volatile SingularAttribute<Encuesta, Usuario> usuId;
    public static volatile SingularAttribute<Encuesta, String> enState;
    public static volatile SingularAttribute<Encuesta, Integer> enId;
    public static volatile SingularAttribute<Encuesta, String> enDes;
    public static volatile CollectionAttribute<Encuesta, Pregunta> preguntaCollection;

}