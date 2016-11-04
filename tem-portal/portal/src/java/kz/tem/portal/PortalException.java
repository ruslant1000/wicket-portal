package kz.tem.portal;

@SuppressWarnings("serial")
public class PortalException extends Exception{

	private String key;

	public PortalException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PortalException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PortalException(String message, Throwable cause) {
		super(message, cause);
	}
	public PortalException(String key, String message, Throwable cause) {
		super(message, cause);
		this.key=key;
	}

	public PortalException(String message) {
		super(message);
	}
	public PortalException(String key, String message) {
		super(message);
		this.key=key;
	}

	public PortalException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
