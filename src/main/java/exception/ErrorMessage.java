package exception;


import org.springframework.beans.BeanUtils;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ErrorMessage {

	/**
	 * contains the same HTTP Status error code returned by the server
	 */
	@XmlElement(name = "status")
	int status;

	/**
	 * application specific error code
	 */
	@XmlElement(name = "errorcode")
	int errorcode;

	/**
	 * message describing the error
	 */
	@XmlElement(name = "message")
	String message;

	/**
	 * extra information that might useful for developers
	 */
	@XmlElement(name = "developerSurprice")
	String developerSurprice;

	public ErrorMessage(AppException ex) {
		BeanUtils.copyProperties(this, ex);
	}

	public ErrorMessage(NotFoundException ex) {
		this.status = Response.Status.NOT_FOUND.getStatusCode();
		this.message = ex.getMessage();

	}

	public ErrorMessage() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperSurprice() {
		return developerSurprice;
	}

	public void setDeveloperSurprice(String developerSurprice) {
		this.developerSurprice = developerSurprice;
	}
}


