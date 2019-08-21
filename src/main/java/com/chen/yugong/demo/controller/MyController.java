package com.chen.yugong.demo.controller;

import com.chen.yugong.demo.controller.resp.Result;
import com.chen.yugong.demo.service.IModifyService;
import com.chen.yugong.demo.service.IQueryService;
import com.chen.yugong.framework.spring.annotation.*;
import com.chen.yugong.framework.spring.web.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
@Controller
@RequestMapping("/web")
public class MyController {

    @Autowired
    IQueryService queryService;
    @Autowired
    IModifyService modifyService;

    /**
     * 通过视图渲染
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/query")
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("name") String name) {

        System.out.println(request);
        System.out.println(response);
        Map<String, Object> map = queryService.query(name);
        return new ModelAndView("query", map);
    }

    /**
     * 通过Json对象
     * @param name
     * @param addr
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestParam("name") String name, @RequestParam("addr") String addr) {
        Map<String, Object> result = modifyService.add(name, addr);
        return Result.ok(result);
    }


}
