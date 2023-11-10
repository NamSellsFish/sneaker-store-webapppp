package com.hcmute.sneakerstore.business;

import java.util.HashSet;
import java.util.Set;

import com.hcmute.sneakerstore.business.enums.SaleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SALE")
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Builder.Default
	@NotNull
	private SaleType type = SaleType.FLASH_SALE;

	@Builder.Default
	@NotNull
	private float percentage = 1.f;
	
	//
	@Builder.Default
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Product> products = new HashSet<>();
	
	//
	
	public Sale(SaleType type, float percentage) {
		this.type = type;
		this.percentage = percentage;
	}
	
	public void addProduct(Product product) {
		products.add(product);
		product.getDiscountedSales().add(this);
	}
	
	public void removeProduct(Product product) {
		products.remove(product);
		product.getDiscountedSales().remove(this);
	}
	
	//
	
	public int getSaleProductCount() {
		return products.size();
	}
}
