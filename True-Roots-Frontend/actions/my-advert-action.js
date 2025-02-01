"use server";

import { convertFormDataToJSON, response } from '@/helpers/form-validation';
import { advertSchema } from '@/helpers/schemas/my-advert-schema';
import { editAdvert } from '@/services/advert-service';
import Swal from 'sweetalert2';

export const saveAdvert = async (advertId, data) => {
  try {
    if (!advertId) {
      console.error("Advert ID is missing");
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Advert ID is missing',
      });
      return;
    }

    const jsonData = JSON.stringify(data);

    const response = await fetch(`https://66c39606d057009ee9c0b9ad.mockapi.io/products/${advertId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: jsonData,
    });

    if (response.ok) {
      Swal.fire({
        icon: 'success',
        title: 'Success',
        text: 'The advert has been successfully updated!',
      }).then(() => {
        
        window.location.href = '/dashboard/my-adverts';
      });
    } else {
      const errorText = await response.text();
      console.error("Error updating advert:", errorText);
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: `An error occurred while updating the advert: ${errorText}`,
      });
    }
  } catch (error) {
    console.error("Network error:", error);
    Swal.fire({
      icon: 'error',
      title: 'Network Error',
      text: `A network error occurred while updating the advert: ${error.message}`,
    });
  }
};

export const editAdvertAction= async (advertId, data) => {
  
  try {
    
   advertSchema.validateSync(data);
    const res=await editAdvert(advertId,data);
    const result=await res.json();
    if(res.ok){

      return response(true,"Advert updated successfully")
    }else{
      return response(false,result.message);
    }

    
  } catch (error) {
    throw error;
  }
}
