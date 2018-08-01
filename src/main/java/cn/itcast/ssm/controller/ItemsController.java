package cn.itcast.ssm.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import cn.itcast.ssm.controller.validation.ValidationGroup1;
import cn.itcast.ssm.exception.CustomException;
import cn.itcast.ssm.pojo.*;
import cn.itcast.ssm.service.ItemsService;

import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
/*
 * 1、contrller方法可以返回ModelAndView, 需对model和view分别进行设置
 * 2、contrller方法返回String，表示返回逻辑视图名,真正视图(jsp)路径=前缀+逻辑视图名+后缀
	重定向redirect特点：商品修改提交后，转到查询列表;浏览器地址栏url会变化，修改提交的request数据无法传到重定向地址。因为重定向后重新进行request（request无法共享）
	页面转发forwaed：浏览器地址栏url不变，request可以共享
 * 3、contrller方法返回void，形参可以定义request和response
  	 1.使用request重定向页面:request.getRequestDispatcher("页面路径").forward(request, response);
  	 2.也可以通过response重定向:response.sendRedirect("url");
  	 3.也可以通过response指定响应结果,例如响应json数据：
  	 	response.setCharacterEncoding("utf-8");
		response.setContentType("application/json.charset=utf-8");
		response.getWriter().write("json串");
*/

//对url进行分类管理，可以窄化请求映射，最终访问url时是根路径+子路径
//比如：商品列表：/items/queryItems.action
@RequestMapping("/items")
public class ItemsController {

	@Autowired
	private ItemsService itemsService;
	
	//商品查询
	@RequestMapping("/queryItems")
	public ModelAndView queryItems(HttpServletRequest request,ItemsQueryVo itemsQueryVo) throws Exception {
	
		//测试forward后request是否可以共享
		String string=request.getParameter("id");
		System.out.println(string);
		
		//调用service查找数据库，查询商品列表
		List<ItemsCustom> itemsList=itemsService.findItemsList(itemsQueryVo);
		
		//返回MOdelAndView
		ModelAndView modelAndView=new ModelAndView();
		//相当于request的setAttribute
		modelAndView.addObject("itemsList",itemsList);
		
		//指定视图
		modelAndView.setViewName("items/itemsList");
		return modelAndView;
	}
	
	//商品信息修改页面展示
	//@RequestMapping("/editItems")
	//限制http请求方法 只能post和get
/*	@RequestMapping(value="/editItems",method= {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView editItems()throws Exception{
		//调用service根据商品id查询商品信息
		ItemsCustom itemsCustom=itemsService.findItemsById(1);
		
		//返回ModelAndView
		ModelAndView modelAndView=new ModelAndView();
		
		//将商品信息放到model
		modelAndView.addObject("itemsCustom",itemsCustom);
		
		//商品修改页面
		modelAndView.setViewName("items/editItems");
		
		return modelAndView;
	}*/
	
	//商品信息修改页面展示
	//@RequestMapping("/editItems")
	//限制http请求方法 只能post和get
	@RequestMapping(value="/editItems",method= {RequestMethod.POST,RequestMethod.GET})
	//@RequestParam指定request传入参数名与形参绑定,通过required属性指定参数是否必须传入,defaultValue设定默认值
	public String editItems(Model model,@RequestParam(value="id",required=true) Integer items_id)throws Exception{
		//调用service根据商品id查询商品信息
		ItemsCustom itemsCustom=itemsService.findItemsById(items_id);		
		
		//判断商品是否为空，根据商品id查询结果为空，抛出异常
		if(itemsCustom==null) {
			throw new CustomException("修改的商品信息不存在！");
		}
		
		//通过形参中的model将数据传到页面
		//相当于modelAndView.addObject()
		model.addAttribute("items",itemsCustom);
		
		return "items/editItems";
	}
	
