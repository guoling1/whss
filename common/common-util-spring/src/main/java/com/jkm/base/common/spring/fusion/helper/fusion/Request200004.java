package com.jkm.base.common.spring.fusion.helper.fusion;


import com.jkm.base.common.spring.fusion.helper.fusion.body.RequestBody200004;
import com.jkm.base.common.spring.fusion.helper.fusion.head.RequestHead2;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request200004 {
		@XmlElement(name="INFO")
		private RequestHead2 info;
		@XmlElement(name="BODY")
		private RequestBody200004 body;

		public RequestHead2 getInfo() {
			return info;
		}

		public void setInfo(RequestHead2 info) {
			this.info = info;
		}

		public RequestBody200004 getBody() {
			return body;
		}

		public void setBody(RequestBody200004 body) {
			this.body = body;
		}
}

