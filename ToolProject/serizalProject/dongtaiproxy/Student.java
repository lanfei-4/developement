package dongtaiproxy;

public class Student implements Person{
    public String name;

    public Student(String name){
        this.name=name;
    }


    public void giveMoney() {
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(name+"上交班费50元");
    }
}
