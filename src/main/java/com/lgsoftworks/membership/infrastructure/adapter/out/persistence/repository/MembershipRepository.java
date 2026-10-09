package com.lgsoftworks.membership.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.infrastructure.adapter.out.persistence.entity.MembershipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, UUID> {

    boolean existsByCustomerIdAndGymIdAndStatus(UUID customerId, UUID gymId, MembershipStatus status);

    Optional<MembershipEntity> findByIdAndGymId(UUID id, UUID gymId);

    Page<MembershipEntity> findByGymIdAndCustomerId(UUID gymId, UUID customerId, Pageable pageable);

    Page<MembershipEntity> findByGymIdAndStatus(UUID gymId, MembershipStatus status, Pageable pageable);
}