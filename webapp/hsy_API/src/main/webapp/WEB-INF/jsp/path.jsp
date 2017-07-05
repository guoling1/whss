<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName();
if(request.getScheme().trim().equals("http") && request.getServerPort()==80)
	basePath = basePath+path+"/";
else if (request.getScheme().trim().equals("https") && request.getServerPort()==443)
	basePath = basePath+path+"/";
else
	basePath = basePath+":"+request.getServerPort()+path+"/";
%>