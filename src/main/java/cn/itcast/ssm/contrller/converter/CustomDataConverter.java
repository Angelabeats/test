package cn.itcast.ssm.contrller.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

//实现Converter
public class CustomDataConverter implements Converter<String, Date>{

	@Override
	public Date convert(String source) {
		//实现将日期字符串转成日期类型（格式是yyyy-MM-dd HH:mm:ss）
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return simpleDateFormat.parse(source);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//如果参数转换失败
			return null;
	}
}
