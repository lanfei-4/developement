import java.io.IOException;

public  class Giao implements java.io.Serializable
{
    public String name;
    public String motto;
    public void saygiao()
    {
        System.out.println(this.motto);
    }
    // 自定义 readObject 方法
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException, IOException {
        //执行默认的readObject()方法
        in.defaultReadObject();

        //执行命令
        Runtime.getRuntime().exec("open -a Calculator");
    }
}