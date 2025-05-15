package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.*;
import java.sql.Blob;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java
 * Persistence API.
 */
class JPAUnitTestCase {

  private EntityManagerFactory entityManagerFactory;

  @BeforeEach
  void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
  }

  @AfterEach
  void destroy() {
    entityManagerFactory.close();
  }


  @Test
  void hibernate_blob_streaming() throws Exception {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // TEST CASE BEGIN


    String zipFilePath = "src/test/resources/test.zip";
    try {

      File file = new File(zipFilePath);


      JarFile jarFile = new JarFile(file);

      System.out.println("Open file: " + jarFile.getName());
      JarEntry entry = jarFile.getJarEntry("pizza.png");
      long size = entry.getSize();
      InputStream is = jarFile.getInputStream(entry);


      Blob blob = NonContextualLobCreator.INSTANCE.wrap(
              NonContextualLobCreator.INSTANCE.createBlob(is, size)
      );
      TestEntity e = new TestEntity();
      e.setId(1L);
      e.setData(blob);

      Session session = entityManager.unwrap(Session.class);
      Transaction transaction = session.beginTransaction();
      session.save(e);
      transaction.commit();

      entityManager.close();

      jarFile.close();
    } catch (IOException e) {
      System.err.println("ERROR ZIP: " + e.getMessage() + e.toString());
      entityManager.close();
    }


    // TEST CASE END
  }
}
