package com.atd.microservices.bytefunctionexecer.services;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;

public interface ByteFunc {
    void execute(ObjectNode input, ObjectNode output, ObjectNode metadata, Map<String, String> optionalParams);
}
