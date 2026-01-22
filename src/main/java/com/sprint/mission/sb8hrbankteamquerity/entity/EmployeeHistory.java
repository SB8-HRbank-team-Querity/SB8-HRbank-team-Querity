package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "employee_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class EmployeeHistory  extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeHistoryType type;

    @Column(name = "memo",nullable = false)
    private String memo;

    @Column(name = "ip_address",nullable = false, length = 200)
    private String ip_address;

    @Column(name = "changed_detail",nullable = false )
    private String changed_detail;

    @Column(name = "employee_name",nullable = false, length = 50)
    private String employee_name;

    @Column(name = "employee_number",nullable = false, length = 50)
    private String employee_number;

    public EmployeeHistory(EmployeeHistoryType type, String memo,String ip_address,String changed_detail,String employee_name,String employee_number ) {
        this.type = type;
        this.memo = memo;
        this.ip_address = ip_address;
        this.changed_detail = changed_detail;
        this.employee_name = employee_name;
        this.employee_number = employee_number;
    }
}
