package com.fhs.agreement.mapper;

import com.fhs.agreement.po.AgreementGoodsSettPO;
import com.fhs.agreement.vo.AgreementGoodsSettVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购协议商品(AgreementGoodsSett)表数据库访问层
 *
 * @author caiyu
 * @since 2022-06-01 11:16:32
 */
@Repository
public interface AgreementGoodsSettMapper extends FhsBaseMapper<AgreementGoodsSettPO> {


    @Delete({"<script> ",
            " DELETE FROM t_agreement_goods_sett WHERE agreement_id = #{agreemenetId}",
            " </script> "})
    void physicsDelete(@Param("agreemenetId") int agreemenetId);

    @Select({"<script> ",
            " SELECT * FROM t_agreement_goods_sett WHERE agreement_id = #{agreemenetId}",
            " </script> "})
    List<AgreementGoodsSettVO> goosByaAreementId(@Param("agreemenetId") int agreemenetId);


}
