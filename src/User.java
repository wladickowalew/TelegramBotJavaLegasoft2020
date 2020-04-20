public class User {
    private String name;
    private String city;
    private String command;

    {
        this.name = "";
        this.city = "";
        this.command = "name1";
    }

    public User(){}

    public String getInfo(){
        return  "Имя: "   + getName() + "\n"+
                "Город: " + getCity();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
