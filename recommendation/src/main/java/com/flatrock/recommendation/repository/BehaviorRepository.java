package com.flatrock.recommendation.repository;

import com.flatrock.recommendation.model.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorRepository extends JpaRepository<UserBehavior, Long> {

}
