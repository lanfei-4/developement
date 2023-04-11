package proxyStudy.staticporxy;


public class UserRealm implements UserServices {

    @Override
    public void getName(String name) {
        System.out.println(name);
    }
}

