package com.atd.microservices.bytefunctionexecer.controller;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionExecObj;
import com.atd.microservices.bytefunctionexecer.domain.CrudResponse;
import com.atd.microservices.bytefunctionexecer.domain.ErrorDetails;
import com.atd.microservices.bytefunctionexecer.repositories.ByteFunctionRepository;
import com.atd.microservices.bytefunctionexecer.services.ByteFunctionExecerService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Date;

@Api(
        value = "ByteFunctionExecer service",
        produces = "application/json"
)
@RestController
@RequestMapping
@Validated
@Slf4j
public class ByteFunctionExecerRestController {

    @Autowired
    ByteFunctionRepository repository;

    @Autowired
    ByteFunctionExecerService service;


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Returns data for a given name",
            notes = "JSON Supported", response = ByteFunctionData.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @GetMapping(value = "/searchbyname/{name}",produces = { "application/json"})
    public Flux<ByteFunctionData> findByName(@ApiParam(value = "name", required = true) @PathVariable("name") String name) {
        return repository.findRecordByNameRegex(name);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Returns data for a given uniqueId",
            notes = "JSON Supported", response = ByteFunctionData.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @GetMapping(value = "/searchbyuniqueid/{uniqueId}",produces = { "application/json"})
    public Mono<ByteFunctionData> findByUniqueId(@ApiParam(value = "uniqueId", required = true) @PathVariable("uniqueId") String uniqueId) {
        return repository.findOneByUniqueId(uniqueId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Returns all data for bytefunctions",
            notes = "JSON Supported", response = ByteFunctionData[].class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @GetMapping(value = "/getallfunctions",produces = { "application/json"})
    public Flux<ByteFunctionData> getAllByteFunctions() {
        return repository.findAll();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Upsert bytefunction ",
            notes = "JSON Supported", response = CrudResponse.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @PutMapping(value = "/update",produces = { "application/json"})
    public Mono<CrudResponse> upsertByteFunctions(@ApiParam(value = "bytefunction data object", required = true) @Valid @RequestBody ByteFunctionData request) {
        return service.upsertRecord(request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Upsert bytefunction ",
            notes = "JSON Supported", response = CrudResponse.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @PostMapping(value = "/insert",produces = { "application/json"})
    public Mono<CrudResponse> postByteFunctions(@ApiParam(value = "bytefunction data object", required = true) @Valid @RequestBody ByteFunctionData request) {
        return service.upsertRecord(request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete specific bytefunction ",
            notes = "JSON Supported", response = CrudResponse.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @DeleteMapping(value = "/delete/{uniqueId}",produces = { "application/json"})
    public Mono<CrudResponse> deleteByteFunctions(@ApiParam(value = "Unique Id", required = true) @PathVariable("uniqueId") String uniqueId) {
        return service.deleteRecord(uniqueId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Test function given input, output, and script",
            notes = "JSON Supported", response = ByteFunctionExecObj.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @PostMapping(value = "/testfunction",produces = { "application/json"})
    public Mono<ByteFunctionExecObj> testFunction(@ApiParam(value = "bytefunction data object", required = true) @Valid @RequestBody ByteFunctionExecObj request) throws Exception {
        return service.dryRun(request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "execute bytefunction by an id",
            notes = "JSON Supported", response = ByteFunctionExecObj.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Fields are with validation errors", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Data not found for given vendor products", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Request not acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 424, message = "Request not processed due to failed dependency", response = ErrorDetails.class)
    })
    @PostMapping(value = "/execute",produces = { "application/json"})
    public Mono<ByteFunctionExecObj> execByteFunctions(@ApiParam(value = "bytefunction data object", required = true) @Valid @RequestBody ByteFunctionExecObj request) {
         return service.runByteFunction(request);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class, Throwable.class})
    public final ResponseEntity<ErrorDetails> handleRuntimeExceptions(Exception ex) {

        HttpStatus responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace();

        ErrorDetails errorDetails = new ErrorDetails(new Date(), responseCode.toString() ,ex.getMessage());

        log.error("Response Code: {}, Message: {}", responseCode.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, responseCode);
    }
}
