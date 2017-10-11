package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;


@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private Category electro;
    private Category kitchen;
    private Product flashlight;
    private Product kitchenRobot;
    private Product plate;

    @BeforeClass
    public void setUp() {
        electro = new Category();
        electro.setName("Electro");
        kitchen = new Category();
        kitchen.setName("Kitchen");

        flashlight = new Product();
        flashlight.setName("Flashlight");
        kitchenRobot = new Product();
        kitchenRobot.setName("Kitchen robot");
        plate = new Product();
        plate.setName("Plate");

        electro.addProduct(flashlight);
        kitchen.addProduct(kitchenRobot);
        kitchen.addProduct(plate);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(flashlight);
        em.persist(kitchenRobot);
        em.persist(plate);
        em.persist(electro);
        em.persist(kitchen);

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void electroTest() {
        EntityManager entityManager = emf.createEntityManager();
        Category category = entityManager.find(Category.class, electro.getId());
        assertContainsProductWithName(category.getProducts(), "Flashlight");
    }

    @Test
    public void kitchenTest() {
        EntityManager entityManager = emf.createEntityManager();
        Category category = entityManager.find(Category.class, kitchen.getId());
        assertContainsProductWithName(category.getProducts(), "Kitchen robot");
        assertContainsProductWithName(category.getProducts(), "Plate");
    }

    @Test
    public void flashlightTest() {
        EntityManager entityManager = emf.createEntityManager();
        Product product = entityManager.find(Product.class, flashlight.getId());
        assertContainsCategoryWithName(product.getCategories(), "Electro");
    }

    @Test
    public void kitchenRobotTest() {
        EntityManager entityManager = emf.createEntityManager();
        Product product = entityManager.find(Product.class, kitchenRobot.getId());
        assertContainsCategoryWithName(product.getCategories(), "Kitchen");
    }

    @Test
    public void plateTest() {
        EntityManager entityManager = emf.createEntityManager();
        Product product = entityManager.find(Product.class, plate.getId());
        assertContainsCategoryWithName(product.getCategories(), "Kitchen");
    }

    private void assertContainsCategoryWithName(Set<Category> categories,
                                                String expectedCategoryName) {
        for (Category cat : categories) {
            if (cat.getName().equals(expectedCategoryName))
                return;
        }

        Assert.fail("Couldn't find category " + expectedCategoryName + " in collection " + categories);
    }

    private void assertContainsProductWithName(Set<Product> products,
                                               String expectedProductName) {

        for (Product prod : products) {
            if (prod.getName().equals(expectedProductName))
                return;
        }

        Assert.fail("Couldn't find product " + expectedProductName + " in collection " + products);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testDoesntSaveNullName() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Product product = new Product();
        product.setName(null);
        entityManager.persist(product);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
