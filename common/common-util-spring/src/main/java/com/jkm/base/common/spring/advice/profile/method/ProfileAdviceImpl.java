package com.jkm.base.common.spring.advice.profile.method;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hutao on 15/9/9.
 * 下午2:58
 */
@Component
@Slf4j
public class ProfileAdviceImpl implements ProfileAdvice {
    private final ThreadLocal<Pair<List<Pair<String, Stopwatch>>, AtomicInteger>> stackThreadLocal = new ThreadLocal<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void pre(final Class clazz, final Method method) {
        if (log.isDebugEnabled()) {
            if (stackThreadLocal.get() == null) {
                stackThreadLocal.set(Pair.<List<Pair<String, Stopwatch>>, AtomicInteger>of(
                        new ArrayList<Pair<String, Stopwatch>>(), new AtomicInteger(0)));
            }

            final Pair<List<Pair<String, Stopwatch>>, AtomicInteger> pair = stackThreadLocal.get();

            final List<Pair<String, Stopwatch>> stackFunctions = pair.getLeft();
            stackFunctions.add(Pair.of(buildRemark(clazz, method), Stopwatch.createStarted()));

            pair.getRight().incrementAndGet();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void post(final Class clazz, final Method method) {
        if (log.isDebugEnabled()) {
            final AtomicInteger oldStackLevel = stackThreadLocal.get().getRight();
            final List<Pair<String, Stopwatch>> stackFunctions = stackThreadLocal.get().getLeft();
            final Stopwatch stopwatch = stackFunctions.get(oldStackLevel.get() - 1).getRight();
            if (stopwatch.isRunning()) {
                stopwatch.stop();
            }
            final int newStackLevel = oldStackLevel.decrementAndGet();

            if (newStackLevel == 0) {
                printTimes(stackFunctions);
                stackFunctions.clear();
            }
        }
    }

    private void printTimes(final List<Pair<String, Stopwatch>> stackFunctions) {
        if (log.isDebugEnabled()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < stackFunctions.size(); i++) {
                stringBuilder.append("\r\n[");
                final Pair<String, Stopwatch> pair = stackFunctions.get(i);
                stringBuilder.append(pair.getKey());
                stringBuilder.append(i == 0 ? "]总共消耗时间:" : "]消耗时间:");
                stringBuilder.append(pair.getRight().elapsed(TimeUnit.MILLISECONDS));
                stringBuilder.append("毫秒");
            }
            log.debug(stringBuilder.toString());
        }
    }

    private String buildRemark(final Class clazz, final Method method) {
        return clazz.getCanonicalName() + ":" + method.getName();
    }
}
