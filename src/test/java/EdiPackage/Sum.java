package EdiPackage;

import com.atd.microservices.bytefunctionexecer.services.ByteFunc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class Sum implements ByteFunc {

    public void execute(ObjectNode input, ObjectNode output, ObjectNode metadata, Map<String, String> optionalParams) {

        JsonNode segmentsArray = output.get("data")
                .get("interchanges")
                .get(0)
                .get("functional_groups")
                .get(0)
                .get("transactions")
                .get(0)
                .get("segments");

        System.out.println("got segments array");
        try{
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode segmentsArrayNode = (ArrayNode) mapper.readTree(segmentsArray.asText());

            Spliterator siter = segmentsArrayNode.spliterator();

            //filter
            ArrayList<JsonNode> filteredList = new ArrayList<JsonNode>();

            siter.forEachRemaining( (node )->{
                JsonNode tempNode = (JsonNode) node;
                Boolean has = tempNode.has("IT1-6000_loop");
                if(has){
                    filteredList.add(tempNode);
                }
            });

            //mapToDouble
            Double summedVals = filteredList.stream().mapToDouble( node ->{
                final JsonNode obj = node
                        .get("IT1-6000_loop")
                        .get(0);

                System.out.println("got a it it1600 to use to calc");
                Float ittwo = obj.get("IT1_02").floatValue();
                System.out.println("got a it ittwo to use to calc : " + ittwo);
                Float itfour = obj.get("IT1_04").floatValue();
                System.out.println("got a it itfour to use to calc : " + itfour);
                return ittwo * itfour;
            }).sum();

            //assign summed vals
            System.out.println(summedVals);

            Iterator iter = segmentsArrayNode.iterator();
            for ( JsonNode jsonNode = (JsonNode)iter.next() ;iter.hasNext() == true; jsonNode = (JsonNode)iter.next()){
               if(jsonNode.has("TDS_01")){
                   ((ObjectNode) (jsonNode)).put("TDS_01", summedVals+"");
               }
            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
