package com.yss.datamiddle.tools;

import com.yss.datamiddle.annotations.QueryCondition;
import com.yss.datamiddle.vo.OrderInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @description: 分页和排序工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public class PageUtil<T> {

    /**
     * 描述：排序处理，默认id降序
     *
     * @param orderInfos
     * @return org.springframework.data.domain.Sort
     * @author fangzhao at 2020/11/13 9:22
     */
    public static Sort getSortOrder(List<OrderInfoVO> orderInfos) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        if (null == orderInfos || CollectionUtils.isEmpty(orderInfos)) {
            Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "id");
            sortOrders.add(sortOrder);
        } else {
            for (OrderInfoVO order : orderInfos) {
                String orderField = order.getOrderField();
                String orderType = order.getOrderType();
                Sort.Order sortOrder = new Sort.Order("asc".equalsIgnoreCase(orderType) ? Sort.Direction.ASC : Sort.Direction.DESC, orderField);
                sortOrders.add(sortOrder);
            }
        }
        return Sort.by(sortOrders);
    }

    /**
     * 描述：生成查询条件
     *
     * @param rangeTime
     * @param timeStart
     * @param timeEnd
     * @return org.springframework.data.jpa.domain.Specification<T>
     * @author fangzhao at 2020/11/13 9:24
     */
    public Specification<T> specification(String rangeTime, LocalDateTime timeStart, LocalDateTime timeEnd) {

        return (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            // 增加筛选条件
            Predicate predicate = criteriaBuilder.conjunction();
            // 起始日期
            if (null != timeStart) {
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get(rangeTime).as(LocalDateTime.class), timeStart));
            }
            // 结束日期
            if (null != timeEnd) {
                predicate.getExpressions().add(criteriaBuilder.lessThan(root.get(rangeTime).as(LocalDateTime.class), timeEnd));
            }
            return predicate;
        };
    }

    /**
     * @description: 根据注解信息生成查询条件
     * @creater: fangzhao
     * @updater:
     * @create: 2020/3/24 13:09
     * @update: 2020/3/24 13:09
     * @Param: request entity
     * @return:
     */
    public Specification specification(T request, T entity) {

        List<Field> reqFieldList = getAllFieldsWithRoot(request.getClass());
        Map<T, List<Field>> map = new HashMap<>(16);
        map.put(request, reqFieldList);
        for (Field field : reqFieldList) {
            if (field.getType().equals(entity.getClass())) {
                List<Field> entityFieldList = getAllFieldsWithRoot(field.getType());
                map.put(entity, entityFieldList);
                break;
            }
        }
        return getSpecification(request, map);
    }

    /**
     * 描述：根据注解信息生成查询条件
     *
     * @param request
     * @return org.springframework.data.jpa.domain.Specification
     * @author fangzhao at 2020/4/9 10:00
     */
    public Specification specification(T request) {

        List<Field> reqFieldList = getAllFieldsWithRoot(request.getClass());
        Map<T, List<Field>> map = new HashMap<>(4);
        map.put(request, reqFieldList);
        return getSpecification(request, map);
    }

    /**
     * 描述：获取类clazz的所有Field，包括其父类的Field
     *
     * @param clazz
     * @return java.util.List<java.lang.reflect.Field>
     * @author fangzhao at 2020/4/9 10:00
     */
    private List<Field> getAllFieldsWithRoot(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] dFields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(dFields)) {
            fieldList.addAll(Arrays.asList(dFields));
        }

        // 若父类是Object，则直接返回当前Field列表
        Class<?> superClass = clazz.getSuperclass();
        if (Object.class == superClass) {
            return Arrays.asList(dFields);
        }

        // 递归查询父类的field列表
        List<Field> superFields = getAllFieldsWithRoot(superClass);

        if (!CollectionUtils.isEmpty(superFields)) {
            superFields.stream().
                    filter(field -> !fieldList.contains(field)).
                    forEach(field -> fieldList.add(field));
        }
        return fieldList;
    }

    /**
     * 描述：生成查询条件
     *
     * @param request
     * @param map
     * @return org.springframework.data.jpa.domain.Specification
     * @author fangzhao at 2020/4/9 9:59
     */
    private Specification getSpecification(T request, Map<T, List<Field>> map) {
        Specification<T> specification = new Specification<T>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (null == map || CollectionUtils.isEmpty(map)) {
                    return predicate;
                }
                Set objects = map.keySet();
                for (Object obj : objects) {
                    List<Field> fieldList = map.get(obj);
                    for (Field field : fieldList) {
                        QueryCondition qc = field.getAnnotation(QueryCondition.class);
                        if (null == qc) {
                            continue;
                        }
                        // 如果主注解上colume为默认值""，则以field为准
                        String column = StringUtils.isNotBlank(qc.column()) ? qc.column() : field.getName();
                        field.setAccessible(true);

                        Object value = null;
                        // 默认getter方法获取属性值，如果是boolean基本类型，设置为is
                        String getValueType = "boolean".equals(field.getGenericType().toString()) ? "is" : "get";
                        char[] chars = field.getName().toCharArray();
                        chars[0] -= 32;
                        try {
                            Method method = obj.getClass().getMethod(getValueType + new String(chars));
                            value = method.invoke(obj);
                        } catch (Exception e) {
                            log.error("dataMiddle tools: {}", e.getMessage());
                        }
                        // 如果值为null，且注解未标注nullable，跳过
                        if (null == value && !qc.nullable()) {
                            continue;
                        }
                        if (null != value && String.class.isAssignableFrom(value.getClass())) {
                            String s = (String) value;
                            // 如果值为""，且注解未标注emptyable，跳过
                            if ("".equals(s) && !qc.emptyable()) {
                                continue;
                            }
                        }
                        // 通过注解上func属性，构建路径表达式
                        Path path = null;
                        try {
                            path = root.get(column);
                        } catch (IllegalArgumentException e) {
                            log.error("dataMiddle tools: {}", e.getMessage());
                        }
                        if (null != path) {
                            switch (qc.func()) {
                                case EQUAL:
                                    predicate.getExpressions().add(criteriaBuilder.equal(path, value));
                                    break;
                                case LIKE:
                                    predicate.getExpressions().add(criteriaBuilder.like(path, "%" + value + "%"));
                                    break;
                                case GE:
                                    if (value instanceof LocalDateTime) {
                                        predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(path.as(LocalDateTime.class), (LocalDateTime) value));
                                    }
                                    break;
                                case LT:
                                    if (value instanceof LocalDateTime) {
                                        predicate.getExpressions().add(criteriaBuilder.lessThan(path.as(LocalDateTime.class), (LocalDateTime) value));
                                    }
                                    break;
                                default:
                            }
                        }
                    }
                }
                return predicate;
            }
        };
        return specification;
    }
}
