package com.mateusz113.medical_clinic_proxy.model;

import java.util.List;

public record PageableContentDto<S>(
        long totalEntries,
        int totalNumberOfPages,
        int pageNumber,
        List<S> content
) {
}
