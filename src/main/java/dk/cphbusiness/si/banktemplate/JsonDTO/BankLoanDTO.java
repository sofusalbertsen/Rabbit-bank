package dk.cphbusiness.si.banktemplate.JsonDTO;

public class BankLoanDTO {
	public BankLoanDTO() {
		// TODO Auto-generated constructor stub
	}

	int ssn;
	int creditScore;
	Double loanAmount;
	int loanDuration;
	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}


	public int getSsn() {
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

	public int getLoanDuration() {
		return loanDuration;
	}

	public void setLoanDuration(int loanDuration) {
		this.loanDuration = loanDuration;
	}

}
