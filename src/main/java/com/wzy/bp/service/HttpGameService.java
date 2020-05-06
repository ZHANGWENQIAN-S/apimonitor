package com.wzy.bp.service;

import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.model.VsGame;
import com.wzy.bp.model.VsGameRequestHistory;

import java.util.List;
import java.util.Map;

public interface HttpGameService {
    boolean startVs(String guid);

    boolean deleteVs(String guid);

    boolean stopVs(String guid);

    VsGame getByGuid(String guid);

    List<Map<String, Object>> getGameList();

    void insert(VsGame vsGame);

    void update(VsGame vsGame);

    void updateEnabled(Boolean enable,String guid);

    void deleteHttpLog(String guid);

    void archivedHttpData(String guid);

    void excuteVs(String guid);

    List<Map<String, Object>> getLogByGuid(String guid);

    Map<String, Object> getLogById(Long id);

    void insertRequest(VsGameRequestHistory vsGameRequestHistory);
}
