package com.lgsoftworks.gym.domain.model;

import com.lgsoftworks.gym.domain.enums.GymStatus;
import com.lgsoftworks.gym.domain.exception.InvalidGymDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Gym {

    private UUID id;
    private String name;
    private String document;
    private String email;
    private String phone;
    private String address;
    private String city;
    private GymStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Gym(
            String name,
            String document,
            String email,
            String phone,
            String address,
            String city
    ) {
        validateName(name);
        validateDocument(document);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);
        validateCity(city);

        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.document = document.trim();
        this.email = email != null ? email.trim().toLowerCase() : null;
        this.phone = phone != null ? phone.trim() : null;
        this.address = address.trim();
        this.city = city.trim();
        this.status = GymStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Gym create(
            String name,
            String document,
            String email,
            String phone,
            String address,
            String city
    ) {
        return new Gym(
                name,
                document,
                email,
                phone,
                address,
                city
        );
    }

    public static Gym reconstitute(
            UUID id,
            String name,
            String document,
            String email,
            String phone,
            String address,
            String city,
            GymStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        Gym gym = new Gym();

        gym.id = id;
        gym.name = name;
        gym.document = document;
        gym.email = email;
        gym.phone = phone;
        gym.address = address;
        gym.city = city;
        gym.status = status;
        gym.createdAt = createdAt;
        gym.updatedAt = updatedAt;

        return gym;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidGymDataException("El nombre del gym es requerido");
        }

        if (name.trim().length() < 3) {
            throw new InvalidGymDataException(
                    "El nombre del gym no puede ser menor a 3 caracteres"
            );
        }

        if (name.trim().length() > 100) {
            throw new InvalidGymDataException(
                    "El nombre del gym no puede ser mayor a 100 caracteres"
            );
        }
    }

    private void validateDocument(String document) {
        if (document == null || document.isBlank()) {
            throw new InvalidGymDataException("El documento del gym es requerido");
        }

        if (document.trim().length() > 50) {
            throw new InvalidGymDataException(
                    "El documento del gym no puede ser mayor a 50 caracteres"
            );
        }
    }

    private void validateEmail(String email) {

        if (email == null || email.isBlank()) {
            return;
        }

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.trim().matches(regex)) {
            throw new InvalidGymDataException(
                    "El formato de correo del gym es invalido"
            );
        }

        if (email.trim().length() > 100) {
            throw new InvalidGymDataException(
                    "El correo del gym no puede ser mayor a 100 caracteres"
            );
        }
    }

    private void validatePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return;
        }

        if (phone.trim().length() < 7) {
            throw new InvalidGymDataException(
                    "El número de celular del gym no puede ser menor a 7 caracteres"
            );
        }

        if (phone.trim().length() > 10) {
            throw new InvalidGymDataException(
                    "El número de celular del gym no puede ser mayor a 10 caracteres"
            );
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new InvalidGymDataException("La dirección del gym es requerida");
        }
    }

    private void validateCity(String city) {
        if (city == null || city.isBlank()) {
            throw new InvalidGymDataException("La ciudad del gym es requerida");
        }
    }

    public void updateInformation(
            String name,
            String email,
            String phone,
            String address,
            String city
    ) {
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);
        validateCity(city);

        this.name = name.trim();
        this.email = email != null ? email.trim().toLowerCase() : null;
        this.phone = phone != null ? phone.trim() : null;
        this.address = address.trim();
        this.city = city.trim();

        touch();
    }

    public void activate() {
        this.status = GymStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = GymStatus.INACTIVE;
        touch();
    }

    public void suspend() {
        this.status = GymStatus.SUSPENDED;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}