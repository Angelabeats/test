package cn.itcast.ssm.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.ssm.mapper.ItemsCustomMapper;
import cn.itcast.ssm.mapper.ItemsMapper;
import cn.itcast.ssm.pojo.Items;
import cn.itcast.ssm.pojo.ItemsCustom;
import cn.itcast.ssm.pojo.ItemsQueryVo;
import cn.itcast.ssm.service.ItemsService;

public class ItemsServiceImpl implements ItemsService{

	@Autowired
	private ItemsCustomMapper itemsCustomMapper;
	
	@Autowired
	private ItemsMapper itemsMapper;
	
	@Override
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception {
		// 通过ItemsMapperCustom查询数据库
		return itemsCustomMapper.findItemsList(itemsQueryVo);
	}

	@Override
	public ItemsCustom findItemsById(Integer id) throws Exception {
		Items items = itemsMapper.selectByPrimaryKey(id);
		
		//中间业务处理...
		
		//拷贝属性值
		ItemsCustom itemsCustom =null;
		
		if(items!=null) {
			itemsCustom=new ItemsCustom();
			BeanUtils.copyProperties(items, itemsCustom);
		}
		
		return itemsCustom;
	}

	@Override
	public void updateItems(Integer id, ItemsCustom itemsCustom) throws Exception {
		//添加业务参数校验  id是否合法等
		
		//更新商品信息
		itemsCustom.setId(id);
		itemsMapper.updateByPrimaryKey(itemsCustom);
	}

}
