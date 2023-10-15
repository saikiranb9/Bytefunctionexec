package com.atd.microservices.bytefunctionexecer.repositories;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ByteFunctionRepository extends ReactiveMongoRepository<ByteFunctionData, String>, ByteFunctionRepositoryCustom {
    Mono<ByteFunctionData> findOneByUniqueId(String uniqueId);
}