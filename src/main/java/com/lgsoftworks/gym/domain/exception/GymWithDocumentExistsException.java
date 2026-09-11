package com.lgsoftworks.gym.domain.exception;

public class GymWithDocumentExistsException extends RuntimeException {

    public GymWithDocumentExistsException(String document) {
        super("Ya existe un gimnasio con el documento: " + document);
    }
}