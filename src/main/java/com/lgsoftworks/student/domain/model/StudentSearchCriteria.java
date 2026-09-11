package com.lgsoftworks.student.domain.model;

import com.lgsoftworks.student.domain.enums.DocumentType;
import com.lgsoftworks.student.domain.enums.StudentStatus;

public record StudentSearchCriteria(
        String search,
        DocumentType documentType,
        StudentStatus status
) {
    public static StudentSearchCriteria of(
            String search,
            DocumentType documentType,
            StudentStatus status
    ) {
        return new StudentSearchCriteria(search, documentType, status);
    }
}