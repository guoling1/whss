package com.jkm.base.common.spring.fusion.helper.fusion.body;


import com.jkm.base.common.spring.fusion.helper.fusion.detail.ResponseDetail100004;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"transDetail"})
public class ResponseBody100004 {
	@XmlElement(name="TRANS_DETAIL")
	private ResponseDetail100004 transDetail;

	public ResponseDetail100004 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(ResponseDetail100004 transDetail) {
		this.transDetail = transDetail;
	}
}

