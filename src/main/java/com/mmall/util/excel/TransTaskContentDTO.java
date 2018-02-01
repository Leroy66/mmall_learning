package com.mmall.util.excel;

import com.smartwork.msip.cores.utils.excel.annotation.ExcelField;

public class TransTaskContentDTO {

	@ExcelField(title = "源语言（中文）", align = 2, sort = 1)
	private String chinese;
	@ExcelField(title = "源语言（en）", align = 2, sort = 1)
	private String english;
	@ExcelField(title = "日", align = 2, sort = 1)
	private String japanese;
	@ExcelField(title = "韩", align = 2, sort = 1)
	private String korean;
	@ExcelField(title = "西", align = 2, sort = 1)
	private String spanish;
	@ExcelField(title = "俄", align = 2, sort = 1)
	private String russian;
	@ExcelField(title = "越", align = 2, sort = 1)
	private String vietnamese; 
	@ExcelField(title = "法", align = 2, sort = 1)
	private String french;
	@ExcelField(title = "德", align = 2, sort = 1)
	private String german;

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getJapanese() {
		return japanese;
	}

	public void setJapanese(String japanese) {
		this.japanese = japanese;
	}

	public String getKorean() {
		return korean;
	}

	public void setKorean(String korean) {
		this.korean = korean;
	}

	public String getSpanish() {
		return spanish;
	}

	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}

	public String getRussian() {
		return russian;
	}

	public void setRussian(String russian) {
		this.russian = russian;
	}

	public String getVietnamese() {
		return vietnamese;
	}

	public void setVietnamese(String vietnamese) {
		this.vietnamese = vietnamese;
	}

	public String getFrench() {
		return french;
	}

	public void setFrench(String french) {
		this.french = french;
	}

	public String getGerman() {
		return german;
	}

	public void setGerman(String german) {
		this.german = german;
	}

}
