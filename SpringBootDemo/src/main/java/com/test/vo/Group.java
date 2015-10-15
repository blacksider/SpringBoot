package com.test.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Group {
	private int groupId;

	private String groupName;

	@JsonProperty("groupType")
	private String groupTypeStr;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public GroupType getGroupType() {
		return GroupType.parse(this.groupTypeStr);
	}

	public void setGroupType(GroupType groupType) {
		this.groupTypeStr = groupType.getValue();
	}
}
