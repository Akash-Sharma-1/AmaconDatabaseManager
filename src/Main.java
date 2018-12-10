        //AKASH SHARMA 2017327

import java.io.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;
class category
{
    String name;
    ArrayList<category> subcat;

    category(String s, ArrayList<category> a)
    {
        name=s;
        subcat=a;
    }


}
class database implements java.io.Serializable
{
    private ArrayList<category> tcat;
    ArrayList<product> productlist;

    public ArrayList<category> getTcat() {
        return tcat;
    }

    public ArrayList<product> getProductlist() {
        return productlist;
    }

    private int revenue;

    database()
    {
        ArrayList<category> tcat=new ArrayList<category>();
        ArrayList<product> productlist=new ArrayList<product>();
        revenue=0;
    }

    public int getrev()
    {
        return revenue;
    }
    public void setrev(int r)
    {
        revenue=r;
    }
    public void insert(String path, product x) throws DuplicateException
    {
        String [] cat= path.split(">");
        // for(String s:cat)
        // {
        //     System.out.println(s);
        // }
        ArrayList<category> search=this.tcat;
        category parent=new category("",new ArrayList<category>());
        for(int i=0;i<cat.length;i++)
        {
            int flag=0;
            try
            {
                for(category c:search)
                {
                    if((c.name).equals(cat[i]))
                    {
                        flag=1;
                        search=c.subcat;
                        parent=c;
                        break;
                    }
                }
                if(flag==0)
                {
                    if(i==0)
                    {
                        category q=new category(cat[i],new ArrayList<category>());
                        this.tcat.add(q);
                        search=q.subcat;
                        parent=q;
                    }

                    else
                    {
                        category q=new category(cat[i],new ArrayList<category>());
                        parent.subcat.add(q);
                        search=q.subcat;
                        parent=q;

                    }
                }
            }
            catch(NullPointerException e)
            {
                category q=new category(cat[i],new ArrayList<category>());
                if(i==0)
                {
                    ArrayList<category> t = new ArrayList<category>();
                    t.add(q);
                    this.tcat=t;
                    search=q.subcat;
                    parent=q;
                }
                else
                {
                    parent.subcat=new ArrayList<category>();
                    parent.subcat.add(q);
                    search=q.subcat;
                    parent=q;
                }
            }
        }
        try
        {
            int flag=0;
            for(product p: this.productlist)
            {
                if(p.name.equals(x.name))
                {
                    flag=1;
                    throw new DuplicateException("Duplicate product already exists");
                }
            }
            if(flag==0) this.productlist.add(x);

        }
        catch(NullPointerException e)
        {
            ArrayList<product> t = new ArrayList<product>();
            this.productlist=t;
            this.productlist.add(x);
        }
        catch(DuplicateException ex)
        {
            throw new DuplicateException(ex.getMessage());
        }

    }

    public void deleteproduct(String s) throws NoSuchProduct
    {
        try {
            int flag = 0;
            int i = 0;
            for (product p : this.productlist) {
                if (s.equals(p.name)) {
                    flag = 1;
                    this.productlist.remove(i);
                    break;
                }
                i++;
            }
            if (flag != 1) {
                throw new NoSuchProduct("No such product ...");
            }
        }
        catch(NoSuchProduct ex)
        {
            throw new NoSuchProduct(ex.getMessage());

        }
        catch(NullPointerException w)
        {

            throw new NoSuchProduct("No such product ...");

        }


    }
    public void deletesubcat(String path) throws Invalid
    {
        String [] cat= path.split(">");
        category parent=new category("",null);
        ArrayList<category> search=this.tcat;

        try
        {

            for(int i=0;i<(cat.length-1);i++)
            {
                int flag=0;
                for(category c:search)
                {
                    if((c.name).equals(cat[i]))
                    {
                        flag=1;
                        search=c.subcat;
                        parent=c;
                        break;
                    }
                }
                if(flag==0)
                {
                    throw new Invalid("invalid path given");
                }
            }

            int l=0;
            for(category c:search)
            {

                if((c.name).equals(cat[cat.length-1]))
                {
                    search.remove(l);
                }
                l++;
            }

        }
        catch(NullPointerException e)
        {
            throw new Invalid("invalid path given");

        }

        catch(ConcurrentModificationException e)
        {
            int l=0;
            for(category c:this.tcat)
            {

                if((c.name).equals(cat[cat.length-1]))
                {
                    this.tcat.remove(l);
                }
                l++;
            }
        }

    }
    public product search(String s) throws Invalid{
        try
        {
            int flag=0;
            product q=new product(0,0,null,"");
            for(product p:this.productlist)
            {
                if(s.equals(p.name))
                {
                    flag=1;
                    q= p;
                    break;
                }

            }
            if(flag==0)
            {
                throw new Invalid("No such product ...");
            }
            return q;
        }
        catch(Exception e)
        {

            throw new Invalid(e.getMessage());

        }

    }

