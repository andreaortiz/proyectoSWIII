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
import entidades.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getEncuestaCollection() == null) {
            usuario.setEncuestaCollection(new ArrayList<Encuesta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Encuesta> attachedEncuestaCollection = new ArrayList<Encuesta>();
            for (Encuesta encuestaCollectionEncuestaToAttach : usuario.getEncuestaCollection()) {
                encuestaCollectionEncuestaToAttach = em.getReference(encuestaCollectionEncuestaToAttach.getClass(), encuestaCollectionEncuestaToAttach.getEnId());
                attachedEncuestaCollection.add(encuestaCollectionEncuestaToAttach);
            }
            usuario.setEncuestaCollection(attachedEncuestaCollection);
            em.persist(usuario);
            for (Encuesta encuestaCollectionEncuesta : usuario.getEncuestaCollection()) {
                Usuario oldUsuIdOfEncuestaCollectionEncuesta = encuestaCollectionEncuesta.getUsuId();
                encuestaCollectionEncuesta.setUsuId(usuario);
                encuestaCollectionEncuesta = em.merge(encuestaCollectionEncuesta);
                if (oldUsuIdOfEncuestaCollectionEncuesta != null) {
                    oldUsuIdOfEncuestaCollectionEncuesta.getEncuestaCollection().remove(encuestaCollectionEncuesta);
                    oldUsuIdOfEncuestaCollectionEncuesta = em.merge(oldUsuIdOfEncuestaCollectionEncuesta);
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuId());
            Collection<Encuesta> encuestaCollectionOld = persistentUsuario.getEncuestaCollection();
            Collection<Encuesta> encuestaCollectionNew = usuario.getEncuestaCollection();
            List<String> illegalOrphanMessages = null;
            for (Encuesta encuestaCollectionOldEncuesta : encuestaCollectionOld) {
                if (!encuestaCollectionNew.contains(encuestaCollectionOldEncuesta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encuesta " + encuestaCollectionOldEncuesta + " since its usuId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Encuesta> attachedEncuestaCollectionNew = new ArrayList<Encuesta>();
            for (Encuesta encuestaCollectionNewEncuestaToAttach : encuestaCollectionNew) {
                encuestaCollectionNewEncuestaToAttach = em.getReference(encuestaCollectionNewEncuestaToAttach.getClass(), encuestaCollectionNewEncuestaToAttach.getEnId());
                attachedEncuestaCollectionNew.add(encuestaCollectionNewEncuestaToAttach);
            }
            encuestaCollectionNew = attachedEncuestaCollectionNew;
            usuario.setEncuestaCollection(encuestaCollectionNew);
            usuario = em.merge(usuario);
            for (Encuesta encuestaCollectionNewEncuesta : encuestaCollectionNew) {
                if (!encuestaCollectionOld.contains(encuestaCollectionNewEncuesta)) {
                    Usuario oldUsuIdOfEncuestaCollectionNewEncuesta = encuestaCollectionNewEncuesta.getUsuId();
                    encuestaCollectionNewEncuesta.setUsuId(usuario);
                    encuestaCollectionNewEncuesta = em.merge(encuestaCollectionNewEncuesta);
                    if (oldUsuIdOfEncuestaCollectionNewEncuesta != null && !oldUsuIdOfEncuestaCollectionNewEncuesta.equals(usuario)) {
                        oldUsuIdOfEncuestaCollectionNewEncuesta.getEncuestaCollection().remove(encuestaCollectionNewEncuesta);
                        oldUsuIdOfEncuestaCollectionNewEncuesta = em.merge(oldUsuIdOfEncuestaCollectionNewEncuesta);
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
                Integer id = usuario.getUsuId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Encuesta> encuestaCollectionOrphanCheck = usuario.getEncuestaCollection();
            for (Encuesta encuestaCollectionOrphanCheckEncuesta : encuestaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Encuesta " + encuestaCollectionOrphanCheckEncuesta + " in its encuestaCollection field has a non-nullable usuId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
