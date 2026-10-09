package com.lgsoftworks.membership.infrastructure.adapter.out.persistence;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.membership.domain.port.out.MembershipRepositoryPort;
import com.lgsoftworks.membership.infrastructure.adapter.out.persistence.entity.MembershipEntity;
import com.lgsoftworks.membership.infrastructure.adapter.out.persistence.mapper.MembershipDboMapper;
import com.lgsoftworks.membership.infrastructure.adapter.out.persistence.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MembershipJpaAdapter implements MembershipRepositoryPort {

    private final MembershipRepository membershipRepository;
    private final MembershipDboMapper membershipDboMapper;

    @Override
    public Membership save(Membership membership) {
        MembershipEntity membershipEntity = membershipRepository.save(membershipDboMapper.toEntity(membership));
        return membershipDboMapper.toDomain(membershipEntity);
    }

    @Override
    public boolean existsActiveByCustomerIdAndGymId(UUID customerId, UUID gymId) {
        return membershipRepository.existsByCustomerIdAndGymIdAndStatus(
                customerId, gymId, MembershipStatus.ACTIVE
        );
    }

    @Override
    public Page<Membership> findByCustomerIdAndGymId(UUID customerId, UUID gymId, Pageable pageable) {
        return membershipRepository.findByGymIdAndCustomerId(gymId, customerId, pageable)
                .map(membershipDboMapper::toDomain);
    }

    @Override
    public Page<Membership> findByGymIdAndStatus(UUID gymId, MembershipStatus status, Pageable pageable) {
        return membershipRepository.findByGymIdAndStatus(gymId, status, pageable)
                .map(membershipDboMapper::toDomain);
    }

    @Override
    public Optional<Membership> findByIdAndGymId(UUID id, UUID gymId) {
        return membershipRepository
                .findByIdAndGymId(id, gymId)
                .map(membershipDboMapper::toDomain);
    }

}