package com.atd.microservices.bytefunctionexecer.repositories;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.atd.microservices.bytefunctionexecer.configs.ApplicationConstants.*;

public class ByteFunctionRepositoryCustomImpl implements ByteFunctionRepositoryCustom{

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<ByteFunctionData> upsertByteFunction(ByteFunctionData data) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uniqueId").is(data.getUniqueId()));
        FindAndReplaceOptions options = new FindAndReplaceOptions();
        options.upsert();
        //(Query query, T replacement, FindAndReplaceOptions options, Class<T> entityType, String collectionName) {
        return mongoTemplate.findAndReplace(query, data, options, ByteFunctionData.class, COLLECTION);
    }

    public Flux<ByteFunctionData> findRecordByNameRegex(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(name));
        return mongoTemplate.find(query, ByteFunctionData.class);
    }

    @Override
    public Mono<DeleteResult> deleteFunctionByUniqueId(String uniqueId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uniqueId").is(uniqueId));
        return mongoTemplate.remove(query, COLLECTION);
    }
}
