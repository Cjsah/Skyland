package net.cjsah.skyland;

import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Test {

    private static final List<Class<?>> skyland$blockFeatures;
    private static final Logger log = LoggerFactory.getLogger(Test.class);
    public static final List<Class<?>> cache = new ArrayList<>();

    static {
        skyland$blockFeatures = List.of(
                MonsterRoomFeature.class, // 地牢
                SnowAndFreezeFeature.class, // 冰湖和雪片
                OreFeature.class             // 矿
        );
    }

    public static boolean test(Class<?> clazz) {
        if (!skyland$blockFeatures.contains(clazz) && !cache.contains(clazz)) {
            cache.add(clazz);
            log.info("test class: {}", clazz);
        }
        return skyland$blockFeatures.contains(clazz);
    }

}
