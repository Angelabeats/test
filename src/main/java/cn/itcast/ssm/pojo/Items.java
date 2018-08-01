package cn.itcast.ssm.pojo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.itcast.ssm.controller.validation.ValidationGroup1;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-05-20
 */
public class Items {
    private Integer id;

    //校验名称在1-30之间
    //message是校验出错的提示信息
    //groups定义校验属于那些分组
    @Size(min=1,max=30,message="{items.name.length.error}",groups= {ValidationGroup1.class})
    private String name;

    private Float price;

    private String detail;

    private String pic;

    //非空校验
    @NotNull(message="{items.createtime.isNULL}")
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}