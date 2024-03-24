<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

%>
<html>
<head>
   <base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		/*
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});*/
		
		//固有父元素remarkDivList,动态不行
		//.on("事件类型(事件函数跟事件类型一一对应)",目标选择器(类选择器)",事件函数(回调函数));
		//this表正在发生事件的div
		//以下是给所有的div添加鼠标悬停的事件了
		
		$("#remarkDivList").on("mouseover",".remarkDiv",function(){
		    $(this).children("div").children("div").show();
		});
		
		/*$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});*/
		//以下是给所有的div添加鼠标移开的事件了
		$("#remarkDivList").on("mouseout",".remarkDiv",function(){
		    $(this).children("div").children("div").hide();
		});
		
		/*$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});*/
		
		$("#remarkDivList").on("mouseover",".myHref",function(){
		    $(this).children("span").css("color","red");
		});
		
		/*$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});*/
		
		$("#remarkDivList").on("mouseout",".myHref",function(){
		    $(this).children("span").css("color","#E6E6E6");
		});
		
		//给"保存"按钮添加单击事件
		$("#saveCreateActivityRemarkBtn").click(function(){
		    //1.发请求 2.处理响应
		    //收集参数 (输入框)noteContent activityId retData
		    var noteContent=$.trim($("#remark").val());
		    //在浏览器市场活动明细页面的作用域中储存的
		    //在页面detail.jsp中获取作用域中的数据使用EL表达式
		    //js中必须要加''，浏览器才能当成字符串处理，否则当成变量处理
		    //jsp页面的运行原理
		    var activityId='${activity.id}';
		    //必须要保证合法，发送到后台
		    //表单验证
		    //备注内容不能为空,自己输入的要验证
		    if(noteContent==""){
		       alert("备注内容不能为空");
		       return;
		    }
		    //发送请求 异步
		    $.ajax({
		       url:'workbench/activity/saveCreateActivityRemark.do',
		       data:{
		           noteContent:noteContent,
		           activityId:activityId
		       },
		       type:'post',
		       dataType:'json',   
		       //处理响应
		       success:function(data){
		         //解析json,渲染页面
		         if(data.code=="1"){
		            //清空输入框
		            $("#remark").val("");//赋空值
		            //刷新备注列表
		            //定义一个字符串拼接，动态数据改成从哪里取
		            var htmlStr="";
		            
		            htmlStr+="<div id=\"div_"+data.retData.id+"\" class=\"remarkDiv\" style=\"height: 60px;\">";
					htmlStr+="<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
					htmlStr+="<div style=\"position: relative; top: -40px; left: 40px;\" >";
					htmlStr+="<h5 id=\"h5_"+data.retData.id+"\">"+data.retData.noteContent+"</h5>";
					htmlStr+="<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${activity.name}</b> <small style=\"color: gray;\"> "+data.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
					htmlStr+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
					htmlStr+="<a class=\"myHref\" name=\"editA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
					htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;";
					htmlStr+="<a class=\"myHref\" name=\"deleteA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
					htmlStr+="</div>";
					htmlStr+="</div>";
		            htmlStr+="</div>";
		            
		            //拼接好之后，追加显示
		            //追加显示在指定标签的外部的后边
		            $("#remarkDiv").before(htmlStr);            
		         }else{
		            //提示信息
		            alert(data.message);
		         }
		       }
		    
		    });    
		
		});
		
		//给所有"删除"图标添加单击事件
		//添加单击事件 传统方式/on(有可能新添加，动态生成)
		//事件类型，目标选择器使用.myHref不可以，删除修改就一样了，需要自定义属性name,a[]a标签属性过滤
		//这样就选择好了所有备注下的删除图标了
		$("#remarkDivList").on("click","a[name='deleteA']",function(){
		      //1.发请求
		      //收集参数
		      //扩展属性 只要不是value属性，自定义的属性只能通过html，找到jquery对象定义的函数,this表当前正在发生事件的元素-图标dom对象
		      //自己生成的数据，不用去空格，验证合法不合法
		      var id=$(this).attr("remarkId");
		      //发送请求
		      $.ajax({
		         url:'workbench/activity/deleteActivityRemarkById.do',
		         data:{
		            id:id
		         },
		         type:'post',
		         dataType:'json',
		         //处理响应，从回调函数中处理
		         success:function(data){
		            if(data.code=="1"){
			            //刷新备注列表
			            //动态刷新，把div元素从页面删除即可
			            //从页面删除元素remove,拿到元素的jquery对象
			            //要定位div 父子标签 通过this找到parent，parent,parent
			            //扩展属性的思想，定位标签，优先考虑id，每一个标签都有一个id值且不重复
			            //remove从浏览器页面直接删掉某个元素
			            $("#div_"+id).remove(); 
		            }else{
		                //提示信息
		                alert(data.message);
		            }
		         }     
		      });	      
		});
		
		//给所有的市场活动备注后边的"修改"图标添加单击事件on方式要找到 固有的父元素
		$("#remarkDivList").on("click","a[name='editA']",function(){
		    //获取备注的id和noteContent
		    //现在每一个修改图标a标签跟所在的备注已经绑定了(自定义的属性remarkId)用html获取，获取这个图标的jQuery对象dom对象可转成$(this)
		    //a标签找到父标签，再找到兄弟标签h5
		    //a标签删除图标找到最大的备注标签div，一个删除图标对应一个div，点删除图标有对应备注id，每个备注对应一个div，拿到备注的id
		    //<h5 id=\"h5_"+data.retData.id+"\">
		    //父子选择器>直接，空格间接
		    var id=$(this).attr("remarkId");//获取属性 备注id
		    var noteContent=$("#div_"+id+" h5").text();//拿到h5标签中的内容
		    //把备注的id和noteContent写到修改备注的模态窗口中
		    //设置隐藏域<input type="hidden" id="edit-id">
            $("#edit-id").val(id);//拿到隐藏域，设置value属性值
            $("#edit-noteContent").val(noteContent);//备注内容写入模态窗口中
            
            //弹出修改市场活动备注的模态窗口
            $("#editRemarkModal").modal("show");
		});
		
		//给"更新"按钮添加单击事件
		$("#updateRemarkBtn").click(function(data){
		    //发请求处理响应
		    //收集参数 id和noteContent id隐藏域中edit-id,备注框中内容edit-noteContent
		    var id=$("#edit-id").val();
		    var noteContent=$.trim($("#edit-noteContent").val());//用户自己输入的需去空格
		    //表单验证
		    //备注内容，不能为空
		    if(noteContent==""){
		       alert("备注内容不能为空");
		       return;//后面代码都不执行了
		    }
		    //发送请求 异步
		    $.ajax({
		       url:'workbench/activity/saveEditActivityRemark.do',
		       data:{//参数
		          id:id,
		          noteContent:noteContent 
		       },
		       type:'post',
		       dataType:'json',
		       success:function(data){
		          if(data.code=="1"){
		             //关闭模态窗口
		             $("#editRemarkModal").modal("hide");
		             //刷新备注列表 data.retData.id也可以
		             $("#div_"+id+" h5").text(data.retData.noteContent);//h5标签内容，覆盖显示
		             //更新时间 small标签  当前用户中取 js中使用EL表达式必须放""中
		             $("#div_"+id+" small").text(" "+data.retData.editTime+" 由${sessionScope.sessionUser.name}修改");
		             
		          }else{
		             //提示信息
		             alert(data.message);
		             //模态窗口不关闭
		             $("#editRemarkModal").modal("show");
		          
		          }    
		       }  
		    
		    });
		});
			
	});
	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                      <input type="hidden" id="edit-id">
                        <div class="form-group">
                            <label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		<!-- 遍历remarkList,显示所有的备注-->
		<!-- 遍历集合或数组，数据在作用域中，使用jstl标签库forEach标签结合EL表达式使用-->	
		<!-- 遍历哪个集合或数组 items=""并使用EL表达式获取,var=""表循环变量，执行循环体-->
		<!-- EL表达式中使用的变量remark一定要是作用域中的数据-->
		<c:forEach items="${remarkList}" var="remark">
		    <div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
				<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
					<div style="position: relative; top: -40px; left: 40px;" >
						<h5>${remark.noteContent}</h5>
						<!--<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> <c:if test="${remark.editFlag=='1'}">${remark.editTime}</c:if><c:if test="${remark.editFlag!='1'}">${remark.createTime}</c:if> 由zhangsan</small>-->
						<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag=='1'?remark.editTime:remark.createTime} 由${remark.editFlag=='1'?remark.editBy:remark.createBy}${remark.editFlag=='1'?'修改':'创建'}</small>
						<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
							<a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
						</div>
					</div>
		    </div>
		</c:forEach>
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>