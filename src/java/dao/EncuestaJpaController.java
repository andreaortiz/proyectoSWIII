/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Encuesta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Pregunta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author AnDeiiTa
 */
public class EncuestaJpaController implements Serializable {

    public EncuestaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encuesta encuesta) throws RollbackFailureException, Exception {
        if (encuesta.getPreguntaCollection() == null) {
            encuesta.setPreguntaCollection(new ArrayList<Pregunta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuId = encuesta.getUsuId();
            if (usuId != null) {
                usuId = em.getReference(usuId.getClass(), usuId.getUsuId());
                encuesta.setUsuId(usuId);
            }
            Collection<Pregunta> attachedPreguntaCollection = new ArrayList<Pregunta>();
            for (Pregunta preguntaCollectionPreguntaToAttach : encuesta.getPreguntaCollection()) {
                preguntaCollectionPreguntaToAttach = em.getReference(preguntaCollectionPreguntaToAttach.getClass(), preguntaCollectionPreguntaToAttach.getPreId());
                attachedPreguntaCollection.add(preguntaCollectionPreguntaToAttach);
            }
            encuesta.setPreguntaCollection(attachedPreguntaCollection);
            em.persist(encuesta);
            if (usuId != null) {
                usuId.getEncuestaCollection().add(encuesta);
                usuId = em.merge(usuId);
            }
            for (Pregunta preguntaCollectionPregunta : encuesta.getPreguntaCollection()) {
                Encuesta oldEnIdOfPreguntaCollectionPregunta = preguntaCollectionPregunta.getEnId();
                preguntaCollectionPregunta.setEnId(encuesta);
                preguntaCollectionPregunta = em.merge(preguntaCollectionPregunta);
                if (oldEnIdOfPreguntaCollectionPregunta != null) {
                    oldEnIdOfPreguntaCollectionPregunta.getPreguntaCollection().remove(preguntaCollectionPregunta);
                    oldEnIdOfPreguntaCollectionPregunta = em.merge(oldEnIdOfPreguntaCollectionPregunta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encuesta encuesta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Encuesta persistentEncuesta = em.find(Encuesta.class, encuesta.getEnId());
            Usuario usuIdOld = persistentEncuesta.getUsuId();
            Usuario usuIdNew = encuesta.getUsuId();
            Collection<Pregunta> preguntaCollectionOld = persistentEncuesta.getPreguntaCollection();
            Collection<Pregunta> preguntaCollectionNew = encuesta.getPreguntaCollection();
            List<String> illegalOrphanMessages = null;
            for (Pregunta preguntaCollectionOldPregunta : preguntaCollectionOld) {
                if (!preguntaCollectionNew.contains(preguntaCollectionOldPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pregunta " + preguntaCollectionOldPregunta + " since its enId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuIdNew != null) {
                usuIdNew = em.getReference(usuIdNew.getClass(), usuIdNew.getUsuId());
                encuesta.setUsuId(usuIdNew);
            }
            Collection<Pregunta> attachedPreguntaCollectionNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaCollectionNewPreguntaToAttach : preguntaCollectionNew) {
                preguntaCollectionNewPreguntaToAttach = em.getReference(preguntaCollectionNewPreguntaToAttach.getClass(), preguntaCollectionNewPreguntaToAttach.getPreId());
                attachedPreguntaCollectionNew.add(preguntaCollectionNewPreguntaToAttach);
            }
            preguntaCollectionNew = attachedPreguntaCollectionNew;
            encuesta.setPreguntaCollection(preguntaCollectionNew);
            encuesta = em.merge(encuesta);
            if (usuIdOld != null && !usuIdOld.equals(usuIdNew)) {
                usuIdOld.getEncuestaCollection().remove(encuesta);
                usuIdOld = em.merge(usuIdOld);
            }
            if (usuIdNew != null && !usuIdNew.equals(usuIdOld)) {
                usuIdNew.getEncuestaCollection().add(encuesta);
                usuIdNew = em.merge(usuIdNew);
            }
            for (Pregunta preguntaCollectionNewPregunta : preguntaCollectionNew) {
                if (!preguntaCollectionOld.contains(preguntaCollectionNewPregunta)) {
                    Encuesta oldEnIdOfPreguntaCollectionNewPregunta = preguntaCollectionNewPregunta.getEnId();
                    preguntaCollectionNewPregunta.setEnId(encuesta);
                    preguntaCollectionNewPregunta = em.merge(preguntaCollectionNewPregunta);
                    if (oldEnIdOfPreguntaCollectionNewPregunta != null && !oldEnIdOfPreguntaCollectionNewPregunta.equals(encuesta)) {
                        oldEnIdOfPreguntaCollectionNewPregunta.getPreguntaCollection().remove(preguntaCollectionNewPregunta);
                        oldEnIdOfPreguntaCollectionNewPregunta = em.merge(oldEnIdOfPreguntaCollectionNewPregunta);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encuesta.getEnId();
                if (findEncuesta(id) == null) {
                    throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Encuesta encuesta;
            try {
                encuesta = em.getReference(Encuesta.class, id);
                encuesta.getEnId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pregunta> preguntaCollectionOrphanCheck = encuesta.getPreguntaCollection();
            for (Pregunta preguntaCollectionOrphanCheckPregunta : preguntaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encuesta (" + encuesta + ") cannot be destroyed since the Pregunta " + preguntaCollectionOrphanCheckPregunta + " in its preguntaCollection field has a non-nullable enId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuId = encuesta.getUsuId();
            if (usuId != null) {
                usuId.getEncuestaCollection().remove(encuesta);
                usuId = em.merge(usuId);
            }
            em.remove(encuesta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encuesta> findEncuestaEntities() {
        return findEncuestaEntities(true, -1, -1);
    }

    public List<Encuesta> findEncuestaEntities(int maxResults, int firstResult) {
        return findEncuestaEntities(false, maxResults, firstResult);
    }

    private List<Encuesta> findEncuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encuesta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Encuesta findEncuesta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encuesta> rt = cq.from(Encuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
