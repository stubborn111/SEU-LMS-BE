package edu.seu.lms.backend.seulmsbe.user.controller;


import com.alibaba.fastjson.JSON;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    /**
     * 测试
     * 实际不需要创建
     */
    @GetMapping("/create")
    public String createuser(String id, String name, String psw, int access){
        Map<String,Object> map = new HashMap<String,Object>();
        User user = new User();
        user.setId(id);
        user.setNickname(name);
        user.setPsw(psw);
        user.setAccess(access);
        try{
            int result=userService.createuser(user);
            if(result==1) {
                map.put("status", "200");
                map.put("msg", "添加成功");
            }else{
                map.put("status", "201");
                map.put("msg", "添加失败");
            }
        }catch(Exception ex){
            map.put("status","-1");
            map.put("errorMsg",ex.getMessage());
        }
        return JSON.toJSONString(map);
    }
}
