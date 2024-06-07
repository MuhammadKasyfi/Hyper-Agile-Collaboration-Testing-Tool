package demo_ver.demo.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import demo_ver.demo.model.TestCase;

public class TestCaseDAO {

private final DataSource dataSource;

    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    public TestCaseDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<TestCase> findAllTestCases() throws SQLException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
        // Implement logic using JPA with EntityManager
        return em.createQuery("SELECT tc FROM TestCase tc", TestCase.class)
            .getResultList();
        } finally {
        em.close();
        }
    }

    public TestCase findById(Long id) throws SQLException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
        return em.createQuery("SELECT tc FROM TestCase tc WHERE tc.idtest_cases = :id", TestCase.class)
            .setParameter("id", id)
            .getSingleResult();
        } finally {
        em.close();
        }
    }

    public List<TestCase> findTestCasesByUsername(String username) throws SQLException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
          // Assuming a "username" field exists in the TestCase entity
          return em.createQuery("SELECT tc FROM TestCase tc WHERE tc.username = :username", TestCase.class)
              .setParameter("username", username)
              .getResultList();
        } finally {
          em.close();
        }
      }
}
