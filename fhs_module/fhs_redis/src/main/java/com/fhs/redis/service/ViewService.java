package com.fhs.redis.service;

import java.util.Set;

import com.fhs.redis.util.Pagination;
import com.fhs.redis.util.RKey;
import com.fhs.redis.util.ztree.ZNode;

public interface ViewService {

	Set<ZNode> getLeftTree();

	Set<RKey> getRedisKeys(Pagination pagination, String serverName, String dbIndex,
			String[] keyPrefixs, String queryKey, String queryValue);

	Set<ZNode> refresh();

	void changeRefreshMode(String mode);

	void changeShowType(String state);

	void refreshAllKeys();

}
