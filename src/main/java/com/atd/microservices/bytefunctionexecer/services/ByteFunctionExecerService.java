package com.atd.microservices.bytefunctionexecer.services;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionExecObj;
import com.atd.microservices.bytefunctionexecer.domain.CrudResponse;
import reactor.core.publisher.Mono;

public interface ByteFunctionExecerService {

    Mono<CrudResponse> upsertRecord(ByteFunctionData request);

    Mono<CrudResponse> deleteRecord(String uniqueId);

    Mono<ByteFunctionExecObj> dryRun(ByteFunctionExecObj request) throws Exception;

    Mono<ByteFunctionExecObj> runByteFunction(ByteFunctionExecObj request);

}
