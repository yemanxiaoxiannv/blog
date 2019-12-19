package com.scs.web.blog.factory;

import com.scs.web.blog.service.*;
import com.scs.web.blog.service.impl.*;

/**
 * @author mq_xu
 * @ClassName ServiceFactory
 * @Description Service工厂类
 * @Date 10:56 2019/11/7
 * @Version 1.0
 **/
public class ServiceFactory {
    public static UserService getUserServiceInstance() {
        return new UserServiceImpl();
    }

    public static ArticleService getArticleServiceInstance() {
        return new ArticleServiceImpl();
    }

    public static TopicService getTopicServiceInstance() {
        return new TopicServiceImpl();
    }

    public static ArticleAddService getArticleAddServiceInstance() { return new ArticleAddServiceImpl(); }

    public static TopicFollowService getTopicFollowServiceInstance(){return  new TopicFollowServiceImpl(); }

    public static CommentService getCommentServiceINstance(){return  new CommentServiceImpl(); }
}

