package com.example.demo.entity.for_product;

import com.example.demo.entity.for_productVariant.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Schema(defaultValue = "false")
    Boolean isDeleted = false;

    @NotBlank(message = "Name must not be blank!")
    @Column(unique = true)
    String name;

    Date created_at;

    @OneToMany(mappedBy = "size")
            @JsonIgnore
    List<ProductVariant> productVariants;
}
