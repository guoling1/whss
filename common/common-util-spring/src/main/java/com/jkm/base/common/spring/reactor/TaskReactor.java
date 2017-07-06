package com.jkm.base.common.spring.reactor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.Event;
import reactor.event.dispatch.WorkQueueDispatcher;
import reactor.event.selector.ObjectSelector;
import reactor.function.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutao on 15/7/9.
 * 下午9:53
 */
@Component("taskReactor")
@Scope("singleton")
@Slf4j
public class TaskReactor implements AutoCloseable {
    private final static int DEFAULT_EVENT_BACK_LOG = 4096;
    private final static int DEFAULT_THREAD_POOL_SIZE = 32;
    private final Reactor reactor;
    @Setter
    private List<ProcessorRegister> processorRegisters = new ArrayList<>();

    /**
     * 构造函数
     */
    public TaskReactor() {
        this(DEFAULT_THREAD_POOL_SIZE, DEFAULT_EVENT_BACK_LOG);
    }

    public TaskReactor(final int threadPoolSize,
                       final int backlog) {
        this.reactor = Reactors.reactor(new Environment(),
                new WorkQueueDispatcher("name", DEFAULT_THREAD_POOL_SIZE,
                        DEFAULT_EVENT_BACK_LOG, new Consumer<Throwable>() {
                    /**
                     * 处理异常
                     * @param throwable 异常
                     */
                    @Override
                    public void accept(final Throwable throwable) {
                        log.error("AccountTaskReactor error:{}", throwable.getMessage(), throwable);
                    }
                }, ProducerType.MULTI, new BlockingWaitStrategy()));
    }

    /**
     * 添加新的事件处理器
     *
     * @param tClass    事件类型
     * @param processor 处理器
     * @param <T>       事件类型
     */
    public <T extends TaskEvent> void addEventProcessor(final Class<T> tClass,
                                                        final AbstractTaskProcessor<T> processor) {
        this.reactor.on(new ObjectSelector<>(tClass), processor);
    }

    /**
     * 添加待处理事件
     *
     * @param accountEvent 事件
     * @param <T>          事件类型
     */
    public <T extends TaskEvent> void addEvent(final T accountEvent) {
        this.reactor.notify(accountEvent.getClass(), Event.wrap(accountEvent));
    }

    /**
     * 添加待处理事件
     *
     * @param accountEvent    事件
     * @param completeHandler 事件处理完的回调函数
     * @param <T>             事件类型
     */
    public <T extends TaskEvent> void addEvent(final T accountEvent, final AbstractTaskProcessor<T> completeHandler) {
        this.reactor.notify(accountEvent.getClass(), Event.wrap(accountEvent), completeHandler);
    }

    /**
     * 初始化工作
     */
    @PostConstruct
    public void initialize() {
        //TODO
        log.debug("开始注册reactor事件处理器");
        for (final ProcessorRegister processorRegister : this.processorRegisters) {
            processorRegister.register(this);
        }
    }

    /**
     * 关闭工作
     */
    @PreDestroy
    public void shutdown() {
        try {
            this.reactor.getDispatcher().awaitAndShutdown();
            //shutdownImpl();
        } catch (final Exception e) {
            log.error("shutdown AccountTaskReactor error:{}", e.getMessage(), e);
        }
    }

    /**
     * 关闭
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        shutdown();
    }
/*
    private final static long DEFAULT_SHUTDOWN_WAIT_TIME = 30 * TimeConstant.SECOND;
     private final static int TRY_SHUTDOWN_MAX_COUNT = 3;
   private void shutdownImpl() {
        int count = 0;
        final Dispatcher dispatcher = reactor.getDispatcher();
        while (count < TRY_SHUTDOWN_MAX_COUNT) {
            if (dispatcher.awaitAndShutdown(DEFAULT_SHUTDOWN_WAIT_TIME, TimeUnit.SECONDS)) {
                break;
            } else {
                log.info("AccountTaskReactor shutdown time out");
                ++count;
                if (count == TRY_SHUTDOWN_MAX_COUNT) {
                    forceShutdown(dispatcher);
                }
            }
        }
    }

    private void forceShutdown(final Dispatcher dispatcher) {
        log.info("force shutdown AccountTaskReactor");
        dispatcher.halt();
        dispatcher.shutdown();
    }*/
}