    public void sale(ArrayList<product> cart, ArrayList<Integer> q, int fund, int result) throws NotEnoughFunds
    {
        try
        {
            int i=0;
            for(product p:cart)
            {
                fund-=(p.pricee()*q.get(i));
                if(fund<0 )
                {
                    throw new NotEnoughFunds("not enough funds, add more");

                }
                else
                {
                    // this.revenue+=(p.price*q.get(i));
                    for(product s:this.productlist)
                    {
                        if((p.name).equals(s.name))
                        {
                            // s.items_available-=q.get(i);
                            if(s.items_availablee()<q.get(i))
                            {
                                q.set(i,s.items_availablee());
                                System.out.println("not enough  items_available");
                            }
                            this.revenue+=(p.pricee()*q.get(i));
                        }
                    }

                }

                i++;
            }
        }
        catch(NotEnoughFunds ex)
        {
            throw new NotEnoughFunds(ex.getMessage());

        }

    }

    public String serialiser(database db,String filename)
    {
        //serialization of database
//        db.setrev(32);
        try
        {
            System.out.println("Object has been serialized");
            FileOutputStream file=new FileOutputStream(filename);
            ObjectOutputStream output=new ObjectOutputStream(file);
            output.writeObject(db);
            output.close();
            file.close();

        }
        catch(IOException ex)
        {
//            System.out.println("IOException is caught");
        }

        return filename;

    }
    public database deserialiser(String filename)
    {
        //deserialising the database
        database obj=null;
        try
        {
            System.out.println("Object has been deserialized ");
            FileInputStream file=new FileInputStream(filename);
            ObjectInputStream input=new ObjectInputStream(file);
            obj=(database)input.readObject();
            input.close();
            file.close();
//            System.out.println(obj.getrev());

        }
        catch(IOException ex)
        {
//            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

        return obj;
    }

    @Override
    public boolean equals(Object obj) {
        database d=(database)obj;
        try
        {
            if(this.tcat.equals(d.getTcat()) && this.productlist.equals(d.getProductlist()) && this.revenue==d.getrev())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (NullPointerException e)
        {
            return true;
        }

    }
}
class admin
{
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class admindb
{
    ArrayList<admin> alist;

    admindb()
    {
        this.alist=new ArrayList<admin>();
    }

    public admin searcher(String naam)
    {
        try
        {
            for(admin ad:this.alist)
            {
                String q=ad.getName();
                System.out.println(q);
                if((q).equals(naam))
                {
                    System.out.println("Welcome back, "+naam+" !");
                    return ad;

                }
            }
            admin newad=new admin();
            newad.setName(naam);
            this.alist.add(newad);
            return newad;
        }
        catch(NullPointerException e)
        {
            admin newad=new admin();
            newad.setName(naam);
            this.alist=new ArrayList<admin>();
            this.alist.add(newad);
            return newad;
        }
    }

}

class product
{
    private int items_available;
    String[] path;
    private int price;
    String name;

    product(int x, int y , String[] c, String n)
    {
        items_available=x;
        price=y;
        path=c;
        name=n;

    }

    public int pricee()
    {
        return price;
    }

    public int items_availablee()
    {
        return items_available;
    }

    public void pricey(int i)
    {
        price=i;
    }

    public void items_availabley(int i)
    {
        items_available=i;
    }
}

class DuplicateException extends Exception
{
    public DuplicateException(String s)
    {
        super(s);
    }
}

class Invalid extends Exception
{
    public Invalid(String s)
    {
        super(s);
    }
}

class NoSuchProduct extends Exception
{
    public NoSuchProduct(String s)
    {
        super(s);
    }
}

class CantAdd extends Exception
{
    public CantAdd(String s)
    {
        super(s);
    }
}

class NotEnoughFunds extends Exception
{
    public NotEnoughFunds(String s)
    {
        super(s);
    }
}

class customerdb
{
    ArrayList<customer> clist;

    public customerdb() {
       this.clist=new ArrayList<customer>();

    }

    public customer searcher(String naam)
    {
        try
        {
            for(customer cus:this.clist)
            {
                String q=cus.getName();
//                System.out.println(q);
                if((q).equals(naam))
                {
                    System.out.println("Welcome back, "+naam+" !");
                    return cus;

                }
            }
            customer newcus=new customer(naam);
            this.clist.add(newcus);
            return newcus;
        }
        catch(NullPointerException e)
        {
            customer newcus=new customer(naam);
            this.clist=new ArrayList<customer>();
            this.clist.add(newcus);
            return newcus;
        }
    }

}
class customer
{
    private String name;
    ArrayList<product> cart;
    ArrayList<Integer> quant;
    private int funds;

    public String getName()
    {
        return name;
    }

    public void addfunds(int newadded)
    {
        this.funds+=newadded;
//        System.out.println(funds);
    }

    public void setName(String name) {
        this.name = name;
    }

    customer(String h)
    {
        funds=0;
        cart=new ArrayList<product>();
        quant=new ArrayList<Integer>();
        name=h;
    }

    public void addtomycart(product p,int quan) throws CantAdd
    {
        try
        {
            if(p.items_availablee()>quan)
            {
                this.cart.add(p);
                this.quant.add(quan);
            }
            else
            {
                throw new CantAdd("Out of Stock");
            }


        }
        catch(NullPointerException  e)
        {
            this.cart=new ArrayList<product>();
            this.cart.add(p);
            this.quant.add(quan);


        }
    }

    public void checkout(database db) throws NotEnoughFunds
    {


            db.sale(cart,quant,funds,0);




        cart=new ArrayList<product>();
        quant=new ArrayList<Integer>();



    }
    public String serialiser(customer db,String filename)
    {
        //serialization of database
//        db.setrev(32);
        try
        {
            System.out.println("Object has been serialized");
            FileOutputStream file=new FileOutputStream(filename);
            ObjectOutputStream output=new ObjectOutputStream(file);
            output.writeObject(db);
            output.close();
            file.close();

        }
        catch(IOException ex)
        {
//            System.out.println("IOException is caught");
        }

        return filename;

    }
    public customer deserialiser(String filename)
    {
        //deserialising the database
        customer obj=null;
        try
        {
            System.out.println("Object has been deserialized ");
            FileInputStream file=new FileInputStream(filename);
            ObjectInputStream input=new ObjectInputStream(file);
            obj=(customer)input.readObject();
            input.close();
            file.close();
//            System.out.println(obj.getrev());

        }
        catch(IOException ex)
        {
//            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

        return obj;
    }

    @Override
    public boolean equals(Object obj) {
        customer c=(customer) obj;
        try{
            if(c.name.equals(this.name) && this.cart.equals(c.cart) && this.quant.equals(c.quant) && this.funds==c.funds)
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        catch (NullPointerException e)
        {
            return true;
        }
    }
}
public class Main {


    public static void main(String[] args)
    {

        Scanner sc= new Scanner(System.in);
        database db= new database();
        customerdb cdb= new customerdb();
        admindb adb=new admindb();
        while(true)
        {
            System.out.println("--------------MAIN MENU----------------");
            System.out.println("Choose from the following");
            System.out.println("(1) Admin");
            System.out.println("(2) Customer");
            System.out.println("(3) Exit");

            int t=sc.nextInt();
            if(t==1)
            {

                //Admin
                System.out.println("Enter your name:");
                String y=sc.next();
//                System.out.println(y);
                admin ab=adb.searcher(y);
                while(true)
                {
                    System.out.println("Choose from the following");
                    System.out.println("(1) Insert");
                    System.out.println("(2) Delete");
                    System.out.println("(3) Search");
                    System.out.println("(4) Modify");
                    System.out.println("(5) Exit");
                    int n=sc.nextInt();

                    if(n==1)
                    {
                        System.out.print("enter path");
                        String p=sc.next();

                        System.out.print("enter product name");

                        String pr=sc.next();
                        System.out.print("Enter quantity and price");
                        String[] cat= p.split(">");
                        product pro = new product(sc.nextInt(),sc.nextInt(),cat,pr);
                        try
                        {
                            db.insert(p,pro);
                        }
                        catch(DuplicateException ex)
                        {
                            System.out.println(ex.getMessage());
                        }


                    }
                    if(n==2)
                    {
                        System.out.println("Delete a (1)product or (2)subcategory");

                        int z=sc.nextInt();

                        if(z==1)
                        {
                            System.out.print("enter product name");

                            String s=sc.next();
                            try
                            {
                                db.deleteproduct(s);
                            }
                            catch(NoSuchProduct ex)
                            {
                                System.out.println(ex.getMessage());
                            }

                        }
                        else
                        {
                            System.out.print("enter path");

                            String s=sc.next();
                            try
                            {
                                db.deletesubcat(s);

                            }
                            catch(Invalid ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                    if(n==3)
                    {
                        System.out.print("enter name");
                        String sear=sc.next();
                        product p=null;
                        try
                        {
                             p = db.search(sear);

                        }
                        catch(Invalid e)
                        {
                            System.out.println(e.getMessage());
                        }
                        if(p!=null)for(String sas:p.path) System.out.print(sas+" > ");

                    }
                    else if(n==4)
                    {
                        System.out.print("enter name");
                        product p=null;

                        String sear=sc.next();
                        try
                        {
                             p = db.search(sear);

                        }
                        catch(Invalid e)
                        {
                            System.out.println(e.getMessage());
                        }
                        if(p!=null)
                        {
                            System.out.print("enter price and quantity");

                            p.pricey(sc.nextInt());
                            p.items_availabley(sc.nextInt());
                        }

                    }
                    else if(n==5)
                    {
                        break;
                    }
                    else if(n>5)
                    {
                        System.out.print("Invalid Choice");

                    }
                }



            }
            else if(t==2)
            {
                System.out.println("Enter your name:");
                String y=sc.next();
//                System.out.println(y);
                customer cb=cdb.searcher(y);
                //Customer
                while(true)
                {

                    System.out.println("Choose from the following");
                    System.out.println("(1) AddFunds");
                    System.out.println("(2) Add to Cart");
                    System.out.println("(3) Check out");
                    System.out.println("(4) Exit");
                    int n=sc.nextInt();

                    if(n==1)
                    {
                        System.out.print("Enter Value");
                        cb.addfunds(sc.nextInt());
                    }
                    else if(n==2)
                    {
                        System.out.print("Enter name and quantity");
                        product p=null;
                        try
                        {
                             p=db.search(sc.next());

                        }
                        catch(Invalid e)
                        {
                            System.out.println(e.getMessage());
                        }
                        if(p!=null)
                        {
                            try
                            {
                                cb.addtomycart(p,sc.nextInt());

                            }
                            catch(CantAdd e)
                            {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    else if(n==3)
                    {
                        try
                        {
                            cb.checkout(db);

                        }
                        catch(NotEnoughFunds ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                        finally {
                            System.out.print("Checking out..");
                        }


                    }
                    else if(n==4)
                    {
                        break;
                    }
                    else if(n>4)
                    {
                        System.out.print("Invalid Choice");

                    }



                }
            }
            else if(t==3)
            {
                break;
            }
            else if(t>3)
            {
                System.out.print("Invalid Choice");
            }

            // String p=sc.next();
            //         String pr=sc.next();
            //         System.out.print("Enter quan and price");
            //         String [] cat= p.split(">");
            //         product pro = new product(sc.nextInt(),sc.nextInt(),cat,pr);
            //         db.insert(p,pro);

            // for(product c:db.productlist)
            //         {
            //               System.out.println(c.name);

            //         }

            //         System.out.print("Enter name and quantity");
            //         product pp=db.search(sc.next());
            //         if(pp!=null)
            //         {
            //             cb.addtomycart(pp,sc.nextInt());
            //         }
            // for(product pa:cb.cart)
            //         {
            //             System.out.println(pa.name);
            //         }
            //         cb.addfunds(123213);
            //        cb.checkout(db);

            //         for(product c:db.productlist)
            //         {
            //               System.out.println(c.items_available);

            //         }


        }
//        String k=db.serialiser(db,"file.ser");
//        db.deserialiser(k);


        System.out.println(db.getrev());



    }







}