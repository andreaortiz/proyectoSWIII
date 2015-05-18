/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Encuesta;
import entidades.Opcion;
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
public class PreguntaJpaController implements Serializable {

    public PreguntaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pregunta pregunta) throws RollbackFailureException, Exception {
        if (pregunta.getOpcionCollection() == null) {
            pregunta.setOpcionCollection(new ArrayList<Opcion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Encuesta enId = pregunta.getEnId();
            if (enId != null) {
                enId = em.getReference(enId.getClass(), enId.getEnId());
                pregunta.setEnId(enId);
            }
            Collection<Opcion> attachedOpcionCollection = new ArrayList<Opcion>();
            for (Opcion opcionCollectionOpcionToAttach : pregunta.getOpcionCollection()) {
                opcionCollectionOpcionToAttach = em.getReference(opcionCollectionOpcionToAttach.getClass(), opcionCollectionOpcionToAttach.getOpId());
                attachedOpcionCollection.add(opcionCollectionOpcionToAttach);
            }
            pregunta.setOpcionCollection(attachedOpcionCollection);
            em.persist(pregunta);
            if (enId != null) {
                enId.getPreguntaCollection().add(pregunta);
                enId = em.merge(enId);
            }
            for (Opcion opcionCollectionOpcion : pregunta.getOpcionCollection()) {
                Pregunta oldPreIdOfOpcionCollectionOpcion = opcionCollectionOpcion.getPreId();
                opcionCollectionOpcion.setPreId(pregunta);
                opcionCollectionOpcion = em.merge(opcionCollectionOpcion);
                if (oldPreIdOfOpcionCollectionOpcion != null) {
                    oldPreIdOfOpcionCollectionOpcion.getOpcionCollection().remove(opcionCollectionOpcion);
                    oldPreIdOfOpcionCollectionOpcion = em.merge(oldPreIdOfOpcionCollectionOpcion);
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

    public void edit(Pregunta pregunta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getPreId());
            Encuesta enIdOld = persistentPregunta.getEnId();
            Encuesta enIdNew = pregunta.getEnId();
            Collection<Opcion> opcionCollectionOld = persistentPregunta.getOpcionCollection();
            Collection<Opcion> opcionCollectionNew = pregunta.getOpcionCollection();
            List<String> illegalOrphanMessages = null;
            for (Opcion opcionCollectionOldOpcion : opcionCollectionOld) {
                if (!opcionCollectionNew.contains(opcionCollectionOldOpcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opcion " + opcionCollectionOldOpcion + " since its preId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (enIdNew != null) {
                enIdNew = em.getReference(enIdNew.getClass(), enIdNew.getEnId());
                pregunta.setEnId(enIdNew);
            }
            Collection<Opcion> attachedOpcionCollectionNew = new ArrayList<Opcion>();
            for (Opcion opcionCollectionNewOpcionToAttach : opcionCollectionNew) {
                opcionCollectionNewOpcionToAttach = em.getReference(opcionCollectionNewOpcionToAttach.getClass(), opcionCollectionNewOpcionToAttach.getOpId());
                attachedOpcionCollectionNew.add(opcionCollectionNewOpcionToAttach);
            }
            opcionCollectionNew = attachedOpcionCollectionNew;
            pregunta.setOpcionCollection(opcionCollectionNew);
            pregunta = em.merge(pregunta);
            if (enIdOld != null && !enIdOld.equals(enIdNew)) {
                enIdOld.getPreguntaCollection().remove(pregunta);
                enIdOld = em.merge(enIdOld);
            }
            if (enIdNew != null && !enIdNew.equals(enIdOld)) {
                enIdNew.getPreguntaCollection().add(pregunta);
                enIdNew = em.merge(enIdNew);
            }
            for (Opcion opcionCollectionNewOpcion : opcionCollectionNew) {
                if (!opcionCollectionOld.contains(opcionCollectionNewOpcion)) {
                    Pregunta oldPreIdOfOpcionCollectionNewOpcion = opcionCollectionNewOpcion.getPreId();
                    opcionCollectionNewOpcion.setPreId(pregunta);
                    opcionCollectionNewOpcion = em.merge(opcionCollectionNewOpcion);
                    if (oldPreIdOfOpcionCollectionNewOpcion != null && !oldPreIdOfOpcionCollectionNewOpcion.equals(pregunta)) {
                        oldPreIdOfOpcionCollectionNewOpcion.getOpcionCollection().remove(opcionCollectionNewOpcion);
                        oldPreIdOfOpcionCollectionNewOpcion = em.merge(oldPreIdOfOpcionCollectionNewOpcion);
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
                Integer id = pregunta.getPreId();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
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
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getPreId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Opcion> opcionCollectionOrphanCheck = pregunta.getOpcionCollection();
            for (Opcion opcionCollectionOrphanCheckOpcion : opcionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pregunta (" + pregunta + ") cannot be destroyed since the Opcion " + opcionCollectionOrphanCheckOpcion + " in its opcionCollection field has a non-nullable preId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Encuesta enId = pregunta.getEnId();
            if (enId != null) {
                enId.getPreguntaCollection().remove(pregunta);
                enId = em.merge(enId);
            }
            em.remove(pregunta);
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

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
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

    public Pregunta findPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
