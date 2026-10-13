package com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.specification;

import com.lgsoftworks.attendance.domain.model.AttendanceSearchCriteria;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.entity.AttendanceEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttendanceSpecification {

    private AttendanceSpecification() {}

    public static Specification<AttendanceEntity> withCriteria(UUID gymId, AttendanceSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("gymId"), gymId));

            if (criteria != null) {

                if (criteria.search() != null && !criteria.search().isBlank()) {
                    String[] tokens = criteria.search().trim().toLowerCase().split("\\s+");

                    for (String token : tokens) {
                        predicates.add(
                                cb.like(cb.lower(root.get("customerName")), "%" + token + "%")
                        );
                    }
                }

                if (criteria.startDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            root.get("checkIn"),
                            criteria.startDate().atStartOfDay()
                    ));
                }

                if (criteria.endDate() != null) {
                    predicates.add(cb.lessThan(
                            root.get("checkIn"),
                            criteria.endDate().plusDays(1).atStartOfDay()
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}