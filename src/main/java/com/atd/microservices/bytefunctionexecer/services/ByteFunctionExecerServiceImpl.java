package com.atd.microservices.bytefunctionexecer.services;

import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionData;
import com.atd.microservices.bytefunctionexecer.domain.ByteFunctionExecObj;
import com.atd.microservices.bytefunctionexecer.domain.CrudResponse;
import com.atd.microservices.bytefunctionexecer.repositories.ByteFunctionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import static com.atd.microservices.bytefunctionexecer.configs.ApplicationConstants.*;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import reactor.core.publisher.Mono;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class ByteFunctionExecerServiceImpl implements ByteFunctionExecerService {

    @Autowired
    ByteFunctionRepository repository;
    @Override
    public Mono<CrudResponse> upsertRecord(ByteFunctionData request) {
        //check to see if the request has a uniqueId;
        if (!request.hasAUniqueId()){
            //generate uniqueId if it does not have a unique Id
            String id = request.getName() + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
            request.setUniqueId(id);
        }

       return repository.upsertByteFunction(request).switchIfEmpty(Mono.just(request)).map((data)->{
           CrudResponse res = new CrudResponse();
            res.setStatus(SUCCESS_MESSAGE);
            res.setUniqueId(request.getUniqueId());
            return res;
        });
    }

    @Override
    public Mono<CrudResponse> deleteRecord(String uniqueId) {
        return repository.deleteFunctionByUniqueId(uniqueId).map((data)->{
            CrudResponse res = new CrudResponse();
            res.setStatus(SUCCESS_MESSAGE);
            res.setUniqueId(uniqueId);
            return res;
        });
    }

    @Override
    public Mono<ByteFunctionExecObj> runByteFunction(ByteFunctionExecObj request){
       return repository
                .findOneByUniqueId(request.getUniqueId())
                .switchIfEmpty(Mono.just(new ByteFunctionData()))
                .map( byteFunctionData ->{
                    if(!byteFunctionData.hasAUniqueId()){
                        throw new RuntimeException("No unique id found for request :" + request.toString());
                    }

                    log.info("Running function️ : " + byteFunctionData.getUniqueId());

                    try{
                        runTheFunction(request, byteFunctionData);
                    }catch (Exception e){
                        throw new RuntimeException(e.getLocalizedMessage());
                    }

                    return request;
                });
    }

    @Override
    public Mono<ByteFunctionExecObj> dryRun(ByteFunctionExecObj request) throws Exception {
        ByteFunctionData byteFunctionData = new ByteFunctionData();
        byteFunctionData.setScript(request.getScript());
        try {
            runTheFunction(request, byteFunctionData);
            request.setStatus(SUCCESS_MESSAGE);
            request.setMetadata(null);
            request.setOptionalParams(null);
            request.setScript(null);
            request.setLanguage(null);
        } catch(Exception e) {
            request = new ByteFunctionExecObj();
            request.setStatus(ERROR_MESSAGE);
            request.setDetails(e.getMessage());
        }
        return Mono.just(request);
    }

    void runTheFunction(ByteFunctionExecObj request, ByteFunctionData data) throws Exception{
        File sourceFile = null;
        String funcUuid = null;
        try{
            String script  = data.getScript();
            //generate dynamic name
            funcUuid = RandomStringUtils.randomAlphabetic(10).toUpperCase();
            request.setFuncUuid(funcUuid);
            script = "package EdiPackage"+funcUuid+";\n" + script;
            script = script.replaceFirst("class.*Func.*\\ i", "class "+"Func"+funcUuid + " i");

            FileWriter writer = new FileWriter("generated/Func"+funcUuid+".java");
            sourceFile = new File("generated/Func"+funcUuid+".java");

            writer.write(script);
            writer.close();

            log.debug("Created file");
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                    null, null, null);

            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays
                    .asList(new File("generated/")));
            // Compile the file
            //todo figure out how to catch compilation error from task and return correlated error
            compiler.getTask(null, fileManager, new FuncDiagnosticListener(), null, null,
                            fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)))
                    .call();
            fileManager.close();

            runIt(request);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            sourceFile.delete();
            try{
                String filePath = "generated/EdiPackage"+funcUuid+"/"+"Func"+funcUuid+".class";
                log.debug(filePath);
                File classFile = new File(filePath);
                classFile.delete();
                File dir = new File("generated/EdiPackage"+funcUuid+"/");
                dir.delete();
            }catch (Exception e){
                log.error("Issue deleting builds :" + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void runIt(ByteFunctionExecObj request) throws Exception {
        try {
            URI path = new File("generated/").toURI();
            log.debug("in run it : " +  path.toString());
            URL[] arr = new URL[] {path.toURL()};

            URLClassLoader classLoader = URLClassLoader.newInstance(arr);
            Class<?> cls = Class.forName("EdiPackage"+request.getFuncUuid()+".Func"+request.getFuncUuid(), true, classLoader);
            Object instance = cls.getDeclaredConstructor().newInstance();
            ByteFunc func = ((ByteFunc) instance);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> optionalParams = mapper.
                    convertValue(request.getOptionalParams(), new TypeReference<Map<String, String>>(){});

            func.execute((ObjectNode) request.getInput(),
                    (ObjectNode) request.getOutput(),
                    (ObjectNode) request.getMetadata(),
                    optionalParams);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static final class FuncDiagnosticListener implements DiagnosticListener {
        //only gets executed when theres an error on compilation
        @Override
        public void report(Diagnostic diagnostic) {
            if(diagnostic.getKind().equals(Diagnostic.Kind.ERROR)){
                throw new RuntimeException("Couldnt compile due to :" + diagnostic);
            }else{
                log.info(diagnostic.toString());
            }
        }
    }
}
