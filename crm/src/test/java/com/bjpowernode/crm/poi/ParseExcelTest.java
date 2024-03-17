package com.bjpowernode.crm.poi;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjpowernode.crm.commons.utils.HSSFUtils;

/**
 * 使用apache-poi解析excel文件
 */
public class ParseExcelTest {
	public static void main(String[] args) throws Exception {
		// 创建HSSFWorkbook对象，来解析excel文件
		// 根据指定的excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
		InputStream is = new FileInputStream("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\aaa.xls");
		HSSFWorkbook wb = new HSSFWorkbook(is);
		// 根据wb获取HSSFSheet对象，封装了某一页的所有信息
		HSSFSheet sheet = wb.getSheetAt(0);// 页的下标，下标从0开始，依次增加
		// 根据sheet获取HSSFRow对象，封装了一行的所有信息
		HSSFRow row = null;
		HSSFCell cell = null;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {// sheet.getLastRowNum()最后一行的下标即 总行数-1
			row = sheet.getRow(i);// 行的下标，下标从0开始，依次增加

			for (int j = 0; j < row.getLastCellNum(); j++) {// row.getLastCellNum()最后一列的下标 总列数+1
				// 根据row对象获取HSSFCell对象，封装了一列的所有信息
				cell = row.getCell(j);// 列的下标，下标从0开始，依次增加

				// 获取列中的数据
				System.out.print(HSSFUtils.getCellValueForStr(cell) + " ");
			}
			// 每一行中所有列都打完，打印一个换行
			System.out.println();
		}
	}

}
