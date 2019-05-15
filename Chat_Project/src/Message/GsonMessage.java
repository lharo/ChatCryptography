package Message;

public class GsonMessage {

	private String message;
	private Integer contentCode;
	private int type;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getContentCode() {
		return contentCode;
	}
	public void setContentCode(Integer contentCode) {
		this.contentCode = contentCode;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
