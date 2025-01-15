package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.sql.Blob;

@Entity
public class TestEntity {

  @Id
  Long id;

  @Lob
  Blob data;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Blob getData() {
    return data;
  }

  public void setData(Blob data) {
    this.data = data;
  }
}
