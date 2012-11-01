package com.liorginsberg.talktome;

public class Task {

	private String text;
	private boolean crossed;

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

	public Task(String text, boolean crossed) {
		super();
		this.text = text;
		this.crossed = crossed;
	}
}
