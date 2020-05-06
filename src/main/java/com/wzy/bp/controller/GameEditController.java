package com.wzy.bp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.wzy.bp.model.VsGame;
import com.wzy.bp.model.VsGameForm;
import com.wzy.bp.service.HttpGameService;
import com.wzy.bp.util.GuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class GameEditController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GameEditController.class);

    @Autowired
    private HttpGameService httpGameService;


    @RequestMapping("/addVsGame")
    public String addSingleMonitor(ModelMap map) {
        VsGame form = new VsGame();
        map.put("form", form);
        return "vs_game_add";
    }

    @RequestMapping("/showVsHis")
    public String showVsHis(ModelMap map, HttpServletRequest request) {
        String guid = request.getParameter("guid");
        String name = request.getParameter("name");
        List<Map<String, Object>> list = httpGameService.getLogByGuid(guid);
        map.addAttribute("vsHistory", list);
        map.addAttribute("vsName", name);
        return "vs_his";
    }

    @ResponseBody
    @RequestMapping("/vsLogDetail")
    public Object vsLogDetail(@RequestParam Long id) {
        Map<String, Object> result = httpGameService.getLogById(id);
        Object his = result.get("log");
        return his;
    }

    @ResponseBody
    @RequestMapping(value = "/saveVsGame", method = RequestMethod.POST)
    public boolean saveVsGame(@ModelAttribute VsGame form) {
        try {
            //添加
            if (StringUtil.isEmpty(form.getGuid())) {
                form.setGuid(GuidGenerator.generate());
                httpGameService.insert(form);
            } else {
                //修改
                httpGameService.update(form);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("错误日志", e);
            return false;
        }
    }

    @RequestMapping("/editVsGame")
    public String editSingleMonitor(ModelMap map, HttpServletRequest request) {
        String guid = request.getParameter("guid");
        VsGame vsGame = httpGameService.getByGuid(guid);
        VsGameForm form = VsGameForm.getVsGameForm(vsGame);
        map.put("form", form);
        return "vs_game_add";
    }

    @ResponseBody
    @RequestMapping(value = "/startVs", method = RequestMethod.GET)
    public boolean enableMonitor(HttpServletRequest request, HttpServletResponse response) {
        String guid = request.getParameter("guid");
        String enabled = request.getParameter("enable");
        boolean b;
        if ("true".equals(enabled)) {
            b = httpGameService.stopVs(guid);
        } else {
            b = httpGameService.startVs(guid);
        }
        return b;
    }

}
