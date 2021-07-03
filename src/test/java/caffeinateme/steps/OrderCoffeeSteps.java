package caffeinateme.steps;

import caffeinateme.model.*;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderCoffeeSteps {
    Customer cathy = Customer.named("Cathy");
    ProductCatalog productCatalog = new ProductCatalog();
    CoffeeShop coffeeShop = new CoffeeShop(productCatalog);
    Order order;
    Receipt receipt;

    @Given("Cathy is {int} metre(s) from the coffee shop")
    public void cathy_is_metres_from_the_coffee_shop(Integer distanceInMetres) {
        cathy.setDistanceFromShop(distanceInMetres);
    }

    @When("^Cathy (?:orders|has ordered) an? (.*)")
    public void cathy_orders_a(String orderedProduct) {
        this.order = Order.of(1,orderedProduct).forCustomer(cathy);
        cathy.placesAnOrderFor(order).at(coffeeShop);
    }

    @DataTableType
    public OrderItem mapRowToOrderItem(Map<String, String> entry) {
        return new OrderItem(entry.get("Product"),
                Integer.parseInt(entry.get("Quantity")));
    }

    @Given("^Cathy has ordered:$")
    public void sarahHasOrdered(List<OrderItem> orders) {
        for(OrderItem item : orders) {
            Order order = Order.of(item.getQuantity(),item.getProduct()).forCustomer(cathy);
            cathy.placesAnOrderFor(order).at(coffeeShop);
        }
    }

    @Then("Barry should receive the order")
    public void barry_should_receive_the_order() {
        assertThat(coffeeShop.getPendingOrders()).contains(order);
    }

    @ParameterType(name = "order-status", value="(Normal|High|Urgent)")
    public OrderStatus orderStatus(String statusValue) {
        return OrderStatus.valueOf(statusValue);
    }

    @Then("Barry should know that the order is {order-status}")
    public void barry_should_know_that_the_order_is(OrderStatus expectedStatus) {
        Order cathysOrder = coffeeShop.getOrderFor(cathy)
                .orElseThrow(() -> new AssertionError("No order found!"));
        assertThat(cathysOrder.getStatus()).isEqualTo(expectedStatus);
    }

    @And("Cathy is {int} minutes away")
    public void customerIsMinutesAway(int etaInMinutes) {
        coffeeShop.setCustomerETA(cathy, etaInMinutes);
    }
}