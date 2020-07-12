package com.armenforget.examples.rest;

import com.armenforget.examples.rest.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.spf4j.annotations.RecorderSourceInstance;
import org.spf4j.annotations.PerformanceMonitor;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;


  /**
   * Get all products.
   * @return List of all products saved to database
   */
  @GetMapping("/products")
  @PerformanceMonitor(warnThresholdMillis=1, errorThresholdMillis=100, recorderSource=RecorderSourceInstance.Rs15m.class)
  public List<Product> getAllProducts() throws InterruptedException {

    sleepForRandom(100, 2000);
    return productRepository.findAll();
  }


  /**
   * Get one product by ID
   * @param productId ID of product to fetch
   * @return Product with target ID
   * @throws ResourceNotFoundException Exception when product with ID not found in database
   */
  @GetMapping("/products/{id}")
  @PerformanceMonitor(warnThresholdMillis=1, errorThresholdMillis=100, recorderSource=RecorderSourceInstance.Rs15m.class)
  public ResponseEntity<Product> getProductsById(
          @PathVariable(value = "id") Long productId)
          throws ResourceNotFoundException, InterruptedException {

    sleepForRandom(100, 2000);

    Product product = getProductById(productId);
    return ResponseEntity.ok().body(product);
  }


  /**
   * Create a single product
   * @param product Product to create in database, ID will be assigned automatically
   * @return Newly created product with all fields populated
   */
  @PostMapping("/products")
  @PerformanceMonitor(warnThresholdMillis=1, errorThresholdMillis=100, recorderSource=RecorderSourceInstance.Rs15m.class)
  public Product createProduct(
          @Valid @RequestBody Product product)
          throws InterruptedException {

    sleepForRandom(100, 2000);

    product.setUpdatedAt(new Date());
    product.setCreatedAt(new Date());
    return productRepository.save(product);
  }


  /**
   * Modify existing product
   * @param productId ID of product to update
   * @param newProduct Product whose fields contain values to update
   * @return Status of update
   * @throws ResourceNotFoundException Exception when product with ID not found in database
   */
  @PutMapping("/products/{id}")
  @PerformanceMonitor(warnThresholdMillis=1, errorThresholdMillis=100, recorderSource=RecorderSourceInstance.Rs15m.class)
  public ResponseEntity<Product> updateProduct(
          @PathVariable(value = "id") Long productId,
          @Valid @RequestBody Product newProduct)
          throws ResourceNotFoundException, InterruptedException {

    sleepForRandom(100, 2000);

    Product product = getProductById(productId);
    product.setManufacturer(newProduct.getManufacturer());
    product.setModel(newProduct.getModel());
    product.setDescription(newProduct.getDescription());
    product.setUpdatedAt(new Date());

    final Product updatedProduct = productRepository.save(product);
    return ResponseEntity.ok(updatedProduct);
  }


  /**
   *
   * @param productId ID of product to delete
   * @return Status of delete operation
   * @throws ResourceNotFoundException Exception when product with ID not found in database
   */
  @DeleteMapping("/product/{id}")
  @PerformanceMonitor(warnThresholdMillis=1, errorThresholdMillis=100, recorderSource=RecorderSourceInstance.Rs15m.class)
  public Map<String, Boolean> deleteProduct(
          @PathVariable(value = "id") Long productId)
          throws ResourceNotFoundException, InterruptedException {

    sleepForRandom(100, 2000);

    Product product = getProductById(productId);
    productRepository.delete(product);

    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }


  private Product getProductById(Long productId) throws ResourceNotFoundException {
    return productRepository
            .findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: ID = " + productId));
  }

  private void sleepForRandom(int baselineMillis, int randomOffsetMillis) throws InterruptedException {
    Thread.sleep(baselineMillis + new Random().nextInt(randomOffsetMillis));
  }

}
