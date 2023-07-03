package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BasicModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    LocalDateTime lastModificationDate;
    LocalDateTime creationDate;
    @PrePersist
    public void prePersist(){
        creationDate = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        lastModificationDate = LocalDateTime.now();
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
