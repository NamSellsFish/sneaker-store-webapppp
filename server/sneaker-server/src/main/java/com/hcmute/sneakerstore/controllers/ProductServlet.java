package com.hcmute.sneakerstore.controllers;

import java.io.IOException;
import java.util.Set;

import com.hcmute.sneakerstore.business.Product;
import com.hcmute.sneakerstore.business.ProductInventory;
import com.hcmute.sneakerstore.data.DAOs.ProductDao;
import com.hcmute.sneakerstore.utils.HttpResponseHandler;
import com.hcmute.sneakerstore.utils.PathParams;
import com.hcmute.sneakerstore.utils.StatusMessage;
import com.hcmute.sneakerstore.utils.ValidationUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

	private static final long serialVersionUID = 3359810340026853619L;

	
	@Data
	private class ProductReponseBody{
		private Product product;
		private Set<ProductInventory> productInventories;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
	
		PathParams pathParams = new PathParams(req);
		String productIdStr = pathParams.get(0);
		//
		if (ValidationUtils.isNullOrEmpty(productIdStr)) {
			HttpResponseHandler.sendErrorResponse(res, res.SC_NOT_FOUND,
					StatusMessage.SM_NOT_FOUND.getDescription());
			return;
		}
		//
		long productId = 0;
		try {
			productId = Long.parseLong(productIdStr);
		} catch (NumberFormatException err) {
			HttpResponseHandler.sendErrorResponse(res, res.SC_BAD_REQUEST,
					StatusMessage.SM_BAD_REQUEST.getDescription());
			return;
		}
		//
		Product product = ProductDao.selectOne(productId);
		//
		if (product == null) {			
			HttpResponseHandler.sendErrorResponse(res, res.SC_NOT_FOUND,
					StatusMessage.SM_NOT_FOUND.getDescription());
			return;
		}
		//
		ProductReponseBody body = new ProductReponseBody();
		body.setProduct(product);
		body.setProductInventories(product.getProductInventories());
		//
		HttpResponseHandler.sendSuccessJsonResponse(res, res.SC_OK, body);
	}
	
	@Override
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		doGet(req, res);
	}
}
