package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "套餐查询类")
public class SetmealPageQueryDTO implements Serializable {

    @ApiModelProperty("页码")
    private int page;
    @ApiModelProperty("分页数")
    private int pageSize;
    @ApiModelProperty("套餐名字")
    private String name;
    @ApiModelProperty("分类id")
    //分类id
    private Integer categoryId;
    @ApiModelProperty("状态")
    //状态 0表示禁用 1表示启用
    private Integer status;

}