	//商品信息修改页面展示
/*	@RequestMapping("/editItemsSubmit")
	public ModelAndView editItemsSubmit()throws Exception{
		
		
		//返回ModelAndView
		ModelAndView modelAndView=new ModelAndView();
		
		//返回成功页面
		modelAndView.setViewName("success");
		
		return modelAndView;
	}*/
	
	//商品信息修改提交
	@RequestMapping("/editItemsSubmit")
	//页面的input的name与形参中pojo属性名一致，则自动绑定
	//需自定义参数绑定组件注入HandlerAdapter,将字符串转为pojo中日期类型属性 
	
	//itemsCustom的name属性会出现乱码  在web.xml中添加post乱码过滤器
	//在需要校验的pojo前添加@Validated，后添加BindingResult接收校验出错信息，配对出现且一前一后顺序固定
	//value= {ValidationGroup1.class指定只使用分组校验
	//@ModelAttribute可以指定pojo回显到页面在request中的key;还可将方法返回值传到页面
	//接收商品图片
	public String editItemsSubmit(Model model,HttpServletRequest request,Integer id,
			@ModelAttribute("items") @Validated(value= {ValidationGroup1.class}) ItemsCustom itemsCustom,
			BindingResult bindingResult, MultipartFile items_pic)throws Exception{
		
		//输出校验出错信息
		if(bindingResult.hasErrors()) {
			List<ObjectError> allErrors=bindingResult.getAllErrors();
			for(ObjectError objectError:allErrors)
				System.out.println(objectError.getDefaultMessage());
			//在页面显示出错信息
			model.addAttribute("allErrors",allErrors);
			
			//可以直接使用model将提交的pojo回显到页面
			//支持简单数据类型的回显
			//model.addAttribute("items", itemsCustom);
			
			//出错返回商品修改页面
			return "items/editItems";
		}
		//上传图片
		if(items_pic!=null) {
			//存储图片的物理路径
			String pic_path="C:\\Users\\Dell\\Pictures\\";
			//原始名称
			String filename=items_pic.getOriginalFilename();
			//新的图片名称
			String newFilename=UUID.randomUUID()+filename.substring(filename.lastIndexOf("."));
			//新图片
			File newFile=new File(newFilename);
			//将内存中的数据写入磁盘
			items_pic.transferTo(newFile);
			//将新图片名称写到itesCustom
			itemsCustom.setPic(newFilename);
		}
				
		//调用service更新商品信息，页面需将商品信息传到此方法
		itemsService.updateItems(id, itemsCustom);
		
		//重定向
		//return "redirect:queryItems.action";
		
		//页面转发
		//return "forward:queryItems.action";
		
		return "success";
	}
	
	//批量删除商品信息
	@RequestMapping("/deleteItems")
	public String deleteItems(Integer[] items_id)throws Exception{
		//调用service批量删除商品
		//...
		return "success";
	}
	
	//进入修改商品信息页面
	@RequestMapping("/editItemsQuery")
	public ModelAndView editItemsQuery(HttpServletRequest request,ItemsQueryVo itemsQueryVo) throws Exception {
	
		//调用service查找数据库，查询商品列表
		List<ItemsCustom> itemsList=itemsService.findItemsList(itemsQueryVo);
		
		//返回MOdelAndView
		ModelAndView modelAndView=new ModelAndView();
		//相当于request的setAttribute
		modelAndView.addObject("itemsList",itemsList);
		
		//指定视图
		modelAndView.setViewName("items/editItemsQuery");
		return modelAndView;
	}
	
	//批量修改商品提交
	//list、map类型的形参必须是包装类型的属性
	@RequestMapping("/editItemsAllSubmit")
	public String editItemsAllSubmit(ItemsQueryVo itemsQueryVo)throws Exception{
		return "success";
	}
	
	//商品分类
	//itemtypes表示最终将方法返回值返回到request中的key
	@ModelAttribute("itemtypes")
	public Map<String,String> getItemTypes(){
		Map<String, String> itemTypes=new HashMap<>();
		itemTypes.put("101", "数码");
		itemTypes.put("102","母婴");
		return itemTypes;
	}
}
