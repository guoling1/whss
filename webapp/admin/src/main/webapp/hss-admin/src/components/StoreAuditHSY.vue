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
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">注册手机:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cellphone" readonly>
                <el-button type="text" @click="isPhone = true">修改</el-button>
              </td>
              <th style="text-align: right">注册时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.createTime|changeTime" readonly></td>
              <th style="text-align: right">注册方式:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="--" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">一级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.markCode==0?'':msg.markCode" readonly></td>
              <th style="text-align: right">一级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">二级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.markCode1==0?'':msg.markCode1" readonly></td>
              <th style="text-align: right">二级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">报单员:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.username" readonly></td>
              <th style="text-align: right">姓名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.realname" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <!--<tr>
              <th style="text-align: right">推荐人编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">推荐人名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">推荐人注册手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">推荐所属一级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq" readonly></td>
              <th style="text-align: right">推荐所属二级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq1" readonly></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>-->
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商铺名称（全称）:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">商铺简称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.shortName" readonly></td>
              <!--<th style="text-align: right">商户营业执照号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.licenceID" readonly></td>-->
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">店主（法人）实名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.contactName"  readonly></td>
              <th style="text-align: right">店主身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.idcardNO" readonly></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">商铺上报名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">经营种类:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.industryCode" readonly></td>
              <th style="text-align: right">省市区:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.districtCode" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">商铺详细地址:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.address" readonly></td>
              <th style="text-align: right">资料提交时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.updateTime|changeTime" readonly></td>
              <th style="text-align: right">资料审核状态:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.stat" readonly></td>
            </tr>
            </tbody></table>
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
              <th class="col-md-3" style="text-align: center;" v-if="msg.isPublic==1">营业执照:</th>
              <th class="col-md-3" style="text-align: center;">店面照片:</th>
              <th class="col-md-3" style="text-align: center;">收银台:</th>
              <th class="col-md-3" style="text-align: center;">室内照片:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px" @click="changeBig()" :src="msg.idcardf" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardf" alt="" v-if="$msg.idcardf!=null&&$msg.idcardf!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('1')">点击更换</el-button>
                <!--<el-upload v-if="$msg.idcardf==null&&$msg.idcardf==''&&$msg.status!=1&&$msg.status!==2" id="upload" style="position: relative" action="/admin/photoChange/hsySavePhotoChang"
                           type="drag" :thumbnail-mode="true"
                           name="photo"
                           :data={sid:id,hsyType:1}
                           :on-preview="handlePreview"
                           :on-success="handleSuccess"
                           :on-error="handleErr"
                           :default-file-list="fileList">
                  <i class="el-icon-upload"></i>
                  <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div style="position: absolute;top: 126px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div style="position: absolute;top: 1px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                </el-upload>-->
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px"  @click="changeBig()" :src="msg.idcardb" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardb" alt="" v-if="$msg.idcardb!=null&&$msg.idcardb!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('2')">点击更换</el-button>
                <!--<el-upload v-if="$msg.idcardb==null&&$msg.idcardb==''&&$msg.status!=1&&$msg.status!=2" id="upload" style="position: relative" action="/admin/photoChange/hsySavePhotoChang"
                           type="drag" :thumbnail-mode="true"
                           name="photo"
                           :data={sid:id,hsyType:2}
                           :on-preview="handlePreview"
                           :on-success="handleSuccess"
                           :on-error="handleErr"
                           :default-file-list="fileList">
                  <i class="el-icon-upload"></i>
                  <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div style="position: absolute;top: 126px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div style="position: absolute;top: 1px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                </el-upload>-->
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px"  @click="changeBig()" :src="msg.idcardb" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.idcardc" alt="" v-if="$msg.idcardc!=null&&$msg.idcardc!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.idcardc!=null&&$msg.idcardc!=''&&$msg.status==1||$msg.status==2" type="text" @click="changePhoto('7')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.contractId" alt="" v-if="$msg.contractId!=null&&$msg.contractId!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.contractId!=null&&$msg.contractId!=''&&$msg.status==1||$msg.status==2" type="text" @click="changePhoto('8')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;" v-if="msg.isPublic==1">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.licenceID" alt="" v-if="$msg.licenceID!=null&&$msg.licenceID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('3')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px"  @click="changeBig()" :src="msg.storefrontID" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.storefrontID" alt="" v-if="$msg.storefrontID!=null&&$msg.storefrontID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('4')">点击更换</el-button>
                <!--<el-upload v-if="$msg.storefrontID==null&&$msg.storefrontID==''&&$msg.status!=1&&$msg.status!=2" id="upload" style="position: relative" action="/admin/photoChange/hsySavePhotoChang"
                           type="drag" :thumbnail-mode="true"
                           name="photo"
                           :data={sid:id,hsyType:4}
                           :on-preview="handlePreview"
                           :on-success="handleSuccess"
                           :on-error="handleErr"
                           :default-file-list="fileList">
                  <i class="el-icon-upload"></i>
                  <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div style="position: absolute;top: 126px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div style="position: absolute;top: 1px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                </el-upload>-->
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px"  @click="changeBig()" :src="msg.counterID" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.counterID" alt="" v-if="$msg.counterID!=null&&$msg.counterID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('5')">点击更换</el-button>
                <!--<el-upload v-if="$msg.counterID==null&&$msg.counterID==''&&$msg.status!=1&&$msg.status!=2" id="upload" style="position: relative" action="/admin/photoChange/hsySavePhotoChang"
                           type="drag" :thumbnail-mode="true"
                           name="photo"
                           :data={sid:id,hsyType:5}
                           :on-preview="handlePreview"
                           :on-success="handleSuccess"
                           :on-error="handleErr"
                           :default-file-list="fileList">
                  <i class="el-icon-upload"></i>
                  <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div style="position: absolute;top: 126px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div style="position: absolute;top: 1px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                </el-upload>-->
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <!--<img style="width: 200px"  @click="changeBig()" :src="msg.indoorID" alt=""/>-->
                <img style="width: 200px;" @click="changeBig()" :src="$msg.indoorID" alt="" v-if="$msg.indoorID!=null&&$msg.indoorID!=''"/>
                <el-button style="display: block;margin: 0 auto" v-if="$msg.status==1||$msg.status==2" type="text" @click="changePhoto('6')">点击更换</el-button>
                <!--<el-upload v-if="$msg.indoorID==null&&$msg.indoorID==''&&$msg.status!=1||$msg.status!=2" id="upload" style="position: relative" action="/admin/photoChange/hsySavePhotoChang"
                           type="drag" :thumbnail-mode="true"
                           name="photo"
                           :data={sid:id,hsyType:6}
                           :on-preview="handlePreview"
                           :on-success="handleSuccess"
                           :on-error="handleErr"
                           :default-file-list="fileList">
                  <i class="el-icon-upload"></i>
                  <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div style="position: absolute;top: 126px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div style="position: absolute;top: 1px;margin-left:0px;width: 200px;height: 30px;background: #fbfdff"></div>
                </el-upload>-->
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户结算信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">结算卡类型:</th>
              <td>
                <el-radio-group v-model="msg.isPublic">
                  <el-radio :label="1">对公</el-radio>
                  <el-radio :label="0">对私</el-radio>
                </el-radio-group>
              </td>
              <th style="text-align: right">结算卡开户名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardAccountName" readonly></td>
              <th style="text-align: right" v-show="msg.isPublic==0">身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.idcardNO" readonly v-show="msg.isPublic==0"></td>
            </tr>
            <tr>
              <th style="text-align: right">商户结算卡号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardNO" readonly></td>
              <th style="text-align: right">结算卡所属银行:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardBank" readonly></td>
              <th style="text-align: right">支行信息:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankAddress" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">结算方式:</th>
              <td>
                <el-radio-group>
                  <el-radio :label="3">T1结算到卡</el-radio>
                  <el-radio :label="4">T1结算到账户余额</el-radio>
                </el-radio-group>
              </td>
              <th style="text-align: right">联行号:</th>
              <td>
                <input type="text" style="background:#efecec;padding-left:5px;" :value="$msg.branchCode" readonly>
                <el-button type="text" @click="wad" v-if="$msg.status==2&&($msg.branchCode==''||$msg.branchCode==null)">补填联行号</el-button>
              </td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            </tbody></table>
        </div>
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
            <el-button type="primary" size="small" v-if="isInput" @click="rateChange" :disabled="auditClick">保存</el-button>
            <el-button type="primary" size="small" v-if="isInput" @click="rateNoChange">取消</el-button>
          </template>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <span class="lead">商户通道</span>
        <el-button type="text" @click="isReenter = true" v-if="status==1">重新入网</el-button>
        <!--<el-button type="text" @click="isReject = true" v-if="status==1">驳回重填</el-button>-->
        <el-button type="text" @click="isModify = true" v-if="status==1">修改信息</el-button>
        <el-button type="text" @click="isWxChannel = true">添加微信官方通道</el-button>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <div>当前使用中的通道：[微信：{{$userChannelList.wxChannelName}}]   [支付宝：{{$userChannelList.zfbChannelName}}]
            <el-button type="primary" size="small" @click="channelChange">修改</el-button>
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
              <el-table-column prop="appId" label="公众号" ></el-table-column>
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
            <el-autocomplete v-model="form.branchName" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配" @select="handleSelect"></el-autocomplete>
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
      <el-dialog title="修改信息" v-model="isModify" size="tiny">
        <p style="text-align: center;font-weight: 700">确认修改信息吗？？</p>
        <span slot="footer" class="dialog-footer">
            <el-button @click="isModify = false">取 消</el-button>
            <el-button type="primary" @click="modify" :disabled="modifyClick">确 定</el-button>
          </span>
      </el-dialog>
      <!--<el-dialog title="驳回重填" v-model="isReject" size="tiny">-->
          <!--<p style="text-align: center;font-weight: 700">确认驳回重填吗？</p>-->
          <!--<p style="text-align: center">只有全部通道都入网失败的才可以驳回</p>-->
          <!--<span slot="footer" class="dialog-footer">-->
            <!--<el-button @click="isReject = false">取 消</el-button>-->
            <!--<el-button type="primary" @click="reject" :disabled="rejectClick">确 定</el-button>-->
          <!--</span>-->
        <!--</el-dialog>-->
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
          <el-form-item label="子商户公众号ID：" width="120" style="margin-bottom: 0">
            <el-input v-model="wxChannelForm.appId" placeholder="可选"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="isWxChannel = false" style="position: relative;top: -20px;">取 消</el-button>
          <el-button @click="submitWxChannel" type="primary" style="position: relative;top: -20px;" :disabled="auditClick">确 定</el-button>
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
        msg:{},
        auditClick:false,
        isReenter:false,
        isReject:false,
        isWad: false,
        reason:'',
        isShow:true,
        reenterClick:false,
        rejectClick:false,
        modifyClick:false,
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
        channelForm:{
          userId:'',
          wechatChannelTypeSign:'',
          alipayChannelTypeSign:''
        },
        isWxChannel:false,
        wxChannelForm:{
          userId:'',
          exchannelCode:"",
          appId:""
        }
      }
    },
    created: function () {
      this.id = this.$route.query.id;
      this.status = this.$route.query.status;
      if(this.$route.query.status !=2){
        this.isShow = false;
      }
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
    },
    methods: {
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
      handleSelect(item) {
        console.log(item);
        this.form.branchCode = item.branchCode;
      },
      province_select: function (provinceCode) {
        for (let m = 0; m < this.item_province.length; m++) {
          if (this.item_province[m].code == provinceCode) {
            this.form.belongProvinceName = this.item_province[m].aname;
          }
        }
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
      },
      city_select: function (cityCode) {
        for (let n = 0; n < this.item_city.length; n++) {
          if (this.item_city[n].code == cityCode) {
            this.form.belongCityName = this.item_city[n].aname;
          }
        }
      },
      querySearchAsync(queryString, cb) {
        var restaurants = this.restaurants;
        var results=[];
        //查支行
        this.$http.post('/admin/wad/branch',{branchName:queryString,bankName:this.msg.cardBank,districtCode:this.form.city})
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
          })
        this.$http.post('/admin/hsyMerchantList/useChannel',{userId:this.msg.uid,policyType:'alipay'})
          .then(res =>{
            this.alichannel = res.data;
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
                appId:""
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
            this.res = res.data.list;
            this.rateData = res.data.rateList;
            this.channelList = res.data.channelList;
            this.userChannelList = res.data.userChannelList;
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
      // 驳回
      reject:function () {
        this.rejectClick = true;
        this.$http.post('/admin/hsyMerchantAudit/reject', {
          id: this.id,
          uid: this.msg.uid,
        }).then(function (res) {
          this.isReject = false;
          this.rejectClick = false;
          this.$message({
            showClose: true,
            message: '驳回重填成功',
            type: 'success'
          })
        }, function (err) {
          this.isReject = false;
          this.rejectClick = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      // 修改信息
      modify:function () {
        this.modifyClick = true;
        this.$http.post('/admin/hsyMerchantAudit/modify', {
          shopId: this.id,//店铺编码
          userId: this.msg.uid,//商户编码
        }).then(function (res) {
          this.isModify = false;
          this.modifyClick = false;
          this.getData();
          this.$message({
            showClose: true,
            message: '修改成功',
            type: 'success'
          })
        }, function (err) {
          this.isModify = false;
          this.modifyClick = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      audit: function (event) {
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
      },
      unAudit: function () {
        this.auditClick = true;
        this.$http.post('/admin/hsyMerchantAudit/rejectToExamine', {
          id: this.id,
          uid: this.msg.uid,
          name: this.msg.name,
          checkErrorInfo: this.reason,
          cellphone: this.msg.cellphone,
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
