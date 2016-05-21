package oops.saturn.exception;

/**
 * 通用异常类
 *
 */
public class SaturnCommonException extends Exception{
	
	private static final long serialVersionUID = -9124929022248512280L;

	public SaturnCommonException(){
		super();
	}
	
	public SaturnCommonException(String message) {
		super(message);
	 }

	public SaturnCommonException(Throwable cause) {
		super(cause);
	}

	public SaturnCommonException(String message, Throwable cause) {
		super(message, cause);
	}
}
