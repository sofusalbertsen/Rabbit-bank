package dk.cphbusiness.si.banktemplate;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="LoanResponse")
public class BLDTOResponse {
	@Element
private double interestRate=0;
	@Element
	private long ssn;

public long getSsn() {
	return ssn;
}

public void setSsn(long ssn) {
	this.ssn = ssn;
}

public double getInterestRate() {
	return interestRate;
}

public void setInterestRate(double interestRate) {
	this.interestRate = interestRate;
}
}
