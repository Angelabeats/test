package cn.itcast.ssm.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//全局异常处理器
public class CustomExceptionResolver implements HandlerExceptionResolver{

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		CustomException customException;
		if(ex instanceof CustomException) {
			customException=(CustomException)ex;
		}else {
			customException=new CustomException("未知类型");
		}
		//错误信息
		String message=customException.getMessage();
		ModelAndView modelAndView =new ModelAndView();
		//错误信息传到页面
		modelAndView.addObject("message", message);
		//指向错误页面
		modelAndView.setViewName("error");
		return modelAndView;
	}
}
