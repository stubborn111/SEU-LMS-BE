package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

import java.io.Serializable;


@Data
public class WikiListRequest implements Serializable {
    private int currentPage;
    private int pageSize;
}
