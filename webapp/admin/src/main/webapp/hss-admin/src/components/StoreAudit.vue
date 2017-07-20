<template lang="html">
  <div id="storeAudit" v-loading.body="loading">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 v-if="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户审核</h3>
      <h3 v-else="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户资料</h3>
      <a href="javascript:window.close();" class="pull-right btn btn-primary" style="color: #fff">关闭</a>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">注册信息 <a style="font-size: 14px;color: #20a0ff;font-weight: 500;cursor: pointer" @click="dealerMask = true" v-if="!isShow">修改代理商归属</a></p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">注册手机：<span>{{$msg.mobile}}</span>
              <!--<a>修改登录手机号</a>-->
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">注册时间：<span>{{$msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">注册方式：
              <span v-if="$msg.source==0">扫码注册</span>
              <span v-if="$msg.source==1">商户推荐注册</span>
              <span v-if="$msg.source==2">代理商推荐注册</span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">分公司编号：<span>{{$msg.companyCode}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">分公司名称：<span>{{$msg.branchCompany}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">一级代理编号：<span>{{$msg.markCode1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">一级代理名称：<span>{{$msg.proxyName}}</span>
              <!--<a>修改代理商归属</a>-->
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐商户注册手机号：<span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">二级代理编号：<span>{{$msg.markCode2}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">二级代理名称：<span>{{$msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">邀请层级：<span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">推荐商户编号：<span>{{$msg.recommenderCode}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐商户名称：<span>{{$msg.recommenderName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐人注册手机号<span>{{$msg.recommenderPhone}}</span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">推荐所属一级代理名：<span>{{$msg.proxyNameYq}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐所属二级代理名：<span>{{$msg.proxyNameYq1}}</span></div>
          </el-col>
          <el-col :span="5"></el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <p class="lead">认证信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商铺名称（全称）：<span>{{$msg.merchantName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商铺简称：<span>——</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">经营种类：<span>——</span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商铺上报名称：<span>{{$msg.merchantChangeName}}</span>
              <a @click="reset" v-if="!isShow">修改上报名称</a>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">店主（法人）实名：<span>{{$msg.name}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">店主身份证号：<span>{{$msg.identity}}</span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">省市区：<span>--</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商铺详细地址：<span>{{$msg.address}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">资料提交时间：<span>——</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">资料审核状态：<span>{{$msg.stat}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <span class="lead">认证资料</span>
        <el-button type="text" @click="toDet">认证资料历史</el-button>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr class="row">
              <th class="col-md-3" style="text-align: center;">身份证正面:</th>
              <th class="col-md-3" style="text-align: center;">身份证反面:</th>
              <th class="col-md-3" style="text-align: center;">手持身份证:</th>
              <th class="col-md-3" style="text-align: center;">银行卡正面:</th>
              <th class="col-md-3" style="text-align: center;">手持结算卡:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px;" @click="changeBig()" :src="$msg.identityFacePic" alt="" v-if="$msg.identityFacePic!=null&&$msg.identityFacePic!=''"/>
                <el-button style="display: block;margin: 0 auto"  type="text" @click="changePhoto('4')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="$msg.identityHandPic" alt="" v-if="$msg.identityHandPic!=null&&$msg.identityHandPic!=''"/>
                <el-button style="display: block;margin: 0 auto" type="text" @click="changePhoto('3')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="$msg.identityOppositePic" alt="" v-if="$msg.identityOppositePic!=null&&$msg.identityOppositePic!=''"/>
                <el-button style="display: block;margin: 0 auto" type="text" @click="changePhoto('5')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="$msg.bankPic" alt="" v-if="$msg.bankPic!=null&&$msg.bankPic!=''"/>
                <el-button style="display: block;margin: 0 auto" type="text" @click="changePhoto('1')">点击更换</el-button>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="$msg.bankHandPic" alt="" v-if="$msg.bankHandPic!=null&&$msg.bankHandPic!=''"/>
                <el-button style="display: block;margin: 0 auto" type="text" @click="changePhoto('2')">点击更换</el-button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <span class="lead" style="display: inline-block">默认结算卡</span>
        <a href="javascript:;" @click="changeBank = true" v-if="!isShow">修改默认结算卡</a>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">结算卡开户名：<span>{{$msg.name}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">身份证号：<span>{{$msg.identity}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户结算卡号：<span>{{$msg.bankNo}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">结算卡所属银行：<span>{{$msg.bankName}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">支行信息：<span>{{$msg.branchName}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">联行号：<span>{{$msg.branchCode}}</span>
              <el-button type="text" size="small" @click="wad" style="padding: 0;font-weight: normal;" v-if="msg.branchCode==''||msg.branchCode==null">补填</el-button>
              <el-button type="text" size="small" @click="wad" style="padding: 0;font-weight: normal;" v-else>修改</el-button>
            </div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-dialog title="选择支行" :visible.sync="isWad">
          <el-form :model="form">
            <el-form-item label="银行名称" label-width="120px">
              {{$msg.bankName}}
            </el-form-item>
            <el-form-item label="省/市/区" label-width="120px">
              <el-cascader :placeholder="请选择"
                           style="width: 100%"
                           :options="options2"
                           v-model="cityCode"
                           size="small"
                           @active-item-change="handleItemChange"
                           :props="props"
              ></el-cascader>
            </el-form-item>
            <el-form-item label="支行" label-width="120px">
              <el-autocomplete v-model="autobankName" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配" @select="handleSelect" style="width:100%"></el-autocomplete>
            </el-form-item>
            <el-form-item label="联行号" label-width="120px">
              <el-input v-model="form.branchCode" size="small" style="width: 100%" disabled></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="isWad = false">取 消</el-button>
            <el-button type="primary" @click="submit" :loading="btnLoad">确 定</el-button>
          </div>
        </el-dialog>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">认证状态：<span>{{$msg.isAuthen}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">费率信息</p>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateInfo" border style="width: 100%">
              <el-table-column prop="channelName" label="通道名称"></el-table-column>
              <el-table-column prop="merchantRate" label="支付结算手续费"></el-table-column>
              <el-table-column prop="merchantBalanceType" label="结算时间"></el-table-column>
              <el-table-column prop="withdrawMoney" label="提现手续费"></el-table-column>
              <el-table-column prop="entNet" label="商户入网状态"></el-table-column>
              <el-table-column prop="remarks" label="商户入网备注信息"></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <el-dialog title="修改默认结算卡" v-model="changeBank">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="结算卡号：" width="120">
            <el-input style="width: 70%" size="small" v-model="bankQuery.bankNo"></el-input>
          </el-form-item>
          <el-form-item label="开户行:"  width="120">
            <el-autocomplete style="width: 70%" v-model="bankQuery.bankName" :fetch-suggestions="marryBankSearch" size="small" placeholder="请输入开户行名称" @select="marryBank"></el-autocomplete>
          </el-form-item>
          <el-form-item label="省/市/区：" width="120">
            <el-cascader :placeholder="请选择"
                         style="width: 70%"
                         :options="options2"
                         v-model="cityCode"
                         size="small"
                         @active-item-change="handleItemChange"
                         :props="props"
            ></el-cascader>
          </el-form-item>
          <el-form-item label="支行：" width="120">
            <el-autocomplete v-model="autobankName" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配" @select="handleSelect" style="width:70%"></el-autocomplete>
          </el-form-item>
          <el-form-item label="银行绑定手机号：" width="120">
            <el-input style="width: 70%" size="small" v-model="bankQuery.reserveMobile"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button type="primary" style="width: 200px;margin-top: -50px;position: relative;top: -30px;" @click="changeBankNo" :disabled="bankDis">确 定</el-button>
        </div>
      </el-dialog>
      <!--修改归属-->
      <el-dialog title="修改商户归属信息" v-model="dealerMask">
        <el-form :label-position="right" label-width="150px">
          <el-form-item label="切换类型：" width="120" style="margin-bottom: 0">归属关系
          </el-form-item>
          <el-form-item label="当前一级代理：" width="120" style="margin-bottom: 0">
            {{msg.proxyName|filterDealer}}
          </el-form-item>
          <el-form-item label="当前二级代理：" width="120" style="margin-bottom: 0">
            {{msg.proxyName1|filterDealer}}
          </el-form-item>
          <el-form-item label="切换对象：" width="120" style="margin-bottom: 0">
            <el-select size="small" placeholder="请选择" v-model="dealerQuery.changeType">
              <el-option label="切换金开门直属" value="1"></el-option>
              <el-option label="切换为一级直属" value="2"></el-option>
              <el-option label="切换到二级" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="代理商编号：" width="120" style="margin-bottom: 0">
            <el-input style="width: 70%" size="small" v-model="dealerNo" placeholder="请输入代理商编号，切换为金开门直属无需输入" maxlength="12"></el-input>
          </el-form-item>
          <el-form-item label="代理商名称：" width="120" style="margin-bottom: 0">
            {{dealerName}}
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="dealerMask = false" style="position: relative;top: -20px;">取 消</el-button>
          <el-button @click="changeDealer" type="primary" style="position: relative;top: -20px;">确 定</el-button>
          <div style="text-align: center;margin-bottom: 10px">切换成功后，新产生的收款立即生效</div>
        </div>
      </el-dialog>
      <div class="box box-primary" v-if="!isShow||res.length!=0">
        <p class="lead">审核日志</p>
        <div class="table-responsive">
          <div class="col-sm-12">
            <table id="example2" class="table table-bordered table-hover dataTable" role="grid"
                   aria-describedby="example2_info">
              <thead>
              <tr role="row">
                <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Browser: activate to sort column ascending">审核时间
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Platform(s): activate to sort column ascending">审核人
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Engine version: activate to sort column ascending">批复信息
                </th>
              </tr>
              </thead>
              <tbody id="content">
              <tr role="row" class="odd" v-for="re in this.$data.res">
                <td class="sorting_1">{{re.status|status}}</td>
                <td>{{re.createTime|changeTime}}</td>
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
              <td><input type="text" name="name" placeholder="不通过必填" v-model="reason"
                         style="height: 35px;line-height: 35px;width: 50%;"></td>
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
              action="/admin/photoChange/savePhotoChang"
              name="photo"
              :data={merchantId:id,type:photoType}
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
    </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'storeAudit',
    data () {
      return {
        btnLoad:false,
        auditClick:false,
        loading: true,
        dealerMask: false,
        isWad: false,
        id: '',
        msg: {
          id: '',
          merchantName: '',
          identity: '',
          address: '',
          bankNo: '',
          mobile: '',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1: '',
          proxyName: '',
          reserveMobile: '',
          createTime: '',
          proxyNameYQ: '',
          proxyNameYQ1: '',
        },
        reason: '',
        status: '',
        isShow: true,
        res: [],
        rateInfo: [],
        changeBank: false,
        bankQuery:{
          branchName:'',
          branchProvinceCode:'',
          branchProvinceName:'',
          branchCityCode:'',
          branchCityName:'',
          branchCountyCode:'',
          branchCountyName:'',
          merchantId:'',
          bankNo:'',
          reserveMobile:'',
          bankName:''
        },
        dealerNo:'',
        dealerName:'',
        dealerQuery:{
          changeType:'',
          markCode:'',
          merchantId:''
        },
        isUpload: false,
        photoType:'',
        bankDis:false,
        src:'',
        current:0,
        height:0,
        width:0,
        options2:[],
        props:{
          value: 'value',
          children: 'cities'
        },
        form:{
          branchProvince_name:'',
          branchProvince_code:'',
          branchCityCode:'',
          branchCityName:'',
          branchCountyCode:'',
          branchCountyName:'',
          bankName:'',
          branchCode:''
        },
        autobankName:''
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      this.bankQuery.merchantId = this.$route.query.id;
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
      changeBankFn: function () {
        this.changeBank = true;
        this.bankQuery = {
            branchName:'',
            branchProvinceCode:'',
            branchProvinceName:'',
            branchCityCode:'',
            branchCityName:'',
            branchCountyCode:'',
            branchCountyName:'',
            merchantId:'',
            bankNo:'',
            reserveMobile:'',
            bankName:''
        };
      },
      marryBankSearch: function (queryString, cb) {
        var results=[],url='';
        this.$http.post('/admin/hsyMerchantList/getPersonalBankNameList',{bankName:queryString,cardNo:this.bankQuery.bankNo})
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
      wad:function () {
        this.form = {
          branchProvince_name:'',
          branchProvince_code:'',
          branchCityCode:'',
          branchCityName:'',
          branchCountyCode:'',
          branchCountyName:'',
          bankName:'',
          branchCode:''
        }
        this.isWad = true;
      },
      handleItemChange: function (val) {
        if(val.length==1){
          this.$http.post('/join/selectCities',{code:val[0]})
            .then(res=>{
            for(let i=0;i<this.options2.length;i++){
            if(this.options2[i].value==val[0]){
              this.form.branchProvince_name = this.options2[i].aname;
              this.form.branchProvince_code = this.options2[i].value;
              this.bankQuery.branchProvinceName = this.options2[i].aname;
              this.bankQuery.branchProvinceCode = this.options2[i].value;
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
            this.citys = res.data;
            for(let i=0;i<this.options2.length;i++){
              if(this.options2[i].value==val[0]){
                for(let k=0;k<this.options2[i].cities.length;k++){
                  if(this.options2[i].cities[k].value==val[1]){
                    this.form.branchCityCode = this.options2[i].cities[k].value;
                    this.form.branchCityName = this.options2[i].cities[k].aname;
                    this.bankQuery.branchCityCode = this.options2[i].cities[k].value;
                    this.bankQuery.branchCityName = this.options2[i].cities[k].aname;
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
      querySearchAsync(queryString, cb) {
        var results=[],districtCode='',cardBank = '';
        //查支行
        districtCode=this.form.branchCityCode;
        if(this.changeBank){
          cardBank=this.bankQuery.bankName;
        }else {
          cardBank=this.msg.bankName;
        }
        this.form.branchCode=''
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
      handleSelect(item) {
        console.log(item);
        this.form.branchCode = item.branchCode;
        this.bankQuery.branchName = item.branchName;
      },
      submit: function () {
        for(let i=0; i<this.citys.length;i++){
            if(this.citys[i].value == this.cityCode[2]){
              this.form.branchCountyCode = this.citys[i].aname;
              this.form.branchCountyName = this.citys[i].value;
            }
        }
        this.form.id = this.id;
        this.form.accountId = this.msg.accountId;
        this.form.bankName = this.autobankName;
        this.form.status = this.status;
        var flag=false;
        for(let k in this.form){
            if(this.form[k]==''){
                flag = true;
                break;
            }
        }
        if(flag){
          this.$message({
            showClose: true,
            message: '请补全信息',
            type: 'success'
          });
        }else{
          this.btnLoad = true;
          this.$http.post('/admin/QueryMerchantInfoRecord/saveNo',this.form)
            .then(res=>{
              this.$message({
                showClose: true,
                message: '补填成功',
                type: 'success'
              });
              this.btnLoad = false;
              this.isWad = false;
              this.getData()
              location.reload()
            })
            .catch(err=>{
              this.btnLoad = false;
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        }
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
          var img1 = mask.getElementsByTagName('img')[0];
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
          var img1 = mask.getElementsByTagName('img')[0];
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
        console.log(err)
        this.$message({
          showClose: true,
          message: '上传失败',
          type: 'error'
        });
      },
      getData:function () {
        this.loading = true;
        this.$http.post('/admin/QueryMerchantInfoRecord/getAll', {id: this.$data.id})
          .then(function (res) {
            this.$data.msg = res.data.list[0];
            this.$data.res = res.data.res;
            this.status = res.data.list[0].status;
            if (this.status != 2) {
              this.isShow = false;
            }else {
              this.isShow = true;
            }
            this.loading = false;
            this.$data.rateInfo = res.data.rateInfo;
            for (let i = 0; i < this.rateInfo.length; i++) {
              this.rateInfo[i].merchantRate = parseFloat(this.rateInfo[i].merchantRate * 100).toFixed(2) + '%'
              this.rateInfo[i].withdrawMoney = this.rateInfo[i].withdrawMoney + '元/笔'
            }
          }, function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changeDealer: function () {
        this.loading = true;
        this.dealerQuery.markCode = this.dealerNo;
        this.dealerQuery.merchantId = this.$route.query.id;
        this.$http.post('/admin/merchantInfo/changeDealer',this.dealerQuery)
          .then(()=>{
            this.$message({
              showClose: true,
              message: '更新代理商成功',
              type: 'success'
            });
            this.dealerMask = false;
            setTimeout(function () {
              location.reload();
            },200);
            this.loading = false
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.loading = false
          })
      },
      //修改名称
      reset: function () {
        this.$prompt('请输入新名称', '修改上报名称', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          this.$http.post('/admin/changeMerchantName/change', {id: this.$route.query.id, merchantChangeName: value})
            .then(function (res) {
              this.$data.msg.merchantChangeName = value;
              this.$message({
                showClose: true,
                type: 'success',
                message: '修改成功'
              });
            })
            .catch(function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消修改'
          });
        });
      },
      //身份证校验
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
        if(this.msg.identity==''||this.msg.identity==null){
          this.$message({
            showClose: true,
            message: '身份证号不正确',
            type: 'error'
          })
        }else if(!this.test(this.msg.identity)){
          this.$message({
            showClose: true,
            message: '身份证号不正确',
            type: 'error'
          });
        }else{
          this.auditClick = true;
          this.$http.post('/admin/merchantInfoCheckRecord/record', {
            merchantId: this.$data.id,
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
        this.$http.post('/admin/merchantInfoCheckRecord/auditFailure', {
          merchantId: this.$data.id,
          descr: this.$data.reason
        })
          .then(function (res) {
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
      // 修改结算卡
      changeBankNo: function () {
        this.bankDis = true;
        for(let i=0; i<this.citys.length;i++){
          if(this.citys[i].value == this.cityCode[2]){
            this.bankQuery.branchCountyCode = this.citys[i].aname;
            this.bankQuery.branchCountyName = this.citys[i].value;
          }
        }
        this.bankQuery.merchantId = this.id;
        this.$http.post('/admin/accountBank/changeBankCard',this.bankQuery)
          .then(res=>{
            this.$message({
              showClose: true,
              type: 'success',
              message: '修改成功'
            });
            location.reload()
            this.getData();
            this.changeBank = false;
            this.bankDis = false;
          })
          .catch(err=>{
            this.bankDis = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changeBig: function (e) {
        e = e || window.event;
        var obj = e.srcElement || e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        this.src = img.src = obj.src;
        mask.style.display = 'block'
        var Img = new Image;
        Img.src = this.src;
        Img.onload = ()=>{
          this.height = Img.height;//图片的高度
          this.width = Img.width;//图片的宽度
          this.reduction()
        }
      },
      isNo: function () {
        document.getElementById('mask').style.display = 'none'
      },
      toDet:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/dataHistory?merchantId='+this.id+'&type=hss');
//        this.$router.push({path:'/admin/record/dataHistory',query:{merchantId:this.id}})
      }
    },
    computed:{
      $msg:function () {
        return this.msg
      }
    },
    filters: {
      status: function (val) {
        val = Number(val)
        if (val == 0) {
          val = "已注册"
        } else if (val == 1) {
          val = "已提交基本资料"
        } else if (val == 2) {
          val = "待审核"
        } else if (val == 3) {
          val = "审核通过"
        } else if (val == 4) {
          val = "审核未通过"
        }
        return val;
      },
      changeTime: function (val) {
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
        }
      },
      filterDealer: function (val) {
        return val = val ? val : '无'
      }
    },
    watch: {
      dealerNo:function (val, oldVal) {
        if(val.length==12){
          this.$http.post('/admin/dealer/getDealerByMarkCode',{markCode:val})
            .then(res =>{
                this.dealerName = res.data;
            })
            .catch(err =>{
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  .lead{
    margin: 0 10px 0 15px;
  }
  .box-primary {
    padding-bottom: 15px;
  }

  .label {
    color: #333;
    span, a {
      font-weight: normal;
    }
  }
  a {
    color: #20a0ff;
  }
  .mask {
    background: rgba(0, 0, 0, 0.3);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    p {
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

    img {
      display: inherit;
      /*height: 850px;*/
      margin: 0 auto;
    }
  }
</style>
