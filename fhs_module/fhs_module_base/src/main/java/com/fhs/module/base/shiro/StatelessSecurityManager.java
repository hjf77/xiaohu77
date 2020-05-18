package com.fhs.module.base.shiro;

import com.fhs.common.constant.Constant;
import com.fhs.core.cache.service.RedisCacheService;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 没有状态的权限管理器
 * @author user
 * @since 2019-05-18 11:38:16
 */
public class StatelessSecurityManager extends DefaultWebSecurityManager {

    @Autowired
    private RedisCacheService<Subject> redisCacheService;


    @Value("${server.session.timeout:3600}")
    private Integer sesstionTimeout;

    @Override
    public Subject createSubject(SubjectContext subjectContext) {
        SessionKey sessionKey = getSessionKey(subjectContext);
        if (WebUtils.isHttp(sessionKey)) {
            ServletRequest request = WebUtils.getRequest(sessionKey);
            ServletResponse response = WebUtils.getResponse(sessionKey);
            String token = WebUtils.toHttp(request).getHeader(Constant.VUE_HEADER_TOKEN_KEY);
            if (token != null) {
                try {
                    Subject subjectCache = redisCacheService.get("shiro:" + token);
                    redisCacheService.expire("shiro:" + token, sesstionTimeout);
                    redisCacheService.expire("shiro:user:" + token, sesstionTimeout);
                    redisCacheService.expire("shiro:dp:" + token, sesstionTimeout);
                    if (subjectCache != null) {
                        Subject subject = super.createSubject(subjectContext);
                        WebDelegatingSubject subject2 = (WebDelegatingSubject) subject;
                        return new WebDelegatingSubject(subjectCache.getPrincipals(), subjectCache.isAuthenticated(),
                                subject2.getHost(), subject2.getSession(), request, response,
                                subject2.getSecurityManager());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return super.createSubject(subjectContext);
    }

}