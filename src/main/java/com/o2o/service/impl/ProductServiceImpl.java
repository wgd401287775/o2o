package com.o2o.service.impl;

import com.o2o.dto.ProductExecution;
import com.o2o.enums.ProductStateEnum;
import com.o2o.mapper.ProductImgMapper;
import com.o2o.mapper.ProductMapper;
import com.o2o.pojo.Product;
import com.o2o.pojo.ProductImg;
import com.o2o.service.ProductService;
import com.o2o.utils.ImageUtil;
import com.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImgMapper productImgMapper;

    @Override
    public ProductExecution getProductList(long shopId) {
        List<Product> productList = productMapper.queryProductList(shopId);
        return new ProductExecution(ProductStateEnum.SUCCESS, productList);
    }

    @Override
    @Transactional
    public ProductExecution insertProduct(Product product, CommonsMultipartFile productImg, List<CommonsMultipartFile> detailImgList) {
        if(product != null && productImg != null && detailImgList.size() > 0){
            Date date = new Date();
            product.setCreateTime(date);
            product.setLastEditTime(date);
            product.setEnableStatus(1);
            try {
                addThumbnail(product, productImg);
                int effectedNum = productMapper.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建商品失败");
                }
                addProductImgs(product, detailImgList);
                return new ProductExecution(ProductStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new RuntimeException("创建商品失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution modifyProduct(Product product, CommonsMultipartFile productImg, List<CommonsMultipartFile> detailImgList) {
        if(product == null) {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
        try {
            product.setLastEditTime(new Date());
            if(productImg != null){
                Product originalProduct = productMapper.queryProductByProductId(product.getProductId());
                ImageUtil.deleteFileOrPath(originalProduct.getImgAddr());
                addThumbnail(product, productImg);
            }
            int i = productMapper.modifyProduct(product);
            if(i <= 0) {
                return new ProductExecution(ProductStateEnum.INNER_ERROR);
            }
            if(detailImgList != null && detailImgList.size() > 0) {
                List<ProductImg> productImgList = productImgMapper.queryProductImgByProductId(product.getProductId());
                if(productImgList != null && productImgList.size() > 0){
                    for (ProductImg img : productImgList) {
                        ImageUtil.deleteFileOrPath(img.getImgAddr());
                    }
                    productImgMapper.deleteProductImg(product.getProductId());
                }
                addProductImgs(product, detailImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException("修改商品信息失败，" + e.getMessage());
        }
    }

    @Override
    public Product queryProductById(long productId) {
        Product product = productMapper.queryProductByProductId(productId);
        return product;
    }

    private void addProductImgs(Product product, List<CommonsMultipartFile> detailImgList) throws RuntimeException {
        List<ProductImg> productImgList = new ArrayList<>();
        String imagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<String> imgAddrList = ImageUtil.generateNormalImgList(detailImgList, imagePath);
        for(String path : imgAddrList){
            ProductImg productImg = new ProductImg();
            productImg.setCreateTime(new Date());
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(path);
            productImgList.add(productImg);
        }
        try {
            int i = productImgMapper.batchInsertProductImg(productImgList);
            if(i <= 0) {
                throw new RuntimeException("创建商品详情图片失败");
            }
        } catch (Exception e){
            throw new RuntimeException("创建商品详情图片失败:" + e.toString());
        }
    }

    private void addThumbnail(Product product, CommonsMultipartFile file){
        String fileName = file.getOriginalFilename();
        try {
            InputStream is = file.getInputStream();
            String imagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
            String imgAddr = ImageUtil.generateNormalImg(is, fileName, imagePath);
            product.setImgAddr(imgAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
