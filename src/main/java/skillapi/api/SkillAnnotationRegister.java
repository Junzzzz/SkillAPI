package skillapi.api;

/**
 * @author Jun
 * @date 2020/8/26.
 */
public interface SkillAnnotationRegister {
    /**
     * 为含有注解的类提供加载方法
     *
     * @param target 目标类
     */
    void register(Class<?> target);
}
