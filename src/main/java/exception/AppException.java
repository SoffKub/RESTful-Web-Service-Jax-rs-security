package exception;

public class AppException extends Exception {


	private static final long serialVersionUID = -8999932578270387947L;

	Integer status;

	int errorcode;

	String developerSurprice;

	public AppException(int status, int code, String message,
						String developerSurprice) {
		super(message);
		this.status = status;
		this.errorcode = code;
		this.developerSurprice = developerSurprice;

	}

	public AppException() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getDeveloperSurprice() {
		return developerSurprice;
	}

	public void setDeveloperSurpeice(String developerSurprice) {
		this.developerSurprice = developerSurprice;
	}
}
