package com.fhs.core.jsonfilter.filter;

import com.alibaba.fastjson.serializer.BeforeFilter;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.pojo.SuperBean;
import com.fhs.core.jsonfilter.bean.VoConverterObject;
import com.fhs.core.result.HttpResult;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 自动序列化一些翻译值参数到前端 比如 sex 在transMap有sexName
 * 通过这个过滤器可以将sexName直接给到对象里
 * by wanglei
 * @author jackwong
 * @date 2020-05-19 15:01:18
 */
public class TransFormatterFilter extends BeforeFilter {



    public TransFormatterFilter() {

    }

    @Override
    public void writeBefore(Object o) {
        if (o instanceof HttpResult) {
            return;
        }
        if (o instanceof Collection) {
            Collection<?> objs = (Collection<?>) o;
            for (Object obj : objs) {
                writeObject(o);
            }
            return;
        }
        writeObject(o);
    }

    /**
     * 写一个obejct
     *
     * @param obj obj
     */
    private void writeObject(Object obj) {
        if (obj == null) {
            return;
        }
        boolean isSuperBean = obj instanceof SuperBean;
        SuperBean superBean = null;
        if (isSuperBean) {
            superBean = (SuperBean) obj;
        }
        //这块暂时不弄缓存
        List<Field> fields =  ReflectUtils.getAnnotationField(superBean.getClass(), Trans.class);
        for (Field field : fields) {
            Trans trans = field.getAnnotation(Trans.class);
            if(TransType.WORD_BOOK.equals(trans.type())){
                super.writeKeyValue(field.getName() + "Name", superBean.getTransMap().get(field.getName() + "Name"));
            }
            else if(CheckUtils.isNotEmpty(trans.jsonKey())){
                super.writeKeyValue(field.getName() + "Name", superBean.getTransMap().get(trans.jsonKey()));
            }
        }
    }


}
