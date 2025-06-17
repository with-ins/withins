package com.withins.core.welfarecenter.component;

import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WelfareCenterReader {

    private final WelfareCenterRepository welfareCenterRepository;

    public Optional<WelfareCenter> read(Long welfareCenterId) {
        return welfareCenterRepository.findById(welfareCenterId);
    }

    public Optional<WelfareCenter> readBy(String welfareCenterName) {
        return welfareCenterRepository.findByName(welfareCenterName);
    }

    public List<WelfareCenter> readAll() {
        return welfareCenterRepository.findAll();
    }
}
