<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>六金比例表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sixGoldScaleController.do?save">
			<input id="id" name="id" type="hidden" value="${sixGoldScalePage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							六金地点:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="sixGoldPlace" name="sixGoldPlace"  value="${sixGoldScalePage.sixGoldPlace}"  datatype="*"/>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							养老保险（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyEndowment" name="companyEndowment"  value="${sixGoldScalePage.companyEndowment}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							养老保险（个人）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="personalEndowment" name="personalEndowment" value="${sixGoldScalePage.personalEndowment}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							养老保险最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="endowmentMax" name="endowmentMax" value="${sixGoldScalePage.endowmentMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							养老保险最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="endowmentMin" name="endowmentMin" value="${sixGoldScalePage.endowmentMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							医疗保险（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyMedical" name="companyMedical" value="${sixGoldScalePage.companyMedical}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							医疗保险（个人）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="personalMedical" name="personalMedical" value="${sixGoldScalePage.personalMedical}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							医疗保险最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="medicalMax" name="medicalMax" value="${sixGoldScalePage.medicalMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							医疗保险最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="medicalMin" name="medicalMin" value="${sixGoldScalePage.medicalMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							失业保险（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyUnemployment" name="companyUnemployment" value="${sixGoldScalePage.companyUnemployment}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							失业保险（个人）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="personalUnemployment" name="personalUnemployment" value="${sixGoldScalePage.personalUnemployment}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							失业保险最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="unemploymentMax" name="unemploymentMax" value="${sixGoldScalePage.unemploymentMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							失业保险最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="unemploymentMin" name="unemploymentMin" value="${sixGoldScalePage.unemploymentMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							工伤（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyInjury" name="companyInjury" value="${sixGoldScalePage.companyInjury}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							工伤最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="injuryMax" name="injuryMax" value="${sixGoldScalePage.injuryMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							工伤最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="injuryMin" name="injuryMin" value="${sixGoldScalePage.injuryMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							生育（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyMaternity" name="companyMaternity" value="${sixGoldScalePage.companyMaternity}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							生育最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="maternityMax" name="maternityMax" value="${sixGoldScalePage.maternityMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							生育最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="maternityMin" name="maternityMin" value="${sixGoldScalePage.maternityMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							住房公积金（企业）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="companyHousingFund" name="companyHousingFund" value="${sixGoldScalePage.companyHousingFund}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							住房公积金（个人）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="personalHousingFund" name="personalHousingFund" value="${sixGoldScalePage.personalHousingFund}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							住房公积金最高:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="housingFundMax" name="housingFundMax" value="${sixGoldScalePage.housingFundMax}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							住房公积金最低:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="housingFundMin" name="housingFundMin" value="${sixGoldScalePage.housingFundMin}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>