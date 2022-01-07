package com.fhs.core.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.trans.vo.VO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 所有的service 都必须实现此service 如果是简单的CRUD操作，
 * 允许controller直接调用baseserivce实现类里面的方法。
 *
 * @author wanglei
 * @version [版本号, 2015年5月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface BaseService<V extends VO, P extends BasePO> {


    /**
     * 做判空处理的insert -- jpa方法
     *
     * @param entity do
     * @return 受影响的行数
     */
    int insertSelective(P entity);

    /**
     * 批量插入 -- jpa方法
     *
     * @param list 需要插入的集合
     * @return 受影响的行数
     * @since 1.0.0
     */
    boolean batchInsert(List<P> list);



    /**
     * 根据实体删除对象
     *
     * @param entity
     * @return
     */
    int deleteBean(P entity);

    /**
     * 根据id集合删除
     *
     * @param idList id集合
     * @return 受影响行数
     */
    int deleteBatchIds(Collection<? extends Serializable> idList);


    /**
     * 根据id删除数据 -- jpa方法
     *
     * @param primaryValue id
     * @return 受影响行数
     * @since 1.0.0
     */
    int deleteById(Serializable primaryValue);


    /**
     * 根据id跟新 -- 判空  -- jpa方法
     *
     * @param entity 待更新数据
     * @return 受影响行数
     * @since 1.0.0
     */
    int updateSelectiveById(P entity);


    /**
     * 批量更新--必须要有id
     *
     * @param list 需要更新的数据
     * @return 受影响条数
     */
    boolean batchUpdate(List<P> list);

    /**
     * 根据id、查询 -- jpa方法
     *
     * @param primaryValue id
     * @return model
     * @since 1.0.0
     */
    V selectById(Serializable primaryValue);


    /**
     * 查询 返回一行一列 结果为int类型 参数为obj
     *
     * @param bean bean
     * @return 结果
     */
    Long findCount(P bean);


    /**
     * 查询数据 参数为object
     *
     * @param bean bean
     * @return 查询出来的数据集合
     */
    List<V> findForList(P bean);

    /**
     * 查询数据 参数为object
     *
     * @param bean bean
     * @return 查询出来的数据集合
     */
    FhsPager<V> findForPager(P bean, FhsPager fhsPager);


    /**
     * select(这里用一句话描述这个方法的作用) -- jpa方法
     * (这里描述这个方法适用条件 – 可选)
     *
     * @return 查询所有
     * @since 1.0.0
     */
    List<V> select();


    /**
     * 根据参数不为空的字段作为过滤条件查询
     *
     * @param param 参数
     * @return 结果
     */
    V selectBean(P param);


    /**
     * 根据id集合查询
     *
     * @param idList id集合
     * @return 对应的结果
     */
    List<V> selectBatchIdsMP(Collection<? extends Serializable> idList);

    /**
     * 查询单个
     *
     * @param queryWrapper 过滤条件
     * @return 单个对象
     */
    V selectOneMP(Wrapper<P> queryWrapper);

    /**
     * 查询count
     *
     * @param queryWrapper 过滤条件
     * @return 符合条件的数据数量
     */
    Long selectCountMP(Wrapper<P> queryWrapper);

    /**
     * 查询list
     *
     * @param queryWrapper 过滤条件
     * @return 集合
     */
    List<V> selectListMP(Wrapper<P> queryWrapper);

    /**
     * 查询返回map集合
     *
     * @param queryWrapper 过滤条件
     * @return 集合
     */
    List<Map<String, Object>> selectMapsMP(Wrapper<P> queryWrapper);

    /**
     * 查询object
     *
     * @param queryWrapper 过滤条件
     * @return 集合
     */
    List<Object> selectObjsMP(Wrapper<P> queryWrapper);

    /**
     * 查询带分页
     *
     * @param page         分页信息
     * @param queryWrapper 过滤条件
     * @return 分页数据
     */
    IPage<V> selectPageMP(IPage<P> page, Wrapper<P> queryWrapper);

    /**
     * 查询分页-返回map
     *
     * @param page         分页信息
     * @param queryWrapper 过滤条件
     * @return 分页数据
     */
    @Deprecated
    IPage<Map<String, Object>> selectMapsPageMP(IPage<P> page, Wrapper<P> queryWrapper);


    /**
     * vo转do
     *
     * @param vo vo
     * @return po
     */
    P v2p(V vo);


    Class<P> getPoClass();

    /**
     * 获取vo的class
     *
     * @return
     */
    Class<V> getVOClass();
}
