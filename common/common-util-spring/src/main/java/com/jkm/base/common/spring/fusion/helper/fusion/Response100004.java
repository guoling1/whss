package com.jkm.base.common.spring.fusion.helper.fusion;



import com.jkm.base.common.spring.fusion.helper.fusion.body.ResponseBody100004;
import com.jkm.base.common.spring.fusion.helper.fusion.head.ResponseHead;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG", propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Response100004 {
		// 响应报文头
		@XmlElement(name="INFO")
		private ResponseHead info;
		// 响应报文体信息
		@XmlElement(name="BODY")
		private ResponseBody100004 body;
		public ResponseHead getInfo() {
			return info;
		}
		public void setInfo(ResponseHead info) {
			this.info = info;
		}
		public ResponseBody100004 getBody() {
			return body;
		}
		public void setBody(ResponseBody100004 body) {
			this.body = body;
		}
}

