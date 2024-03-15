package cn.xiaoph.library.http;

public class RequestToken {

	private String key = "token";
	private String value;

	public RequestToken() {
		super();
	}

	public RequestToken(String value) {
		super();
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
