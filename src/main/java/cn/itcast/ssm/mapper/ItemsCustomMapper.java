package cn.itcast.ssm.mapper;

import java.util.List;

import cn.itcast.ssm.pojo.*;

public interface ItemsCustomMapper {

	//查询商品列表
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)throws Exception;
}
