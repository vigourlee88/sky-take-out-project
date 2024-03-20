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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<!--  PAGINATION plugin -->

<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	$(function(){
		//给"创建"按钮(元素)添加单击事件
		//选中元素$(""),哪个元素的jquery对象
		//id选择器#createActivityBtn,加单击事件调用函数.click()
		$("#createActivityBtn").click(function(){
		  //初始化工作
		  //重置表单初始化状态，下面给form表单起个id，拿到jQuery对象转dom对象[0]或get(0),然后重置表单
		  $("#createActivityForm").get(0).reset();

		  //弹出创建市场活动的模态窗口
		  //得到哪个窗口div的jquery对象，然后调用modal函数就行
		  //"show"弹出来，"hide"隐藏
		  $("#createActivityModal").modal("show");
		
		});		
		 //给"保存"按钮，添加单击事件
		 $("#saveCreateActivityBtn").click(function(){
		     //收集参数
		     var owner=$("#create-marketActivityOwner").val();
		     var name=$.trim($("#create-marketActivityName").val());
		     var startDate=$("#create-startDate").val();
		     var endDate=$("#create-endDate").val();
		     var cost=$.trim($("#create-cost").val());
		     var description=$.trim($("#create-description").val());
		     
		     //表单验证
		     if(owner==""){
		        alert("所有者不能为空");
		        return;
		     }
		     if(name==""){
		        alert("名称不能为空");
		        return;
		     }
		     if(startDate!=""&&endDate!=""){
		        //js中字符串转成Date对象,比较大小
		        //使用字符串的大小代替日期的大小
		        if(endDate<startDate){
		           alert("结束日期不能比开始日期小");
		           return;
		        }     
		     }
		     /*
			  正则表达式：
			     1，语言，语法：定义字符串的匹配模式，可以用来判断指定的具体字符串是否符合匹配模式。
			     2,语法通则：
			       1)//:在js中定义一个正则表达式.  var regExp=/...../;
			       2)^：匹配字符串的开头位置
			         $: 匹配字符串的结尾
			       3)[]:匹配指定字符集中的一位字符。 var regExp=/^[abc]$/;
			                                    var regExp=/^[a-z0-9]$/;
			       4){}:匹配次数.var regExp=/^[abc]{5}$/;
			            {m}:匹配m此
			            {m,n}：匹配m次到n次
			            {m,}：匹配m次或者更多次
			       5)特殊符号：
			         \d:匹配一位数字，相当于[0-9]
			         \D:匹配一位非数字
			         \w：匹配所有字符，包括字母、数字、下划线。
			         \W:匹配非字符，除了字母、数字、下划线之外的字符。

			         *:匹配0次或者多次，相当于{0,}
			         +:匹配1次或者多次，相当于{1,}
			         ?:匹配0次或者1次，相当于{0,1}
			 */
			 //定义非负整数的正则表达式
             var regExp=/^(([1-9]\d*)|0)$/;           
		     if(!regExp.test(cost)){
		        alert("成本只能为非负整数");
		        return;   
		     }
		     //发送请求
		     $.ajax({
		 
		       url:'workbench/activity/saveCreateActivity.do',
		       data:{
		          owner:owner ,//参数名(controller里面接收参数的实体类的属性名一致):参数值
		          name:name,
		          startDate:startDate,
		          endDate:endDate,
		          cost:cost,
		          description:description      
		       },
		       type:'post',
		       dataType:'json',
		       //处理响应
		       success:function(data){
		         //解析json,渲染页面
		         if(data.code=="1"){
		            //关闭模态窗口
		            $("#createActivityModal").modal("hide");
		            //刷新市场活动列，显示第一页数据，保持每页显示条数不变
		            queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
		         }else{
		            //提示信息
		            alert(data.message);
		            //模态窗口不关闭
		            $("#createActivityModal").modal("show");//可以不写，data-dismiss="modal"
		         }
		       }
		     
		     });
		 });
		 
		  //当容器加载完成之后，对容器调用工具函数
		  //一个选择器选中多个元素，就不能用id选择器，用属性，类选择器
		  //根据标签选择器，跟属性过滤一下
		  //$("input[name='mydate']").datetimepicker({
		  $(".mydate").datetimepicker({
			language:'zh-CN',//语言
			format:'yyyy-mm-dd',//日期的格式
			minView:'month',//可以选择的最小视图
			initialDate:new Date(),//初始化显示的日期
			autoclose:true,//选择完日期或者时间之后，是否自动关闭日历
		    todayBtn:true,//设置是否显示"今天"按钮，默认是false不显示
		    clearBtn:true//设置是否显示"清空"按钮,默认是false
	      });   
	      
	      //当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数,默认每页显示10条;	 
	      //当页面加载完，需要调用者输入参数
	      queryActivityByConditionForPage(1,10);
	      //给"查询"按钮添加单击事件
	      $("#queryActivityBtn").click(function(){
	         //点击"查询"按钮，查询所有符合条件数据的第一页以及所有符合条件的数据的总条数
	         //queryActivityByConditionForPage(1,10);
	         queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
	      
	      }); 
	      
	      //单击事件  	 
	      //queryActivityByConditionForPage();
	      
	      //给"全选"按钮添加单击事件
	      $("#chckAll").click(function(){
	        //如果"全选"按钮是选中状态，则列表中所有checkbox都选中
	        //$("#chckAll").prop("checked");
	        //this 表当前正在发生这个事件的这个元素的dom对象
	        /*if(this.checked==true){
	           //让列表中的checkbox被选中 >直接子标签 空格可直接可间接
	           $("#tBody input[type='checkbox']").prop("checked",true);
	        }else{
	           $("#tBody input[type='checkbox']").prop("checked",flase);      
	        }*/
	   
	      $("#tBody input[type='checkbox']").prop("checked",this.checked);
	    
	     });
	     
	     /*$("#tBody input[type='checkbox']").click(function(){
	        alert("aaaa");
	        //如果列表中的所有checkbox都选中状态，则"全选"按钮也选中
	        if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
	           //设置选中的属性为true,prop设置属性
	           $("#chckAll").prop("checked",true);	           
	        }else{//如果至少有一个没选中，则"全选"按钮也取消
	           $("#chckAll").prop("checked",false);	
	        }
	     });*/
	     
	     $("#tBody").on("click","input[type='checkbox']",function(){
	        //如果列表中的所有checkbox都选中状态，则"全选"按钮也选中
	        if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
	           //设置选中的属性为true,prop设置属性
	           $("#chckAll").prop("checked",true);	           
	        }else{//如果至少有一个没选中，则"全选"按钮也取消
	           $("#chckAll").prop("checked",false);	
	        }
	     });
	     
	     //给"删除"按钮添加单击事件
	     //当按钮上发生单击事件click，自动触发，事件函数
	     $("#deleteActivityBtn").click(function(){
	         //收集参数，即市场功能的id
	         //获取列表中所有被选中的checkbox上面绑定value值
	         var checkedIds = $("#tBody input[type='checkbox']:checked");
	         if(checkedIds.size()==0){
	           alert("请选择要删除的市场活动");
	           return;
	         }
	         if(window.confirm("确定删除吗?")){
	            var ids="";
		         //遍历数组，获取每一个checkbox上绑定的id
		         $.each(checkedIds,function(){//id=xxx&id=xxx&...&id=xxx&
		           //获取id值以字符串的形式拼接发送到后台
		           ids+="id="+this.value+"&";
		         });
		      
		         ids=ids.substr(0,ids.length-1);//id=xxx&id=xxx&...&id=xxx
		     
		         //后台发送请求(表单已经验证了，数据也准备好了),局部更新即异步请求$.ajax();传入一个对象{}
		         $.ajax({
		           //发送请求
		           url:'workbench/activity/deleteActivityByIds.do',
		           data:ids,//一个参数名对应多个参数值
		           type:'post',
		           dataType:'json',
		           //处理响应
		           success:function(data){//响应信息都在data中
		              //解析json渲染页面，删除成功还是失败
			           if(data.code=="1"){
			             //刷新市场活动列表(查页面)，显示第一页数据，保持每页显示条数不变
			             queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
			           }else{
			             //提示信息,列表不刷新
			             alert(data.message);
			           }		           
		           }
		  	         
		        });	         
	            
	        }   
	   });
	   
	   //给"修改"按钮添加单击事件
	   $("#editActivityBtn").click(function(){
	        //收集参数
	        //获取到列表中被选中的checkbox
	        var chkedIds=$("#tBody input[type='checkbox']:checked");
	        if(chkedIds.size()==0){
	            alert("请选择要修改的市场活动");
	            return;
	        }
	        if(chkedIds.size()>1){
	            alert("每次只能修改一条市场活动");
	            return; 
	        }
	        //var id=chkedIds.val();一定只有一个元素
	        //var id=chkedIds.get(0).value;
	        var id=chkedIds[0].value;
	        //发送请求,传入一个js对象{}
	        $.ajax({
	           url:'workbench/activity/queryActivityById.do',
	           data:{
	              //只有一个参数，参数名:参数值
	              id:id
	           },
	           type:'post',
	           dataType:'json',
	           //处理响应，data接收响应信息(属性名=属性值)
	           success:function(data){
	              //把市场活动的信息显示在修改的模态窗口上
	              $("#edit-id").val(data.id);
	              $("#edit-marketActivityOwner").val(data.owner);
	              $("#edit-marketActivityName").val(data.name);
	              $("#edit-startTime").val(data.startDate);
	              $("#edit-endTime").val(data.endDate);
	              $("#edit-cost").val(data.cost);
	              $("#edit-description").val(data.description);
	              //弹出模态窗口
	              $("#editActivityModal").modal("show");
	              
	           } 
	        });
	   });
	   
	   //给"更新"按钮添加单击事件
	   $("#saveEditActivityBtn").click(function(){
	      //收集参数   
	      var id=$("#edit-id").val();//修改市场活动模态窗口下隐藏域中id
	      var owner=$("#edit-marketActivityOwner").val();
	      var name=$.trim($("#edit-marketActivityName").val());
	      var startDate=$("#edit-startTime").val();
	      var endDate=$("#edit-endTime").val();
	      var cost=$.trim($("#edit-cost").val());
	      var description=$.trim($("#edit-description").val());
	      //获取完参数，发送到后台，要保证这些参数合法
	      //表单验证(跟创建时一样)
	      
	      //发送请求
	      $.ajax({
	         url:'workbench/activity/saveEditActivity.do',
	         data:{
	            //参数名要和controller层方法中接收参数的形参(实体类的属性名)保持一致才能接收到
	            id:id,
	            owner:owner,
	            name:name,
	            startDate:startDate,
	            endDate:endDate,
	            cost:cost,
	            description:description        
	         },
	         type:'post',//请求方式
	         dataType:'json',//响应信息类型
	         success:function(data){//回调函数中有个data,接收响应信息
	            //解析json,渲染页面(成功?失败?)
	            if(data.code=="1"){
	               //关闭模态窗口
	               //获取模态窗口的jquery对象，调modal函数
	               $("#editActivityModal").modal("hide");//要关闭
	               //刷新市场活动列表，保持页号和每页显示条数都不变
	               queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption','currentPage'),$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
	            }else{
	               //失败，提示信息
	               alert(data.message);
	               //模态窗口不关闭
	               $("#editActivityModal").modal("show");
	            }          
	            
	         }
	         
	      });      
	   });
	    //给"批量导出"按钮添加单击事件
	    $("#exportActivityAllBtn").click(function(){
	       //发送同步请求
	       window.location.href="workbench/activity/exportAllActivitys.do";
	    
	    });
	    
	    //给"导入"按钮添加单击事件
	    $("#importActivityBtn").click(function(){
	        //1.发请求，有参数，要收集参数(excel文件)
	        //文件也是表单输入组件，拿到value属性值就行即<input type="file" id="activityFile">
	        //拿到value属性值方法，拿到jQuery对象调val()；不传参数，表获取值
	        //只能获取到文件名
	        var activityFileName=$("#activityFile").val();
	        //2.根据文件名进行表单验证，是不是excel文件
	        //str.substr(startIndex);从下标为startIndex的字符开始截取，截取到字符串的最后
	        var suffix=activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();//xls,XLS,Xls...
	        if(suffix!="xls"){//后缀名不区分大小写
	            alert("只支持xls文件");
	            return;     
	        }      
	    
	        //浏览，选择文件，打开后，文件就赋值到表单组件<input type="file" id="activityFile">中，在组件中value的值保存的是文件名
	        //而文件本身保存到表单组件的dom对象中，
	        //3.获取表单组件的dom对象:document.getElementById();jquery对象get(0);
	        var activityFile=$("#activityFile")[0].files[0];//获取数组的第一个元素
	        //4.验证文件的大小
	        //alert(activityFile.size);
	        if(activityFile.size>5*1024*1024){
	           alert("文件大小不超过5MB");
	           return;
	        }
	        //FormData类是ajax中提供的接口，ajax在js定义的，可以模拟键值对向后台提交参数
	        //最大优势是不但能提交文本数据，还能提交二进制数据
	        //语法:
	        var formData=new FormData();
	        formData.append("activityFile",activityFile);//"activityFile"参数名必须和controller中接收参数的形参名一致
	        formData.append("userName","张三");
	       
	        //5.发送请求，异步请求
	        $.ajax({
	           url:'workbench/activity/importActivity.do',
	           data:formData,
	           processData:false,//设置ajax向后台提交参数之前，是否把参数统一换成字符串:true--是，false--不是，默认是true
	           contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码:true--是，false--不是，默认是true
	           type:'post',//请求方式
	           dataType:'json',//文件上传，只能用json
	           //处理响应
	           success:function(data){//接收响应信息data即json
	              //解析json,渲染页面
	              if(data.code==1){
	                //提示成功导入记录数据
	                alert("成功导入"+data.retData+"条记录");
	                //关闭模态窗口
	                //获取模态窗口的div的jquery对象
	                $("#importActivityModal").modal("hide");
	                //刷新市场活动列表，显示第一页数据，保持每页显示条数不变
	                queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
	              }else{
	                //提示信息
	                alert(data.message);
	                //模态窗口不关闭
	                $("#importActivityModal").modal("show");
	              }
	              
	           }
	           
	        });
	    });
   });
	
	//入口函数外面，封装函数
	function queryActivityByConditionForPage(pageNo,pageSize){
	   //收集参数
	      var name=$("#query-name").val();
	      var owner=$("#query-owner").val();
	      var startDate=$("#query-startDate").val();
	      var endDate=$("#query-endDate").val();
	      //var pageNo=1;
	      //var pageSize=10;
	      //直接发送请求(查询数据时，不去空格，不用做表单验证)
	      //调ajax异步请求(ajax()),传参数加{}表对象
	      //前面加了BasePath标签从中查找url
	      //name:name 参数名:参数值,理论上参数名要和controller方法中的接收的形参名保持一致
	      //dataType:'json'响应信息类型
	      //以上前台的第一个职责完成(整个页面信息加载完成后向后台发请求)
	      //第二个职责(处理响应)在回调函数中处理success:function(data){}
	      //data 是json字符串
	      //遍历js的变量data要用jquery即 $.each();
	      //遍历从controller往作用域里面放数据用js.tl标签是跟EL表达式结合用的主要遍历作用域里的数据
	      $.ajax({
	          url:'workbench/activity/queryActivityByConditionForPage.do',
	          data:{
	             name:name,
	             owner:owner,
	             startDate:startDate,
	             endDate:endDate,
	             pageNo:pageNo,
	             pageSize:pageSize
	          },
	          type:'post',
	          dataType:'json',
	          success:function(data){
	              //显示总条数 text();html();替换
	              //$("#totalRowsB").text(data.totalRows);
	              //显示市场活动的列表
	              //遍历数组activityList，拼接所有行数据
	              //each(遍历哪一个数组，回调函数)
	              //function(index,obj){},index表遍历的下标,从0开始,
	              //obj表循环变量,从data.activityList里每拿一个元素就放进obj中
	              //内置变量this相当于obj
	              var htmlStr="";
	              $.each(data.activityList,function(index,obj){
	                 htmlStr+="<tr class=\"active\">";
					 htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					 htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
                     htmlStr+="<td>"+obj.owner+"</td>";
					 htmlStr+="<td>"+obj.startDate+"</td>";
					 htmlStr+="<td>"+obj.endDate+"</td>";
					 htmlStr+="</tr>";
	                  
	              }); 
	              //将大字符串显示到.html();列表<tbody>中
	              //text();显示文本信息，不能有标签，html();可以显示标签，覆盖显示
	              $("#tBody").html(htmlStr); 
	              
	              //取消"全选"按钮
	              $("#chckAll").prop("checked",false);
	              
	              //计算总页数
	              var totalPages=1;
	              if(data.totalRows%pageSize==0){
	                 totalPages=data.totalRows/pageSize;
	              }else{
	                 totalPages=parseInt(data.totalRows/pageSize)+1;
	              }
	              
	              //对容器调用bs_pagination工具函数，显示翻页信息 
	              $("#demo_pag1").bs_pagination({
		                currentPage:pageNo,//当前页号,相当于pageNo
		
		                rowsPerPage:pageSize,//每页显示条数,相当于pageSize
		                totalRows:data.totalRows,//总条数
		                totalPages:totalPages,  //总页数,必填参数.
		
		                visiblePageLinks:5,//最多可以显示的卡片数
		
		                showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
		                showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
		                showRowsInfo:true,//是否显示记录的信息，默认true--显示
		
		                //用户每次切换页号，都自动触发本函数;
		                //每次返回切换页号之后的pageNo和pageSize
		                onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
			                    //js代码
			                    //alert(pageObj.currentPage);
			                    //alert(pageObj.rowsPerPage);
			                    queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
			            }
			        });                	          
	             }	        	      
	         });
	}
	
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
								    <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate"　name="mydate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" name="mydate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					   <input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
								    <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chckAll" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                       <%-- <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr> --%>
					</tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div> --%>
			
		</div>
		
	</div>
</body>
</html>