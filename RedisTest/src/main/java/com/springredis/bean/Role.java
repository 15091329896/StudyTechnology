package com.springredis.bean;

import java.io.Serializable;

public class Role implements Serializable {
	/**
	 * 注意，对象要可序列化，需要实现Serializable接口，往往要重写serialVersionUID
	 */
	
	private static final long serialVersionUID = -5219315092108115813L;
	private long id;
	private String roleName;
	private String note;

	public Role() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + ", note=" + note + "]";
	}

}