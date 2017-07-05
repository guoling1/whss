package com.jkm.base.common.spring.fusion.helper.fusion.body;

import com.jkm.base.common.spring.fusion.helper.fusion.detail.RequestDetail200004;

import javax.xml.bind.annotation.*;

/**
 * Created by xingliujie on 2017/2/7.
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY",propOrder={"transDetail"})
public class RequestBody200004 {
    @XmlElement(name="TRANS_DETAIL")
    private RequestDetail200004 transDetail;

    public RequestDetail200004 getTransDetail() {
        return transDetail;
    }

    public void setTransDetail(RequestDetail200004 transDetail) {
        this.transDetail = transDetail;
    }
}
