/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Opcion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pregunta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author AnDeiiTa
 */
public class OpcionJpaController implements Serializable {

    public OpcionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Opcion opcion) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pregunta preId = opcion.getPreId();
            if (preId != null) {
                preId = em.getReference(preId.getClass(), preId.getPreId());
                opcion.setPreId(preId);
            }
            em.persist(opcion);
            if (preId != null) {
                preId.getOpcionCollection().add(opcion);
                preId = em.merge(preId);
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

    public void edit(Opcion opcion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Opcion persistentOpcion = em.find(Opcion.class, opcion.getOpId());
            Pregunta preIdOld = persistentOpcion.getPreId();
            Pregunta preIdNew = opcion.getPreId();
            if (preIdNew != null) {
                preIdNew = em.getReference(preIdNew.getClass(), preIdNew.getPreId());
                opcion.setPreId(preIdNew);
            }
            opcion = em.merge(opcion);
            if (preIdOld != null && !preIdOld.equals(preIdNew)) {
                preIdOld.getOpcionCollection().remove(opcion);
                preIdOld = em.merge(preIdOld);
            }
            if (preIdNew != null && !preIdNew.equals(preIdOld)) {
                preIdNew.getOpcionCollection().add(opcion);
                preIdNew = em.merge(preIdNew);
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
                Integer id = opcion.getOpId();
                if (findOpcion(id) == null) {
                    throw new NonexistentEntityException("The opcion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Opcion opcion;
            try {
                opcion = em.getReference(Opcion.class, id);
                opcion.getOpId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The opcion with id " + id + " no longer exists.", enfe);
            }
            Pregunta preId = opcion.getPreId();
            if (preId != null) {
                preId.getOpcionCollection().remove(opcion);
                preId = em.merge(preId);
            }
            em.remove(opcion);
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

    public List<Opcion> findOpcionEntities() {
        return findOpcionEntities(true, -1, -1);
    }

    public List<Opcion> findOpcionEntities(int maxResults, int firstResult) {
        return findOpcionEntities(false, maxResults, firstResult);
    }

    private List<Opcion> findOpcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Opcion.class));
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

    public Opcion findOpcion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Opcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Opcion> rt = cq.from(Opcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
