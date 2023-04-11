package proxyStudy.staticporxy;

public class UserRealmProxy implements UserServices {
    private UserServices userservices;

    public UserRealmProxy(UserServices userServices){
        this.userservices=userServices;
    }

    @Override
    public void getName(String name) {
        System.out.println("UserRealmProxy start");
        this.userservices.getName(name);
        System.out.println("UserRealmProxy End");


    }

    public static void main(String[] args) {
        UserServices userServices = new UserRealm();
        UserRealmProxy proxy = new UserRealmProxy(userServices);
        proxy.getName("lanfei");
    }
}
