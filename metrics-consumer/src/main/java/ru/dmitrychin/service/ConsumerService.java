package ru.dmitrychin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitrychin.dao.MetricDAO;
import ru.dmitrychin.entity.DiskFreeMetrics;
import ru.dmitrychin.entity.HttpRequestsMetrics;
import ru.dmitrychin.entityDAO.AppMetric;

import java.util.List;
import java.util.Optional;

import static ru.dmitrychin.model.KafkaTopic.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final MetricDAO metricDAO;

    @KafkaListener(id = DISK_METRICS_TOPIC_ID, topics = DISK_METRICS_TOPIC)
    @Transactional
    public void listenDiskFreeMetrics(List<DiskFreeMetrics> diskFreeMetrics) {
        log.info("Received listen: {}", diskFreeMetrics);
        for (DiskFreeMetrics diskFreeMetric : diskFreeMetrics) {
            AppMetric appMetric = new AppMetric(diskFreeMetric.toString());
            metricDAO.save(appMetric);
        }

    }

    @KafkaListener(id = HTTP_METRICS_TOPIC_ID, topics = HTTP_METRICS_TOPIC)
    @Transactional
    public void listenHttpMetrics(List<HttpRequestsMetrics> httpRequestsMetrics) {
        log.info("Received listen: {}", httpRequestsMetrics);
        for (HttpRequestsMetrics httpRequestsMetric : httpRequestsMetrics) {
            AppMetric appMetric = new AppMetric(httpRequestsMetric.toString());
            metricDAO.save(appMetric);
        }
    }

    @Transactional
    public List<AppMetric> metricsList() {
        return metricDAO.findAll();
    }

    @Transactional
    public Optional<AppMetric> findById(Long id) {
        return metricDAO.findById(id);
    }
}
