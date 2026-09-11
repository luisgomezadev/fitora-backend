package com.lgsoftworks.student.application.dto.request;

import com.lgsoftworks.student.domain.enums.DocumentType;
import com.lgsoftworks.student.domain.enums.StudentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFilter {

    private String search;

    private DocumentType documentType;

    private StudentStatus status;
}
