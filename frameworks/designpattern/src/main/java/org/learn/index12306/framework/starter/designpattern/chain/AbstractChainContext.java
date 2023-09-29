package org.learn.index12306.framework.starter.designpattern.chain;

import org.learn.index12306.framework.starter.bases.ApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象责任链上下文
 *
 * @author Milk
 * @version 2023/9/29 15:03
 */
public final class AbstractChainContext<T> implements CommandLineRunner {

    /**
     * 名称和截器相对应的存储责任链的容器
     */
    private final Map<String, List<AbstractChainHandler>>abstractChainHandlerContainer = new HashMap<>();

    /**
     * 责任链组件执行
     *
     * @param mark         具体业务名称(责任链组件标识)
     * @param requestParam 需要过滤的数据
     */
    public void handler(String mark, T requestParam){
        List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(mark);
        if(CollectionUtils.isEmpty(abstractChainHandlers)){
            throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", mark));
        }
        abstractChainHandlers.forEach(each -> each.handler(requestParam));
    }


    @Override
    public void run(String... args) throws Exception {
        // 通过上下文寻找拦截器类型的bean
        Map<String, AbstractChainHandler> chainFilterMap = ApplicationContextHolder.getBeansOfType(AbstractChainHandler.class);
        chainFilterMap.forEach((beanName, bean) ->{
            List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(bean.mark());
            if(CollectionUtils.isEmpty(abstractChainHandlers)){
                abstractChainHandlers = new ArrayList<>();
            }
            abstractChainHandlers.add(bean);
            // 通过拦截器类型的 order 来决定拦截器的顺序
            List<AbstractChainHandler> actualAbstractChainHandlers = abstractChainHandlers.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());
            abstractChainHandlerContainer.put(bean.mark(), actualAbstractChainHandlers);
        });
    }
}
