package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Random;

@Entity
@Table(name = "employee")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee extends BaseUpdatableEntity {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "employee_number", length = 50, nullable = false, unique = true)
    private String employeeNumber;

    @Column(name = "position", length = 50, nullable = false)
    private String position;

    @Column(name = "hire_date", length = 100, nullable = false)
    private Instant hireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private FileMeta profileImageId;

    public Employee(String name, String email, String employeeNumber, Department department, String position, Instant hireDate, FileMeta profileImageId) {
        this.name = name;
        this.email = email;
        this.employeeNumber = employeeNumber;
        this.department = department;
        this.position = position;
        this.hireDate = hireDate;
        this.profileImageId = profileImageId;
    }

    public static Employee create(String name, String email, Department department, String position, Instant hireDate) {
        String employeeNumber = createEmployeeNumber(hireDate);
        return new Employee(name, email, employeeNumber, department, position, hireDate, null);
    }

    public static Employee createProfile(String name, String email, Department department, String position, Instant hireDate, FileMeta profileImageId) {
        String employeeNumber = createEmployeeNumber(hireDate);
        return new Employee(name, email, employeeNumber, department, position, hireDate, profileImageId);
    }

    private static String createEmployeeNumber(Instant hireDate) {
        int year = hireDate.atZone(ZoneId.systemDefault()).getYear();
        int random = new Random().nextInt(9000) + 1000;
        return String.format("EMP-%d-%d", year, random);
    }

}

