package com.lgsoftworks.customer.infrastructure.adapter.out.persistence.specification;

import com.lgsoftworks.customer.domain.model.CustomerSearchCriteria;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerSpecification {

    private CustomerSpecification() {}

    public static Specification<CustomerEntity> withCriteria(UUID gymId, CustomerSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("gymId"), gymId));

            if (criteria != null) {

                if (criteria.search() != null && !criteria.search().isBlank()) {
                    String[] tokens = criteria.search().trim().toLowerCase().split("\\s+");

                    for (String token : tokens) {
                        String pattern = "%" + token + "%";

                        Predicate tokenMatch = cb.or(
                                cb.like(cb.lower(root.get("firstName")), pattern),
                                cb.like(cb.lower(root.get("lastName")), pattern),
                                cb.like(cb.lower(root.get("documentNumber")), pattern),
                                cb.like(cb.lower(root.get("email")), pattern)
                        );

                        predicates.add(tokenMatch);
                    }
                }

                if (criteria.documentType() != null) {
                    predicates.add(cb.equal(root.get("documentType"), criteria.documentType()));
                }

                if (criteria.status() != null) {
                    predicates.add(cb.equal(root.get("status"), criteria.status()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}