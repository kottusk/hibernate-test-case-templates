package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

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

  public InputStream getInputStream() {
    try {
      return data.getBinaryStream();
    } catch (SQLException e) {
      throw new IllegalArgumentException("Could not obtain requested input stream", e);
    }
  }

  public void setData(Blob data) {
    this.data = data;
  }
}
