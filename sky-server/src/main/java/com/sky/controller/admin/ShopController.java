package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
//TODO 学习Restcontroller的注解
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setSatus(@PathVariable Integer status) {
        log.info("setSatus {}", status);
        Long id = BaseContext.getCurrentId();
        redisTemplate.opsForValue().set("SHOP_SATUS" + id, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getSatus() {
        Long id = BaseContext.getCurrentId();
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_SATUS" + id);
        log.info("getSatus {}", status);
        return Result.success(status);
    }
}
