package com.liorginsberg.talktome;

public class Task {

	private String text;
	private boolean crossed;
	private boolean placeHolder;
	

	public boolean isPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(boolean placeHolder) {
		this.placeHolder = placeHolder;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCrossed() {
		return crossed;
	}

	public void setCrossed(boolean crossed) {
		this.crossed = crossed;
	}

	public Task(String text, boolean placeHolder) {
		super();
		this.text = text;
		this.crossed = false;
		this.placeHolder = placeHolder;
	}
	public Task(String text) {
		super();
		this.text = text;
		this.crossed = false;
		this.placeHolder = false;
	}
}
