package com.lgsoftworks.customer.domain.port.out;

import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.domain.model.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findByIdAndGymId(UUID id, UUID gymId);

    List<Customer> findAllByIdInAndGymId(Collection<UUID> ids, UUID gymId);

    Optional<Customer> findByDocumentNumberAndGymId(String documentNumber, UUID gymId);

    Optional<Customer> findByEmailAndGymId(String email, UUID gymId);

    Optional<Customer> findByPhoneAndGymId(String phone, UUID gymId);

    Page<Customer> findByGymId(
            UUID gymId,
            CustomerSearchCriteria criteria,
            Pageable pageable
    );

    boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId);

    boolean existsByEmailAndGymId(String email, UUID gymId);

    boolean existsByPhoneAndGymId(String phone, UUID gymId);

}
