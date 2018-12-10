import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class customerTest {
    database db=new database();
    customer cb=new customer("Akash");
    product pro=null;

    @Before
    public void initiator()
    {
        String[] cat={"elec","smartphone"};
        product pro = new product(21,231,cat,"oneplus");
        try{

            db.insert("elec>smartphone",pro);

        }
        catch(DuplicateException ex)
        {

        }
    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void addtomycart() throws CantAdd {
        exceptionRule.expect(CantAdd.class);
        exceptionRule.expectMessage("Out of Stock");
        try
        {
            pro=db.search("oneplus");

        }
        catch (Invalid e)
        {
            pro=null;
        }
        cb.addtomycart(pro,23);

    }

    @Test
    public void checkout() throws NotEnoughFunds{
        exceptionRule.expect(NotEnoughFunds.class);
        exceptionRule.expectMessage("not enough funds, add more");

        try
        {
            pro=db.search("oneplus");

        }
        catch (Invalid e)
        {
            pro=null;
        }
        try
        {
            cb.addtomycart(pro,2);

        }
        catch (CantAdd a)
        {

        }
        cb.addfunds(12);
        cb.checkout(db);

    }
    @org.junit.Test
    public void serialisertest(){

        String k=cb.serialiser(cb,"file.ser");
        customer c=cb.deserialiser(k);
        Assert.assertEquals(cb, c);

    }

}