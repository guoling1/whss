package com.jkm.base.common.spring.fusion.helper.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL",propOrder={"QUERY_SN","QUERY_DATE","QUERY_REMARK"})
public class RequestDetail200004 {
	@XmlElement(name = "QUERY_SN")
	private String QUERY_SN;
	@XmlElement(name = "QUERY_DATE")
	private String QUERY_DATE;
	@XmlElement(name = "QUERY_REMARK")
	private String QUERY_REMARK;

	public String getQUERY_SN() {
		return QUERY_SN;
	}

	public void setQUERY_SN(String QUERY_SN) {
		this.QUERY_SN = QUERY_SN;
	}

	public String getQUERY_DATE() {
		return QUERY_DATE;
	}

	public void setQUERY_DATE(String QUERY_DATE) {
		this.QUERY_DATE = QUERY_DATE;
	}

	public String getQUERY_REMARK() {
		return QUERY_REMARK;
	}

	public void setQUERY_REMARK(String QUERY_REMARK) {
		this.QUERY_REMARK = QUERY_REMARK;
	}
}

