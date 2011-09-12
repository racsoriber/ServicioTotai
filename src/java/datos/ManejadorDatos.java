/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author timoteo
 */
public class ManejadorDatos {

    private EntityManager entityManager;
    private static final ManejadorDatos instance = new ManejadorDatos();

    public static ManejadorDatos getInstance() {
        return instance;
    }

    private ManejadorDatos() {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("totai3capasWebService");
        entityManager = managerFactory.createEntityManager();
    }

    public void save(Object obj) {
        entityManager.getTransaction().begin();
        entityManager.persist(obj);
        entityManager.getTransaction().commit();
    }

    public void delete(Object obj) {
        entityManager.getTransaction().begin();
        entityManager.remove(obj);
        entityManager.getTransaction().commit();
    }

    public <T> List<T> list(Class<T> classToFind) {
        javax.persistence.Query query = entityManager.createQuery("SELECT t FROM " + classToFind.getSimpleName() + " t");
        return query.getResultList();

    }

    public <T> List<T> list(Class<T> classToFind, String whereQuery) {
        javax.persistence.Query query = entityManager.createQuery("SELECT t FROM " + classToFind.getSimpleName() + " t " + whereQuery);
        return query.getResultList();

    }
    
    public <T> List<T> list(String queryString) {
        javax.persistence.Query query = entityManager.createQuery(queryString);
        return query.getResultList();

    }

    public <T>T getById(Class<?> classToFind, Object id) {
        return (T) entityManager.find(classToFind, id);
    }
}
