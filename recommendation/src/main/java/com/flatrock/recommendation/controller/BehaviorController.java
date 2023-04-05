package com.flatrock.recommendation.controller;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.recommendation.model.UserBehavior;
import com.flatrock.recommendation.service.BehaviorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@Transactional
public class BehaviorController {

    private final Logger log = LoggerFactory.getLogger(BehaviorController.class);

    private static final String ENTITY_NAME = "Recommendation";
    private final BehaviorService behaviorService;

    public BehaviorController(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }

    @PostMapping("/behavior")
    public ResponseEntity<Void> createBehavior(@Valid @RequestBody UserBehavior userBehavior) throws URISyntaxException {
        log.debug("REST request to save User behavior : {}", userBehavior);
        if (userBehavior.getId() != null) {
            throw new BadRequestAlertException("A new behavior cannot already have an ID", ENTITY_NAME, "idexists");
        }

        behaviorService.save(userBehavior);

        return ResponseEntity.ok().build();
    }

}
