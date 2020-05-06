package com.wzy.bp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.dao.HttpGameLogMapper;
import com.wzy.bp.dao.HttpGameMapper;
import com.wzy.bp.dao.VsGameRequestLogMapper;
import com.wzy.bp.game.DdzGame;
import com.wzy.bp.game.RunnerGame;
import com.wzy.bp.model.MonitorFrequency;
import com.wzy.bp.model.VsGame;
import com.wzy.bp.model.VsGameHistory;
import com.wzy.bp.model.VsGameRequestHistory;
import com.wzy.bp.quartz.DynamicGameManager;
import com.wzy.bp.service.HttpGameService;
import com.wzy.bp.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HttpGameServiceImpl implements HttpGameService {
    @Autowired
    HttpGameMapper    httpGameMapper;
    @Autowired
    HttpGameLogMapper httpGameLogMapper;
    @Autowired
    VsGameRequestLogMapper vsGameRequestLogMapper;

    @Override
    public boolean startVs(String guid) {
        VsGame instance = httpGameMapper.getByGuid(guid);
        DynamicGameManager dynamicJobManager = new DynamicGameManager(instance);
        return dynamicJobManager.enable();
    }

    @Override
    public boolean deleteVs(String guid) {
        return false;
    }

    @Override
    public boolean stopVs(String guid) {
        VsGame instance = httpGameMapper.getByGuid(guid);
        DynamicGameManager dynamicJobManager = new DynamicGameManager(instance);
        return dynamicJobManager.kill();
    }

    @Override
    public VsGame getByGuid(String guid) {

        return httpGameMapper.getByGuid(guid);
    }

    @Override
    public List<Map<String, Object>> getGameList() {
        List<Map<String, Object>> list = httpGameMapper.selectGameList();
        for (Map<String, Object> item : list) {
            item.put("frequency", MonitorFrequency.valueOf(item.get("frequency").toString()).getLabel());
            item.put("responseTime", item.get("responseTime"));
            String guid = String.valueOf(item.get("guid"));
            if ((boolean) item.get("enable")) {
                //平均响应时间
                Object avgCostTime = httpGameLogMapper.selectAvgCostTimeByGuid(guid);
                item.put("avgCostTime", avgCostTime);
                //最近一次请求状态
                Object recentStatus = httpGameLogMapper.selectRecentStatusByGuid(guid);
                if (recentStatus == null) {
                    item.put("status", "未监控");
                    item.put("textColor", "text-muted");
                } else {
                    item.put("status", (boolean) recentStatus == true ? "正常" : "故障");
                    item.put("textColor", (boolean) recentStatus == true ? "text-green" : "text-red");
                }
                //可用率
                List<Map<String, Object>> usabilityList = httpGameLogMapper.selectUsabilityByGuid(guid);
                long count = 0;
                long uCount = 0;
                for (Map<String, Object> u : usabilityList) {
                    count += (long) u.get("count");
                    if ((boolean) u.get("status")) {
                        uCount += (long) u.get("count");
                    }
                }
                item.put("usability", MathUtil.percent(uCount, count));

            } else {
                //未启动
                item.put("status", "未监控");
                item.put("textColor", "text-muted");
                item.put("usability", "0%");
                item.put("avgCostTime", "0");
            }
        }
        return list;
    }

    @Override
    public void insert(VsGame vsGame) {
        httpGameMapper.insert(vsGame);
    }

    @Override
    public void update(VsGame vsGame) {
        httpGameMapper.update(vsGame);
    }

    @Override
    public void updateEnabled(Boolean isEable, String guid) {
        httpGameMapper.updateEnable(isEable, guid);
    }

    @Override
    public void deleteHttpLog(String guid) {
        httpGameMapper.deleteLog(guid);
    }

    @Override
    public void archivedHttpData(String guid) {
        httpGameMapper.archived(guid);
    }

    @Transactional
    @Override
    public void excuteVs(String guid) {
        long start = System.currentTimeMillis();
        VsGame vsGame = httpGameMapper.getByGuid(guid);
        JSONObject AIInfo = JSONObject.parseObject(vsGame.getAIInfo());
        String res = "";
        switch (vsGame.getGroup()){
            case "PDK":
                res = RunnerGame.instance.doAction(AIInfo);
                break;
            case "DDZ":
                res = DdzGame.instance.doAction(AIInfo);
                break;

        }

        JSONObject resJson = JSONObject.parseObject(res);
        VsGameHistory vsGameHistory = new VsGameHistory();
        JSONArray vsGameRequestHistorys = new JSONArray();
        if(resJson.getInteger("code")==200){
            vsGameHistory.setStatus(1);
            vsGameHistory.setWinner(resJson.getInteger("winner"));
        }else{
            vsGameHistory.setStatus(0);
        }

        vsGameHistory.setCostTime(System.currentTimeMillis() - start);
        vsGameHistory.setGuid(guid);
        vsGameHistory.setCreateTime(new Date());
        vsGameRequestHistorys = resJson.getJSONArray("reqHis");
        for(int i = 0;i<vsGameRequestHistorys.size();i++){
            JSONObject vsHis = vsGameRequestHistorys.getJSONObject(i);
            VsGameRequestHistory vsGameRequestHistory = JSONObject.parseObject(vsHis.toJSONString(),VsGameRequestHistory.class);
            vsGameRequestLogMapper.insert(vsGameRequestHistory);
        }
        resJson.remove("reqHis");
        vsGameHistory.setLog(resJson.toJSONString());
        httpGameLogMapper.insert(vsGameHistory);
    }

    @Override
    public List<Map<String, Object>> getLogByGuid(String guid) {
        return httpGameLogMapper.getLogByGuid(guid);
    }

    @Override
    public Map<String, Object> getLogById(Long id) {
        return httpGameLogMapper.getLogById(id);
    }

    @Override
    public void insertRequest(VsGameRequestHistory vsGameRequestHistory) {
        vsGameRequestLogMapper.insert(vsGameRequestHistory);
    }
}
