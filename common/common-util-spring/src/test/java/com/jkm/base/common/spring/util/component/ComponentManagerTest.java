package com.jkm.base.common.spring.util.component;

import com.jkm.base.common.spring.TestBase;

/**
 * Created by hutao on 15/8/24.
 * 下午3:17
 */
public class ComponentManagerTest extends TestBase {
//    @Autowired
//    private ComponentManager componentManager;
//    @Autowired
//    private ConfigManagerComponent configManagerComponent;
//
//    @Test
//    public void should_success_init_component_manager() throws Exception {
//        final AtomicInteger count = new AtomicInteger(0);
//        componentManager.setComponentList(Lists.<Component>newArrayList(
//                new RefreshableComponent() {
//                    @Override
//                    public void init() {
//                        count.addAndGet(1);
//                    }
//
//                    @Override
//                    public void refresh() {
//                        count.addAndGet(2);
//                    }
//
//                    @Override
//                    public void shutdown() {
//                        count.addAndGet(3);
//                    }
//                }, configManagerComponent));
//        componentManager.init();
//        assertThat(count.get(), is(1));
//        componentManager.refresh();
//        assertThat(count.get(), is(3));
//        componentManager.shutdown();
//        assertThat(count.get(), is(6));
//    }
}