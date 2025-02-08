package com.nt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.entity.ProductEntity;
import com.nt.entity.UnitEntity;
import com.nt.repository.ProductRepository;
import com.nt.repository.UnitRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UnitRepository unitRepository;

	public void saveProduct(ProductEntity product, List<String> units) {
		ProductEntity savedProduct = productRepository.save(product);

		for (String unit : units) {
			UnitEntity unitEntity = new UnitEntity();
			unitEntity.setUnitName(unit);
			unitRepository.save(unitEntity);
		}
	}

	public List<ProductEntity> getAllProducts() {
		return productRepository.findAll();
	}

	public ProductEntity getProductById(Long id) {
	    System.out.println("Fetching Product by ID: " + id);
	    return productRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
	}

	public void updateProduct(ProductEntity product) {
	    System.out.println("Saving Product: " + product);
		if (product.getId() == null || !productRepository.existsById(product.getId())) {
			throw new RuntimeException("Cannot update product. Product not found.");
		}
		productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(Long id) {
		if (productRepository.existsById(id)) {

			System.out.println("Deleting product with ID: " + id);

			unitRepository.deleteById(id);

			productRepository.deleteById(id);
		} else {
			throw new RuntimeException("Product not found with ID: " + id);
		}
	}
}
