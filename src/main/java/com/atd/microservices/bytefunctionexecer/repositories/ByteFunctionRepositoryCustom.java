package com.atd.microservices.bytefunctionexecer.repositories;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import com.mongodb.client.result.DeleteResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ByteFunctionRepositoryCustom {
    Mono<ByteFunctionData> upsertByteFunction(ByteFunctionData data);
    Flux<ByteFunctionData> findRecordByNameRegex(String name);
    Mono<DeleteResult> deleteFunctionByUniqueId(String uniqueId);
}
