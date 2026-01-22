package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseUpdatableEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "established_date", nullable = false)
    private Date establishedDate;

    @OneToMany(mappedBy = "departmentId")
    private List<Employee> employees = new ArrayList<>();

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
