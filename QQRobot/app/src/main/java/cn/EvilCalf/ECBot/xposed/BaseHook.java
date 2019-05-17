package cn.EvilCalf.ECBot.xposed;



public class BaseHook {
    protected ClassLoader classLoader;
    BaseHook(ClassLoader classLoader){
        this.classLoader = classLoader;
    }
}
