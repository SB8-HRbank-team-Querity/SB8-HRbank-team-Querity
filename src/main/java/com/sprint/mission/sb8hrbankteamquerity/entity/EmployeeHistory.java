package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
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
public class EmployeeHistory extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeHistoryType type;

    @Column(name = "memo", nullable = false)
    private String memo;

    @Column(name = "ip_address", nullable = false, length = 200)
    private String ip_address;

    @Column(name = "changed_detail", nullable = false)
    private String changed_detail;

    @Column(name = "employee_name", nullable = false, length = 50)
    private String employee_name;

    @Column(name = "employee_number", columnDefinition = "jsonb", nullable = false, length = 50)
    private String employee_number;

    public EmployeeHistory(EmployeeHistorySaveRequest emp) {
        this.type = emp.type();
        this.memo = emp.memo();
        this.ip_address = emp.ip_address();
        this.changed_detail = emp.changed_detail();
        this.employee_name = emp.employee_name();
        this.employee_number = emp.employee_number();
    }
}
