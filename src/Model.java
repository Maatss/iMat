/**
 * @author oloft
 */

import javafx.scene.image.Image;
import se.chalmers.cse.dat216.project.*;

import java.util.*;


/**
 * Wrapper around the IMatDataHandler. The idea is that it might be useful to
 * add an extra layer that can provide special features
 */
public class Model {

    private static Model instance = null;
    private IMatDataHandler iMatDataHandler;

    private final ArrayList<String> availableCardTypes = new ArrayList<String>(Arrays.asList("MasterCard", "Visa"));
    private final ArrayList<String> months = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));
    private final ArrayList<String> years = new ArrayList<String>(Arrays.asList("19", "20", "21", "22", "23", "24", "25"));

    private static String deliveryTime;
    private static String deliveryDateWeekday;
    private static String deliveryDateDDM;
    private static Date deliveryDate;

    private boolean doShowHelpWizard = false;

    private int helpWizardIndex = 0;

    /**
     * Constructor that should never be called, use getInstance() instead.
     */
    protected Model() {
        // Exists only to defeat instantiation.
    }

    /**
     * Returns the single instance of the Model class.
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
            instance.init();
        }
        return instance;
    }

    private void init() {
        iMatDataHandler = IMatDataHandler.getInstance();
    }

    public List<Product> getProducts() {
        return iMatDataHandler.getProducts();
    }

    public List<Product> getFavoriteProducts() {
        return iMatDataHandler.favorites();
    }

    public List<Product> getCategoryProducts(ProductCategory pc) {
        return iMatDataHandler.getProducts(pc);
    }

    public Product getProduct(int idNbr) {
        return iMatDataHandler.getProduct(idNbr);
    }

    public List<Product> findProducts(String s) {
        return iMatDataHandler.findProducts(s);
    }

    public Image getImage(Product p) {
        return iMatDataHandler.getFXImage(p);
    }

    public Image getImage(Product p, double width, double height) {
        return iMatDataHandler.getFXImage(p, width, height);
    }

    public void addToShoppingCart(Product p) {
        addToShoppingCart(p, 1);
    }

    public void addToShoppingCart(Product p, double amount) {
        boolean gotStacked = false;
        for (ShoppingItem cartItem : getShoppingCart().getItems()) {
            if (cartItem.getProduct().getProductId() == p.getProductId()) {
                cartItem.setAmount(cartItem.getAmount() + amount);
                gotStacked = true;
                getShoppingCart().fireShoppingCartChanged(cartItem, true); //Update change listeners
                break;
            }
        }
        if (!gotStacked) {
            ShoppingItem newCartItem = new ShoppingItem(p);
            newCartItem.setAmount(amount);
            getShoppingCart().addItem(newCartItem);
        }
    }

    public void removeFromShoppingCart(Product p, double amount) {
        for (ShoppingItem cartItem : getShoppingCart().getItems()) {
            if (cartItem.getProduct().getProductId() == p.getProductId()) {
                if (cartItem.getAmount() <= amount) {
                    getShoppingCart().removeItem(cartItem);
                } else {
                    cartItem.setAmount(cartItem.getAmount() - amount);
                    getShoppingCart().fireShoppingCartChanged(cartItem, true); //Update change listeners
                }
                break;
            }
        }
    }

    public void removeFromShoppingCart(Product p) {
        removeFromShoppingCart(p, 1);
    }

    public int getCountInShoppingCart(ShoppingItem item) {
        int count = 0;
        for (ShoppingItem cartItem : getShoppingCart().getItems()) {
            if (cartItem.getProduct().getProductId() == item.getProduct().getProductId()) {
                count += cartItem.getAmount();
            }
        }
        return count;
    }

    public int getCountInShoppingCart() {
        int count = 0;
        for (ShoppingItem cartItem : getShoppingCart().getItems()) {
            count += cartItem.getAmount();
        }
        return count;
    }

    public List<String> getCardTypes() {
        return availableCardTypes;
    }

    public List<String> getMonths() {
        return months;
    }

    public List<String> getYears() {
        return years;
    }

    public CreditCard getCreditCard() {
        return iMatDataHandler.getCreditCard();
    }

    public Customer getCustomer() {
        return iMatDataHandler.getCustomer();
    }

    public static void setDeliveryTime(String deliveryTime) {
        Model.deliveryTime = deliveryTime;
    }

    public static String getDeliveryTime() {
        return deliveryTime;
    }

    public static void setDeliveryDateWeekday(String deliveryDateWeekday) {
        Model.deliveryDateWeekday = deliveryDateWeekday;
    }

    public static String getDeliveryDateWeekday() {
        return deliveryDateWeekday;
    }

    public static String getDeliveryDateDDM() {
        return deliveryDateDDM;
    }

    public static void setDeliveryDateDDM(String deliveryDateDDM) {
        Model.deliveryDateDDM = deliveryDateDDM;
    }

    public static Date getDeliveryDate() {
        return deliveryDate;
    }

    public static void setDeliveryDate(Date deliveryDate) {
        Model.deliveryDate = deliveryDate;
    }

    public ShoppingCart getShoppingCart() {
        return iMatDataHandler.getShoppingCart();
    }

    public void clearShoppingCart() {
        iMatDataHandler.getShoppingCart().clear();
    }

    public void placeOrder() {
        iMatDataHandler.placeOrder();
    }

    public List<Order> getOrders() {
        List<Order> temp = new ArrayList<>();
        temp.addAll(iMatDataHandler.getOrders());
        Collections.reverse(temp);
        return temp;
    }

    public int getNumberOfOrders() {
        return iMatDataHandler.getOrders().size();
    }

    public void shutDown() {
        iMatDataHandler.shutDown();
    }

    public void toggleFavorite(Product p) {
        if (iMatDataHandler.isFavorite(p)) {
            iMatDataHandler.removeFavorite(p);
        } else {
            iMatDataHandler.favorites().add(0, p);
        }
    }

    public boolean checkIfFavorite(Product p) {
        return iMatDataHandler.isFavorite(p);
    }

    public boolean isDoShowHelpWizard() {
        return doShowHelpWizard;
    }

    public void setDoShowHelpWizard(boolean doShowHelpWizard) {
        this.doShowHelpWizard = doShowHelpWizard;
    }

    public int getHelpWizardIndex() {
        return helpWizardIndex;
    }

    public void setHelpWizardIndex(int helpWizardIndex) {
        this.helpWizardIndex = helpWizardIndex;
    }
}
