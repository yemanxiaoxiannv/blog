package com.scs.web.blog.factory;

import com.scs.web.blog.dao.ArticleDao;
import com.scs.web.blog.dao.RegionDao;
import com.scs.web.blog.dao.TopicDao;
import com.scs.web.blog.dao.UserDao;
import com.scs.web.blog.dao.impl.ArticleDaoImpl;
import com.scs.web.blog.dao.impl.RegionDaoImpl;
import com.scs.web.blog.dao.impl.TopicDaoImpl;
import com.scs.web.blog.dao.impl.UserDaoImpl;

/**
 * @author xxcai
 * @ClassName DaoFactory
 * @Description Dao工厂类
 * @Date 2019/11/6
 * @Version 1.0
 **/
public class DaoFactory {

    public static UserDao getUserDaoInstance() {
        return new UserDaoImpl();
    }

    public static ArticleDao getArticleDaoInstance() {
        return new ArticleDaoImpl();
    }

    public static TopicDao getTopicDaoInstance() {
        return new TopicDaoImpl();
    }

    public static RegionDao getRegionDaoInstance() {
        return new RegionDaoImpl();
    }

}
