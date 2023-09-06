package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusListHomeworkRequest {
    private String syllabusID;
    private int currentPage;
    private int pageSize;
}
