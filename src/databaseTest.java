import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Assert;

public class databaseTest {
    database db=new database();
    @Before
    public void initiator()
    {
        String[] cat={"elec","smartphone"};
        product pro = new product(21,2,cat,"oneplus");
        try{
            db.insert("elec>smartphone",pro);

        }
        catch(DuplicateException ex)
        {

        }
    }

    @org.junit.Test(expected = DuplicateException.class)
    public void inserttest() throws DuplicateException{
        String[] cat={"elec","smartphone"};
        product prod = new product(21,2,cat,"oneplus");
        db.insert("elec>smartphone",prod);

    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @org.junit.Test
    public void deleteproduct_test() throws NoSuchProduct {
        exceptionRule.expect(NoSuchProduct.class);
        exceptionRule.expectMessage("No such product ...");

        db.deleteproduct("apple iphone");
    }

    @org.junit.Test
    public void search() throws Invalid{

        exceptionRule.expect(Invalid.class);
        exceptionRule.expectMessage("No such product ...");

        db.search("apple iphone");
    }
    @org.junit.Test
    public void serialisertest(){

        String k=db.serialiser(db,"file.ser");
        database d=db.deserialiser(k);
        Assert.assertEquals(db, d);

    }




}