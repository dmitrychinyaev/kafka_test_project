package ru.dmitrychin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dmitrychin.entityDAO.AppMetric;
@Repository
public interface MetricDAO extends JpaRepository <AppMetric,Long> {
}
