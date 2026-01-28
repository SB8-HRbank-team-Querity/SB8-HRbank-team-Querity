package com.sprint.mission.sb8hrbankteamquerity.service.specification;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeHistoryType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeHistorySpecification {

    public static Specification<EmployeeHistory> filter(EmployeeHistoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.type() != null && !filter.type().equals(EmployeeHistoryType.ALL)) {
                predicates.add(cb.equal(root.get("type"), filter.type()));
            }

            if (filter.employeeNumber() != null && !filter.employeeNumber().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("employeeNumber")),
                        "%" + filter.employeeNumber().toLowerCase() + "%"
                    )
                );
            }

            if (filter.memo() != null && !filter.memo().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("memo")),
                        "%" + filter.memo().toLowerCase() + "%"
                    )
                );
            }

            if (filter.ipAddress() != null && !filter.ipAddress().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("ipAddress")),
                        "%" + filter.ipAddress().toLowerCase() + "%"
                    )
                );
            }

            if (filter.atFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.atFrom()));
            }

            if (filter.atTo() != null) {
                predicates.add(cb.lessThan(root.get("createdAt"), filter.atTo()));
            }

            // Cursor 조건 (핵심)
            if (filter.cursor() != null) {
                Predicate older =
                    cb.lessThan(root.get("createdAt"), filter.cursor());

                if (filter.idAfter() != null) {
                    Predicate sameTimeLowerId =
                        cb.and(
                            cb.equal(root.get("createdAt"), filter.cursor()),
                            cb.lessThan(root.get("id"), filter.idAfter())
                        );
                    predicates.add(cb.or(older, sameTimeLowerId));
                } else {
                    predicates.add(older);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

