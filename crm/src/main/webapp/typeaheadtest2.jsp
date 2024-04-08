<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	  <base href="<%=basePath%>">
	  <!--  JQUERY -->
	  <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	
	    
	  <!--  BOOTSTRAP -->
	  <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
	  <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	  
	  <!-- 引入自动补全开发包bs_typeahead -->
	  <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
	  <meta charset="UTF-8">
	  <title>演示自动补全插件</title>
	  
	  <script type="text/javascript">
	    $(function(){
	    	//当容器加载完成之后，对容器调用工具函数(可网上查询typeahead)
	    	$("#customerName").typeahead({
	    		source:function(jquery,process){
		    	　　//每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
	　　　　　　　　 //process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
	                //var customerName=$("#customerName").val();   
		    	    //data:{customerName:customerName},
	                //jquery：在容器中输入的关键字代替上面的
	                //回到TranController中定义形参customerName
	                    
	                //发送请求
	                $.ajax({
	                	url:'workbench/transaction/queryAllCustomerName.do',
	                	data:{
	                		customerName:jquery
	                	},
	                	type:'post',
	                	dataType:'json',
	                	success:function(data){//['xxx','xxxxx','xxxxxx',.....]  	
	                	  //动态查询
	                	  process(data);
	                		
	                	}
	                });
		
	    		}
	    	});
	    	
	    });
	  </script>

</head>
<body>
<!-- 创建容器 -->
<input type="text" id="customerName">

</body>
</html>