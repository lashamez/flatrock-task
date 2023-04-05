package com.flatrock.recommendation.service;

import com.flatrock.recommendation.model.UserBehavior;
import com.flatrock.recommendation.repository.BehaviorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BehaviorService {
    private final BehaviorRepository behaviorRepository;

    public BehaviorService(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }

    public void save(UserBehavior userBehavior) {
        userBehavior.setTimestamp(LocalDateTime.now());
        behaviorRepository.save(userBehavior);
    }
}
