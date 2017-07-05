package com.jkm.base.common.spring.fusion.helper.fusion;


import com.jkm.base.common.spring.fusion.helper.fusion.body.RequestBody100004;
import com.jkm.base.common.spring.fusion.helper.fusion.head.RequestHead;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request100004 {
		@XmlElement(name="INFO")
		private RequestHead info;
		@XmlElement(name="BODY")
		private RequestBody100004 body;

		public RequestHead getInfo() {
			return info;
		}

		public void setInfo(RequestHead info) {
			this.info = info;
		}

		public RequestBody100004 getBody() {
			return body;
		}

		public void setBody(RequestBody100004 body) {
			this.body = body;
		}

}

