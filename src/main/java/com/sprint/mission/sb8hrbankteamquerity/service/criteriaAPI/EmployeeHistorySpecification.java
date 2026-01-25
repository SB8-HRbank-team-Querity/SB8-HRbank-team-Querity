package com.sprint.mission.sb8hrbankteamquerity.service.criteriaAPI;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class EmployeeHistorySpecification {

    public static Specification<EmployeeHistory> filter(EmployeeHistoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.idAfter() != null) {
                predicates.add(cb.greaterThan(root.get("id"), filter.idAfter()));
            }

            if (filter.type() != null) {
                predicates.add(cb.equal(root.get("type"), filter.type()));
            }

            if (filter.employeeNumber() != null && !filter.employeeNumber().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("employeeNumber")),
                        "%" + filter.employeeNumber().toLowerCase() + "%"
                    )
                );
            }

            if (filter.memo() != null && !filter.memo().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("memo")),
                        "%" + filter.memo().toLowerCase() + "%"
                    )
                );
            }

            if (filter.ipAddress() != null && !filter.ipAddress().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("ipAddress")),
                        "%" + filter.ipAddress().toLowerCase() + "%"
                    )
                );
            }

            if (filter.atFrom() != null) {
                Instant from = LocalDate.parse(filter.atFrom())
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }

            if (filter.atTo() != null) {
                Instant to = LocalDate.parse(filter.atTo())
                    .plusDays(1)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC);
                predicates.add(cb.lessThan(root.get("createdAt"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

