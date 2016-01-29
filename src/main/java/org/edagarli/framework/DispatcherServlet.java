package org.edagarli.framework;

import org.edagarli.framework.bean.Handler;
import org.edagarli.framework.bean.Param;
import org.edagarli.framework.bean.View;
import org.edagarli.framework.helper.ConfigHelper;
import org.edagarli.framework.helper.ControllerHelper;
import org.edagarli.framework.helper.ServletHelper;
import org.edagarli.framework.util.JsonUtil;
import org.edagarli.framework.util.ReflectionUtil;
import org.edagarli.framework.util.StringUtil;
import org.edagarli.framework.bean.Data;
import org.edagarli.framework.helper.BeanHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/23
 * Time: 11:19
 * Desc:
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
        //初始化相关Helper类
        HelperLoader.init();
        //获取ServletContext 对象(用于注册Servlet)
        ServletContext servletContext = config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
        ServletHelper.init(req,resp);
        try{
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();
            //获取Action处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if(handler != null){
                //获取Controller 类及其 Bean 实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                Map<String,Object> paramMap = new HashMap<String, Object>();
                Enumeration<String> paramNames = req.getParameterNames();
                while (paramNames.hasMoreElements()){
                    String paramName = paramNames.nextElement();
                    String paramValue = req.getParameter(paramName);
                    paramMap.put(paramValue, paramValue);
                }
                Param param = new Param(paramMap);
                Object result;
                Method actionMethod = handler.getActionMethod();
                if(param.isEmpty()){
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                }else{
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }

                if(result instanceof View){
                    View view = (View) result;
                    String path = view.getPath();
                    if(StringUtil.isNotEmpty(path)){
                        if(path.startsWith("/")){
                            resp.sendRedirect(req.getContextPath() + path);
                        }else{
                            Map<String, Object> model = view.getModel();
                            for(Map.Entry<String,Object> entry : model.entrySet()){
                                req.setAttribute(entry.getKey(), entry.getValue());
                            }
                            req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                        }
                    }
                }else if(result instanceof Data){
                    Data data = (Data) result;
                    Object model = data.getModel();
                    if(model != null){
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        PrintWriter writer = resp.getWriter();
                        String json = JsonUtil.toJson(model);
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                }
            }
        }finally {
            ServletHelper.destory();
        }
    }
}
