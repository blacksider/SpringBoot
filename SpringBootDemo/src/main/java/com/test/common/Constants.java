package com.test.common;

public final class Constants {

	public enum LdapGroupType {
		SERVER("SERVER"), GROUP("GROUP");

		private String value;

		LdapGroupType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return this.value;
		}

		public static LdapGroupType parse(String typeStr) {
			LdapGroupType ret = null;
			for (LdapGroupType type : LdapGroupType.values()) {
				if (type.getValue().equals(typeStr)) {
					ret = type;
					break;
				}
			}
			return ret;
		}
	}
}
