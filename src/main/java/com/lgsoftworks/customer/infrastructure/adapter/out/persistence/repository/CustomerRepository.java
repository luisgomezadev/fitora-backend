package com.lgsoftworks.customer.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends
        JpaRepository<CustomerEntity, UUID>,
        JpaSpecificationExecutor<CustomerEntity> {

    Optional<CustomerEntity> findByIdAndGymId(UUID id, UUID gymId);
    List<CustomerEntity> findAllByIdInAndGymId(Collection<UUID> ids, UUID gymId);

    Optional<CustomerEntity> findByEmailAndGymId(String email, UUID gymId);
    Optional<CustomerEntity> findByDocumentNumberAndGymId(String documentNumber, UUID gymId);
    Optional<CustomerEntity> findByPhoneAndGymId(String phone, UUID gymId);

    boolean existsByEmailAndGymId(String email, UUID gymId);
    boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId);
    boolean existsByPhoneAndGymId(String phone, UUID gymId);
}