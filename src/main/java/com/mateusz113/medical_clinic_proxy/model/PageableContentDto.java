package com.mateusz113.medical_clinic_proxy.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PageableContentDto<S>(
        long totalEntries,
        int totalNumberOfPages,
        int pageNumber,
        List<S> content
) {
}
