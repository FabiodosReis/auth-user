package com.application.security.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPage<T> {
    private List<T> content;
    private CustomPageable pageable;

    public CustomPage(List<T> content, CustomPageable pageable) {
        this.content = content;
        this.pageable = pageable;
    }

}
