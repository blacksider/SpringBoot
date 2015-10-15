package com.test.vo;

public enum GroupType {
	LOCAL("Local"), COUNTRY("Country");

	private String value;

	GroupType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static GroupType parse(String value) {
		GroupType ret = null;
		for (GroupType type : GroupType.values()) {
			if (type.getValue().equals(value)) {
				ret = type;
				break;
			}
		}
		return ret;
	}
}
