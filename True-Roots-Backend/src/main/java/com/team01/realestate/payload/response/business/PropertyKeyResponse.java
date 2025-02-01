package com.team01.realestate.payload.response.business;  

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;  

@Data  
@NoArgsConstructor  
@AllArgsConstructor
@Builder
public class PropertyKeyResponse {  
    private Long id;   // Özellik anahtarının ID'si  
    private String name; // Özellik anahtarının adı  
}