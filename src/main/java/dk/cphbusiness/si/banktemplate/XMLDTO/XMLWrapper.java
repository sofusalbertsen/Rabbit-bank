package dk.cphbusiness.si.banktemplate.XMLDTO;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import dk.cphbusiness.si.banktemplate.BLDTOResponse;

public class XMLWrapper {
public static String BLDTOToXML(XMLBankLoanDTO bldto) throws Exception {
	Serializer serializer = new Persister();
	ByteArrayOutputStream bout= new ByteArrayOutputStream();
	serializer.write(bldto, bout);
	return bout.toString();
}
public static String responseToXML(BLDTOResponse bldto) throws Exception{
	Serializer serializer = new Persister();
	ByteArrayOutputStream bout= new ByteArrayOutputStream();
	serializer.write(bldto, bout);
	return bout.toString();
}
public static XMLBankLoanDTO XMLTODTO(String xml) throws Exception {
	Serializer serializer = new Persister();
	return serializer.read(XMLBankLoanDTO.class, xml);
}
public static void main(String[] args) {
	XMLBankLoanDTO dto = new XMLBankLoanDTO();
	dto.setLoanAmount(10d);
	dto.setLoanDuration(new Date(0));
	dto.setCreditScore(685);
	dto.setSsn(12345678);
	try {
		System.out.println(XMLWrapper.BLDTOToXML(dto));
		System.out.println(XMLWrapper.BLDTOToXML(XMLWrapper.XMLTODTO(XMLWrapper.BLDTOToXML(dto))));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
