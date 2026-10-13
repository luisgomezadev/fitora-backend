package com.lgsoftworks.membership.domain.port.out;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.model.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepositoryPort {

    Membership save(Membership membership);

    boolean existsActiveByCustomerIdAndGymId(UUID customerId, UUID gymId);

    Page<Membership> findByCustomerIdAndGymId(UUID customerId, UUID gymId, Pageable pageable);

    Page<Membership> findByGymIdAndStatus(UUID gymId, MembershipStatus status, Pageable pageable);

    Optional<Membership> findByIdAndGymId(UUID id, UUID gymId);

    Optional<Membership> findActiveByCustomerIdAndGymId(UUID customerId, UUID gymId, LocalDate date);

}
