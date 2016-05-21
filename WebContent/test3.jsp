<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%-- <%@ include file="/pages/back/taglib.jsp"%>  --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>虚拟支付 > 押金退款一审</title>
<!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /> -->
<%-- <link type="text/css" href="${pageContext.request.contextPath}/resource/back/css/main.css" rel="stylesheet" /> --%>
<style>
	pre{
 		font-size:12px;
 		line-height:20px;
 		margin:20px 0px 10px 0px;
 		font-family:Verdana, Arial, Helvetica, sans-serif;
	}
	
</style>
<script type="text/javascript" src="jquery-1.4.4.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/public/jquery-latest.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/back/js/table.js"></script>
<link type="text/css" href="${pageContext.request.contextPath}/resource/back/css/calendar.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/back/js/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/back/js/ui.datepicker.js"></script> --%>
<script type="text/javascript">
function keyDown(e) {
	document.onkeydown = function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e.keyCode == 13) {
			checkRefundEvent.checkDeductionAmt();
		}
	}
}
	/* var checkRefundEvent = new function(){
		this.checkDeductionAmt = function(){
			alert("核算开始");
			var checkDeductionAmt = $("[name=checkDeductionAmt]").val();
			alert(checkDeductionAmt);
			var payAmt = ${detailFirstVerifyDto.payAmt };
			alert(payAmt);
			if(checkDeductionAmt==""){
				alert("请填写扣罚金额，若不扣罚则填0");
				return false;
			}else {
				if(isNaN(checkDeductionAmt){
					alert("请填写数字");
					return false;
				}else{
					if(checkDeductionAmt>payAmt){
						alert("扣罚金额超过支付金额，请重新填写");
						return false;
					}else{
						
					}
				}
			}
		};
	}; */
	
	var checkRefundEvent = {};
	
	checkRefundEvent.checkDeductionAmt = function(){
			alert("核算开始");
			var checkDeductionAmts = $("[name=checkDeductionAmt]").val();
			alert(checkDeductionAmts);
			var payAmt = "s";//${detailFirstVerifyDto.payAmt};
			alert(payAmt);
			if(checkDeductionAmts==""){
				alert("请填写扣罚金额，若不扣罚则填0");
				return false;
			}else {
				if(isNaN(checkDeductionAmts)) {
					alert("请填写数字");
					return false;
				}else{
					if(checkDeductionAmts>payAmt){
						alert("扣罚金额超过支付金额，请重新填写");
						return false;
					}else{
						
					}
				}
			}
		};
		
	function test() {
		alert('!!!');
	}
	
	$(function() {
		
		test();
		
		$('#sss').click(function() {
			alert("!!");
		})
	});
	
