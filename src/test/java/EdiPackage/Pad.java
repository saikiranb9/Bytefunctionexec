package EdiPackage;//Let the app control the package defintion

import com.atd.microservices.bytefunctionexecer.services.ByteFunc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
public class Pad implements ByteFunc{

    public void execute(ObjectNode input, ObjectNode output, ObjectNode metadata, Map<String, String> optionalParams) {

        System.out.println("Doing Padding operation");
        JsonNode headerinfo = output.get("data")
                .get("interchanges")
                .get(0);

        JsonNode senderIdObj = headerinfo.get("ISA_06_SenderId");
        String senderId = senderIdObj.toString();
        if(senderId.length() < 15 ) {
            senderId = senderId + StringUtils.repeat(" ", 15 - senderId.length());
        }

        ((ObjectNode)headerinfo).put("ISA_06_SenderId", senderId);

        JsonNode recieverIdObj = headerinfo.get("ISA_08_ReceiverId");
        String recieverId = recieverIdObj.toString();

        if(recieverId.length() < 15 ) {
            recieverId = recieverId + StringUtils.repeat(" ", 15 - recieverId.length());
        }
        ((ObjectNode)headerinfo).put("ISA_08_ReceiverId", recieverId);
    }

}