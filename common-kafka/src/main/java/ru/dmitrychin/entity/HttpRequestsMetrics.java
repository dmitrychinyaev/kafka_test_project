package ru.dmitrychin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestsMetrics {
    private Set<String> exceptionSet;
    private Set<String> methodSet;
    private Set<String> errorSet;
    private Set<String> uriSet;
}
