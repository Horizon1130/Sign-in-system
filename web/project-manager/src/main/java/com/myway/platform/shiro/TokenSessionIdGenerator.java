package com.myway.platform.shiro;

import com.myway.platform.utils.UUIDUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;

import java.io.Serializable;

/**
 * sessionId生成器
 *
 */
public class TokenSessionIdGenerator extends JavaUuidSessionIdGenerator {

    public TokenSessionIdGenerator() {
        super();
    }

    @Override
    public Serializable generateId(Session session) {
        return UUIDUtils.getEncryUUID();
    }
}
