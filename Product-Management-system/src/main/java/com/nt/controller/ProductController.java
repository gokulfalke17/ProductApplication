package com.nt.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.entity.ProductEntity;
import com.nt.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public String getHomePage() {
		return "index";
	}

	@GetMapping("/list")
	public String getAllProducts(Model model) {
		List<ProductEntity> products = productService.getAllProducts();
		model.addAttribute("products", products);
		return "list";
	}

	@GetMapping("/new")
	public String showCreateProductForm(Model model) {
		model.addAttribute("product", new ProductEntity());
		return "create";
	}

	@PostMapping("/save")
	public String saveProduct(@ModelAttribute ProductEntity product, @RequestParam("units") String units, Model model) {
		if (units == null || units.isEmpty()) {
			model.addAttribute("error", "Units cannot be empty!");
			return "error-page";
		}

		List<String> unitList = Arrays.asList(units.split(","));

		productService.saveProduct(product, unitList);
		return "redirect:/products/list";
	} 

	@GetMapping("/view/{id}")
	public String getProductById(@PathVariable("id") Long id, Model model) {
		Optional<ProductEntity> product = Optional.empty();
		if (product.isPresent()) {
			model.addAttribute("product", product.get());
			return "detail";
		} else {
			model.addAttribute("error", "Product not found!");
			return "error-page";
		}
	}
	
	
	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable("id") Long id, Model model) {
	    ProductEntity product = productService.getProductById(id); // Fetch the product
	    model.addAttribute("product", product); // Add product to the model
	    return "edit"; // Render the edit.html template
	}

	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") ProductEntity product) {
	    System.out.println("Updating Product: " + product);
	    try {
	        System.out.println("Updating Product: " + product);
	        productService.updateProduct(product);
	        return "redirect:/products/list"; 
	    } catch (RuntimeException e) {
	        System.out.println("Error: " + e.getMessage());
	        return "redirect:/products/error";
	    }
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		return "redirect:/products/list";

	}

}
