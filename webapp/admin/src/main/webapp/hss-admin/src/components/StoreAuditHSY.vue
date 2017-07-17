<template lang="html">
  <div id="storeAudit">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 v-if="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户审核</h3>
      <h3 v-else="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户资料</h3>
      <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">商户注册信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin: 15px 0">
          <el-col :span="5">
            <div class="label">注册手机号：<span>{{$msg.cellphone}}</span>
            </div>
            <el-button type="text" @click="isPhone = true" style="padding: 0">修改</el-button>
          </el-col>
          <el-col :span="5">
            <div class="label">商户编号：<span>{{$msg.globalID}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin: 15px 0">
          <el-col :span="5">
            <div class="label">注册时间：<span>{{$msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">注册方式：
              <span>报单注册</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">一级代理编号：<span v-if="$msg.markCode!=0">{{$msg.markCode}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">一级代理名称：<span>{{$msg.proxyName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">二级代理编号：<span v-if="$msg.markCode1!=0">{{$msg.markCode1}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">二级代理名称：<span>{{$msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="padding-bottom: 15px">
          <el-col :span="5">
            <div class="label">报单员：<span>{{$msg.username}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">姓名：<span>{{$msg.realname}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <span class="lead">商户认证信息</span>
        <el-button type="text" @click="isChange = true" v-if="isChange == false&&isShow">修改</el-button>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin: 15px 0">
          <el-col :span="5">
            <div class="label">商户名称（全称）：
              <span v-if="!isChange">{{$msg.name}}</span>
              <el-input size="small" v-model="$msg.name" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">商铺简称：
              <span v-if="!isChange">{{$msg.shortName}}</span>
              <el-input size="small" v-model="$msg.shortName" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">经营种类：
              <span v-if="!isChange">{{$msg.industryCode}}</span>
              <el-select v-model="$msg.industryCode" placeholder="请选择" v-else size="small">
                <el-option label="餐饮" value="餐饮"></el-option>
                <el-option label="商超" value="商超"></el-option>
                <el-option label="生活服务" value="生活服务"></el-option>
                <el-option label="购物" value="购物"></el-option>
                <el-option label="丽人" value="丽人"></el-option>
                <el-option label="健身" value="健身"></el-option>
                <el-option label="酒店" value="酒店"></el-option>
              </el-select>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户营业执照号：
              <span v-if="!isChange">{{$msg.licenceNo}}</span>
              <el-input size="small" v-model="$msg.licenceNo" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">营业执照有效期：
              <span v-if="!isChange">{{$msg.startEndDate}}</span>
              <el-date-picker
                v-else
                v-model="date"
                @change="datetimeSelect"
                type="daterange"
                size="small"
                placeholder="选择日期范围">
              </el-date-picker>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">店主（法人）实名：
              <span v-if="!isChange">{{$msg.realnames}}</span>
              <el-input size="small" v-model="$msg.realnames" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">店主（法人）身份证号：
              <span v-if="!isChange">{{$msg.idcard}}</span>
              <el-input size="small" v-model="$msg.idcard" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">联系人姓名:
              <span v-if="!isChange">{{$msg.contactName}}</span>
              <el-input size="small" v-model="$msg.contactName" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">联系人手机号：
              <span v-if="!isChange">{{$msg.contactCellphone}}</span>
              <el-input size="small" v-model="$msg.contactCellphone" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">省市区:
              <span v-if="!isChange">{{$msg.districtCode}}</span>
              <el-cascader v-else
                           :placeholder="placeCity"
                :options="options2"
                v-model="cityCode"
                @active-item-change="handleItemChange"
                :props="props"
              ></el-cascader>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">商户详细地址：
              <span v-if="!isChange">{{$msg.address}}</span>
              <el-input size="small" v-model="$msg.address" v-else></el-input>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="padding-bottom: 15px">
          <el-col :span="5">
            <div class="label">资料提交时间：<span>{{$msg.updateTime|changeTime}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">资料审核状态：<span>{{$msg.stat}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-button type="primary" size="small" v-if="isChange" @click="identChange" :loading="auditClick" style="margin: 0 0 15px 15px">保存</el-button>
        <el-button type="primary" size="small" v-if="isChange" @click="identNoChange">取消</el-button>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <span class="lead">店铺信息</span>
        <div style="width: 70%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="shopInfo" border style="width: 100%;margin: 15px 0;font-size: 12px">
              <el-table-column width="62" label="序号" fixed="left" type="index"></el-table-column>
              <el-table-column label="店铺类型">
                <template scope="scope">
                  <span v-if="scope.row.type==1">主店</span>
                  <span v-if="scope.row.type==2">分店</span>
                </template>
              </el-table-column>
              <el-table-column prop="shortName" label="店铺名称"></el-table-column>
              <el-table-column prop="globalID" label="店铺编号"></el-table-column>
              <el-table-column prop="districtCode" label="所在地"></el-table-column>
              <el-table-column prop="address" label="地址"></el-table-column>
              <el-table-column prop="contactCellphone" label="联系电话"></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <div class="box box-primary">
        <span class="lead">商户认证资料</span>
        <el-button type="text" @click="toDet">认证资料历史</el-button>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr class="row">
              <th class="col-md-3" style="text-align: center;">身份证正面:</th>
              <th class="col-md-3" style="text-align: center;">身份证反面:</th>
              <th class="col-md-3" style="text-align: center;">结算卡正面:</th>
              <th class="col-md-3" style="text-align: center;">签约合同照:</th>
              <th class="col-md-3" style="text-align: center;">营业执照:</th>
              <th class="col-md-3" style="text-align: center;">店面照片:</th>
              <th class="col-md-3" style="text-align: center;">收银台:</th>
              <th class="col-md-3" style="text-align: center;">室内照片:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardf" alt="" v-if="$msg.idcardf!=null&&$msg.idcardf!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('1')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardb" alt="" v-if="$msg.idcardb!=null&&$msg.idcardb!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('2')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardc" alt="" v-if="$msg.idcardc!=null&&$msg.idcardc!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.idcardc!=null&&$msg.idcardc!=''&&$msg.status==1||$msg.status==2" type="text" @click="changePhoto('7')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.contractId" alt="" v-if="$msg.contractId!=null&&$msg.contractId!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.contractId!=null&&$msg.contractId!=''&&$msg.status==1||$msg.status==2" type="text" @click="changePhoto('8')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.licenceID" alt="" v-if="$msg.licenceID!=null&&$msg.licenceID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('3')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.storefrontID" alt="" v-if="$msg.storefrontID!=null&&$msg.storefrontID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('4')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.counterID" alt="" v-if="$msg.counterID!=null&&$msg.counterID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('5')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.indoorID" alt="" v-if="$msg.indoorID!=null&&$msg.indoorID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('6')">点击更换</el-button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <span class="lead">商户结算信息</span>
        <el-button type="text" @click="bankChange">修改结算卡信息</el-button>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin: 15px 0">
          <el-col :span="5">
            <div class="label">结算卡类型：
              <span v-if="$msg.isPublic==1">对公</span>
              <span v-if="$msg.isPublic==0">对私</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">结算卡开户名：<span>{{$msg.cardAccountName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户结算卡号:<span>{{$msg.cardNO}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">结算卡所属银行：<span>{{$msg.cardBank}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="padding-bottom: 15px">
          <el-col :span="5">
            <div class="label">支行信息:
              <span>{{$msg.bankAddress}}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">联行号：
              <span>{{$msg.branchCode}}</span>
              <el-button type="text" @click="wad" v-if="$msg.status==2&&($msg.branchCode==''||$msg.branchCode==null)" style="padding: 0">补填</el-button>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">
              <span></span>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <span class="lead">商户费率信息</span>
        <el-button type="text" v-if="isInput == false" @click="isInput = true">修改费率</el-button>
        <div style="width: 70%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateData" border style="width: 100%;margin-bottom: 15px">
              <el-table-column prop="rateName" label="支付方式" ></el-table-column>
              <el-table-column label="T1">
                <template scope="scope">
                  <span v-if="!isInput&&scope.row.tradeRateT1!=null">{{scope.row.tradeRateT1}}
                    <span v-if="scope.row.policyType!='withdraw'">%</span>
                    <span v-if="scope.row.policyType=='withdraw'">元/笔</span>
                  </span>
                  <el-input placeholder="请输入内容" v-model="scope.row.tradeRateT1" v-if="isInput" size="small">
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="time" label="D1">
                <template scope="scope">
                  <span v-if="!isInput&&scope.row.tradeRateD1!=null">{{scope.row.tradeRateD1}}
                    <span v-if="scope.row.policyType!='withdraw'">%</span>
                    <span v-if="scope.row.policyType=='withdraw'">元/笔</span>
                  </span>
                  <el-input placeholder="请输入内容" v-model="scope.row.tradeRateD1" v-if="isInput" size="small">
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="money" label="D0">
                <template scope="scope">
                  <span v-if="!isInput&&scope.row.tradeRateD0!=null">{{scope.row.tradeRateD0}}
                    <span v-if="scope.row.policyType!='withdraw'">%</span>
                    <span v-if="scope.row.policyType=='withdraw'">元/笔</span>
                  </span>
                  <el-input placeholder="请输入内容" v-model="scope.row.tradeRateD0" v-if="isInput" size="small">
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
            </el-table>
            <el-button type="primary" size="small" v-if="isInput" @click="rateChange"  :loading="auditClick">保存</el-button>
            <el-button type="primary" size="small" v-if="isInput" @click="rateNoChange">取消</el-button>
          </template>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <span class="lead">商户通道</span>
        <el-button type="text" @click="isReenter = true" v-if="status==1">重新入网</el-button>
        <el-button type="text" @click="isWxChannel = true">添加微信官方通道</el-button>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <div>当前使用中的通道：[微信：{{$userChannelList.wxChannelName}}]   [支付宝：{{$userChannelList.zfbChannelName}}]
            <el-button type="text" size="small" @click="channelChange" style="margin-left: 15px;font-size: 14px">修改</el-button>
          </div>
          <template>
            <el-table :data="$channelList" border style="width: 100%;margin-top: 15px;">
              <el-table-column prop="channelName" label="通道名称" ></el-table-column>
              <el-table-column prop="settleType" label="结算时间"></el-table-column>
              <el-table-column prop="netStatus" label="入网状态" >
                <template scope="scope">
                  <span v-if="scope.row.netStatus==1">未入网</span>
                  <span v-if="scope.row.netStatus==2">不需入网</span>
                  <span v-if="scope.row.netStatus==3">已提交</span>
                  <span v-if="scope.row.netStatus==4">成功</span>
                  <span v-if="scope.row.netStatus==5">失败</span>
                </template>
              </el-table-column>
              <el-table-column prop="netMarks" label="入网信息" ></el-table-column>
              <el-table-column prop="openProductStatus" label="产品开通状态">
                <template scope="scope">
                  <span v-if="scope.row.openProductStatus==1">开通产品成功</span>
                  <span v-if="scope.row.openProductStatus==2">开通产品失败</span>
                  <span v-if="scope.row.openProductStatus==3">已提交</span>
                </template>
              </el-table-column>
              <el-table-column prop="openProductMarks" label="产品开通信息" ></el-table-column>
              <el-table-column prop="exchannelCode" label="渠道商户编号"></el-table-column>
              <el-table-column prop="appId" label="主公众号" ></el-table-column>
              <el-table-column prop="subAppId" label="子公众号" ></el-table-column>
              <el-table-column prop="exchannelEventCode" label="活动编号" ></el-table-column>
              <el-table-column prop="isUse" label="使用状态">
                <template scope="scope">
                  <span v-if="scope.row.isUse==1">使用中</span>
                  <span v-if="scope.row.isUse==0">未使用</span>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <span class="lead">入网记录</span>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="$netLogList" border style="width: 100%;margin: 15px 0;">
              <el-table-column prop="adminName" label="操作人" ></el-table-column>
              <el-table-column prop="opt" label="操作" ></el-table-column>
              <el-table-column prop="channelTypeSignName" label="通道名称" >
                <template scope="scope">
                  <span v-if="scope.row.channelTypeSignName=='收银家微信T1'">收银家</span>
                  <span v-if="scope.row.channelTypeSignName!='收银家微信T1'">{{scope.row.channelTypeSignName}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="act" label="动作"></el-table-column>
              <el-table-column prop="result" label="结果" ></el-table-column>
              <el-table-column prop="createTime" label="操作时间">
                <template scope="scope">
                  {{scope.row.createTime|changeTime}}
                </template>
              </el-table-column>
            </el-table>
            <div class="block" style="text-align: right">
              <el-pagination @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="netLogQuery.pageNo"
                             :page-sizes="[10, 20, 50]"
                             :page-size="netLogQuery.pageSize"
                             layout="total, sizes, prev, pager, next, jumper"
                             :total="count">
              </el-pagination>
            </div>
          </template>
        </div>
      </div>
      <el-dialog title="修改商户结算卡" :visible.sync="isBank">
        <el-form :model="bankForm">
          <el-form-item label="账户类型" label-width="120px">
            <span v-if="msg.isPublic==1">对公</span>
            <span v-if="msg.isPublic==0">对私</span>
          </el-form-item>
          <el-form-item label="账号" label-width="120px">
            <el-input v-model="bankForm.cardNo" size="small" style="width: 100%"></el-input>
          </el-form-item>
          <el-form-item label="开户行" label-width="120px">
            <el-autocomplete style="width: 100%" v-model="bankForm.bankName" :fetch-suggestions="marryBankSearch" size="small" placeholder="请输入开户行名称" @select="marryBank"></el-autocomplete>
          </el-form-item>
          <el-form-item label="省" label-width="120px">
            <el-select v-model="bankForm.province" size="small" style="width:100%" placeholder="请选择"
                       @change="province_select">
              <el-option v-for="item in item_province"
                         :label="item.aname"
                         :value="item.code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="市" label-width="120px">
            <el-select v-model="bankForm.districtCode" size="small" style="width:100%" placeholder="请选择"
                       @change="city_select">
              <el-option v-for="item in item_city"
                         :label="item.aname"
                         :value="item.code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="支行" label-width="120px">
            <el-autocomplete style="width: 100%" v-model="bankForm.bankAddress" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配" @select="handleSelect"></el-autocomplete>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="isBank = false">取 消</el-button>
          <el-button type="primary" @click="bankSubmit" :loading="auditClick">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog title="选择支行" :visible.sync="isWad">
        <el-form :model="form">
          <el-form-item label="银行名称" label-width="120px">
            {{$msg.cardBank}}
          </el-form-item>
          <el-form-item label="省" label-width="120px">
            <el-select v-model="form.province" size="small" style="width:100%" placeholder="请选择"
                       @change="province_select">
              <el-option v-for="item in item_province"
                         :label="item.aname"
                         :value="item.code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="市" label-width="120px">
            <el-select v-model="form.city" size="small" style="width:100%" placeholder="请选择"
                       @change="city_select">
              <el-option v-for="item in item_city"
                         :label="item.aname"
                         :value="item.code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="支行" label-width="120px">
            <el-autocomplete v-model="form.branchName" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配" @select="handleSelect" style="width:100%"></el-autocomplete>
          </el-form-item>
          <el-form-item label="联行号" label-width="120px">
            <el-input v-model="form.branchCode" size="small" style="width: 100%" disabled></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="isWad = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog title="重新入网" v-model="isReenter" size="tiny">
          <p style="text-align: center;font-weight: 700">确认重新发起入网吗？？</p>
          <span slot="footer" class="dialog-footer">
            <el-button @click="isReenter = false">取 消</el-button>
            <el-button type="primary" @click="reenter" :disabled="reenterClick">确 定</el-button>
          </span>
      </el-dialog>
      <div class="box box-primary" v-if="!isShow||res.length!=0">
        <p class="lead">审核日志</p>
        <div class="table-responsive">
          <div class="col-sm-12">
            <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
              <thead>
              <tr role="row">
                <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">审核时间</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">审核人</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">批复信息</th>
              </tr>
              </thead>
              <tbody id="content">
              <tr role="row" class="odd" v-for="re in this.$data.res">
                <td class="sorting_1">{{re.stat}}</td>
                <td>{{re.createTimes}}</td>
                <td>{{re.name}}</td>
                <td>{{re.descr}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="mask" id="mask" style="display: none">
        <p @click="isNo">×</p>
        <div style="cursor: move;position: absolute;" id="imgBox" @mousedown.prevent.stop="move">
          <img src="" alt="" id="img">
        </div>
        <div style="width:280px;position: absolute;left: 43%;top: 2%;">
          <el-button @click.prevent.stop="enlarge">放大</el-button>
          <el-button @click.prevent.stop="lessen">缩小</el-button>
          <el-button @click.prevent.stop="rotate">旋转</el-button>
          <el-button @click.prevent.stop="reduction">还原</el-button>
        </div>
      </div>
      <div class="box box-primary" v-if="isShow">
        <p class="lead">审核</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right;height: 35px;line-height: 35px;">审核意见:</th>
              <td><input type="text" name="name" placeholder="不通过必填" v-model="reason" style="height: 35px;line-height: 35px;width: 50%;"></td>
            </tr>
            <tr>
              <th style="text-align: right">
                <el-button type="danger" @click="unAudit" :disabled="auditClick">不 通 过</el-button>
              </th>
              <td>
                <el-button type="success" @click="audit($event)" :disabled="auditClick">通 过</el-button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <el-dialog title="更换认证资料" v-model="isUpload">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="上传照片：" width="120" style="margin-bottom: 0">
            <el-upload
              class="upload-demo"
              action="/admin/photoChange/hsySavePhotoChang"
              name="photo"
              :data={sid:id,hsyType:photoType}
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :on-error="handleErr"
              :on-remove="handleRemove"
              :file-list="fileList">
              <el-button id="btn" size="small" type="primary">点击上传</el-button>
              <div style="position: absolute;top: 36px;left:-1px;width: 220px;height: 30px;background: #fff"></div>
            </el-upload>
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-dialog title="更换注册手机号" v-model="isPhone">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="原手机号：" width="120" style="margin-bottom: 0">
            {{msg.cellphone}}
          </el-form-item>
          <el-form-item label="新手机号：" width="120" style="margin-bottom: 0">
            <el-input v-model="newPhone" placeholder="请输入内容"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="isPhone = false" style="position: relative;top: -20px;">取 消</el-button>
          <el-button @click="changePhone" type="primary" style="position: relative;top: -20px;"  :disabled="auditClick">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog title="切换商户通道" v-model="isChannel">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="微信通道：" width="120" style="margin-bottom: 0">
            <el-select v-model="channelForm.wechatChannelTypeSign" placeholder="请选择">
              <el-option
                v-for="item in wxchannel"
                :key="item.value"
                :label="item.channelName"
                :value="item.channelTypeSign">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="支付宝通道：" width="120" style="margin-bottom: 0">
            <el-select v-model="channelForm.alipayChannelTypeSign" placeholder="请选择">
              <el-option
                v-for="item in alichannel"
                :key="item.value"
                :label="item.channelName"
                :value="item.channelTypeSign">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="isChannel = false" style="position: relative;top: -20px;">取 消</el-button>
          <el-button @click="submitChannel" type="primary" style="position: relative;top: -20px;" :disabled="auditClick">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog title="添加微信官方通道" v-model="isWxChannel">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="微信子商户编号：" width="120" style="margin-bottom: 0">
            <el-input v-model="wxChannelForm.exchannelCode" placeholder="请输入内容"></el-input>
          </el-form-item>
          <el-form-item label="主商户公众号ID：" width="120" style="margin-bottom: 0">
            <el-input v-model="wxChannelForm.appId" placeholder="可选"></el-input>
          </el-form-item>
          <el-form-item label="子商户公众号ID：" width="120" style="margin-bottom: 0">
            <el-input v-model="wxChannelForm.subAppId" placeholder="可选"></el-input>
          </el-form-item>
          <el-form-item label="密钥：" width="120" style="margin-bottom: 0">
            <el-input v-model="wxChannelForm.appSecret" placeholder="可选"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="isWxChannel = false" style="position: relative;top: -20px;">取 消</el-button>
          <el-button @click="submitWxChannel" type="primary" style="position: relative;top: -20px;" :loading="auditClick">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'dale',
    data () {
      return {
        id: '',
        netLogQuery:{
          userId:'',
          pageNo:1,
          pageSize:10
        },
        netLogList:[],
        shopInfo:[],
        count:0,
        msg:{},
        auditClick:false,
        isReenter:false,
        isWad: false,
        reason:'',
        isShow:true,
        isBank:false,
        reenterClick:false,
        rejectClick:false,
        modifyClick:false,
        isChange: false,
        res: [],
        tableData:[{
          name:'支付宝',
          rate:'',
          time:'T1',
          money:'0元/笔',
          status:"",
          product:'--',
          msg:'--',
          proMsg:'',
        },{
          name:'微信',
          rate:'',
          time:'T1',
          money:'0元/笔',
          status:"",
          product:'--',
          msg:'--',
          proMsg:''
        }],
        isUpload: false,
        photoType:'',
        fileList:[],
        src:'',
        current:0,
        height:0,
        width:0,
        isPhone:false,
        newPhone:'',
        rateData:[],
        isInput: false,
        channelList:[],
        userChannelList:[],
        isChannel: false,
        wxchannel:[],
        alichannel:[],
        form:{
          province:'',
          provinceName:'',
          city:'',
          cityName:'',
          branchName:'',
          branchCode:''
        },
        //修改结算卡参数
        bankForm:{
          province:'',
          districtCode:'',
          cardNo:'',
          bankName:'',
          bankAddress:''
        },
        channelForm:{
          userId:'',
          wechatChannelTypeSign:'',
          alipayChannelTypeSign:''
        },
        isWxChannel:false,
        wxChannelForm:{
          userId:'',
          exchannelCode:"",
          appId:"",
          subAppId:'',
          appSecret:''
        },
        options2:[],
        props:{
          value: 'value',
          children: 'cities'
        },
        placeCity:'',
        cityCode:[],
        date:'',
        startTime:'',
        endTime:'',
        status:''
      }
    },
    created: function () {
      this.id = this.$route.query.id;
      this.getData();
      var $box=$("#imgBox");
      $box.on("mousedown",function(e){
        var disX= e.clientX-$(this).offset().left;
        var disY= e.clientY-$(this).offset().top;
        $(document).on("mousemove.move",function(e){
          var l= e.clientX-disX;
          var t= e.clientY-disY;
          var maxL=$(document).width()-$box.width();
          var maxT=$(document).height()-$box.height();
          if(l>=maxL){
            l=maxL;
          }else if(l<=0){
            l=0;
          }
          if(t>=maxT){
            t=maxT;
          }else if(t<=0){
            t=0;
          }
          $box.css({
            left:l,
            top:t
          });
        });
        $(document).on("mouseup.move",function(){
          $(document).off(".move");
          $box[0].releaseCapture&& $box[0].releaseCapture();
        });
        $box[0].setCapture&&$box[0].setCapture();
        return false;
      })
      this.$http.post('/admin/unionNumber/findAllProvinces').then(res => {
        this.item_province = res.data;
      })
        .catch(err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      this.$http.post('/join/selectProvinces')
        .then(function (res) {
          for(let i=0;i<res.data.length;i++){
            res.data[i].value = res.data[i].code;
            res.data[i].label = res.data[i].aname;
            res.data[i].cities = [];
          }
          this.options2 = res.data;
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });
    },
    methods: {
      datetimeSelect: function (val) {
        if (val == undefined) {
          this.startTime = '';
          this.endTime = '';
        } else {
          let format = val.split(' - ');
          this.startTime = format[0];
          this.endTime = format[1];
        }
      },
      handleItemChange: function (val) {
        if(val.length==1){
          this.$http.post('/join/selectCities',{code:val[0]})
            .then(res=>{
              for(let i=0;i<this.options2.length;i++){
                if(this.options2[i].value==val[0]){
                  for(let j=0;j<res.data.length;j++){
                    res.data[j].value = res.data[j].code;
                    res.data[j].label = res.data[j].aname;
                    res.data[j].cities = [];
                  }
                  this.options2[i].cities = res.data;
                }
              }
            })
        }else if(val.length==2){
          this.$http.post('/join/selectDistrict',{code:val[1]})
            .then(res=>{
              for(let i=0;i<this.options2.length;i++){
                if(this.options2[i].value==val[0]){
                  for(let k=0;k<this.options2[i].cities.length;k++){
                    if(this.options2[i].cities[k].value==val[1]){
                      for(let j=0;j<res.data.length;j++){
                        res.data[j].value = res.data[j].code;
                        res.data[j].label = res.data[j].aname;
                      }
                      this.options2[i].cities[k].cities = res.data;
                    }
                  }
                }
              }
            })
        }
      },
      // 修改认证信息
      identChange: function () {
        let query = {
          name:this.msg.name,
          shortName:this.msg.shortName,
          industryCode:this.msg.industryCode,
          licenceNo:this.msg.licenceNo,
          realnames:this.msg.realnames,
          idcardNO:this.msg.idcard,
          contactName:this.msg.contactName,
          contactCellphone:this.msg.contactCellphone,
          districtCode:this.msg.districtCodes,
          address:this.msg.address,
          id: this.id
        }
        if(this.cityCode.length!=0){
          query.districtCode = this.cityCode[2];
        }
        if(this.msg.industryCode=='餐饮'){
          query.industryCode = '1000';
        }else if(this.msg.industryCode=='商超'){
          query.industryCode = '1001';
        }else if(this.msg.industryCode=='生活服务'){
          query.industryCode = '1002';
        }else if(this.msg.industryCode=='购物'){
          query.industryCode = '1003';
        }else if(this.msg.industryCode=='丽人'){
          query.industryCode = '1004';
        }else if(this.msg.industryCode=='健身'){
          query.industryCode = '1005';
        }else if(this.msg.industryCode=='健身'){
          query.industryCode = '1006';
        }
        if(this.msg.industryCode=='丽人'){
          query.industryCode = '1004';
        }else
        query.startTime = this.startTime;
        query.endTime = this.endTime;
        if(!this.test(query.idcardNO)){
          this.$message({
            showClose: true,
            message: '身份证号不正确',
            type: 'error'
          })
          return;
        }else{
          this.auditClick = true;
          this.$http.post('/admin/hsyMerchantList/modifyInfo',query)
            .then(res=>{
              this.isChange = false;
              this.auditClick = false;
              this.$message({
                showClose: true,
                message: '修改成功',
                type: 'success'
              })
              this.getData();
            })
            .catch(err=>{
              this.auditClick = false;
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        }
      },
      identNoChange: function () {
        this.isChange = false;
        this.getData();
      },
      getNetLogData: function(){
        this.netLogQuery.userId = this.msg.uid;
         this.$http.post('/admin/hsyMerchantAudit/netLogList',this.netLogQuery)
           .then(res=>{
             this.netLogList = res.data.records;
             this.count = res.data.count;
           })
           .catch(err=>{
             this.$message({
               showClose: true,
               message: err.statusMessage,
               type: 'error'
             })
           })
      },
      handleSizeChange(val) {
        this.netLogQuery.pageNo = 1;
        this.netLogQuery.pageSize = val;
        this.getNetLogData();
      },
      handleCurrentChange(val) {
        this.netLogQuery.pageNo = val;
        this.getNetLogData()
      },
      bankChange:function(){
        this.isBank = true;
        this.bankForm={
          province:'',
          districtCode:'',
          cardNo:'',
          bankName:'',
          bankAddress:''
        }
      },
      wad:function () {
        this.form = {
          province:'',
          provinceName:'',
          city:'',
          cityName:'',
          branchName:'',
          branchCode:''
        }
        this.isWad = true;
      },
      submit: function () {
        if(this.form.branchCode==""){
          this.$message({
            showClose: true,
            message: '请匹配联行号',
            type: 'error'
          });
        }else{
          this.$http.post('/admin/wad/updateBranch',{sid:this.id,branchCode:this.form.branchCode,branchName:this.form.branchName,districtCode:this.form.city})
            .then(res=>{
              this.$message({
                showClose: true,
                message: '补填成功',
                type: 'success'
              });
              this.isWad = false;
              this.getData()
            })
            .catch(err=>{
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        }
      },
      marryBank: function () {

      },
      luhmCheck: function (bankno) {
        bankno=bankno.toString()
        var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）

        var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
        var newArr=new Array();
        for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
          newArr.push(first15Num.substr(i,1));
        }
        var arrJiShu=new Array();  //奇数位*2的积 <9
        var arrJiShu2=new Array(); //奇数位*2的积 >9

        var arrOuShu=new Array();  //偶数位数组
        for(var j=0;j<newArr.length;j++){
          if((j+1)%2==1){//奇数位
            if(parseInt(newArr[j])*2<9)
              arrJiShu.push(parseInt(newArr[j])*2);
            else
              arrJiShu2.push(parseInt(newArr[j])*2);
          }
          else //偶数位
            arrOuShu.push(newArr[j]);
        }

        var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
        var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
        for(var h=0;h<arrJiShu2.length;h++){
          jishu_child1.push(parseInt(arrJiShu2[h])%10);
          jishu_child2.push(parseInt(arrJiShu2[h])/10);
        }

        var sumJiShu=0; //奇数位*2 < 9 的数组之和
        var sumOuShu=0; //偶数位数组之和
        var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
        var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
        var sumTotal=0;
        for(var m=0;m<arrJiShu.length;m++){
          sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
        }

        for(var n=0;n<arrOuShu.length;n++){
          sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
        }

        for(var p=0;p<jishu_child1.length;p++){
          sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);
          sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);
        }
        //计算总和
        sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);

        //计算Luhm值
        var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;
        var luhm= 10-k;

        if(lastNum==luhm && lastNum.length != 0){
          return true;
        }
        else{
          return false;
        }
      },
      bankSubmit: function () {
        this.bankForm.id = this.id;
        this.auditClick = true;
        this.$http.post('/admin/hsyMerchantList/changeSettlementCard',this.bankForm)
          .then(res =>{
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            this.isBank = false;
            this.auditClick = false;
            this.getData()
          })
          .catch(err=>{
            this.auditClick = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      marryBankSearch: function (queryString, cb) {
        var results=[],url='';
        if(this.msg.isPublic==1){
          //对公
          url='/admin/hsyMerchantList/getBankNameList';
        }else if(this.msg.isPublic==0){
          url='/admin/hsyMerchantList/getPersonalBankNameList';
        }
        this.$http.post(url,{bankName:queryString,cardNo:this.bankForm.cardNo})
          .then(res=>{
            for(let i=0; i<res.data.length; i++){
              res.data[i].value = res.data[i].bankName;
            }
            results = res.data;
          })
          .catch(err=>{

          });
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(results);
        }, 1000 * Math.random());
      },
      handleSelect(item) {
        console.log(item);
        this.form.branchCode = item.branchCode;
      },
      province_select: function (provinceCode) {
        for (let m = 0; m < this.item_province.length; m++) {
          if (this.item_province[m].code == provinceCode) {
            if(this.isBank==true){
              this.bankForm.belongProvinceName = this.item_province[m].aname;
            }else{
              this.form.belongProvinceName = this.item_province[m].aname;
            }
          }
        }
        if(this.isBank==true){
          if(this.bankForm.belongProvinceName=="北京市"||this.bankForm.belongProvinceName=="天津市"||this.bankForm.belongProvinceName=="上海市"||this.bankForm.belongProvinceName=="重庆市"){
            this.item_city = [{
              code:this.bankForm.province,
              aname:this.bankForm.belongProvinceName
            }]
            this.bankForm.districtCode = this.item_city[0].code;
            this.bankForm.belongCityName = this.item_city[0].aname;
          }else{
            this.$http.post('/admin/unionNumber/findAllCities', {
              code: provinceCode
            }).then(res => {
              this.item_city = res.data;
              this.bankForm.districtCode = res.data[0].code;
              this.bankForm.belongCityName = res.data[0].aname;
            }, err => {
              this.$message({
                showClose: true,
                message: err.data.msg,
                type: 'error'
              });
            })
          }
        }else {
          if(this.form.belongProvinceName=="北京市"||this.form.belongProvinceName=="天津市"||this.form.belongProvinceName=="上海市"||this.form.belongProvinceName=="重庆市"){
            this.item_city = [{
              code:this.form.province,
              aname:this.form.belongProvinceName
            }]
            this.form.city = this.item_city[0].code;
            this.form.belongCityName = this.item_city[0].aname;
          }else{
            this.$http.post('/admin/unionNumber/findAllCities', {
              code: provinceCode
            }).then(res => {
              this.item_city = res.data;
              this.form.city = res.data[0].code;
              this.form.belongCityName = res.data[0].aname;
            }, err => {
              this.$message({
                showClose: true,
                message: err.data.msg,
                type: 'error'
              });
            })
          }
        }
      },
      city_select: function (cityCode) {
        for (let n = 0; n < this.item_city.length; n++) {
          if (this.item_city[n].code == cityCode) {
            if(this.isBank ==true){
              this.bankForm.belongCityName = this.item_city[n].aname;
            }else {
              this.form.belongCityName = this.item_city[n].aname;
            }
          }
        }
      },
      querySearchAsync(queryString, cb) {
        var results=[],districtCode='',cardBank = '';
        //查支行
        if(this.isBank==true){
          districtCode=this.bankForm.districtCode
          cardBank = this.bankForm.bankName
        }else {
          districtCode=this.form.city
          cardBank=this.msg.cardBank
        }
        this.$http.post('/admin/wad/branch',{branchName:queryString,bankName:cardBank,districtCode:districtCode})
          .then(res=>{
            for(let i=0; i<res.data.length; i++){
              res.data[i].value = res.data[i].branchName;
            }
            results = res.data;
          })
          //        this.$http.post('/admin/unionNumber/bankName',{bankName:queryString})
          //          .then(res=>{
          //          for(let i=0; i<res.data.length; i++){
          //            res.data[i].value = res.data[i].bankName;
          //          }
          //          results = res.data;
          //        })
          .catch(err=>{

          });
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(results);
        }, 1000 * Math.random());
      },
      channelChange: function () {
        this.channelForm = {
          userId:'',
          wechatChannelTypeSign:'',
          alipayChannelTypeSign:''
        };
        this.isChannel = true;
        this.$http.post('/admin/hsyMerchantList/useChannel',{userId:this.msg.uid,policyType:'wechat'})
          .then(res =>{
            this.wxchannel = res.data;
            this.channelForm.wechatChannelTypeSign = res.data[0].channelTypeSign
          })
        this.$http.post('/admin/hsyMerchantList/useChannel',{userId:this.msg.uid,policyType:'alipay'})
          .then(res =>{
            this.alichannel = res.data;
            this.channelForm.alipayChannelTypeSign = res.data[0].channelTypeSign
          })
      },
      submitWxChannel: function () {
        this.auditClick = true;
        this.wxChannelForm.userId = this.msg.uid;
        this.$http.post('/admin/hsyMerchantAudit/addWxChannel',this.wxChannelForm)
          .then(res =>{
            this.$message({
              showClose: true,
              message: '添加成功',
              type: 'success'
            });
            this.auditClick = false;
            this.isWxChannel=false;
            this.getData();
            this.wxChannelForm = {
                userId:'',
                exchannelCode:"",
                appId:"",
              subAppId:'',
              appSecret:''
            }
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
      },
      submitChannel:function () {
        this.channelForm.userId = this.msg.uid;
        this.auditClick = true;
        this.$http.post('/admin/hsyMerchantList/changeUseChannel',this.channelForm)
          .then(res=>{
            this.isChannel = false;
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            this.getData()
            this.auditClick = false;
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
      },
      rateChange: function () {
        this.auditClick = true;
        for(let i=0; i<this.rateData.length; i++){
          this.rateData[i].userId = this.msg.uid;
          this.rateData[i].shopId = this.id;
        }
        this.$http.post('/admin/hsyMerchantList/updateRate',this.rateData)
          .then(res=>{
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            })
            this.isInput = false;
            this.auditClick = false;
          })
          .catch(err =>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
      },
      rateNoChange: function () {
        this.isInput = false;
        this.rateData = this.rateList;
      },
      move:function (e) {
        var oBox=document.getElementById("imgBox");
        e=e||window.event;
        var disX= e.clientX-oBox.offsetLeft;
        var disY= e.clientY-oBox.offsetTop;
        document.onmousemove=function(e){
          e=e||window.event;
          var l= e.clientX-disX;
          var t= e.clientY-disY;
          oBox.style.left=l+"px";
          oBox.style.top=t+"px";
        };
        document.onmouseup=function(){
          document.onmousemove=null;
          document.onmouseup=null;
          oBox.releaseCapture && oBox.releaseCapture();
        };
        oBox.setCapture && oBox.setCapture();
        return false;
      },
      reduction:function () {
        let img,height1,width1,v_left,v_top;
        img = new Image;
        img.src = this.src;
        console.log(1,this.height)
        img.onload = ()=>{
          console.log(2,this.height)
          img.height = this.height;//图片的高度
          img.width = this.width;//图片的宽度
          document.getElementById('imgBox').style.transform = 'rotate(0deg)';
          var img1 = mask.getElementsByTagName('img')[0];
          img1.height = this.height;
          img1.width = this.width;
        }
        var oBox = document.getElementById('imgBox');
        oBox.style.left="0px";
        oBox.style.top="0px";
      },
      enlarge: function () {
        let img,height1,width1,v_left,v_top;
        img = new Image;
        img.src = this.src;
        img.onload = function(){
          height1 = img.height;//图片的高度
          width1 = img.width;//图片的宽度
          v_left=(document.body.clientWidth-width1)/2;
          v_top=(document.body.clientHeight-height1)/2;
          img.style.left=v_left;
          img.style.top=v_top;
          var img1 = document.getElementById('img');
          height1 = img1.height;
          width1 = img1.width;
          img1.height = height1 * 1.1;
          img1.width = width1 * 1.1;
        }
      },
      lessen: function () {
        let img,height1,width1,v_left,v_top;
        img = new Image;
        img.src = this.src;
        img.onload = function(){
          height1 = img.height;//图片的高度
          width1 = img.width;//图片的宽度
          v_left=(document.body.clientWidth-width1)/2;
          v_top=(document.body.clientHeight-height1)/2;
          img.style.left=v_left;
          img.style.top=v_top;
          var img1 = document.getElementById('img');
          height1 = img1.height;
          width1 = img1.width;
          img1.height = height1 / 1.1;
          img1.width = width1 / 1.1;
        }
      },
      rotate:function () {
        this.current = (this.current+90)%360;
        document.getElementById('imgBox').style.transform = 'rotate('+this.current+'deg)';
      },
      getData: function () {
        this.$http.post('/admin/hsyMerchantList/getDetails',{id:this.id})
          .then(function (res) {
            this.msg = res.data.res;
            this.status = res.data.res.status;
            if(this.status !=2){
              this.isShow = false;
            }else {
              this.isShow = true;
            }
            this.res = res.data.list;
            this.rateData = res.data.rateList;
            this.channelList = res.data.channelList;
            this.shopInfo = res.data.shopInfo;
            this.placeCity = res.data.res.districtCode;
            if(res.data.res.startEndDate!=null){
              let format = res.data.res.startEndDate.split(' - ');
              this.date = [new Date(format[0]),new Date(format[1])]
              this.startTime = format[0];
              this.endTime = format[1];
            }
            if(res.data.userChannelList==null){
              this.userChannelList={
                wechatChannelTypeSign:'',
                alipayChannelTypeSign:''
              }
            }else {
              this.userChannelList = res.data.userChannelList;
            }
            this.rateList = JSON.parse(JSON.stringify(res.data.rateList));
            if(res.data.res.weixinRate!=null&&res.data.res.weixinRate!=''&&res.data.res.weixinRate!=0){
              this.tableData[1].rate = parseFloat(res.data.res.weixinRate * 100).toFixed(2) + '%';
            }
            if(res.data.res.alipayRate!=null&&res.data.res.alipayRate!=''&&res.data.res.alipayRate!=0){
              this.tableData[0].rate = parseFloat(res.data.res.alipayRate * 100).toFixed(2) + '%';
            }
            this.tableData[0].product = this.tableData[1].product = res.data.res.hxbOpenProduct;
            this.tableData[0].status = this.tableData[1].status = res.data.res.hxbStatus;
            this.tableData[0].msg = this.tableData[1].msg = res.data.res.hxbRemarks;
            this.tableData[0].proMsg = this.tableData[1].proMsg = res.data.res.hxbOpenProductRemarks;
            this.getNetLogData();
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changePhoto: function (val) {
        this.photoType = val;
        this.isUpload = true
      },
      handleSuccess: function (response, file, fileList) {
        this.$message({
          showClose: true,
          message: '上传成功',
          type: 'success'
        });
        location.reload()
        this.isUpload = false;
        this.getData()
      },
      handleErr:function (err) {
        this.$message({
          showClose: true,
          message: '上传失败',
          type: 'error'
        });
      },
      changePhone: function () {
        this.auditClick = true;
        this.$http.post('/admin/hsyMerchantList/changeMobile',{id:this.id,changePhone:this.newPhone})
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            this.auditClick = false;
            location.reload()
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
      },
      toDet:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/dataHistory?merchantId='+this.id+'&type=hsy');
//        this.$router.push({path:'/admin/record/dataHistory',query:{merchantId:this.id}})
      },
      // 重新入网
      reenter:function () {
        this.reenterClick = true;
        this.$http.post('/admin/hsyMerchantAudit/reenter', {
          shopId: this.id,//店铺编码
          userId: this.msg.uid,//商户编码
        }).then(function (res) {
          this.isReenter = false;
          this.reenterClick = false;
          this.$message({
            showClose: true,
            message: '重新入网成功',
            type: 'success'
          })
        }, function (err) {
          this.isReenter = false;
          this.reenterClick = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      test:function(num) {
        num = num.toString();
        num = num.toUpperCase();
        //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。
        if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
          return false;
        }
        //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
        //下面分别分析出生日期和校验位
        var len, re;
        len = num.length;
        if (len == 15) {

          re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
          var arrSplit = num.match(re);

          //检查生日日期是否正确
          var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
          var bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));

          if (!bGoodDay) {
            return false;
          } else {
            return 1;
          }
        }

        if (len == 18) {

          re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
          var arrSplit = num.match(re);

          //检查生日日期是否正确
          var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
          var bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));

          if (!bGoodDay) {
            return false;
          } else {
            //检验18位身份证的校验码是否正确。
            //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
            var valnum;
            var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
            var nTemp = 0, i;
            for(i = 0; i < 17; i ++) {
              nTemp += num.substr(i, 1) * arrInt[i];
            }

            valnum = arrCh[nTemp % 11];
            if (valnum != num.substr(17, 1)) {
              return false;
            }

            return true;
          }
        }

        return false;
      },
      audit: function (event) {
        if(this.msg.idcard==''||this.msg.idcard==null){
          this.$message({
            showClose: true,
            message: '身份证号不正确',
            type: 'error'
          })
        }else if(!this.test(this.msg.idcard)){
          this.$message({
            showClose: true,
            message: '身份证号不正确',
            type: 'error'
          })
          return;
        }else{
          this.auditClick = true;
          this.$http.post('/admin/hsyMerchantAudit/throughAudit', {
            id: this.id,
            uid: this.msg.uid,
            name: this.msg.name,
            checkErrorInfo: this.reason,
            cellphone: this.msg.cellphone,
            branchCode: this.msg.branchCode
          }).then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '操作成功'
            })
            this.auditClick = false;
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
        }
      },
      unAudit: function () {
        this.auditClick = true;
        this.$http.post('/admin/hsyMerchantAudit/rejectToExamine', {
          id: this.id,
          uid: this.msg.uid,
          name: this.msg.name,
          checkErrorInfo: this.reason,
          cellphone: this.msg.cellphone,
          mobile: this.msg.mobile
        })
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '操作成功'
            })
            this.auditClick = false;
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.auditClick = false;
          })
      },
      changeBig: function (e) {
        e = e||window.event;
        var obj = e.srcElement||e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        this.src = img.src = obj.src;
        mask.style.display= 'block';
        var Img = new Image;
        Img.src = this.src;
        Img.onload = ()=>{
          this.height = Img.height;//图片的高度
          this.width = Img.width;//图片的宽度
          this.reduction()
        }
      },
      isNo: function () {
        document.getElementById('mask').style.display='none'
      }
    },
    computed:{
      $msg:function () {
        return this.msg
      },
      $userChannelList: function(){
        return this.userChannelList;
      },
      $channelList: function () {
        return this.channelList;
      },
      $netLogList: function(){
        return this.netLogList;
      }
    },
    filters: {
      changeDeal: function (val) {
        return val=val?val:'无'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .label {
    color: #333;
    span, a,div, input {
      font-weight: normal;
    }
  }
  .lead{
    margin: 0 10px 0 15px;
  }
  .mask{
    background: rgba(0,0,0,0.3);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top:0;
    left: 0;
  p{
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1200;
    width: 65px;
    height: 65px;
    line-height: 55px;
    font-size: 65px;
    color: #f5f2f2;
    text-align: center;
    border: 6px solid #f5f2f2;
    border-radius: 50%;
    box-shadow: 0 0 16px #000;
    text-shadow: 0 0 16px #000;
  }

  img{
    display: inherit;
    /*height: 100%;*/
    margin: 0 auto;
  }
  }
</style>
