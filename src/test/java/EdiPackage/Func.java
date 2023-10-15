package EdiPackage;//Let the app control the package defintion

import com.atd.microservices.bytefunctionexecer.services.ByteFunc;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
public class Func implements ByteFunc{

        public void execute(ObjectNode input, ObjectNode output, ObjectNode metadata, Map<String, String> optionalParams) {

                System.out.println(input.toString());

                input.put("Chicken", "🐔");

                System.out.println(optionalParams.toString());

                System.out.println("Hello world") ;

        }

}