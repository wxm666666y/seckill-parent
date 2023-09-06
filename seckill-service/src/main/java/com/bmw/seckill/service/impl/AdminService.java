package com.bmw.seckill.service.impl;

import com.bmw.seckill.dao.SeckillAdminDao;
import com.bmw.seckill.model.SeckillAdmin;
import com.bmw.seckill.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("adminService")
public class AdminService implements IAdminService {

    @Autowired
    private SeckillAdminDao seckillAdminDao;

    @Override
    public List<SeckillAdmin> listAdmin() {
        SeckillAdmin item = new SeckillAdmin();
        return seckillAdminDao.list(item);
    }

    @Override
    public SeckillAdmin findByUsername(String username) {
        Assert.notNull(username);

        SeckillAdmin item = new SeckillAdmin();
        item.setLoginName(username);
        List<SeckillAdmin> list = seckillAdminDao.list(item);
        if (!CollectionUtils.isEmpty(list)) {
            Assert.isTrue(list.size() == 1);//如果数据库中username重复.则报错
            return list.get(0);
        }
        return null;
    }
}
