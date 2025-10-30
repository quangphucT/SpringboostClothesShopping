package com.example.demo.service.for_product;

import com.example.demo.entity.for_product.Color;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotEnoughException;
import com.example.demo.model.for_product.CreateColorRequest;
import com.example.demo.repository.for_product.ColorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
public class ColorService {
    @Autowired
    ColorRepository colorRepository;
    public void createNewColor(CreateColorRequest createColorRequest) {
        Color color = colorRepository.findColorByName(createColorRequest.getName());
        if(color != null){
            throw new DuplicationException("color already exists");
        }else{
            color = new Color();
            color.setName(createColorRequest.getName());
            color.setCreated_at(new Date());
            colorRepository.save(color);
        }

    }
    public List<Color> getAllColors() {
        return colorRepository.findAllByIsDeletedFalse();
    }
    public void deletedColors( Long id){
       Color color = colorRepository.findColorById(id);
       if(color != null){
           color.setIsDeleted(true);
           colorRepository.save(color);
       }else{
           throw new NotEnoughException("Color not found");
       }
    }
    public void updateColor(Long id,  String name){
        Color color = colorRepository.findColorById(id);
        if(color != null){
            color.setName(name);
            colorRepository.save(color);
        }else {
            throw new NotEnoughException("Color not found");
        }
    }
    public Color getColorById(Long id){
        return colorRepository.findColorById(id);
    }
}
