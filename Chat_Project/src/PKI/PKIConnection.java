package PKI;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PKIConnection {
	
	private DataInputStream in;
	private DataOutputStream ou;
	private String name;
	private String path;
	
	public DataInputStream getIn() {
		return in;
	}
	public void setIn(DataInputStream in) {
		this.in = in;
	}
	public DataOutputStream getOu() {
		return ou;
	}
	public void setOu(DataOutputStream ou) {
		this.ou = ou;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
