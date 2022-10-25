package data;

public class Account 
{
    private static Account instance = null;
    
    private int id;
    private String name;
    private String pass;

    public static Account get()
    {
        if (instance == null)
        {
            instance = new Account();
        }
        return instance;
    }

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getPass() 
    {
        return pass;
    }

    public void setPass(String pass) 
    {
        this.pass = pass;
    }
}
