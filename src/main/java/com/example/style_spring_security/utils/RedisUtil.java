package com.example.style_spring_security.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings(value = { "unchecked", "rawtypes"})
public class RedisUtil {

    @Autowired
    public RedisTemplate redisTemplate;


    /**
     * 缓存基本的对象，String、Integer、实体类等
     * @param key 缓存的键值
     * @param value 缓存对象
     */
    public <T> void setCacheObject(final String key, final T value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本对象，String、Integer、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置有效时间
     * @param key 键
     * @param timeout 超时时间 毫秒
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout){
    	return expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置有效时间
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public Boolean expire(final String key, final long timeout, final TimeUnit unit){
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key){
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     * @param key 缓存键值
     * @return true=成功；false=失败
     */
    public Boolean deleteObject(final String key){
    	return redisTemplate.delete(key);
    }
    /**
     * 删除集合对象
     * @param collection 缓存键值集合
     * @return true=成功；false=失败
     */
    public long deleteObject(final Collection<String> collection){
        if (redisTemplate != null && collection != null) {
            redisTemplate.delete(collection);
        } else {
            throw new NullPointerException("redisTemplate 或 collection 为 null");
        }
        return 0;
    }

    /**
     * 缓存list数据
     * @param key 缓存的键值
     * @param dataList 缓存数据集合
     * @return true=成功；false=失败
     */
    public <T> long setCacheList(final String key, final Collection<T> dataList){
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获取缓存的list数据
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     * @param key  缓存的键值
     * @param dataSet 缓存数据集合
     * @return true=成功；false=失败
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet){
        BoundSetOperations<String, T> setOperations = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperations.add(t);
        }
        return setOperations;
    }

    /**
     * 获得缓存的set
     * @param key  缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Set<T> getCacheSet(final String key){
    	return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存map
     * @param key  缓存的键值
     * @param dataMap 缓存数据集合
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap){
        if (redisTemplate != null && dataMap != null){
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     * @param key  缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Map<String, T> getCacheMap(final String key){
    	return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往hash中存入数据
     * @param key redis缓存键值
     * @param hKey hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value){
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     * @param key redis缓存键值
     * @param hKey hash键
     * @return hash键中的对象

     */
    public <T> T getCacheMapValue(final String key, final String hKey){
    	return (T) redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     * @param key redis缓存键值
     * @param hKey hash键
     */
    public void deleteCacheMapValue(final String key, final String hKey){
        redisTemplate.opsForHash().delete(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     * @param key redis缓存键值
     * @param hKeys hash键
     * @return hash键中的对象
     */
    public <T> List<T> getMultiCacheObject(final String key, final Collection<String> hKeys){
    	return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获取缓存中所有的key
     * @param pattern 字符串前缀
     * @return 缓存中的key集合
     */
    public Collection<String> getKeys(final String pattern){
        return redisTemplate.keys(pattern);
    }
}
