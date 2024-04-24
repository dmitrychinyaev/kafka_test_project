package ru.dmitrychin.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmitrychin.entity.DiskFreeMetrics;
import ru.dmitrychin.entity.HttpRequestsMetrics;

import java.util.Objects;
import java.util.Set;

import static ru.dmitrychin.model.KafkaTopic.DISK_METRICS_TOPIC;
import static ru.dmitrychin.model.KafkaTopic.HTTP_METRICS_TOPIC;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KafkaController {
    @Autowired
    private final KafkaTemplate<Object, Object> template;
    @Autowired(required = false)
    private MetricsEndpoint actuatorMetrics;

    @PostMapping(path = "/send/httpMetrics")
    public void sendHttpMetrics() {
        var httpMetrics = actuatorMetrics.metric("http.server.requests", null);
        HttpRequestsMetrics httpRequestsMetrics = HttpRequestsMetrics.builder()
                .errorSet(findValuesByTag(httpMetrics, "error"))
                .methodSet(findValuesByTag(httpMetrics, "method"))
                .exceptionSet(findValuesByTag(httpMetrics, "exception"))
                .uriSet(findValuesByTag(httpMetrics, "uri"))
                .build();
        template.send(HTTP_METRICS_TOPIC, httpRequestsMetrics);
    }

    @PostMapping(path = "/send/diskMetrics")
    public void sendDiskMetrics() {
        var diskMetrics = actuatorMetrics.metric("disk.free", null);
        DiskFreeMetrics diskFreeMetrics = DiskFreeMetrics.builder()
                .description(diskMetrics.getDescription())
                .byteFree(diskMetrics.getMeasurements().get(0).getValue())
                .path(diskMetrics.getAvailableTags().get(0).getValues().toString())
                .build();
        template.send(DISK_METRICS_TOPIC, diskFreeMetrics);
    }

    private Set<String> findValuesByTag(MetricsEndpoint.MetricDescriptor metricDescriptor, String tag) {
        return Objects.requireNonNull(metricDescriptor.getAvailableTags().stream()
                .filter(availableTag -> tag.equals(availableTag.getTag())).findFirst().orElse(null)).getValues();

    }
}
