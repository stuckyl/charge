package com.charge.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.TotalDigitsDocumentImpl;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.repository.CommonRepository;


/**
 * @author wenst
 * @version v1.0
 * @Title: EmployeeDeclareService
 * @Description: 图表导出service
 * @date 2018年3月22日
 */
@Service
@Transactional
public class ExportDiagramService {
	private static final Logger log = Logger.getLogger(EmailConfigService.class);
	@Autowired
	private CommonRepository commonRepository;
	@Autowired
    private CommonService commonService;
	/**
	 * 导出excel函数 工资表
	 * @date  j 0814
	 * @param list
	 * @return
	 */
	public Map<String,Object> exportFinancialVoucher(String aPlace,Date date){
		Map<String,Object> result = new HashMap<String,Object>();
		List<EmployeeDeclareEntity> list =commonRepository.getVoucherList(aPlace, date);
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = commonRepository.placeToCompany(aPlace)+mo.toString()+"月工资表";
		//读取excel模板
		if(StringUtil.isNotEmpty(list)){
			File excelFile=new File(ExportDiagramService.class.getResource("/")
					.getPath()+"excel-template/FinancialVoucher.xlsx");
			if(!excelFile.exists()){
				return null;
			}
			InputStream is=null;
			Workbook wb=null;
			try {
				is = new FileInputStream(excelFile);
				wb=new XSSFWorkbook(is);
				Sheet sheet=wb.getSheetAt(0);
				Row row = sheet.getRow(0);
				if(null==row){
					row=sheet.createRow(0);
				}
				row.getCell(1).setCellValue(head);
				for(int i=0;i<list.size();i++){
					EmployeeDeclareEntity employee = list.get(i);
					Row oldRow = sheet.getRow(i + 1);
                    row = sheet.getRow(i + 2);
					if(null==row){
						row=sheet.createRow(i+2);
					}
					row.setRowStyle(oldRow.getRowStyle());
					short lastCellNum = oldRow.getLastCellNum();
					for(int j = 1 ;j < lastCellNum;j++) {
                    	Cell createCell = row.createCell(j);
                    	Cell cell = oldRow.getCell(j);
                    	CellStyle cellStyle = wb.createCellStyle();
                    	cellStyle.cloneStyleFrom(cell.getCellStyle());
//                    	CellStyle cellStyle = cell.getCellStyle();
                    	DataFormat format = wb.createDataFormat();
//                    	//poi 设置excel单元格样式封装 样式"($#,##0.00_)"
                    	cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                    	if(j==4||j==5||j==6||j==7||j==8||j==9||j==10){
                    		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    	}
                    	createCell.setCellStyle(cellStyle);
                    }
                    row.getCell(1).setCellValue(employee.getEmployeeInfo().getCode());
                    row.getCell(2).setCellValue(employee.getEmployeeInfo().getName());
                    TSDepart depart = commonService.getEntity(TSDepart.class, employee.getEmployeeDepartment());
                    if(StringUtil.isNotEmpty(depart))
                    	row.getCell(3).setCellValue(depart.getDepartname());
                    Double payAll =0.0;
                    Double sixPay = 0.0;
                    Double Per = 0.0;
                    if(employee.getEmployeeInfo().getA1Place().equals(aPlace)){
                    	if(employee.getPayableSalary()<employee.getEmployeeInfo().getA1Payment()){
                    		payAll = employee.getPayableSalary();
                    	}else{
                    		payAll = employee.getEmployeeInfo().getA1Payment();
                    	}
                    	sixPay = employee.getSixPersonalBurdenOne();
                    	Per =employee.getPerToneTaxOne();
                    	row.getCell(4).setCellValue(payAll);
                    	row.getCell(5).setCellValue(sixPay);
                    	row.getCell(6).setCellValue(Per);
                    	row.getCell(10).setCellValue(employee.getSixCompanyBurdenOne());
                    }else{
                    	if(employee.getPayableSalary()<employee.getEmployeeInfo().getA1Payment()){
                    		payAll = 0.00;
                    	}else{
                    		payAll = employee.getPayableSalary()-employee.getEmployeeInfo().getA1Payment();
                    	}
                    	if(employee.getPerToneTaxTwo()!=null)
                    		Per=employee.getPerToneTaxTwo();
                    	row.getCell(4).setCellValue(payAll);
                    	row.getCell(5).setCellValue(sixPay);
                    	row.getCell(6).setCellValue(Per);
                    	row.getCell(10).setCellValue(0);
                    }
                    row.getCell(7).setCellValue(employee.getDAnnualBonus());
                    row.getCell(8).setCellValue(0.00);
                    row.getCell(9).setCellValue(payAll-sixPay-Per);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("导出错误");
				e.printStackTrace();
			}
			result.put("wb", wb);
			result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
			return result;
		}else{
			return null;
		}

	}
	/**
	 * 导出excel函数 成本统计
	 * @date  j 0814
	 * @param list
	 * @return
	 */
	public Map<String,Object> exportCostStatistics(String place,Date date){
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Double> cost = new HashMap<>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = commonRepository.placeToCompany(place)+mo.toString()+"月成本统计";
		cost =commonRepository.getDepatrCost(place,date);
		if(StringUtil.isNotEmpty(cost)){
			File excelFile=new File(ExportDiagramService.class.getResource("/")
					.getPath()+"excel-template/CostStatistics.xlsx");
			if(!excelFile.exists()){
				return null;
			}
			InputStream is=null;
			Workbook wb=null;
			try {
				is = new FileInputStream(excelFile);
				wb=new XSSFWorkbook(is);
				Sheet sheet=wb.getSheetAt(0);
				Row row = sheet.getRow(0);
				row.getCell(1).setCellValue(head);
				int i=0;
				for(String pl : cost.keySet()){
					Row oldRow = sheet.getRow(i + 1);
                    row = sheet.getRow(i + 2);
					if(null==row){
						row=sheet.createRow(i+2);
					}
					row.setRowStyle(oldRow.getRowStyle());
					short lastCellNum = oldRow.getLastCellNum();
					for(int j = 1 ;j < lastCellNum;j++) {
                    	Cell createCell = row.createCell(j);
                    	Cell cell = oldRow.getCell(j);
                    	CellStyle cellStyle = wb.createCellStyle();
                    	cellStyle.cloneStyleFrom(cell.getCellStyle());
//                    	CellStyle cellStyle = cell.getCellStyle();
                    	DataFormat format = wb.createDataFormat();
//                    	//poi 设置excel单元格样式封装 样式"($#,##0.00_)"
                    	cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                    	if(j==2){
                    		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    	}
                    	createCell.setCellStyle(cellStyle);
                    }
                    row.getCell(1).setCellValue(pl);
                    row.getCell(2).setCellValue(cost.get(pl));
                    i++;
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("导出错误");
				e.printStackTrace();
			}
			result.put("wb", wb);
			result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
			return result;
		}else{
			return null;
		}
	}
	/**
	 * 导出excel函数 网银导入
	 * @date  j 0814
	 * @param list
	 * @return
	 */

	public Map<String, Object> exportEmpDeclareForAccount(String place,Date date) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<EmployeeDeclareEntity> employees =commonRepository.getVoucherList(place, date);
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = commonRepository.placeToCompany(place)+mo.toString()+"月网银导入";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
				.getPath() + "excel-template/Internetbank.xlsx");
		if (!excelFile.exists()) {
			return null;
		}
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(excelFile);
			wb=new XSSFWorkbook(is);
			Sheet sheet=wb.getSheetAt(0);
			Row row = sheet.getRow(0);
			for(int i=0; i<employees.size();i++){
				EmployeeDeclareEntity employee = employees.get(i);
				Row oldRow = sheet.getRow(i);
                row = sheet.getRow(i+1);
				if(null==row){
					row=sheet.createRow(i+1);
				}
				row.setRowStyle(oldRow.getRowStyle());
				short lastCellNum = oldRow.getLastCellNum();
				for(int j = 1 ;j < lastCellNum;j++) {
                	Cell createCell = row.createCell(j);
                	Cell cell = oldRow.getCell(j);
                	CellStyle cellStyle = wb.createCellStyle();
                	cellStyle.cloneStyleFrom(cell.getCellStyle());
//                	CellStyle cellStyle = cell.getCellStyle();
                	DataFormat format = wb.createDataFormat();
//                	//poi 设置excel单元格样式封装 样式"($#,##0.00_)"
                	cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                	if(j==3){
                		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                	}
                	createCell.setCellStyle(cellStyle);
                }
                row.getCell(1).setCellValue(employee.getEmployeeInfo().getCmbAccount());
                row.getCell(2).setCellValue(employee.getEmployeeInfo().getName());
                Double payAll =0.0;
                Double sixPay = 0.0;
                Double Per = 0.0;
                if(employee.getEmployeeInfo().getA1Place().equals(place)){
                	if(employee.getPayableSalary()<employee.getEmployeeInfo().getA1Payment()){
                		payAll = employee.getPayableSalary();
                	}else{
                		payAll = employee.getEmployeeInfo().getA1Payment();
                	}
                	sixPay = employee.getSixPersonalBurdenOne();
                	Per =employee.getPerToneTaxOne();
                }else{
                	if(employee.getPayableSalary()<employee.getEmployeeInfo().getA1Payment()){
                		payAll = 0.00;
                	}else{
                		payAll = employee.getPayableSalary()-employee.getEmployeeInfo().getA1Payment();
                	}
                	if(employee.getPerToneTaxTwo()!=null)
                		Per=employee.getPerToneTaxTwo();
                }
                row.getCell(3).setCellValue(payAll-sixPay-Per);
                //新增部门
                TSDepart tsDepart = commonService.get(TSDepart.class, employee.getEmployeeDepartment());
                row.getCell(4).setCellValue(tsDepart.getDepartname());
			}
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		result.put("wb", wb);
		result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
     * 格式化日期
     *
     * @return
     */
    public String formatDate(Date date, String fmt) {
        String dateStr = "";
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }
}
