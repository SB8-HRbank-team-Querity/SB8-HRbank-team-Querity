package com.sprint.mission.sb8hrbankteamquerity.entity;

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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EntityListeners(AuditingEntityListener.class)
public class EmployeeHistory extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeHistoryType type;

    @Column(name = "memo", nullable = false)
    private String memo;

    @Column(name = "ip_address", nullable = false, length = 200)
    private String ip_address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "changed_detail", nullable = false)
    private Map<String, Object> changed_detail;

    @Column(name = "employee_name", nullable = false, length = 50)
    private String employee_name;

    @Column(name = "employee_number", columnDefinition = "jsonb", nullable = false, length = 50)
    private String employee_number;
}
