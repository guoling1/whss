package com.jkm.base.common.spring.util.config;

import com.jkm.base.common.spring.TestBase;

/**
 * Created by hutao on 15/8/20.
 * 下午4:24
 */
public class ConfigManagerTest extends TestBase {
//    @Autowired
//    private ConfigManager configManager;
//
//    @Test
//    public void should_success_get_config_value_from_config_manager() throws Exception {
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        configManager.setConfigsResources(Lists.<ConfigResource>newArrayList(new RefreshableConfigResource() {
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public List<Pair<String, String>> refresh() {
//                countDownLatch.countDown();
//                return Lists.newArrayList();
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public void init() {
//
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public boolean containsKey(final String key) {
//                return Objects.equals(key, "a");
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public String get(final String key) {
//                return "a";
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public String get(final String key, final String defaultValue) {
//                return "b";
//            }
//        }));
//        ConfigManager.refresh();
//        countDownLatch.await();
//        assertThat(ConfigManager.get("a"), is("a"));
//        assertThat(ConfigManager.get("b"), is(""));
//        assertThat(ConfigManager.get("c", "c"), is("c"));
//
//    }
//
//    @Test
//    public void should_success_subscribe_config_manager() throws Exception {
//        configManager.setConfigsResources(Lists.<ConfigResource>newArrayList(new RefreshableConfigResource() {
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public List<Pair<String, String>> refresh() {
//                return Lists.newArrayList(Pair.of("a", "a"));
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public void init() {
//
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public boolean containsKey(final String key) {
//                return true;
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public String get(final String key) {
//                return "a";
//            }
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public String get(final String key, final String defaultValue) {
//                return "b";
//            }
//        }));
//        final AtomicInteger count = new AtomicInteger(0);
//        ConfigManager.subscribe(new ConfigListener() {
//            @Override
//            public void onConfigChange(ConfigChangeEvent event) {
//                count.addAndGet(1);
//            }
//        });
//        ConfigManager.subscribe(new ConfigListener() {
//            @Override
//            public void onConfigChange(ConfigChangeEvent event) {
//                assertThat(event.getKey(), is("a"));
//                assertThat(event.getValue(), is("a"));
//                count.addAndGet(2);
//            }
//        }, "a");
//        ConfigManager.subscribe(new ConfigListener() {
//            @Override
//            public void onConfigChange(ConfigChangeEvent event) {
//                assertThat(event.getKey(), is("b"));
//                assertThat(event.getValue(), is("b"));
//                count.addAndGet(3);
//            }
//        }, "b");
//        ConfigManager.refresh();
//        assertThat(count.get(), is(3));
//        ConfigManager.notify(Pair.of("a", "a"));
//        assertThat(count.get(), is(6));
//        ConfigManager.notify(Lists.newArrayList(Pair.of("b", "b")));
//        assertThat(count.get(), is(10));
//    }
//
//    @Test
//    public void should_success_sort_config_resources() throws Exception {
//        configManager.setConfigsResources(Lists.newArrayList(
//                new PriorityConfigResource(1, new MapConfigResource().add("a", "a")),
//                new PriorityConfigResource(4, new MapConfigResource().add("a", "b")),
//                new PriorityConfigResource(3, new MapConfigResource().add("a", "c")),
//                new MapConfigResource().add("a", "d")));
//        assertThat(ConfigManager.get("a"), is("b"));
//    }
//
//    @Test
//    public void should_success_get_double_type_config_value() throws Exception {
//        configManager.setConfigsResources(Lists.<ConfigResource>newArrayList(
//                new MapConfigResource().add("a", "1")));
//        assertThat(ConfigManager.getDouble("a"), is(1d));
//        assertThat(ConfigManager.getDouble("b", 2), is(2d));
//        assertThat(ConfigManager.getLong("b", 2), is(2l));
//        assertThat(ConfigManager.getLong("a"), is(1l));
//        assertThat(ConfigManager.getInt("b", 2), is(2));
//        assertThat(ConfigManager.getInt("a"), is(1));
//        assertThat(ConfigManager.getObj("a", new ConfigValueConvertor<Integer>() {
//            @Override
//            public Integer convert(final String value) {
//                return NumberUtils.toInt(value);
//            }
//        }), is(1));
//        assertThat(ConfigManager.getObj("b", 2, new ConfigValueConvertor<Integer>() {
//            @Override
//            public Integer convert(final String value) {
//                return NumberUtils.toInt(value);
//            }
//        }), is(2));
//
//    }
}