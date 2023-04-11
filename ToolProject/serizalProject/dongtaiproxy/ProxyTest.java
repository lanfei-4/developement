package dongtaiproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        //创建一个被代理对象
        Student zhangsan = new Student("张三");

        //创建一个与被代理对象相关联的InvovationHandler
        InvocationHandler stuHandler = new StuInvocationHandler<Person>(zhangsan);

        //创建一个代理对象stuProxy来代理张三、代理对象的每个执行方法都会替换执行Invocation中的invoke方法
        Person stuProxy = (Person) Proxy.newProxyInstance(Student.class.getClassLoader(),new Class<?>[]{Person.class},
                stuHandler);
        stuProxy.giveMoney();
    }
}
