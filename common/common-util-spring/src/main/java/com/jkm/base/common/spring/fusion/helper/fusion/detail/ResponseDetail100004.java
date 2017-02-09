package com.jkm.base.common.spring.fusion.helper.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL", propOrder={"ISFEE"})
public class ResponseDetail100004 {
	@XmlElement(name="ISFEE")
	private String ISFEE;

	public String getISFEE() {
		return ISFEE;
	}

	public void setISFEE(String ISFEE) {
		this.ISFEE = ISFEE;
	}
}