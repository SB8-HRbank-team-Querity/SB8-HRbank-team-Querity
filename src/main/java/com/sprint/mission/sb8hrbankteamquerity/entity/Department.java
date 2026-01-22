package com.sprint.mission.sb8hrbankteamquerity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "established_date", nullable = false)
    private Date establishedDate;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    public Department(String name, String description, Date establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }

    public void update(String newName, String newDescription, Date newEstablishedDate) {
        if(newName != null && !newName.equals(this.name)) {
            this.name = newName;
        }

        if(newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
        }

        if(newEstablishedDate != null && !newEstablishedDate.equals(this.establishedDate)) {
            this.establishedDate = newEstablishedDate;
        }
    }
}
