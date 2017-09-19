package dk.cphbusiness.si.banktemplate.JsonDTO;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class JsonWrapper {
public static String BLDTOToJson(BankLoanDTO bldto) {
	ObjectMapper o = new ObjectMapper();
	try {
		return o.writeValueAsString(bldto);
	} catch (JsonGenerationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
public static BankLoanDTO jsonToBLDTO(String jSon) throws JsonParseException, JsonMappingException, IOException {
	BankLoanDTO bankLoanDTO=null;
	bankLoanDTO= new ObjectMapper().readValue(jSon, BankLoanDTO.class);
	
	return bankLoanDTO;
	
}
}
