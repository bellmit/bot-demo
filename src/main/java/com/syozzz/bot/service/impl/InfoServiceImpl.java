package com.syozzz.bot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.repository.InfoRepository;
import com.syozzz.bot.repository.model.Info;
import com.syozzz.bot.service.IInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * info 服务实现
 * @author syozzz
 * @version 1.0
 * @date 2020-10-17
 */
@Service
public class InfoServiceImpl implements IInfoService {


    private final InfoRepository repository;

    @Autowired
    public InfoServiceImpl(InfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Info info) {
        repository.save(info);
    }

    @Override
    public List<Info> findAllByGroupIdIs(Long groupId) {
        return repository.findAllByGroupIdIs(groupId);
    }

    @Override
    public void clearGroupInfos(Long groupId) {
        repository.deleteAllByGroupIdIs(groupId);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Info findByGroupIdIsAndNameIsLike(Long groupId, String name) {
        return repository.findByGroupIdIsAndNameIsLike(groupId, StrUtil.format("%{}%", name));
    }


}
