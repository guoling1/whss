package com.jkm.base.common.spring.fusion.helper.fusion.body;

import com.jkm.base.common.spring.fusion.helper.fusion.detail.ResponseDetail100004;
import com.jkm.base.common.spring.fusion.helper.fusion.detail.ResponseDetail200004;

import javax.xml.bind.annotation.*;

/**
 * Created by xingliujie on 2017/2/7.
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder={"transDetail"})
public class ResponseBody200004 {
    @XmlElement(name="TRANS_DETAIL")
    private ResponseDetail200004 transDetail;

    public ResponseDetail200004 getTransDetail() {
        return transDetail;
    }

    public void setTransDetail(ResponseDetail200004 transDetail) {
        this.transDetail = transDetail;
    }
}
