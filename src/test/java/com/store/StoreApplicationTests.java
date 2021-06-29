package com.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.model.Order;
import com.store.model.OrderItem;
import com.store.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StoreApplication.class)
@WebAppConfiguration
public class StoreApplicationTests {

	protected MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	private <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void getProduct() throws Exception {
		String uri = "/products/";

		// create a product
		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		String createdProductAsJson = mapToJson(product);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		product = mapFromJson(content, Product.class);

		// get the created product
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + product.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertEquals(200, mvcResult.getResponse().getStatus());
	}

	@Test
	public void getProductsList() throws Exception {
		String uri = "/products/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

	}

	@Test
	public void createProduct() throws Exception {
		String uri = "/products/";

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		String inputJson = mapToJson(product);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);

	}

	@Test
	public void updateProduct() throws Exception {
		String uri = "/products/";

		// create a product
		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		String createdProductAsJson = mapToJson(product);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		product = mapFromJson(content, Product.class);

		// update the created product
		String updatedProductAsJson = mapToJson(product);
		MvcResult mvcUpdateResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(updatedProductAsJson)).andReturn();

		assertEquals(200, mvcUpdateResult.getResponse().getStatus());

	}

	@Test
	public void deleteProduct() throws Exception {
		String uri = "/products/";

		// create a product
		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		String createdProductAsJson = mapToJson(product);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		product = mapFromJson(content, Product.class);

		// delete the created product
		String deletedProductAsJson = mapToJson(product);
		MvcResult mvcDeleteResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(deletedProductAsJson)).andReturn();

		assertEquals(200, mvcDeleteResult.getResponse().getStatus());

	}

	@Test
	public void deleteProductInUse() throws Exception {


		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);
		}

		// attempt to delete the created product
		{

			String uri = "/products/";
			String deletedProductAsJson = mapToJson(product);
			MvcResult mvcDeleteResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(deletedProductAsJson)).andReturn();

			String content = mvcDeleteResult.getResponse().getContentAsString();

			assertEquals("Cannot delete product in use", content);
		}
	}

	@Test
	public void getOrder() throws Exception {
		String uri = "/orders/";

		//create a order
		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		String createdOrderAsJson = mapToJson(order);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		order = mapFromJson(content, Order.class);

		// get the created order
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + order.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertEquals(200, mvcResult.getResponse().getStatus());

	}

	@Test
	public void getOrderList() throws  Exception {
		String uri = "/orders/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void createOrder() throws Exception {
		String uri = "/orders/";

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	public void updateOrder() throws Exception {

		String uri = "/orders/";

		//create a order
		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		String createdOrderAsJson = mapToJson(order);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		order = mapFromJson(content, Order.class);

		// update the created order
		String updatedOrderAsJson = mapToJson(order);
		MvcResult mvcUpdateResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(updatedOrderAsJson)).andReturn();

		assertEquals(200, mvcUpdateResult.getResponse().getStatus());
	}

	@Test
	public void deleteOrder() throws Exception {

		String uri = "/orders/";

		//create a order
		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		String createdOrderAsJson = mapToJson(order);
		MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderAsJson)).andReturn();

		int status = mvcPostResult.getResponse().getStatus();
		assertEquals(201, status);

		String content = mvcPostResult.getResponse().getContentAsString();
		order = mapFromJson(content, Order.class);

		// delete the created order
		String deletedOrderAsJson = mapToJson(order);
		MvcResult mvcDeleteResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(deletedOrderAsJson)).andReturn();

		assertEquals(200, mvcDeleteResult.getResponse().getStatus());
	}

	@Test
	public void getOrderItem() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);

		String uri = "/order_items/";

		{

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();

			orderItem = mapFromJson(content, OrderItem.class);
		}
		{

			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + orderItem.getId())
					.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

			assertEquals(200, mvcResult.getResponse().getStatus());
		}
	}

	@Test
	public void getOrderItemsList() throws Exception {
		String uri = "/order_items/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void createOrderItem() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);
		}
	}

	@Test
	public void updateOrderItem() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);

		String uri = "/order_items/";

		{

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();

			orderItem = mapFromJson(content, OrderItem.class);
		}
		{

			String updatedOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(updatedOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(200, status);
		}
	}

	@Test
	public void deleteOrderItem() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);

		String uri = "/order_items/";

		{

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();

			orderItem = mapFromJson(content, OrderItem.class);
		}
		{

			String deletedOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcDeleteResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(deletedOrderItemAsJson)).andReturn();

			int status = mvcDeleteResult.getResponse().getStatus();
			assertEquals(200, status);
		}
	}

	@Test
	public void createOrderItemWithFinishedOrder() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(true);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			String content = mvcPostResult.getResponse().getContentAsString();

			assertEquals("Cannot add order item to a finished order", content);
		}
	}

	@Test
	public void createOrderItemWithDisabledItem() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.0f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(2.3f);
		product.setService(false);
		product.setEnabled(false);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create a order item
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItem);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			String content = mvcPostResult.getResponse().getContentAsString();

			assertEquals("Cannot add to a order a disabled product", content);
		}
	}

	@Test
	public void createOrderWithDiscount() throws Exception {

		// create a order

		Order order = new Order();
		order.setPrice(0.0f);
		order.setDiscountRate(0.2f);
		order.setFinished(false);

		{
			String uri = "/orders/";

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);
		}

		// create a product

		Product product = new Product();
		product.setName("Just a sample product");
		product.setPrice(10);
		product.setService(false);
		product.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(product);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			product = mapFromJson(content, Product.class);
		}

		// create service
		Product service = new Product();
		service.setName("Just a sample product");
		service.setPrice(10);
		service.setService(true);
		service.setEnabled(true);

		{
			String uri = "/products/";
			String createdProductAsJson = mapToJson(service);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdProductAsJson)).andReturn();

			int status = mvcPostResult.getResponse().getStatus();
			assertEquals(201, status);

			String content = mvcPostResult.getResponse().getContentAsString();
			service = mapFromJson(content, Product.class);
		}

		// create a order item for the product
		OrderItem orderItemProduct = new OrderItem();
		orderItemProduct.setOrder(order);
		orderItemProduct.setProduct(product);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItemProduct);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			String content = mvcPostResult.getResponse().getContentAsString();

			assertEquals(201, mvcPostResult.getResponse().getStatus());
		}

		// create a order item for the service
		OrderItem orderItemService = new OrderItem();
		orderItemService.setOrder(order);
		orderItemService.setProduct(service);
		{
			String uri = "/order_items/";

			String createdOrderItemAsJson = mapToJson(orderItemService);
			MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(createdOrderItemAsJson)).andReturn();

			String content = mvcPostResult.getResponse().getContentAsString();

			assertEquals(201, mvcPostResult.getResponse().getStatus());
		}

		// update the order to finished
		{
			String uri = "/orders/";

			order.setFinished(true);

			String inputJson = mapToJson(order);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);

			String content = mvcResult.getResponse().getContentAsString();

			order = mapFromJson(content, Order.class);

			// check for the correct discount value
			assertEquals(18.0f, order.getPrice());
		}
	}

}
