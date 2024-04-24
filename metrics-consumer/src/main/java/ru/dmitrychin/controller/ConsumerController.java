package ru.dmitrychin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dmitrychin.entityDAO.AppMetric;
import ru.dmitrychin.service.ConsumerService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class ConsumerController {
    private final ConsumerService consumerService;

    @GetMapping()
    public ResponseEntity<List<AppMetric>> allMetrics() {
        return ResponseEntity.ok(consumerService.metricsList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppMetric> getMetricById(@PathVariable Long id) {
        AppMetric appMetric = consumerService.findById(id).orElseThrow(RuntimeException::new);
        return ResponseEntity.ok(appMetric);
    }
}
