package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;


@Data
public class WxConfig extends BaseEntity {


	/**
	*access_token
	*/
	private String accessToken;

	/**
	*jsapi_ticket
	*/
	private String jsapiTicket;

	/**
	*
	*/
	private long expireTime;

	/**
	*0.成功 1.删除
	*/
	private int status;

}
