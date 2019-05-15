package Message;

public class PKIMessage {
	private Integer contentCode;
	private String message;
	private String path;
	private String sender;
	
	public Integer getContentCode() {
		return contentCode;
	}
	public void setContentCode(Integer contentCode) {
		this.contentCode = contentCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
}
