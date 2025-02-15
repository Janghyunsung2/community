package com.myproject.community.infra.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class QuerydslUtils {

    public static List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, PathBuilder<?> entityPath) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            // Expression을 ComparableExpressionBase로 변환
            ComparableExpressionBase<?> orderExpression = entityPath.getComparable(order.getProperty(), Comparable.class);
            if (orderExpression != null) {
                orders.add(new OrderSpecifier<>(direction, orderExpression));
            }
        }
        return orders;
    }
}
