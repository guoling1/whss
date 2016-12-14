package com.jkm.base.common.spring.reactor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/7/11.
 * 下午2:58
 */
public class TaskReactorTest {
    @Test
    public void should_success_handle_account_event_task_in_reactor() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        final CountDownLatch countDownLatch1 = new CountDownLatch(20);
        try (final TaskReactor taskReactor = new TaskReactor()) {
            taskReactor.addEventProcessor(MockAccountEvent.class,
                    new AbstractTaskProcessor<MockAccountEvent>() {
                        @Override
                        public void process(final MockAccountEvent event) {
                            assertThat(event.getI(), is(1));
                            countDownLatch.countDown();
                        }
                    });
            taskReactor.addEventProcessor(MockAccountEvent1.class,
                    new AbstractTaskProcessor<MockAccountEvent1>() {
                        @Override
                        public void process(final MockAccountEvent1 event) {
                            assertThat(event.getI(), is(2));
                            countDownLatch1.countDown();
                        }
                    });
            for (int i = 0; i < 10; i++) {
                taskReactor.addEvent(new MockAccountEvent(1));
                taskReactor.addEvent(new MockAccountEvent1(2), new AbstractTaskProcessor<MockAccountEvent1>() {
                    @Override
                    protected void process(final MockAccountEvent1 data) {
                        countDownLatch1.countDown();
                    }
                });
            }
            countDownLatch.await();
            countDownLatch1.await();
        }
    }

    @AllArgsConstructor
    @Getter
    class MockAccountEvent implements TaskEvent {
        private int i;
    }

    @AllArgsConstructor
    @Getter
    class MockAccountEvent1 implements TaskEvent {
        private int i;
    }
}