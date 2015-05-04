package entidades;

import entidades.Encuesta;
import entidades.Opcion;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-29T09:40:19")
@StaticMetamodel(Pregunta.class)
public class Pregunta_ { 

    public static volatile SingularAttribute<Pregunta, Integer> preId;
    public static volatile SingularAttribute<Pregunta, String> preEnun;
    public static volatile CollectionAttribute<Pregunta, Opcion> opcionCollection;
    public static volatile SingularAttribute<Pregunta, Encuesta> enId;
    public static volatile SingularAttribute<Pregunta, String> preTipo;

}