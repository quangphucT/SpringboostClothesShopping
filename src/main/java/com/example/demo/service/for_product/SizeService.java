package com.example.demo.service.for_product;

import com.example.demo.entity.for_product.Size;
import com.example.demo.exception.DuplicationException;
import com.example.demo.model.for_product.CreateSizeRequest;
import com.example.demo.repository.for_product.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;

    public void createNewSize(CreateSizeRequest createSizeRequest) {
        Size size = sizeRepository.findSizeByName(createSizeRequest.getName());
        if (size != null) {
            throw new DuplicationException("Duplicated name!");
        }else{
            size = new Size();
            size.setName(createSizeRequest.getName());
            size.setCreated_at(new Date());
            sizeRepository.save(size);
        }
    }
    public List<Size> getAllSize() {
        return sizeRepository.findAllByIsDeletedFalse();
    }
    public Size findSizeById(Long id) {
        Size size = sizeRepository.findSizeById(id);
        if (size == null) {
            throw new DuplicationException("Not found!");
        }else{
            return size;
        }
    }
    public void updateSize(Long id, String name) {
        Size size = sizeRepository.findSizeById(id);
        if (size == null) {
            throw new DuplicationException("Not found!");
        }else{
            size.setName(name);
            sizeRepository.save(size);
        }
    }
    public void deleteSize(Long id) {
        Size size = sizeRepository.findSizeById(id);
        if (size == null) {
            throw new DuplicationException("Not found!");
        }else{
            size.setIsDeleted(true);
            sizeRepository.save(size);
        }
    }
}
