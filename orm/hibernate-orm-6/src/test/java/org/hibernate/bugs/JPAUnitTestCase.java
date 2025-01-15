package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.ByteArrayInputStream;
import org.hibernate.engine.jdbc.BlobProxy;
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

  // Entities are auto-discovered, so just add them anywhere on class-path
  // Add your tests, using standard JUnit.
  @Test
  void hibernate_blob_streaming() throws Exception {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    // TEST CASE BEGIN

    // Not only does this test case fail, but also it reads the whole stream into memory.
    // See org.hibernate.type.descriptor.java.BlobJavaType.getOrCreateBlob

    // This is an in-memory stream, but it will be a stream from a large file in real-world scenario,
    // which must not be loaded into memory at once.
    ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});
    long size = in.available();

    TestEntity e = new TestEntity();
    e.setId(1L);
    e.setData(BlobProxy.generateProxy(in, size));

    entityManager.persist(e);

    // TEST CASE END

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