</script>
</head>
<body>
<div class="main">
	<div class="box">虚拟支付 > 押金退款一审</div>
	
	<form id="FORM_IN_JSP" name="FORM_IN_JSP" method="post" onkeydown="javascript:keyDown()">
		<input type="hidden" name="refundInfo" id="refundInfo" />

    <div class="field">
		<div class="title"><u></u><b>退款信息</b><span></span></div>
		<div class="body">
				   <table id="depositRefundTable" cellpadding="0" cellspacing="1" class="table table_white">
				   	<col width="70"><col width="70"><col width="50"><col width="50"><col width="70"><col width="70"><col width="60"><col width="50"><col width="50">
                        <thead>
                            <tr>
                                <td>银行订单号</td>
                                <td>支付时间</td>
                                <td>支付金额</td>
                                <td>支付银行</td>
                                <td>退款状态</td>
                                <td>申请退款时间</td>
                                <td>申请人</td>
                                <td>押金扣罚金额</td>
                                <td></td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${detailFirstVerifyDto.payNo }</td>
                                <td>${detailFirstVerifyDto.payDate }</td>
                                <td>${detailFirstVerifyDto.payAmt }</td>
                                <td>${detailFirstVerifyDto.bankCode }</td>
                                <td><eb:dictnm dictCode="${detailFirstVerifyDto.refundStatus }"/></td>
                                <td>${detailFirstVerifyDto.applyDate }</td>
                                <td>${detailFirstVerifyDto.applyPerson }</td>
                                <td><input name="checkDeductionAmt" type="text" size="10" align="middle"/></td>
                                <td></td>
                            </tr>
                        </tbody> 
                    </table>
					<div class="clear"></div>
		</div>
	</div>    
    <div class="field">
		<div class="title"><u></u><b>押金转发信息</b><span></span></div>
		<div class="body">
				   <table id="refundSegTable" name="refundSegTable" cellpadding="0" cellspacing="1" class="table table_white">
                      <col width="70"><col width="70"><col width="50"><col width="50"><col width="70"><col width="70"><col width="60"><col width="50"><col width="50">
                        <thead>
                            <tr>
                                <td>银行订单号</td>
                                <td>支付时间</td>
                                <td>支付金额</td>
                                <td>支付银行</td>
                                <td>退款状态</td>
                                <td>申请退款时间</td>
                                <td>申请人</td>
                                <td>押金扣罚金额</td>
                                <td>应退款</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${detailFirstVerifyDto.payNo }</td>
                                <td>${detailFirstVerifyDto.payDate }</td>
                                <td>${detailFirstVerifyDto.payAmt }</td>
                                <td>${detailFirstVerifyDto.bankCode }</td>
                                <td><eb:dictnm dictCode="${detailFirstVerifyDto.refundStatus }"/></td>
                                <td>${detailFirstVerifyDto.applyDate }</td>
                                <td>${detailFirstVerifyDto.applyPerson }</td>
                                <td><input id="deductionAmt" type="text" size="10" align="middle" readonly="readonly"/></td>
                                <td><input id="refundAmt" type="text" size="10" align="middle" readonly="readonly"/></td>
                            </tr>
                        </tbody>          
                    </table>
					<div class="clear"></div>
		</div>
	</div>
    
    <div class="field">
		<div class="title"><u></u><b>审核信息</b><span></span></div>
		
		<div class="form">
		<table id="refund_detail_table" cellpadding="0" cellspacing="0" class="table_table">
			<col width="100"><col width="150"><col width="100"><col width="150"><col width="100"><col width="150">
			<tr>
				<td class="a_right">审核执行人：</td>
				<td><input id="auditPerson" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.auditPerson}"/></td>
				<td class="a_right">审核退票金额：</td>
				<td><input id="refundAmt" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.refundAmt}"/></td>
				<td class="a_right">审核时间：</td>
				<td><input id="auditDate" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.auditDate}"/></td>
			</tr>
			<tr>
				<td class="a_right">审核操作意见：</td>
				<td colspan="5"><input id="auditComment" type="text" class="input readonly three" value="${detailFirstVerifyDto.auditComment }"/></td>
			</tr>
			<tr>
				<td class="a_right">退款执行人：</td>
				<td><input id="refundPerson" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.refundPerson }"/></td>
							 
				<td class="a_right">退款金额：</td>
				<td><input id="refundAmt" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.refundAmt }"/></td>
				<td class="a_right">退款时间：</td>
				<td><input id="refundDate" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.refundDate }"/></td>
			</tr>
			<tr>
				<td class="a_right">退款申请人：</td>
				<td><input id="applyPerson" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.applyPerson }"/></td>
				<td class="a_right">退款原因：</td>
				<td>
					<input id="applyReason" type="text" class="input readonly" readonly="readonly" value="<eb:dictnm dictCode='${detailFirstVerifyDto.applyReason }'/>"/>
				</td>
				<td class="a_right">退款申请时间：</td>
				<td><input id="applyDate" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.applyDate }"/></td>
			</tr>
			<tr>
				<td class="a_right">退款备注说明：</td>
				<td colspan="3"><input id="refundRemark" type="text" class="input readonly two" readonly="readonly" value="${detailFirstVerifyDto.refundRemark }"/></td>
				<td class="a_right">联系电话：</td>
				<td><input id="contactNo" type="text" class="input readonly" readonly="readonly" value="${detailFirstVerifyDto.contactNo }"/></td>
			</tr>
		</table> 
		</div>
		
	</div>

    <div class="button_field">
    	<eb:auth res="/back/deposit/deposit-refund!firstVerify.shtml">
    	
    		<input id="check_pass_button" type="button" name="submit" class="button_big" value="审核通过" onclick="javascript:checkRefundEvent.checkPassEvent()" disabled="disabled"/>
    		
    		<s:if test="#request.detailFirstVerifyDto.refundStatus=='33011'||#request.detailFirstVerifyDto.refundStatus=='33013'">
    		
            	<input type="button" name="submit" class="button_big" value="退款预核算" onclick="javascript:checkRefundEvent.checkDeductionAmt()"/>
            	<input type="button" name="submit" class="button_big" value="退款预核算2" onclick="javascript:test()"/>
            	
            	
            	<input type="button" id="refuse_check_button" name="reset" class="button_big reset" value="审核拒绝" onclick="javascript:checkRefundEvent.refuseCheck()"/>
    		
    		</s:if>
    		<s:else>
            	<input type="button" name="submit" class="button_big" value="退款预核算" onclick="javascript:checkRefundEvent.checkDeductionAmt()" disabled="disabled"/>
            	<input type="button" name="reset" class="button_big reset" value="审核拒绝" onclick="javascript:checkRefundEvent.refuseCheck()" disabled="disabled"/>
    		</s:else>
        </eb:auth>
            <input type="button" name="reset" class="button_big reset" value="关闭" onclick="javascript:self.close()"/>
            <input id="sss" type="button" name="submit" class="button_big" value="退款预核算3"/>
    </div>
    	
	
	</form>
	<form id="first_check_form" method="post">
		<div id="checkInfo"></div>
    	<input name="firstCheckDto.segInfos" type="hidden"/>
    	<input name="firstCheckDto.tktIds" type="hidden"/>
    	<input name="firstCheckDto.applyId" type="hidden" value="${tktDetailDto.applyId }"/>
    	<input name="firstCheckDto.auditRemark" type="hidden"/>
    	<input name="firstCheckDto.maxRefundAmt" type="hidden" value="${tktDetailDto.maxRefundAmt }"/>
    	<input name="firstCheckDto.isInter" type="hidden" value="${tktDetailDto.isInter }"/>
    </form>
	<form id="download_form" method="post" action="${pageContext.request.contextPath}/back/refund/checkrefund-manager!downloadFtpRefundFile.shtml">
    	<input type="hidden" name="refundFileId" />
    	<input type="hidden" name="refundFileStatus" />
    </form>
    <input type="hidden" name="detr.applyIds" id="detr.applyIds" value="${tktDetailDto.applyId }"/> 
    <input type="hidden" name="applyPerson" id="applyPerson" value="${detailFirstVerifyDto.applyPerson}"/>
</div>
</body>
</html>