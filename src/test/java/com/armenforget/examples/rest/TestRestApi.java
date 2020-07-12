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

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

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

		ResponseEntity<Product> postResponse = restTemplate.postForEntity(getUrl("/products"), product, Product.class);
		Assert.assertNotNull(postResponse);
		Assert.assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateProduct() {
		String productUrl = getUrl("/products/1");
		Product product = restTemplate.getForObject(productUrl, Product.class);
		product.setManufacturer("honda");
		product.setModel("civic");

		restTemplate.put(productUrl, product);

		Product updatedProduct = restTemplate.getForObject(productUrl, Product.class);
		System.out.println(updatedProduct);
		Assert.assertNotNull(updatedProduct);
	}

	@Test
	public void testDeleteProduct() {
		String productUrl = getUrl("/products/1");
		Product product = restTemplate.getForObject(productUrl, Product.class);
		Assert.assertNotNull(product);

		restTemplate.delete(productUrl);

		try {
			product = restTemplate.getForObject(productUrl, Product.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
