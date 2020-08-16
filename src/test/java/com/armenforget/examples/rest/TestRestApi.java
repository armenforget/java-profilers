package com.armenforget.examples.rest;

import com.armenforget.examples.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRestApi {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String getUrl(String route) {
		return getRootUrl() + route;
	}

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void testGetAllProducts() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				getUrl("/products"),
				HttpMethod.GET,
				entity,
				String.class
		);
		System.out.println(response.getBody());
		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void testGetProductById() {
		Product product = restTemplate.getForObject(getUrl("/products/1"), Product.class);

		System.out.println(product);
		Assert.assertNotNull(product);
	}

	@Test
	public void testCreateProduct() {
		Product product = new Product();
		product.setManufacturer("Sony");
		product.setModel("Playstation");
		product.setDescription("Game console");

		String url = getUrl("/products");
		ResponseEntity<Product> response = restTemplate.postForEntity(url, product, Product.class);

		Assert.assertNotNull(response.getBody());
		Assert.assertEquals("Sony", response.getBody().getManufacturer());
		Assert.assertEquals("Playstation", response.getBody().getModel());
		Assert.assertEquals("Game console", response.getBody().getDescription());
	}

	@Test
	public void testUpdateProduct() {
		String url = getUrl("/products/1");

		// TODO Create object with default settings, preferably using DI

		Product product = restTemplate.getForObject(url, Product.class);
		product.setManufacturer("honda");
		product.setModel("civic");

		restTemplate.put(url, product);

		Product updatedProduct = restTemplate.getForObject(url, Product.class);
		System.out.println(updatedProduct);
		Assert.assertNotNull(updatedProduct);
		Assert.assertEquals("Honda", updatedProduct.getManufacturer());
		Assert.assertEquals("Civic", updatedProduct.getModel());
	}

	@Test
	public void testDeleteProduct() {
		String url = getUrl("/products/1");
		Product product = restTemplate.getForObject(url, Product.class);
		Assert.assertNotNull(product);

		restTemplate.delete(url);

		try {
			product = restTemplate.getForObject(url, Product.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
