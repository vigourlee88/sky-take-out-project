package com.bjpowernode.crm.workbench.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;

@Controller
public class ActivityController {

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityRemarkService activityRemarkService;

	@RequestMapping("/workbench/activity/index.do")
	public String index(HttpServletRequest request) {
		// 调用service层方法，查询所有的用户
		List<User> userList = userService.queryAllUsers();
		// 把数据保存到request中
		request.setAttribute("userList", userList);
		// 请求转发到市场活动的主页面
		return "workbench/activity/index";
	}

	@RequestMapping("/workbench/activity/saveCreateActivity.do")
	public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session) {
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		// activity.setId();
		activity.setId(UUIDUtils.getUUID());
		activity.setCreateTime(DateUtils.formateDateTime(new Date()));// 所有日期和时间在数据库里都是以字符串存储的，将来生成的实体类对象也是以字符串定义的
		activity.setCreateBy(user.getId());// 当前用户在session中获取

		ReturnObject returnObject = new ReturnObject();
		// 注意 写数据时需要判断service层报不报异常
		try {
			// 调用service层方法，
			int ret = activityService.saveCreateActivity(activity);

			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后重试...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后重试...");
		}
		return returnObject;

	}

	@RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
	public @ResponseBody Object queryActivityByConditionForPage(String name, String owner, String startDate,
			String endDate, int pageNo, int pageSize) {
		// 封装参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("owner", owner);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("beginNo", (pageNo - 1) * pageSize);
		map.put("pageSize", pageSize);
		// 调用service层方法，查询数据
		List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
		int totalRows = activityService.queryCountOfActivityByCondition(map);
		// 根据查询结果，生成响应信息
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("activityList", activityList);
		retMap.put("totalRows", totalRows);

		return retMap;
	}

	@RequestMapping("/workbench/activity/deleteActivityByIds.do")
	// 定义形参接收客户端发来的参数，要和客户端发来的参数名保持一致
	public @ResponseBody Object deleteActivityByIds(String[] id) {

		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service层方法，删除市场活动
			int ret = activityService.deleteActivityByIds(id);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后重试....");
			}
		} catch (Exception e) {

			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后重试....");
		}

		return returnObject;
	}

	@RequestMapping("/workbench/activity/queryActivityById.do")
	public @ResponseBody Object queryActivityById(String id) {// 接收来自客户端的请求参数id

		// 调用service层方法，查询市场活动
		Activity activity = activityService.queryActivityById(id);
		// 根据查询结果返回响应信息
		return activity;
	}

	@RequestMapping("/workbench/activity/saveEditActivity.do")
	public @ResponseBody Object saveEditActivity(Activity activity, HttpSession session) {
		User user = (User) session.getAttribute(Contants.SESSION_USER);// 获取当前用户要和放的时候一致
		// 封装参数(还缺少2个形参)
		activity.setEditTime(DateUtils.formateDateTime(new Date()));
		activity.setEditBy(user.getId());// 一定登陆成功了才能修改，用户信息存放在session中,EditBy保存的是用户id,user.getId(),外键唯一

		ReturnObject returnObject = new ReturnObject();
		// 修改，写数据，要考虑写成功还是失败
		try {
			// 调用service层方法，保存修改的市场活动
			int ret = activityService.saveEditActivity(activity);
			// 根据处理结果，生成响应信息

			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍重试....");
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍重试....");
		}

		return returnObject;
	}

	@RequestMapping("/workbench/activity/fileDownload.do")
	public void fileDownload(HttpServletResponse response) throws Exception {
		// 前台发一个文件下载请求，不需要定义形参
		// 读取磁盘上的excel文件
		// 1.设置响应类型 网页类型是text/html
		response.setContentType("application/octet-stream;charset=UTF-8");
		// 2.获取输出流
		// PrintWriter out = response.getWriter();//字符流，写文本文件
		OutputStream out = response.getOutputStream();// 字节流

		// 浏览器接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息；即使打不开，也会调用应用程序打开；只有实在打不开，才会激活文件下载窗口。
		// 可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
		response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");

		// 读取excel文件(InputStream)，把输出到浏览器(OutputStream)
		InputStream is = new FileInputStream("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\studentList.xls");
		byte[] buff = new byte[256];
		int len = 0;
		while ((len = is.read(buff)) != -1) {
			out.write(buff, 0, len);
		}

		// 关闭资源时在Java原则，谁开启new的对象谁关闭
		is.close();
		out.flush();// response new的tomcat会关闭
	}

	@RequestMapping("/workbench/activity/exportAllActivitys.do") // 与它处理的资源路径一致
	public void exportAllActivitys(HttpServletResponse response) throws IOException {
		// 不需要获取参数和封装参数，直接调用service层方法，查询所有市场活动
		List<Activity> activityList = activityService.queryAllActivitys();
		// apache-poi插件创建excel文件，并且把activityList写入到excel恩建中
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("市场活动列表");
		HSSFRow row = sheet.createRow(0);// 行
		HSSFCell cell = row.createCell(0);// 列
		cell.setCellValue("ID");
		cell = row.createCell(1);
		cell.setCellValue("所有者");
		cell = row.createCell(2);
		cell.setCellValue("名称");
		cell = row.createCell(3);
		cell.setCellValue("开始日期");
		cell = row.createCell(4);
		cell.setCellValue("结束日期");
		cell = row.createCell(5);
		cell.setCellValue("成本");
		cell = row.createCell(6);
		cell.setCellValue("描述");
		cell = row.createCell(7);
		cell.setCellValue("创建时间");
		cell = row.createCell(8);
		cell.setCellValue("创建者");
		cell = row.createCell(9);
		cell.setCellValue("修改时间");
		cell = row.createCell(10);
		cell.setCellValue("修改者");

		// 遍历activityList,创建HSSFRow对象，生成所有的数据行
		if (activityList != null && activityList.size() > 0) {
			Activity activity = null;
			for (int i = 0; i < activityList.size(); i++) {
				activity = activityList.get(i);

				// 每次遍历出一个activity,生成一行
				row = sheet.createRow(i + 1);

				// 每一行创建11列，每一列的数据从activity中获取
				cell = row.createCell(0);
				cell.setCellValue(activity.getId());
				cell = row.createCell(1);
				cell.setCellValue(activity.getOwner());
				cell = row.createCell(2);
				cell.setCellValue(activity.getName());
				cell = row.createCell(3);
				cell.setCellValue(activity.getStartDate());
				cell = row.createCell(4);
				cell.setCellValue(activity.getEndDate());
				cell = row.createCell(5);
				cell.setCellValue(activity.getCost());
				cell = row.createCell(6);
				cell.setCellValue(activity.getDescription());
				cell = row.createCell(7);
				cell.setCellValue(activity.getCreateTime());
				cell = row.createCell(8);
				cell.setCellValue(activity.getCreateBy());
				cell = row.createCell(9);
				cell.setCellValue(activity.getEditTime());
				cell = row.createCell(10);
				cell.setCellValue(activity.getEditBy());

			}
		}
		// 根据wb对象生成excel文件
//		OutputStream os = new FileOutputStream("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\activityList.xls");
//		wb.write(os);
		// 关闭资源
//		os.close();
//		wb.close();

		// 把生成的excel文件下载到用户客户端
		// 1.设置响应类型,注入HttpServletResponse
		response.setContentType("application/octet-stream;charset=UTF-8");
		// 设置响应头信息
		response.setHeader("Content-Disposition", "attachment;filename=activityList.xls");
		// 2.获取输出流
		OutputStream out = response.getOutputStream();
		// 3.读取excel文件，往外输出
//		InputStream is = new FileInputStream("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\activityList.xls");
//		byte[] buff = new byte[256];
//		int len = 0;
//		while ((len = is.read(buff)) != -1) {
//			out.write(buff, 0, len);
//		}
//		is.close();

		// 从wb直接写到输出流浏览器
		wb.write(out);
		wb.close();
		out.flush();
	}

	/**
	 * 配置springmvc的文件上传解析器
	 * 
	 * @param userName
	 * @param myFile
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("/workbench/activity/fileUpload.do")
	public @ResponseBody Object fileUpload(String userName, MultipartFile myFile) throws Exception {
		// 把文本数据打印到控制台
		System.out.println("userName=" + userName);
		// 把文件在服务指定的目录中生成一个同样的文件
		// 路径必须手动创建好，文件可以不存在，会自动创建
		String originalFilename = myFile.getOriginalFilename();// 用户上传的文件名
		File file = new File("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\", originalFilename);
		myFile.transferTo(file);

		// 返回响应信息
		ReturnObject returnObject = new ReturnObject();
		returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
		returnObject.setMessage("上传成功");
		return returnObject;
	}

	@RequestMapping("/workbench/activity/importActivity.do")
	public @ResponseBody Object importActivity(MultipartFile activityFile, HttpSession session) {// 接收excel上传的文件,需要提前配置好springmvc文件上传的解析器
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		ReturnObject returnObject = new ReturnObject();
		try {

			// 把接收到的excel文件写到磁盘目录中
			// String originalFilename = activityFile.getOriginalFilename();// 用户上传的文件名
			// File file = new
			// File("D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\",originalFilename);
			// activityFile.transferTo(file);

			// 解析excel文件，获取文件中的数据，并且封装成activityList
			// 根据文件生成wb对象
			// InputStream is = new FileInputStream(
			// "D:\\BaiduNetdiskDownload\\动力节点CRM客户管理系统\\ServerDir\\" + originalFilename);

			InputStream is = activityFile.getInputStream();

			HSSFWorkbook wb = new HSSFWorkbook(is);
			HSSFSheet sheet = wb.getSheetAt(0);
			// 根据sheet获取HSSFRow对象，封装了一行的所有信息
			HSSFRow row = null;
			HSSFCell cell = null;
			Activity activity = null;
			List<Activity> activityList = new ArrayList<>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {// sheet.getLastRowNum()最后一行的下标即 总行数-1
				row = sheet.getRow(i);// 行的下标，下标从0开始，依次增加
				activity = new Activity();
				activity.setId(UUIDUtils.getUUID());
				activity.setOwner(user.getId());
				activity.setCreateTime(DateUtils.formateDateTime(new Date()));
				activity.setCreateBy(user.getId());

				for (int j = 0; j < row.getLastCellNum(); j++) {// row.getLastCellNum()最后一列的下标 总列数+1
					// 根据row对象获取HSSFCell对象，封装了一列的所有信息
					cell = row.getCell(j);// 列的下标，下标从0开始，依次增加

					// 获取列中的数据,不用打印到控制台
					// System.out.print(HSSFUtils.getCellValueForStr(cell) + " ");
					String cellValue = HSSFUtils.getCellValueForStr(cell);
					if (j == 0) {// 第一列变成名称了
						activity.setName(cellValue);
					} else if (j == 1) {
						activity.setStartDate(cellValue);
					} else if (j == 2) {
						activity.setEndDate(cellValue);
					} else if (j == 3) {
						activity.setCost(cellValue);
					} else if (j == 4) {
						activity.setDescription(cellValue);
					}
				}
				// 每一行中所有列都打完，打印一个换行
				// System.out.println();
				// 把每一行中所有列都封装完成之后，把activity保存到List中，循环体外创建List<Activity>
				activityList.add(activity);
			}

			// 调用service层方法，保存市场活动
			int ret = activityService.saveCreateActivityByList(activityList);

			returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			returnObject.setRetData(ret);

		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后重试");
		}
		return returnObject;
	}

	/**
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/workbench/activity/detailActivity.do")
	public String detailActivity(String id, HttpServletRequest request) {// 返回跳转页面的资源路径，字符串string
		// 调用service层方法，查询数据
		// 调市场活动的service,查询市场活动的信息
		Activity activity = activityService.queryActivityForDetailById(id);
		// 调用备注的service,需注入备注的service
		List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);

		// 把数据保存到作用域request中(要注入HttpServletRequest request)
		request.setAttribute("activity", activity);
		request.setAttribute("remarkList", remarkList);
		// 跳转(请求转发)到到明细页面
		return "workbench/activity/detail";// 转发到的资源路径"workbench/activity/detail"
	}
}
