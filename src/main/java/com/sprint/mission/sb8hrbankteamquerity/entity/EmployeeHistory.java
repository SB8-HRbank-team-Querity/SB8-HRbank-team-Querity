package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.DiffDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;

@Entity
@Getter
@Builder
@Table(name = "employee_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class EmployeeHistory extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeHistoryType type;

    @Column(name = "memo", columnDefinition = "text", nullable = false)
    private String memo;

    @Column(name = "ip_address", nullable = false, length = 200)
    private String ipAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "changed_detail", nullable = false)
    private Map<String, DiffDto> changed_detail;

    @Column(name = "employee_name", nullable = false, length = 50)
    private String employeeName;

    @Column(name = "employee_number", nullable = false, length = 50)
    private String employeeNumber;
}
