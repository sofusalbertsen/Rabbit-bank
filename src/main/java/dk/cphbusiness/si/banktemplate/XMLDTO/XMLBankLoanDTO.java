package dk.cphbusiness.si.banktemplate.XMLDTO;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root(name="LoanRequest")
public class XMLBankLoanDTO {
	@Element
	private long ssn;
	@Element
	private int creditScore;
	@Element
	private Double loanAmount;
	@Element
	private Date loanDuration;
	public long getSsn() {
		return ssn;
	}
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	public Double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public Date getLoanDuration() {
		return loanDuration;
	}
	public void setLoanDuration(Date loanDuration) {
		this.loanDuration = loanDuration;
	}
	public int getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	

}
