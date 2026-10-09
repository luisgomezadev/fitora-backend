package com.lgsoftworks.customer.infrastructure.adapter.out.persistence;

import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.model.CustomerSearchCriteria;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.mapper.CustomerDboMapper;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.repository.CustomerRepository;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerJpaAdapter implements CustomerRepositoryPort {

    private final CustomerRepository customerRepository;
    private final CustomerDboMapper customerDboMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerRepository.save(customerDboMapper.toEntity(customer));
        return customerDboMapper.toDomain(customerEntity);
    }

    @Override
    public Optional<Customer> findByIdAndGymId(UUID id, UUID gymId) {
        return customerRepository
                .findByIdAndGymId(id, gymId)
                .map(customerDboMapper::toDomain);
    }

    @Override
    public List<Customer> findAllByIdInAndGymId(Collection<UUID> ids, UUID gymId) {
        return customerRepository.findAllByIdInAndGymId(ids, gymId)
                .stream()
                .map(customerDboMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findByDocumentNumberAndGymId(String documentNumber, UUID gymId) {
        return customerRepository
                .findByDocumentNumberAndGymId(documentNumber, gymId)
                .map(customerDboMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmailAndGymId(String email, UUID gymId) {
        return customerRepository
                .findByEmailAndGymId(email, gymId)
                .map(customerDboMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByPhoneAndGymId(String phone, UUID gymId) {
        return customerRepository
                .findByPhoneAndGymId(phone, gymId)
                .map(customerDboMapper::toDomain);
    }

    @Override
    public Page<Customer> findByGymId(UUID gymId, CustomerSearchCriteria criteria, Pageable pageable) {
        Specification<CustomerEntity> spec = CustomerSpecification.withCriteria(gymId, criteria);
        Page<CustomerEntity> entities = customerRepository.findAll(spec, pageable);
        return entities.map(customerDboMapper::toDomain);
    }

    @Override
    public boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId) {
        return customerRepository.existsByDocumentNumberAndGymId(documentNumber, gymId);
    }

    @Override
    public boolean existsByEmailAndGymId(String email, UUID gymId) {
        return customerRepository.existsByEmailAndGymId(email, gymId);
    }

    @Override
    public boolean existsByPhoneAndGymId(String phone, UUID gymId) {
        return customerRepository.existsByPhoneAndGymId(phone, gymId);
    }
}